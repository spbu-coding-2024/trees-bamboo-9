import nodes.AVLNode
import trees.AVLTree
import kotlin.math.abs

fun init_tree(vararg keys: Int) : AVLTree<Int, Int> {
    val tree = AVLTree<Int,Int>()
    for(i in keys){
        tree.insert(i,i)
    }
    return tree
}

fun check_size(tree : AVLTree<Int, Int>, correctSize : Int) : Boolean{
    if(tree.size != correctSize){
        return false
    }
    return true
}


fun check_correct_binary_search_tree(root : AVLNode<Int, Int>) : Boolean{
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

fun check_correct_left_and_right_sun(node : AVLNode<Int, Int>) : Boolean{
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

class MaxHeight(var height: Int = 0, var key : Int = 0)

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
        val maxHeight = MaxHeight()
        find_max_height_node(left,maxHeight)
        heightLeft = map.get(maxHeight.key) ?: 0
        heightLeft -= map.get(node.key) ?: 0
    }
    if(right != null){
        val maxHeight = MaxHeight()
        find_max_height_node(right,maxHeight)
        heightRight = map.get(maxHeight.key) ?: 0
        heightRight -= map.get(node.key) ?: 0
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

fun find_max_height_node(root: AVLNode<Int, Int>, maxHeight: MaxHeight){
    if(root.height > maxHeight.height){
        maxHeight.height = root.height
        maxHeight.key = root.key
    }
    val left = root.leftChild
    val right = root.rightChild
    if(left != null){
        find_max_height_node(left,maxHeight)
    }
    if(right != null){
        find_max_height_node(right,maxHeight)
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

fun main(){
    val tree = init_tree(5,6,3,10,2)
    if(!check_size(tree,5)){
        println("incorrect size")
        return
    }
    val root = tree.root
    if(root == null){
        println("tree without root")
        return
    }
    if(!check_correct_binary_search_tree(root)){
        println("its not BST")
        return
    }
    if(!check_balance(root)){
        println("its not AVL")
        return
    }
    println("all test passed")
}