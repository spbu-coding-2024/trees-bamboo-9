package AVLTest

import org.junit.jupiter.api.Test
import trees.AVLTree
import nodes.AVLNode
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AVLTestModul {

    class CorrectNode(
        val key: Int?,
        val value: Int?,
        val keyLeft: Int?,
        val valueLeft: Int?,
        val keyRight: Int?,
        val valueRight: Int?
    )

    class Flag(
        var flagOfEquals: Boolean = true, //flag for check equal trees
        var nodeNumber: Int = 0 //number of node in correctTree
    )

    private fun equalsTree(tree: AVLTree<Int, Int>, correctTree: ArrayDeque<CorrectNode>): Boolean {
        val rootTree = tree.root

        //both trees empty
        if (rootTree == null && correctTree.isEmpty()) {
            return true
        } else if (rootTree == null || correctTree.isEmpty()) { //one tree not empty
            return false
        }

        val flag = Flag()
        equalsNode(rootTree, correctTree, flag)
        return flag.flagOfEquals
    }

    //check equals of node and correctTree[flag.nodeNumber]
    private fun equalsNode(node: AVLNode<Int, Int>, correctTree: ArrayDeque<CorrectNode>, flag: Flag) {
        val correctNode = correctTree[flag.nodeNumber]
        if (node.key != correctNode.key || node.value != correctNode.value) {
            flag.flagOfEquals = false
            return
        }
        val left = node.leftChild
        val right = node.rightChild

        //check that suns of node and correctNode are both null or not null
        if (left == null && correctNode.keyLeft != null) {
            flag.flagOfEquals = false
            return
        } else if (left != null && correctNode.keyLeft == null) {
            flag.flagOfEquals = false
            return
        }
        if (right == null && correctNode.keyRight != null) {
            flag.flagOfEquals = false
            return
        } else if (right != null && correctNode.keyRight == null) {
            flag.flagOfEquals = false
            return
        }

        if (left?.key == correctNode.keyLeft && right?.key == correctNode.keyRight &&
            left?.value == correctNode.valueLeft && right?.value == correctNode.valueRight
        ) {
            if (left != null) {
                flag.nodeNumber++
                equalsNode(left, correctTree, flag)
            }
            if (right != null) {
                flag.nodeNumber++
                equalsNode(right, correctTree, flag)
            }
        } else {
            flag.flagOfEquals = false
            return
        }
    }

    @Test
    fun test_simple_insert() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(5, 5)
        tree.insert(15, 15)
        tree.insert(25, 25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(10, 10, 5, 5, 20, 20))
        correctTree.add(CorrectNode(5, 5, null, null, null, null))
        correctTree.add(CorrectNode(20, 20, 15, 15, 25, 25))
        correctTree.add(CorrectNode(15, 15, null, null, null, null))
        correctTree.add(CorrectNode(25, 25, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_left_rotation_insert() {
        val tree = AVLTree<Int, Int>()
        tree.insert(50, 10)
        tree.insert(51, 20)
        tree.insert(52, 5)
        tree.insert(53, 15)
        tree.insert(54, 25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(51, 20, 50, 10, 53, 15))
        correctTree.add(CorrectNode(50, 10, null, null, null, null))
        correctTree.add(CorrectNode(53, 15, 52, 5, 54, 25))
        correctTree.add(CorrectNode(52, 5, null, null, null, null))
        correctTree.add(CorrectNode(54, 25, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_right_rotation_insert() {
        val tree = AVLTree<Int, Int>()
        tree.insert(58, 10)
        tree.insert(11, 20)
        tree.insert(4, 5)
        tree.insert(3, 15)
        tree.insert(1, 25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(11, 20, 3, 15, 58, 10))
        correctTree.add(CorrectNode(3, 15, 1, 25, 4, 5))
        correctTree.add(CorrectNode(1, 25, null, null, null, null))
        correctTree.add(CorrectNode(4, 5, null, null, null, null))
        correctTree.add(CorrectNode(58, 10, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_big_left_rotation_insert() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(30, 30)
        tree.insert(40, 40)
        tree.insert(50, 50)
        tree.insert(25, 25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(30, 30, 20, 20, 40, 40))
        correctTree.add(CorrectNode(20, 20, 10, 10, 25, 25))
        correctTree.add(CorrectNode(10, 10, null, null, null, null))
        correctTree.add(CorrectNode(25, 25, null, null, null, null))
        correctTree.add(CorrectNode(40, 40, null, null, 50, 50))
        correctTree.add(CorrectNode(50, 50, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_big_right_rotation_insert() {
        val tree = AVLTree<Int, Int>()
        tree.insert(50, 50)
        tree.insert(40, 40)
        tree.insert(30, 30)
        tree.insert(20, 20)
        tree.insert(10, 10)
        tree.insert(35, 35)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(30, 30, 20, 20, 40, 40))
        correctTree.add(CorrectNode(20, 20, 10, 10, null, null))
        correctTree.add(CorrectNode(10, 10, null, null, null, null))
        correctTree.add(CorrectNode(40, 40, 35, 35, 50, 50))
        correctTree.add(CorrectNode(35, 35, null, null, null, null))
        correctTree.add(CorrectNode(50, 50, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_all_ot_off_rotation_insert() {
        val tree = AVLTree<Int, Int>()
        tree.insert(87, 87)
        tree.insert(31, 31)
        tree.insert(42, 42)
        tree.insert(64, 64)
        tree.insert(73, 73)
        tree.insert(95, 95)
        tree.insert(24, 24)
        tree.insert(29, 29)
        tree.insert(5, 5)
        tree.insert(78, 78)
        tree.insert(58, 58)
        tree.insert(28, 28)
        tree.insert(43, 43)
        tree.insert(3, 3)
        tree.insert(57, 57)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(42, 42, 24, 24, 73, 73))
        correctTree.add(CorrectNode(24, 24, 5, 5, 29, 29))
        correctTree.add(CorrectNode(5, 5, 3, 3, null, null))
        correctTree.add(CorrectNode(3, 3, null, null, null, null))
        correctTree.add(CorrectNode(29, 29, 28, 28, 31, 31))
        correctTree.add(CorrectNode(28, 28, null, null, null, null))
        correctTree.add(CorrectNode(31, 31, null, null, null, null))
        correctTree.add(CorrectNode(73, 73, 58, 58, 87, 87))
        correctTree.add(CorrectNode(58, 58, 43, 43, 64, 64))
        correctTree.add(CorrectNode(43, 43, null, null, 57, 57))
        correctTree.add(CorrectNode(57, 57, null, null, null, null))
        correctTree.add(CorrectNode(64, 64, null, null, null, null))
        correctTree.add(CorrectNode(87, 87, 78, 78, 95, 95))
        correctTree.add(CorrectNode(78, 78, null, null, null, null))
        correctTree.add(CorrectNode(95, 95, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_insert_new_value_in_key() {
        val tree = AVLTree<Int, Int>()
        tree.insert(50, 50)
        tree.insert(40, 40)
        tree.insert(30, 30)
        tree.insert(20, 20)
        tree.insert(10, 10)
        tree.insert(35, 35)
        tree.insert(40, 100)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(30, 30, 20, 20, 40, 100))
        correctTree.add(CorrectNode(20, 20, 10, 10, null, null))
        correctTree.add(CorrectNode(10, 10, null, null, null, null))
        correctTree.add(CorrectNode(40, 100, 35, 35, 50, 50))
        correctTree.add(CorrectNode(35, 35, null, null, null, null))
        correctTree.add(CorrectNode(50, 50, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }


    @Test
    fun test_remove_root_in_tree_with_one_node() {
        val tree = AVLTree<Int, Int>()
        tree.insert(39, 40)

        tree.remove(39)

        assertNull(tree.root)
    }


    @Test
    fun test_remove_without_sons() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(5, 5)
        tree.insert(15, 15)
        tree.insert(25, 25)


        tree.remove(25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(10, 10, 5, 5, 20, 20))
        correctTree.add(CorrectNode(5, 5, null, null, null, null))
        correctTree.add(CorrectNode(20, 20, 15, 15, null, null))
        correctTree.add(CorrectNode(15, 15, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_remove_without_right_node() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(5, 5)
        tree.insert(15, 15)
        tree.insert(25, 25)
        tree.insert(3, 3)


        tree.remove(5)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(10, 10, 3, 3, 20, 20))
        correctTree.add(CorrectNode(3, 3, null, null, null, null))
        correctTree.add(CorrectNode(20, 20, 15, 15, 25, 25))
        correctTree.add(CorrectNode(15, 15, null, null, null, null))
        correctTree.add(CorrectNode(25, 25, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_remove_with_right_node() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(5, 5)
        tree.insert(15, 15)
        tree.insert(25, 25)
        tree.insert(3, 3)
        tree.insert(30, 30)
        tree.insert(22, 22)


        tree.remove(20)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(10, 10, 5, 5, 22, 22))
        correctTree.add(CorrectNode(5, 5, 3, 3, null, null))
        correctTree.add(CorrectNode(3, 3, null, null, null, null))
        correctTree.add(CorrectNode(22, 22, 15, 15, 25, 25))
        correctTree.add(CorrectNode(15, 15, null, null, null, null))
        correctTree.add(CorrectNode(25, 25, null, null, 30, 30))
        correctTree.add(CorrectNode(30, 30, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_left_rotation_remove() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(5, 5)
        tree.insert(15, 15)
        tree.insert(25, 25)


        tree.remove(5)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(20, 20, 10, 10, 25, 25))
        correctTree.add(CorrectNode(10, 10, null, null, 15, 15))
        correctTree.add(CorrectNode(15, 15, null, null, null, null))
        correctTree.add(CorrectNode(25, 25, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_right_rotation_remove() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(5, 5)
        tree.insert(15, 15)
        tree.insert(25, 25)
        tree.insert(3, 3)


        tree.remove(15)
        tree.remove(25)
        tree.remove(20)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(5, 5, 3, 3, 10, 10))
        correctTree.add(CorrectNode(3, 3, null, null, null, null))
        correctTree.add(CorrectNode(10, 10, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_big_left_rotation_remove() {
        val tree = AVLTree<Int, Int>()
        tree.insert(50, 50)
        tree.insert(40, 40)
        tree.insert(30, 30)
        tree.insert(20, 20)
        tree.insert(10, 10)
        tree.insert(35, 35)
        tree.insert(32, 32)


        tree.remove(10)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(35, 35, 30, 30, 40, 40))
        correctTree.add(CorrectNode(30, 30, 20, 20, 32, 32))
        correctTree.add(CorrectNode(20, 20, null, null, null, null))
        correctTree.add(CorrectNode(32, 32, null, null, null, null))
        correctTree.add(CorrectNode(40, 40, null, null, 50, 50))
        correctTree.add(CorrectNode(50, 50, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_big_right_rotation_remove() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(30, 30)
        tree.insert(40, 40)
        tree.insert(50, 50)
        tree.insert(25, 25)
        tree.insert(27, 27)


        tree.remove(50)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(25, 25, 20, 20, 30, 30))
        correctTree.add(CorrectNode(20, 20, 10, 10, null, null))
        correctTree.add(CorrectNode(10, 10, null, null, null, null))
        correctTree.add(CorrectNode(30, 30, 27, 27, 40, 40))
        correctTree.add(CorrectNode(27, 27, null, null, null, null))
        correctTree.add(CorrectNode(40, 40, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_all_ot_off_rotation_remove() {
        val tree = AVLTree<Int, Int>()
        tree.insert(87, 87)
        tree.insert(31, 31)
        tree.insert(42, 42)
        tree.insert(64, 64)
        tree.insert(73, 73)
        tree.insert(95, 95)
        tree.insert(24, 24)
        tree.insert(29, 29)
        tree.insert(5, 5)
        tree.insert(78, 78)
        tree.insert(58, 58)
        tree.insert(28, 28)
        tree.insert(43, 43)
        tree.insert(3, 3)
        tree.insert(57, 57)


        tree.remove(3)
        tree.remove(28)
        tree.remove(31)
        tree.remove(95)
        tree.remove(78)
        tree.remove(64)
        tree.remove(87)
        tree.remove(5)
        tree.remove(29)
        tree.remove(42)
        tree.remove(24)
        tree.remove(73)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(57, 57, 43, 43, 58, 58))
        correctTree.add(CorrectNode(43, 43, null, null, null, null))
        correctTree.add(CorrectNode(58, 58, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_remove_root() {
        val tree = AVLTree<Int, Int>()
        tree.insert(5, 5)
        tree.insert(6, 4)
        tree.insert(3, 3)
        tree.insert(10, 2)
        tree.insert(2, 1)


        tree.remove(5)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(6, 4, 3, 3, 10, 2))
        correctTree.add(CorrectNode(3, 3, 2, 1, null, null))
        correctTree.add(CorrectNode(2, 1, null, null, null, null))
        correctTree.add(CorrectNode(10, 2, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_remove_root_without_right_node() {
        val tree = AVLTree<Int, Int>()
        tree.insert(5, 5)
        tree.insert(2, 2)


        tree.remove(5)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(2, 2, null, null, null, null))
    }

    @Test
    fun test_remove_key_not_in_the_tree() {
        val tree = AVLTree<Int, Int>()
        tree.insert(10, 10)
        tree.insert(20, 20)
        tree.insert(5, 5)
        tree.insert(15, 15)
        tree.insert(25, 25)


        tree.remove(100)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(10, 10, 5, 5, 20, 20))
        correctTree.add(CorrectNode(5, 5, null, null, null, null))
        correctTree.add(CorrectNode(20, 20, 15, 15, 25, 25))
        correctTree.add(CorrectNode(15, 15, null, null, null, null))
        correctTree.add(CorrectNode(25, 25, null, null, null, null))


        assertEquals(equalsTree(tree, correctTree), true)
    }

    @Test
    fun test_find_in_empty_tree() {
        val tree = AVLTree<Int, Int>()
        assertNull(tree.find(100))
    }

    @Test
    fun test_basic_find() {
        val tree = AVLTree<Int, Int>()
        tree.insert(87, 92)
        tree.insert(31, 932)
        tree.insert(42, 74)
        tree.insert(64, 866)
        tree.insert(73, 3984)
        tree.insert(95, 30)
        tree.insert(24, 27587)
        tree.insert(29, 288)
        tree.insert(5, 4099)
        tree.insert(78, 4984)
        tree.insert(58, 50095)
        tree.insert(28, 85)
        tree.insert(43, 95)
        tree.insert(3, 598)
        tree.insert(57, 59)


        assertEquals(tree.find(87), 92)
        assertEquals(tree.find(31), 932)
        assertEquals(tree.find(64), 866)
        assertEquals(tree.find(73), 3984)
        assertEquals(tree.find(95), 30)
        assertEquals(tree.find(24), 27587)
        assertEquals(tree.find(29), 288)
        assertEquals(tree.find(5), 4099)
        assertEquals(tree.find(78), 4984)
        assertEquals(tree.find(58), 50095)
        assertEquals(tree.find(28), 85)
        assertEquals(tree.find(43), 95)
        assertEquals(tree.find(3), 598)
        assertEquals(tree.find(57), 59)
    }

    @Test
    fun test_find_root() {
        val tree = AVLTree<Int, Int>()
        tree.insert(87, 92)
        tree.insert(31, 932)
        tree.insert(42, 74)
        tree.insert(64, 866)
        tree.insert(73, 3984)
        tree.insert(95, 30)
        tree.insert(24, 27587)
        tree.insert(29, 288)
        tree.insert(5, 4099)
        tree.insert(78, 4984)
        tree.insert(58, 50095)
        tree.insert(28, 85)
        tree.insert(43, 95)
        tree.insert(3, 598)
        tree.insert(57, 59)


        assertEquals(tree.find(42), 74)
    }

    @Test
    fun test_find_element_that_not_in_tree() {
        val tree = AVLTree<Int, Int>()
        tree.insert(87, 92)
        tree.insert(31, 932)
        tree.insert(42, 74)
        tree.insert(64, 866)
        tree.insert(73, 3984)
        tree.insert(95, 30)
        tree.insert(24, 27587)
        tree.insert(29, 288)
        tree.insert(5, 4099)
        tree.insert(78, 4984)
        tree.insert(58, 50095)
        tree.insert(28, 85)
        tree.insert(43, 95)
        tree.insert(3, 598)
        tree.insert(57, 59)


        assertEquals(tree.find(100), null)
    }
}


