

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
        val tree = AVLTree<Int,Int>()
        tree.insert(10,10)
        tree.insert(20,20)
        tree.insert(5,5)
        tree.insert(15,15)
        tree.insert(25,25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(10,10,5,5,20,20))
        correctTree.add(CorrectNode(5,5,null,null,null,null))
        correctTree.add(CorrectNode(20,20,15,15,25,25))
        correctTree.add(CorrectNode(15,15,null,null,null,null))
        correctTree.add(CorrectNode(25,25,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
    }

    @Test
    fun test_left_rotation_insert() {
        val tree = AVLTree<Int,Int>()
        tree.insert(50,10)
        tree.insert(51,20)
        tree.insert(52,5)
        tree.insert(53,15)
        tree.insert(54,25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(51,20,50,10,53,15))
        correctTree.add(CorrectNode(50,10,null,null,null,null))
        correctTree.add(CorrectNode(53,15,52,5,54,25))
        correctTree.add(CorrectNode(52,5,null,null,null,null))
        correctTree.add(CorrectNode(54,25,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
    }

    @Test
    fun test_right_rotation_insert() {
        val tree = AVLTree<Int,Int>()
        tree.insert(58,10)
        tree.insert(11,20)
        tree.insert(4,5)
        tree.insert(3,15)
        tree.insert(1,25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(11,20,3,15,58,10))
        correctTree.add(CorrectNode(3,15,1,25,4,5))
        correctTree.add(CorrectNode(1,25,null,null,null,null))
        correctTree.add(CorrectNode(4,5,null,null,null,null))
        correctTree.add(CorrectNode(58,10,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
    }

    @Test
    fun test_big_left_rotation_insert() {
        val tree = AVLTree<Int,Int>()
        tree.insert(10,10)
        tree.insert(20,20)
        tree.insert(30,30)
        tree.insert(40,40)
        tree.insert(50,50)
        tree.insert(25,25)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(30,30,20,20,40,40))
        correctTree.add(CorrectNode(20,20,10,10,25,25))
        correctTree.add(CorrectNode(10,10,null,null,null,null))
        correctTree.add(CorrectNode(25,25,null,null,null,null))
        correctTree.add(CorrectNode(40,40,null,null,50,50))
        correctTree.add(CorrectNode(50,50,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
    }

    @Test
    fun test_big_right_rotation_insert() {
        val tree = AVLTree<Int,Int>()
        tree.insert(50,50)
        tree.insert(40,40)
        tree.insert(30,30)
        tree.insert(20,20)
        tree.insert(10,10)
        tree.insert(35,35)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(30,30,20,20,40,40))
        correctTree.add(CorrectNode(20,20,10,10,null,null))
        correctTree.add(CorrectNode(10,10,null,null,null,null))
        correctTree.add(CorrectNode(40,40,35,35,50,50))
        correctTree.add(CorrectNode(35,35,null,null,null,null))
        correctTree.add(CorrectNode(50,50,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
    }

    @Test
    fun test_all_ot_off_rotation_insert() {
        val tree = AVLTree<Int,Int>()
        tree.insert(87,87)
        tree.insert(31,31)
        tree.insert(42,42)
        tree.insert(64,64)
        tree.insert(73,73)
        tree.insert(95,95)
        tree.insert(24,24)
        tree.insert(29,29)
        tree.insert(5,5)
        tree.insert(78,78)
        tree.insert(58,58)
        tree.insert(28,28)
        tree.insert(43,43)
        tree.insert(3,3)
        tree.insert(57,57)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(42,42,24,24,73,73))
        correctTree.add(CorrectNode(24,24,5,5,29,29))
        correctTree.add(CorrectNode(5,5,3,3,null,null))
        correctTree.add(CorrectNode(3,3,null,null,null,null))
        correctTree.add(CorrectNode(29,29,28,28,31,31))
        correctTree.add(CorrectNode(28,28,null,null,null,null))
        correctTree.add(CorrectNode(31,31,null,null,null,null))
        correctTree.add(CorrectNode(73,73,58,58,87,87))
        correctTree.add(CorrectNode(58,58,43,43,64,64))
        correctTree.add(CorrectNode(43,43,null,null,57,57))
        correctTree.add(CorrectNode(57,57,null,null,null,null))
        correctTree.add(CorrectNode(64,64,null,null,null,null))
        correctTree.add(CorrectNode(87,87,78,78,95,95))
        correctTree.add(CorrectNode(78,78,null,null,null,null))
        correctTree.add(CorrectNode(95,95,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
    }

    @Test
    fun test_insert_new_value_in_key(){
        val tree = AVLTree<Int,Int>()
        tree.insert(50,50)
        tree.insert(40,40)
        tree.insert(30,30)
        tree.insert(20,20)
        tree.insert(10,10)
        tree.insert(35,35)
        tree.insert(40,100)


        val correctTree = ArrayDeque<CorrectNode>()
        correctTree.add(CorrectNode(30,30,20,20,40,100))
        correctTree.add(CorrectNode(20,20,10,10,null,null))
        correctTree.add(CorrectNode(10,10,null,null,null,null))
        correctTree.add(CorrectNode(40,100,35,35,50,50))
        correctTree.add(CorrectNode(35,35,null,null,null,null))
        correctTree.add(CorrectNode(50,50,null,null,null,null))


        assertEquals(equalsTree(tree,correctTree),true)
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


