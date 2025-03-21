package trees

import nodes.BSNode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


fun <K : Comparable<K>, V> compareTrees(tree1: BSTree<K, V>, tree2: BSTree<K, V>): Boolean {
    if (tree1.size != tree2.size) return false
    val queue = ArrayDeque<BSNode<K, V>?>()
    if (tree1.root == null && tree2.root == null) return true
    queue.add(tree1.root)
    queue.add(tree2.root)
    var currNode1: BSNode<K, V>?
    var currNode2: BSNode<K, V>?
    while (queue.isNotEmpty()) {
        currNode1 = queue.removeFirst()
        currNode2 = queue.removeFirst()
        if (currNode1 != null) {
            if (currNode2 != null) {
                if (currNode1.key != currNode2.key || currNode1.value != currNode2.value) return false
                queue.add(currNode1.leftChild)
                queue.add(currNode2.leftChild)
                queue.add(currNode1.rightChild)
                queue.add(currNode2.rightChild)
            } else return false
        } else if (currNode2 != null) return false
    }
    return true
}

class BSInsertTest {

    @Test
    fun `insert of one element must set root`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(1, 1)
        expectedResult.size = 1
        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `insert three elements to right`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        testTree.insert(3, 3)
        testTree.insert(2, 2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.size = 3
        expectedResult.root = BSNode(1, 1)
        expectedResult.root?.rightChild = BSNode(3, 3)
        expectedResult.root?.rightChild?.leftChild = BSNode(2, 2)


        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `insert three elements to left`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(3, 3)
        testTree.insert(1, 1)
        testTree.insert(2, 2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.size = 3
        expectedResult.root = BSNode(3, 3)
        expectedResult.root?.leftChild = BSNode(1, 1)
        expectedResult.root?.leftChild?.rightChild = BSNode(2, 2)


        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `insert new value in old key (root)`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(1, 1)
        testTree.insert(2, 642)
        testTree.insert(3, 3)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.size = 3
        expectedResult.root = BSNode(2, 642)
        expectedResult.root?.rightChild = BSNode(3, 3)
        expectedResult.root?.leftChild = BSNode(1, 1)


        assert(compareTrees(testTree, expectedResult))
    }
}

class BSRemoveTest {
    @Test
    fun `remove root without children`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        testTree.remove(1)

        val expectedResult: BSTree<Int, Int> = BSTree()

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove root with left child`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(1, 1)
        testTree.remove(2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.size = 1
        expectedResult.root = BSNode(1, 1)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove root with right child`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        testTree.insert(2, 2)
        testTree.remove(1)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.size = 1
        expectedResult.root = BSNode(2, 2)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove root with both children`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(1, 1)
        testTree.insert(3, 3)
        testTree.remove(2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.size = 2
        expectedResult.root = BSNode(3, 3)
        expectedResult.root?.leftChild = BSNode(1, 1)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove with right child without left child`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        testTree.insert(2, 2)
        testTree.insert(3, 3)
        testTree.remove(2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.size = 2
        expectedResult.root = BSNode(1, 1)
        expectedResult.root?.rightChild = BSNode(3, 3)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove with both children and with left child by right child`() {
        val testTree: BSTree<Double, String> = BSTree()
        testTree.insert(1.0, "!")
        testTree.insert(3.0, "?")
        testTree.insert(4.0, "{")
        testTree.insert(2.0, "}")
        testTree.insert(3.5, "?!?!?!")
        testTree.remove(3.0)


        val expectedResult: BSTree<Double, String> = BSTree()
        expectedResult.size = 4
        expectedResult.root = BSNode(1.0, "!")
        expectedResult.root?.rightChild = BSNode(3.5, "?!?!?!")
        expectedResult.root?.rightChild?.leftChild = BSNode(2.0, "}")
        expectedResult.root?.rightChild?.rightChild = BSNode(4.0, "{")

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove key not in tree`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        assertThrows<IllegalArgumentException>("Key not in tree") {
            testTree.remove(2)
        }
    }
}