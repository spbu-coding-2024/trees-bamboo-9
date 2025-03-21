import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import binaryTrees.*
import Nodes.*

class RBTTest {
    private lateinit var tree: RBT<Int,Int>

    @BeforeEach
    fun setup() {
        tree = RBT()
    }

    @Test
    fun one_element_insert() {
        tree.insert(1,1)
        assertEquals(1, tree.root()?.key)
        assertEquals(1, tree.size())
        check_tree_balance(tree.root())
    }

    @Test
    fun insert_in_ascending_order() {
        for (i in 1..10) {
            tree.insert(i,i)
        }
        assertEquals(4, tree.root()?.key)
        assertEquals(10, tree.size())
        val root = tree.root()
        if(root!=null) {
            assertEquals(1, find_min_tree_key(root))
            assertEquals(10 , find_max_tree_key(root))
        }
        check_tree_balance(tree.root())
    }

    @Test
    fun insert_in_descending_order() {
        for (i in 10 downTo 1) {
            tree.insert(i,i)
        }
        assertEquals(10, tree.size())
        val root = tree.root()
        if(root!=null) {
            assertEquals(1, find_min_tree_key(root))
            assertEquals(10, find_max_tree_key(root))
        }
        val b = tree.root()
        val c = b
    }

}

fun find_min_tree_key(root: RBNode<Int,Int>): Int {
    var currentNode: RBNode<Int,Int> = root
    while (currentNode.leftChild != null) {
        currentNode = currentNode.leftChild?:root
    }
    return currentNode.key
}

fun find_max_tree_key(root: RBNode<Int,Int>): Int {
    var currentNode: RBNode<Int,Int> = root
    while (currentNode.rightChild != null) {
        currentNode = currentNode.rightChild?:root
    }
    return currentNode.key
}

fun check_tree_balance(node: RBNode<Int,Int>?) : Int{
    if (node==null){
        return 1
    }else{
        val rightBlackHeight=check_tree_balance(node.rightChild)
        val leftBlackHeight=check_tree_balance(node.leftChild)
        assertEquals(rightBlackHeight, leftBlackHeight)
        if(node.color==Color.BLACK){
            return rightBlackHeight+1
        }else{
            return rightBlackHeight
        }
    }
}