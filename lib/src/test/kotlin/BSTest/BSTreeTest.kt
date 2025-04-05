package BSTest

import nodes.BSNode
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import trees.BSTree
import kotlin.random.Random
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt
import kotlin.random.nextInt
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


fun <K : Comparable<K>, V> compareTrees(tree1: BSTree<K, V>, tree2: BSTree<K, V>): Boolean {
    val queue = ArrayDeque<BSNode<K, V>?>()
    if (tree1.root == null && tree2.root == null) return true
    else if (tree1.root == null || tree2.root == null) return false
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

fun <K : Comparable<K>, V> isBinarySearchTree(tree: BSTree<K, V>): Boolean {
    val root = tree.root ?: return true
    return isBinarySearchTreeNode(root)
}

fun <K : Comparable<K>, V> isBinarySearchTreeNode(node: BSNode<K, V>): Boolean {
    val left = node.leftChild
    val right = node.rightChild
    val isLeftMore = if (left == null) false else node.key < findMaxChild(left)
    val isRightLess = if (right == null) false else node.key > findMinChild(right)
    return if (isRightLess || isLeftMore) false
    else if (left != null && right != null) {
        isBinarySearchTreeNode(left) && isBinarySearchTreeNode(right)
    } else if (left != null) {
        isBinarySearchTreeNode(left)
    } else if (right != null) {
        isBinarySearchTreeNode(right)
    } else true
}

fun <K : Comparable<K>, V> findMinChild(node: BSNode<K, V>): K {
    val left = node.leftChild ?: return node.key
    val newMin = findMinChild(left)
    return if (node.key > newMin) newMin
    else node.key
}

fun <K : Comparable<K>, V> findMaxChild(node: BSNode<K, V>): K {
    val right = node.rightChild ?: return node.key
    val newMax = findMaxChild(right)
    return if (node.key < newMax) newMax
    else node.key
}

class BSInsertTest {

    @Test
    fun `insert of one element must set root`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(1, 1)
        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `insert three elements to right`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        testTree.insert(3, 3)
        testTree.insert(2, 2)

        val expectedResult: BSTree<Int, Int> = BSTree()
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
        expectedResult.root = BSNode(2, 642)
        expectedResult.root?.rightChild = BSNode(3, 3)
        expectedResult.root?.leftChild = BSNode(1, 1)


        assert(compareTrees(testTree, expectedResult))
    }

    @RepeatedTest(20)
    fun `is binary search after inserts (Int-Int)`() {
        val testTree: BSTree<Int, Int> = BSTree()
        val nodeCount = Random.nextInt(0..100)
        for (i in 0..nodeCount) {
            testTree.insert(nextInt(), nextInt())
        }
        assert(isBinarySearchTree(testTree))
    }

    @RepeatedTest(20)
    fun `is binary search after inserts (String-String)`() {
        val testTree: BSTree<String, String> = BSTree()
        val nodeCount = Random.nextInt(0..100)
        val alphabet = "a..z" + "A..Z" + "0..9" + "{}/,.`!@#$'^:*()_-+=~&?;№"
        var str1: String
        var str2: String
        var stringLength: Int
        for (i in 0..<nodeCount) {
            stringLength = nextInt(100_000)
            str1 = List(stringLength) { alphabet.random() }.joinToString()
            stringLength = nextInt(100_000)
            str2 = List(stringLength) { alphabet.random() }.joinToString()

            testTree.insert(str1, str2)
        }
        assert(isBinarySearchTree(testTree))
    }

    @RepeatedTest(20)
    fun `is binary search after inserts (Boolean-Double)`() {
        val testTree: BSTree<Boolean, Double> = BSTree()
        val nodeCount = Random.nextInt(0..100)
        for (i in 0..<nodeCount) {
            testTree.insert(nextBoolean(), nextDouble())
        }
        assert(isBinarySearchTree(testTree))
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
        expectedResult.root = BSNode(1.0, "!")
        expectedResult.root?.rightChild = BSNode(3.5, "?!?!?!")
        expectedResult.root?.rightChild?.leftChild = BSNode(2.0, "}")
        expectedResult.root?.rightChild?.rightChild = BSNode(4.0, "{")

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove key not in tree by right`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        assertThrows<NoSuchElementException>("Key not in tree: 2") {
            testTree.remove(2)
        }
    }

    @Test
    fun `remove key not in tree by left`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        assertThrows<NoSuchElementException>("Key not in tree: 0") {
            testTree.remove(0)
        }
    }

    @Test
    fun `remove left child`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(1, 1)
        testTree.remove(1)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(2, 2)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove left child with both children and no left children by right one`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(4, 4)
        testTree.insert(2, 2)
        testTree.insert(3, 3)
        testTree.insert(1, 1)
        testTree.remove(2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(4, 4)
        expectedResult.root?.leftChild = BSNode(3, 3)
        expectedResult.root?.leftChild?.leftChild = BSNode(1, 1)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove right child with both children and no left children by right one`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        testTree.insert(3, 3)
        testTree.insert(2, 2)
        testTree.insert(4, 4)
        testTree.remove(3)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(1, 1)
        expectedResult.root?.rightChild = BSNode(4, 4)
        expectedResult.root?.rightChild?.leftChild = BSNode(2, 2)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove with no nodes`() {
        val testTree: BSTree<Int, Int> = BSTree()
        assertThrows<NoSuchElementException>("Key not in tree: 1") {
            testTree.remove(1)
        }

        val expectedResult: BSTree<Int, Int> = BSTree()
        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove root with only left child`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(1, 1)
        testTree.remove(2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(1, 1)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove root with only right child`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(3, 3)
        testTree.remove(2)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(3, 3)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove not root without children`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(3, 3)
        testTree.remove(3)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(2, 2)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove not root with left child only`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)
        testTree.insert(3, 3)
        testTree.insert(2, 2)
        testTree.remove(3)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(1, 1)
        expectedResult.root?.rightChild = BSNode(2, 2)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove not root with right child only`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(2, 2)
        testTree.insert(3, 3)
        testTree.insert(4, 4)
        testTree.remove(3)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(2, 2)
        expectedResult.root?.rightChild = BSNode(4, 4)

        assert(compareTrees(testTree, expectedResult))
    }

    @Test
    fun `remove left child with both children with both children by right`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(10, 10)
        testTree.insert(5, 5)
        testTree.insert(3, 3)
        testTree.insert(7, 7)
        testTree.insert(6, 6)
        testTree.insert(8, 8)
        testTree.remove(5)

        val expectedResult: BSTree<Int, Int> = BSTree()
        expectedResult.root = BSNode(10, 10)
        expectedResult.root?.leftChild = BSNode(6, 6)
        expectedResult.root?.leftChild?.leftChild = BSNode(3, 3)
        expectedResult.root?.leftChild?.rightChild = BSNode(7, 7)
        expectedResult.root?.leftChild?.rightChild?.rightChild = BSNode(8, 8)

        assert(compareTrees(testTree, expectedResult))
    }

    @RepeatedTest(20)
    fun `is binary search after remove keys from tree`() {
        val testTree: BSTree<Int, Int> = BSTree()
        val nodeCount = Random.nextInt(0..100)
        val keys: ArrayDeque<Int> = ArrayDeque()
        var key: Int
        for (i in 0..<nodeCount) {
            key = nextInt()
            testTree.insert(key, key)
            keys.add(key)
        }
        for (i in 0..<nodeCount) {
            key = keys.removeFirst()
            testTree.remove(key)
            assert(isBinarySearchTree(testTree))
        }
    }

    @RepeatedTest(20)
    fun `is binary search after remove random key`() {
        val testTree: BSTree<Int, Int> = BSTree()
        val nodeCount = Random.nextInt(0..100)
        val keys: ArrayDeque<Int> = ArrayDeque()
        var key: Int
        for (i in 0..<nodeCount) {
            key = nextInt()
            keys.add(key)
            testTree.insert(key, nextInt())
        }
        val deletingNodeCount = nextInt()
        for (i in 0..<deletingNodeCount) {
            key = nextInt()
            if (key in keys) {
                testTree.remove(key)
                keys.remove(key)
                assert(isBinarySearchTree(testTree))
            } else {
                assertThrows<NoSuchElementException>("Key not in tree: $key") {
                    testTree.remove(key)
                }
                break
            }
        }
    }
}

class BSFindTest {
    @Test
    fun `find root`() {
        val testTree: BSTree<Int, Int> = BSTree()
        testTree.insert(1, 1)

        val expectedResult = 1
        val actualResult = testTree.find(1)

        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `find right child`() {
        val testTree: BSTree<Float, String> = BSTree()
        testTree.insert(1F, "yeeah")
        testTree.insert(2F, "YEEAH")

        val expectedResult = "YEEAH"
        val actualResult = testTree.find(2F)

        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `find left child`() {
        val testTree: BSTree<Float, String> = BSTree()
        testTree.insert(2F, "yeeah")
        testTree.insert(1F, "YEEAH")

        val expectedResult = "YEEAH"
        val actualResult = testTree.find(1F)

        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `find key not in tree by right`() {
        val testTree: BSTree<Float, String> = BSTree()
        testTree.insert(2F, "yeeah")
        testTree.insert(1F, "YEEAH")
        testTree.insert(3F, "NOOAH")

        val actualResult = testTree.find(4F)

        assertNull(actualResult)
    }

    @Test
    fun `find key not in tree by left`() {
        val testTree: BSTree<Float, String> = BSTree()
        testTree.insert(2F, "yeeah")
        testTree.insert(1F, "YEEAH")
        testTree.insert(3F, "NOOAH")

        val actualResult = testTree.find(0F)

        assertNull(actualResult)
    }

    @Test
    fun `find with no element`() {
        val testTree: BSTree<Float, String> = BSTree()

        val actualResult = testTree.find(4F)

        assertNull(actualResult)
    }
}
