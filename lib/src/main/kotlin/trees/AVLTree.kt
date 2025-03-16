package trees

import nodes.AVLNode

import kotlin.math.max

class AVLTree<K: Comparable<K>, V>: Tree<K, V, AVLNode<K, V>>() {

    private fun height(node : AVLNode<K,V>?) : Int {
        if(node == null){
            return 0
        }
        return node.height
    }

    private fun rightRotate(y : AVLNode<K,V>) : AVLNode<K,V> {
        val x: AVLNode<K, V> = y.leftChild ?: throw IllegalArgumentException("invalid argument left")
        val T2: AVLNode<K, V>? = x.rightChild

        //rotation
        x.rightChild = y
        y.leftChild = T2

        //update tree root
        if(y === root){
            root = x
        }

        //update heights
        y.height = 1 + max(height(y.leftChild), height(y.rightChild))
        x.height = 1 + max(height(x.leftChild), height(x.rightChild))

        //return new root
        return x
    }

    private fun leftRotate(x : AVLNode<K,V>) : AVLNode<K,V> {
        val y: AVLNode<K,V> = x.rightChild ?: throw IllegalArgumentException("invalid argument")
        val T2: AVLNode<K,V>? = y.leftChild

        //rotation
        y.leftChild = x
        x.rightChild = T2

        //update tree root
        if(x === root){
            root = y
        }

        //update heights
        x.height = 1 + max(height(x.leftChild), height(x.rightChild))
        y.height = 1 + max(height(y.leftChild), height(y.rightChild))

        //return new root
        return y
    }

    private fun getBalance(node : AVLNode<K,V>?) : Int{
        if(node == null){
            return 0
        }
        return height(node.leftChild) - height(node.rightChild)
    }

    private fun balancing(node : AVLNode<K,V>) : AVLNode<K,V>{
        node.height = 1 + max(height(node.leftChild),height(node.rightChild))
        val balance = getBalance(node)

        if(balance == 2) {
            val left = node.leftChild ?: throw Exception("incorrect tree")
            if (getBalance(left) < 0) {
                node.leftChild = leftRotate(left)
            }
            return rightRotate(node)
        }
        if(balance == -2){
            val right = node.rightChild ?: throw Exception("incorrect tree")
            if(getBalance(right) > 0) {
                node.rightChild = rightRotate(right)
            }
            return leftRotate(node)
        }
        return node
    }

    private fun findMin(node : AVLNode<K,V>) : AVLNode<K,V>{
        val leftNode = node.leftChild
        leftNode ?: return node
        return findMin(leftNode)
    }

    private fun removeMin(node : AVLNode<K,V>) : AVLNode<K,V>?{
        val leftNode = node.leftChild
        leftNode ?: return node.rightChild
        node.leftChild = removeMin(leftNode)
        return balancing(node)
    }

    private fun insertAVL(node : AVLNode<K,V>?, key: K, newValue: V) : AVLNode<K,V>{
        if(node == null){
            val newNode = AVLNode(key,newValue)
            if(size == 0){
                root = newNode
            }
            size++
            return newNode
        }
        if (key < node.key){
            node.leftChild = insertAVL(node.leftChild,key,newValue)
        }
        else if(key > node.key){
            node.rightChild = insertAVL(node.rightChild,key,newValue)
        }
        else{
            node.value = newValue
            return node
        }
        return balancing(node)
    }

    private fun removeAVL(node : AVLNode<K,V>?, key: K) : AVLNode<K,V>?{
        if(node == null){
            return null
        }
        if(key < node.key){
            node.leftChild = removeAVL(node.leftChild, key)
        }
        else if(key > node.key){
            node.rightChild = removeAVL(node.rightChild, key)
        }
        else{
            val leftNode = node.leftChild
            val rightNode = node.rightChild
            if(rightNode == null){
                return leftNode
            }
            val minNode = findMin(rightNode)
            minNode.rightChild = removeMin(rightNode)
            minNode.leftChild = leftNode
            return balancing(minNode)
        }
        return balancing(node)
    }

    override fun insert(key: K, newValue: V) {
        insertAVL(root,key,newValue)
    }

    override fun remove(key: K) {
        if(find(key) != null) {
            removeAVL(root, key)
            size--
        }
    }
    override fun find(key: K): V? {return null}
}