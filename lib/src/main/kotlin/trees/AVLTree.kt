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
        val x: AVLNode<K, V> = y.leftChild ?: throw IllegalArgumentException("invalid argument")
        val T2: AVLNode<K, V>? = x.rightChild

        //rotation
        x.rightChild = y
        y.leftChild = T2

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

        //update heights
        x.height = 1 + max(height(y.leftChild), height(y.rightChild))
        y.height = 1 + max(height(x.leftChild), height(x.rightChild))

        //return new root
        return y
    }

    private fun getBalance(node : AVLNode<K,V>?) : Int{
        if(node == null){
            return 0
        }
        return height(node.leftChild) - height(node.rightChild)
    }

    override fun insert(key: K, newValue: V) {}
    override fun remove(key: K) {}
    override fun find(key: K): V? {return null}
}