import nodes.AVLNode
import trees.AVLTree
import kotlin.math.abs

import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class AVLTestProperty {

    class Flag(var flagOfBalance: Boolean = true)

    class MaxHeight(var height: Int = 0, var key: Int = 0)

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

    private fun check_balance_of_node(node: AVLNode<Int, Int>, map: HashMap<Int, Int>, flag: Flag) {
        val left = node.leftChild
        val right = node.rightChild
        var heightLeft = 0
        var heightRight = 0
        if (left != null) {
            val maxHeight = MaxHeight()
            find_max_height_node(left, maxHeight)
            heightLeft = map.get(maxHeight.key) ?: 0
            heightLeft -= map.get(node.key) ?: 0
        }
        if (right != null) {
            val maxHeight = MaxHeight()
            find_max_height_node(right, maxHeight)
            heightRight = map.get(maxHeight.key) ?: 0
            heightRight -= map.get(node.key) ?: 0
        }
        if (abs(heightLeft - heightRight) > 1) {
            flag.flagOfBalance = false
        } else {
            if (left != null) {
                check_balance_of_node(left, map, flag)
            }
            if (right != null) {
                check_balance_of_node(right, map, flag)
            }
        }
    }

    private fun find_max_height_node(root: AVLNode<Int, Int>, maxHeight: MaxHeight) {
        if (root.height > maxHeight.height) {
            maxHeight.height = root.height
            maxHeight.key = root.key
        }
        val left = root.leftChild
        val right = root.rightChild
        if (left != null) {
            find_max_height_node(left, maxHeight)
        }
        if (right != null) {
            find_max_height_node(right, maxHeight)
        }
    }


    private fun calculate_height_of_all_nodes(root: AVLNode<Int, Int>, height: Int, map: HashMap<Int, Int>) {
        map.put(root.key, height)
        val left = root.leftChild
        val right = root.rightChild
        if (left != null) {
            calculate_height_of_all_nodes(left, height + 1, map)
        }
        if (right != null) {
            calculate_height_of_all_nodes(right, height + 1, map)
        }
    }

    fun check_size(tree: AVLTree<Int, Int>, correctSize: Int): Boolean {
        if (tree.size != correctSize) {
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
        val mapOfHeight = HashMap<Int, Int>()
        calculate_height_of_all_nodes(root, 1, mapOfHeight)
        val flagOfBalance = Flag()
        check_balance_of_node(root, mapOfHeight, flagOfBalance)
        if (!flagOfBalance.flagOfBalance) {
            return false
        }
        return true
    }

    @Test
    fun propertyTest(){
        for(i in 1..1000) {


            val count_of_nodes = Random.nextInt(1, 1000)
            var count_of_keys = 0
            val tree = AVLTree<Int,Int>()
            val keys = HashSet<Int>()

            //test insert
            for(j in 1..count_of_nodes){
                val key = Random.nextInt(1,100)
                val value = Random.nextInt(1,100)
                if(!keys.contains(key)){
                    keys.add(key)
                    count_of_keys++
                }
                tree.insert(key,value)
                val root = tree.root
                var test = false
                if(check_size(tree,count_of_keys) && (root != null) &&
                        check_correct_binary_search_tree(root) && check_correct_AVL(root)){
                    test = true
                }
                assertEquals(test,true)
            }


            //test find
            for(j in 1..1000){
                val key = Random.nextInt(1,100)
                if(keys.contains(key)){
                    assertNotEquals(tree.find(key), null)
                }
                else{
                    assertEquals(tree.find(key), null)
                }
            }


            //test remove
            for(j in 1..1000){
                var test = false
                val root = tree.root
                val key = Random.nextInt(1,100)
                if(keys.contains(key)){
                    keys.remove(key)
                    count_of_keys--
                }
                tree.remove(key)
                if(count_of_keys == 0 && check_size(tree,count_of_keys) && root == null){
                    test = true
                }
                else if(check_size(tree,count_of_keys) && (root != null) &&
                    check_correct_binary_search_tree(root) && check_correct_AVL(root)){
                    test = true
                }
                assertEquals(test,true,"${check_size(tree,count_of_keys)}" +
                        " ${(root != null) && check_correct_binary_search_tree(root)} " +
                        "${(root != null) && check_correct_AVL(root)} " +
                        "${keys.contains(key)} " +
                        "${tree.size} ${count_of_keys} ${keys.size} ${tree.find(key)} ${tree.root?.key}")
            }
        }
    }
}