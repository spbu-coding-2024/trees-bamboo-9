package redBlackTreeTest

import nodes.Color
import nodes.RBNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import trees.RBT

class RBTTest {
    private lateinit var tree: RBT<Int, Int>

    @BeforeEach
    fun setup() {
        tree = RBT()
    }

    @Test
    fun one_element_insert() {
        tree.insert(1, 1)
        assertEquals(1, tree.root()?.key)
        assertEquals(1, tree.size())
        check_tree_balance(tree.root())
    }

    @Test
    fun insert_in_ascending_order() {
        for (i in 1..10) {
            tree.insert(i, i)
        }
        assertEquals(4, tree.root()?.key)
        assertEquals(10, tree.size())
        val root = tree.root()
        if (root != null) {
            assertEquals(1, find_min_tree_key(root))
            assertEquals(10, find_max_tree_key(root))
        }
        check_tree_balance(tree.root)
    }

    @Test
    fun insert_in_descending_order() {
        for (i in 10 downTo 1) {
            tree.insert(i, i)
        }
        assertEquals(10, tree.size())
        val root = tree.root()
        if (root != null) {
            assertEquals(1, find_min_tree_key(root))
            assertEquals(10, find_max_tree_key(root))
        }
    }

    @Test
    fun findTest() {
        for (i in 1..1000) {
            tree.insert(i, i)
        }
        tree.insert(1001, 52)
        val n = tree.find(1001)
        assertEquals(52, n)
    }

    @RepeatedTest(10)
    fun simpleRemove() {
        for (i in 1..1000) {
            tree.insert(i, i)
        }
        for (i in 1..1000) {
            val c = (1..1000).random()
            tree.remove(c)
            val k = tree.find(c)
            assertEquals(null, k)
            check_tree_balance(tree.root())
        }
    }

    @RepeatedTest(50)
    fun insertAndRemoveRandomValues() {
        for (i in 1..10000) {
            val c = (1..1000).random()
            tree.insert(c, i)
            val b = tree.find(c)
            assertEquals(i, b)
            check_tree_balance(tree.root())
            isBinarySearchTree(tree.root)
        }
        for (i in 1..10000) {
            val randomKey = (1..1000).random()
            tree.remove(randomKey)
            val valueOfRandomKey = tree.find(randomKey)
            assertEquals(null, valueOfRandomKey)
            check_tree_balance(tree.root())
            isBinarySearchTree(tree.root)
        }
    }

    private fun check_tree_balance(node: RBNode<Int, Int>?): Int {
        if (node == null) {
            return 1
        } else {
            val rightBlackHeight = check_tree_balance(node.rightChild)
            val leftBlackHeight = check_tree_balance(node.leftChild)
            assertEquals(rightBlackHeight, leftBlackHeight)
            if (node.color == Color.BLACK) {
                return rightBlackHeight + 1
            } else {
                return rightBlackHeight
            }
        }
    }

    private fun find_min_tree_key(root: RBNode<Int, Int>): Int {
        var currentNode: RBNode<Int, Int> = root
        while (currentNode.leftChild != null) {
            currentNode = currentNode.leftChild ?: root
        }
        return currentNode.key
    }

    private fun find_max_tree_key(root: RBNode<Int, Int>): Int {
        var currentNode: RBNode<Int, Int> = root
        while (currentNode.rightChild != null) {
            currentNode = currentNode.rightChild ?: root
        }
        return currentNode.key
    }

    private fun isBinarySearchTree(node: RBNode<Int, Int>?) {
        if (node == null) {
            return
        }
        val leftChild = node.leftChild
        val rightChild = node.rightChild
        if (leftChild != null) {
            checkLeftSubtree(leftChild, node.key)
            isBinarySearchTree(leftChild)
        }
        if (rightChild != null) {
            checkRightSubtree(rightChild, node.key)
            isBinarySearchTree(rightChild)
        }
    }

    private fun checkLeftSubtree(
        node: RBNode<Int, Int>,
        key: Int,
    ) {
        assert(node.key < key)
        val leftChild = node.leftChild
        val rightChild = node.rightChild
        if (leftChild != null) {
            checkLeftSubtree(leftChild, key)
        }
        if (rightChild != null) {
            checkLeftSubtree(rightChild, key)
        }
    }

    private fun checkRightSubtree(
        node: RBNode<Int, Int>,
        key: Int,
    ) {
        assert(node.key > key)
        val leftChild = node.leftChild
        val rightChild = node.rightChild
        if (leftChild != null) {
            checkRightSubtree(leftChild, key)
        }
        if (rightChild != null) {
            checkRightSubtree(rightChild, key)
        }
    }
}
