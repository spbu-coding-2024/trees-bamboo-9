

import org.junit.jupiter.api.Test
import trees.AVLTree
import nodes.AVLNode
import kotlin.test.assertEquals

class AVLTestModul {

    class CorrectNode(
        val key : Int?,
        val value: Int?,
        val keyLeft : Int?,
        val valueLeft: Int?,
        val keyRight : Int?,
        val valueRight: Int?
    )

    class Flag(var flagOfEquals: Boolean = true, var lastNode : Int = 0)

    fun equalsTree(tree: AVLTree<Int,Int>, correctTree : ArrayDeque<CorrectNode>) : Boolean{
        val root = tree.root
        if(root == null && correctTree.isEmpty()){
            return true
        }
        else if(root == null || correctTree.isEmpty()){
            return false
        }

        val flagOfEquals = Flag()
        equalsNode(root,correctTree,flagOfEquals)
        return flagOfEquals.flagOfEquals
    }

    fun equalsNode(node : AVLNode<Int,Int>, correctTree : ArrayDeque<CorrectNode>,flagOfEquals: Flag){
        val correctNode = correctTree[flagOfEquals.lastNode]
        if(node.key != correctNode.key || node.value != correctNode.value){
            flagOfEquals.flagOfEquals = false
            return
        }
        val left = node.leftChild
        val right = node.rightChild

        if(left == null && correctNode.keyLeft != null){
            flagOfEquals.flagOfEquals = false
            return
        }
        else if(left != null && correctNode.keyLeft == null){
            flagOfEquals.flagOfEquals = false
            return
        }
        if(right == null && correctNode.keyRight != null){
            flagOfEquals.flagOfEquals = false
            return
        }
        else if(right != null && correctNode.keyRight == null){
            flagOfEquals.flagOfEquals = false
            return
        }

        if(left?.key == correctNode.keyLeft && right?.key == correctNode.keyRight &&
            left?.value == correctNode.valueLeft && right?.value == correctNode.valueRight){
            if(left != null) {
                flagOfEquals.lastNode++
                equalsNode(left, correctTree, flagOfEquals)
            }
            if(right != null) {
                flagOfEquals.lastNode++
                equalsNode(right, correctTree, flagOfEquals)
            }
        }
        else{
            flagOfEquals.flagOfEquals = false
            return
        }
    }

    @Test
    fun test_simple_insert() {

    }

    @Test
    fun test_left_rotation_insert() {

    }

    @Test
    fun test_right_rotation_insert() {

    }

    @Test
    fun test_big_left_rotation_insert() {

    }

    @Test
    fun test_big_right_rotation_insert() {

    }

    @Test
    fun test_all_ot_off_rotation_insert() {

    }

    @Test
    fun test_remove_without_right_node() {

    }

    @Test
    fun test_remove_with_right_node() {

    }

    @Test
    fun test_left_rotation_remove() {

    }

    @Test
    fun test_right_rotation_remove() {

    }

    @Test
    fun test_big_left_rotation_remove() {

    }

    @Test
    fun test_big_right_rotation_remove() {

    }

    @Test
    fun test_all_ot_off_rotation_remove() {

    }

    @Test
    fun test_remove_root() {


        val tree = AVLTree<Int, Int>()
        tree.insert(5,5)
        tree.insert(6,4)
        tree.insert(3,3)
        tree.insert(10,2)
        tree.insert(2,1)
        tree.remove(5)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(6,4,3,3,10,2))
        correctTree.add(CorrectNode(3,3,2,1,null,null))
        correctTree.add(CorrectNode(2,1,null,null,null,null))
        correctTree.add(CorrectNode(10,2,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
    }

    @Test
    fun test_remove_only_left() {

    }

    @Test
    fun test_remove_only_right() {

    }

    @Test
    fun test_remove_one_right() {

    }

    @Test
    fun test_basic_find() {

    }

    @Test
    fun test_find_root() {

    }

    @Test
    fun test_find_element_that_not_in_tree() {

    }
}


