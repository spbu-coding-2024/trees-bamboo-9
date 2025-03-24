package AVLTest

import nodes.AVLNode
import org.junit.jupiter.api.RepeatedTest
import trees.AVLTree
import kotlin.math.abs


import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class AVLTestProperty {

    class Flag(
        var flagOfBalance: Boolean = true //flag for check that tree is balanced
    )

    class MaxHeight(
        var height: Int = 0, var key: Int = 0 //flag for find node with max height
    )

    class CountNodes(
        var count: Int = 0 //flag for find count of nodes in tree
    )

    private fun check_correct_left_and_right_sun(node: AVLNode<Int, Int>): Boolean {
        val left = node.leftChild
        val right = node.rightChild
        if (left != null) {
            if (left.key > node.key) {
                return false
            }
        }
        if (right != null) {
            if (right.key < node.key) {
                return false
            }
        }
        return true
    }

    private fun check_balance_of_node(node: AVLNode<Int, Int>, mapOfHeight: HashMap<Int, Int>, flagOfBalance: Flag) {
        val left = node.leftChild
        val right = node.rightChild
        var heightLeft = 0
        var heightRight = 0
        if (left != null) {
            val maxHeight = MaxHeight()
            find_max_height_node(left, maxHeight, mapOfHeight)
            //height of left height max - height parent
            heightLeft = mapOfHeight.get(maxHeight.key) ?: 0
            heightLeft -= mapOfHeight.get(node.key) ?: 0
        }
        if (right != null) {
            val maxHeight = MaxHeight()
            find_max_height_node(right, maxHeight, mapOfHeight)
            //height of right height max - height parent
            heightRight = mapOfHeight.get(maxHeight.key) ?: 0
            heightRight -= mapOfHeight.get(node.key) ?: 0
        }
        if (abs(heightLeft - heightRight) > 1) {
            flagOfBalance.flagOfBalance = false
        } else {
            if (left != null) {
                check_balance_of_node(left, mapOfHeight, flagOfBalance)
            }
            if (right != null) {
                check_balance_of_node(right, mapOfHeight, flagOfBalance)
            }
        }
    }

    private fun find_max_height_node(root: AVLNode<Int, Int>, maxHeight: MaxHeight, mapOfHeight: HashMap<Int, Int>) {
        val heightOfRoot = mapOfHeight.get(root.key) ?: 0
        if (heightOfRoot > maxHeight.height) {
            maxHeight.height = heightOfRoot
            maxHeight.key = root.key
        }
        val left = root.leftChild
        val right = root.rightChild
        if (left != null) {
            find_max_height_node(left, maxHeight, mapOfHeight)
        }
        if (right != null) {
            find_max_height_node(right, maxHeight, mapOfHeight)
        }
    }


    private fun calculate_height_of_all_nodes(root: AVLNode<Int, Int>, height: Int, mapOfHeight: HashMap<Int, Int>) {
        mapOfHeight.put(root.key, height)
        val left = root.leftChild
        val right = root.rightChild
        if (left != null) {
            calculate_height_of_all_nodes(left, height + 1, mapOfHeight)
        }
        if (right != null) {
            calculate_height_of_all_nodes(right, height + 1, mapOfHeight)
        }
    }

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
        if (tree.size != correctSize) {
            return false
        }
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

    fun check_correct_binary_search_tree(root: AVLNode<Int, Int>): Boolean {
        if (check_correct_left_and_right_sun(root)) {
            val left = root.leftChild
            val right = root.rightChild
            if (left != null && right != null) {
                return check_correct_binary_search_tree(left)
                        && check_correct_binary_search_tree(right)
            } else if (left != null) {
                return check_correct_binary_search_tree(left)
            } else if (right != null) {
                return check_correct_binary_search_tree(right)
            } else {
                return true
            }
        }
        return false
    }

    fun check_correct_AVL(root: AVLNode<Int, Int>): Boolean {
        val mapOfHeight = HashMap<Int, Int>() //heights of all nodes in tree
        calculate_height_of_all_nodes(root, 1, mapOfHeight)
        val flagOfBalance = Flag()
        check_balance_of_node(root, mapOfHeight, flagOfBalance)
        if (!flagOfBalance.flagOfBalance) {
            return false
        }
        return true
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
            val rootTree = tree.root
            var test = false
            if (check_size(tree, count_of_nodes_in_tree) && (rootTree != null) &&
                check_correct_binary_search_tree(rootTree) && check_correct_AVL(rootTree)
            ) {
                test = true
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
            val rootTree = tree.root
            val key = Random.nextInt(1, 100)
            if (keys.contains(key)) {
                keys.remove(key)
                count_of_nodes_in_tree--
            }
            tree.remove(key)
            if (count_of_nodes_in_tree == 0 && check_size(tree, count_of_nodes_in_tree) && rootTree == null) {
                test = true
            } else if (check_size(tree, count_of_nodes_in_tree) && (rootTree != null) &&
                check_correct_binary_search_tree(rootTree) && check_correct_AVL(rootTree)
            ) {
                test = true
            }
            assertEquals(test, true, "failed remove")
        }
    }
}