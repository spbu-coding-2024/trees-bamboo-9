package AVLTest

import trees.AVLTree
import nodes.AVLNode
import trees.Tree
import kotlin.math.abs

fun init_tree(vararg keys: Int) : AVLTree<Int,Int>{
    val tree = AVLTree<Int,Int>()
    for(i in keys){
        tree.insert(i,i)
    }
    return tree
}

fun check_size(tree : AVLTree<Int,Int>, correctSize : Int) : Boolean{
    if(tree.size != correctSize){
        return false
    }
    return true
}


fun check_correct_binary_search_tree(root : AVLNode<Int,Int>) : Boolean{
    if(check_correct_left_and_right_sun(root)){
        val left = root.leftChild
        val right = root.rightChild
        if(left != null && right != null){
            return check_correct_binary_search_tree(left)
                    && check_correct_binary_search_tree(right)
        }
        else if(left != null){
            return check_correct_binary_search_tree(left)
        }
        else if(right != null){
            return check_correct_binary_search_tree(right)
        }
        else{
            return true
        }
    }
    return false
}

fun check_correct_left_and_right_sun(node : AVLNode<Int,Int>) : Boolean{
    val left = node.leftChild
    val right = node.rightChild
    if(left != null){
        if(left.key > node.key){
            return false
        }
    }
    if(right != null) {
        if (right.key < node.key) {
            return false
        }
    }
    return true
}
class Flag(var flagOfBalance : Boolean)

fun check_balance(root: AVLNode<Int, Int>) : Boolean{
    val mapOfHeight = HashMap<Int,Int>()
    calculate_height_of_all_nodes(root, 1, mapOfHeight)
    val flagOfBalance = Flag(true)
    check_balance_of_node(root,mapOfHeight,flagOfBalance)
    if(!flagOfBalance.flagOfBalance){
        return false
    }
    return true
}

fun check_balance_of_node(node: AVLNode<Int, Int>, map: HashMap<Int, Int>, flag: Flag){
    val left = node.leftChild
    val right = node.rightChild
    var heightLeft = 0
    var heightRight = 0
    if(left != null){
        heightLeft = map.get(left.key) ?: 0
    }
    if(right != null){
        heightRight = map.get(right.key) ?: 0
    }
    if(abs(heightLeft - heightRight) > 1){
        flag.flagOfBalance = false
    }
    else{
        if(left != null){
            check_balance_of_node(left,map,flag)
        }
        if(right != null){
            check_balance_of_node(right,map,flag)
        }
    }
}


fun calculate_height_of_all_nodes(root: AVLNode<Int, Int>, height : Int, map : HashMap<Int,Int>){
    map.put(root.key,height)
    val left = root.leftChild
    val right = root.rightChild
    if(left != null){
        calculate_height_of_all_nodes(left,height + 1, map)
    }
    if(right != null){
        calculate_height_of_all_nodes(right, height + 1, map)
    }
}

fun check_correct_tree(){

}

fun test_simple_insert() : Boolean{


}

fun test_left_rotation_insert() : Boolean{

}

fun test_right_rotation_insert() : Boolean{

}

fun test_big_left_rotation_insert() : Boolean{

}

fun test_big_right_rotation_insert() : Boolean{

}

fun test_all_ot_off_rotation_insert() : Boolean{

}

fun test_remove_without_right_node() : Boolean{

}

fun test_remove_with_right_node() : Boolean{

}

fun test_left_rotation_remove() : Boolean{

}

fun test_right_rotation_remove() : Boolean{

}

fun test_big_left_rotation_remove() : Boolean{

}

fun test_big_right_rotation_remove() : Boolean{

}

fun test_all_ot_off_rotation_remove() : Boolean{

}

fun test_remove_root() : Boolean{

}
fun test_basic_find() : Boolean{

}

fun test_find_root() : Boolean{

}

fun test_find_element_that_not_in_tree() : Boolean{

}


fun main(){
    test_simple_insert()
    test_left_rotation_insert()
    test_right_rotation_insert()
    test_big_left_rotation_insert()
    test_big_right_rotation_insert()
    test_all_ot_off_rotation_insert()


    test_remove_without_right_node()
    test_remove_with_right_node()
    test_left_rotation_remove()
    test_right_rotation_remove()
    test_big_left_rotation_remove()
    test_big_right_rotation_remove()
    test_all_ot_off_rotation_remove()
    test_remove_root()

    test_basic_find()
    test_find_root()
    test_find_element_that_not_in_tree()
}
