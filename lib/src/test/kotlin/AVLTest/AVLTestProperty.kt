package AVLTest

import nodes.AVLNode
import org.junit.jupiter.api.RepeatedTest
import trees.AVLTree
import kotlin.math.abs


import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class AVLTestProperty {

    class CountNodes(
        var count: Int = 0 //flag for find count of nodes in tree
    )

    class HeightOfNode(
        var height: Int = 0 //flag for find height of node
    )

    fun count_size_tree(root: AVLNode<Int, Int>, countNodes: CountNodes) {
        countNodes.count++
        val left = root.leftChild
        val right = root.rightChild
        if (left != null) {
            count_size_tree(left, countNodes)
        }
        if (right != null) {
            count_size_tree(right, countNodes)
        }
    }

    fun check_size(tree: AVLTree<Int, Int>, correctSize: Int): Boolean {
        val root = tree.root
        if (root == null && correctSize == 0) {
            return true
        } else if (root == null) {
            return false
        }
        val countNodes = CountNodes()
        count_size_tree(root, countNodes)
        if (countNodes.count != correctSize) {
            return false
        }
        return true
    }

    fun isBinarySearchTree(tree: AVLTree<Int, Int>): Boolean {
        val root = tree.root ?: return true
        return isBinarySearchTreeNode(root)
    }

    fun isBinarySearchTreeNode(node: AVLNode<Int, Int>): Boolean {
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

    fun findMinChild(node: AVLNode<Int, Int>): Int {
        val left = node.leftChild ?: return node.key
        val newMin = findMinChild(left)
        return if (node.key > newMin) newMin
        else node.key
    }

    fun findMaxChild(node: AVLNode<Int, Int>): Int {
        val right = node.rightChild ?: return node.key
        val newMax = findMaxChild(right)
        return if (node.key < newMax) newMax
        else node.key
    }

    fun isAVLTree(tree: AVLTree<Int, Int>): Boolean {
        val root = tree.root ?: return true
        return isAVLNode(root)
    }

    fun isAVLNode(node: AVLNode<Int, Int>): Boolean {
        val left = node.leftChild
        val right = node.rightChild
        val heightLeft = HeightOfNode()
        val heightRight = HeightOfNode()
        calcHeight(left, heightLeft, 0)
        calcHeight(right, heightRight, 0)
        if (abs(heightLeft.height - heightRight.height) < 2) {
            return if (left != null && right != null) isAVLNode(left) && isAVLNode(right)
            else if (left != null) {
                isAVLNode(left)
            } else if (right != null) {
                isAVLNode(right)
            } else true
        } else return false
    }

    fun calcHeight(node: AVLNode<Int, Int>?, maxHeight: HeightOfNode, height: Int) {
        node ?: return
        val newHeight = height + 1
        if (newHeight > maxHeight.height) {
            maxHeight.height = newHeight
        }
        calcHeight(node.leftChild, maxHeight, newHeight)
        calcHeight(node.rightChild, maxHeight, newHeight)
    }


    @RepeatedTest(100)
    fun propertyTest() {


        val count_of_nodes = Random.nextInt(1, 1000)
        var count_of_nodes_in_tree = 0
        val tree = AVLTree<Int, Int>()
        val keys = HashSet<Int>()

        //test insert
        for (j in 1..count_of_nodes) {
            val key = Random.nextInt(1, 100)
            val value = Random.nextInt(1, 100)
            if (!keys.contains(key)) {
                keys.add(key)
                count_of_nodes_in_tree++
            }
            tree.insert(key, value)
            var test = false
            if (check_size(tree, count_of_nodes_in_tree) &&
                isBinarySearchTree(tree) && isAVLTree(tree)
            ) {
                test = true
            }
            if (test == false) {
                println("1")
            }
            assertEquals(test, true, "failed insert")
        }


        //test find
        for (j in 1..1000) {
            val key = Random.nextInt(1, 100)
            if (keys.contains(key)) {
                assertNotEquals(tree.find(key), null, "failed find")
            } else {
                assertEquals(tree.find(key), null, "failed find")
            }
        }


        //test remove
        for (j in 1..1000) {
            var test = false
            val key = Random.nextInt(1, 100)
            if (keys.contains(key)) {
                keys.remove(key)
                count_of_nodes_in_tree--
            }
            tree.remove(key)
            if (check_size(tree, count_of_nodes_in_tree) &&
                isBinarySearchTree(tree) && isAVLTree(tree)
            ) {
                test = true
            }
            assertEquals(test, true, "failed remove")
        }
    }
}