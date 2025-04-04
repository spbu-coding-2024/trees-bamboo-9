package trees

import nodes.AVLNode

import kotlin.math.max

class AVLTree<K : Comparable<K>, V> : Tree<K, V, AVLNode<K, V>>() {

    private fun height(node: AVLNode<K, V>?): Int {
        if (node == null) {
            return 0
        }
        return node.height
    }

    private fun rightRotate(rootNode: AVLNode<K, V>): AVLNode<K, V> {
        val left: AVLNode<K, V> = rootNode.leftChild ?: throw IllegalArgumentException("invalid argument left")
        val leftRight: AVLNode<K, V>? = left.rightChild

        //rotation
        left.rightChild = rootNode
        rootNode.leftChild = leftRight

        //update tree root
        if (rootNode === root) {
            root = left
        }

        //update heights
        rootNode.height = 1 + max(height(rootNode.leftChild), height(rootNode.rightChild))
        left.height = 1 + max(height(left.leftChild), height(left.rightChild))

        //return new root
        return left
    }

    private fun leftRotate(rootNode: AVLNode<K, V>): AVLNode<K, V> {
        val right: AVLNode<K, V> = rootNode.rightChild ?: throw IllegalArgumentException("invalid argument")
        val rightLeft: AVLNode<K, V>? = right.leftChild

        //rotation
        right.leftChild = rootNode
        rootNode.rightChild = rightLeft

        //update tree root
        if (rootNode === root) {
            root = right
        }

        //update heights
        rootNode.height = 1 + max(height(rootNode.leftChild), height(rootNode.rightChild))
        right.height = 1 + max(height(right.leftChild), height(right.rightChild))

        //return new root
        return right
    }

    private fun getBalance(node: AVLNode<K, V>): Int {
        return height(node.leftChild) - height(node.rightChild)
    }

    private fun balancing(node: AVLNode<K, V>): AVLNode<K, V> {
        node.height = 1 + max(height(node.leftChild), height(node.rightChild))
        val balance = getBalance(node)

        if (balance == 2) {
            val left = node.leftChild ?: throw Exception("incorrect tree")
            if (getBalance(left) < 0) {
                node.leftChild = leftRotate(left)
            }
            return rightRotate(node)
        }
        if (balance == -2) {
            val right = node.rightChild ?: throw Exception("incorrect tree")
            if (getBalance(right) > 0) {
                node.rightChild = rightRotate(right)
            }
            return leftRotate(node)
        }
        return node
    }

    //find min element in tree with root in node
    private fun findMin(node: AVLNode<K, V>): AVLNode<K, V> {
        val leftNode = node.leftChild
        leftNode ?: return node
        return findMin(leftNode)
    }

    //remove min element in tree with root in node
    private fun removeMin(node: AVLNode<K, V>): AVLNode<K, V>? {
        val leftNode = node.leftChild
        leftNode ?: return node.rightChild
        node.leftChild = removeMin(leftNode)
        return balancing(node)
    }


    //insert in tree with root in node
    private fun insertAVL(node: AVLNode<K, V>?, key: K, newValue: V): AVLNode<K, V> {
        if (node == null) {
            val newNode = AVLNode(key, newValue)
            if (size == 0) {
                root = newNode
            }
            size++
            return newNode
        }
        if (key < node.key) {
            node.leftChild = insertAVL(node.leftChild, key, newValue)
        } else if (key > node.key) {
            node.rightChild = insertAVL(node.rightChild, key, newValue)
        } else {
            node.value = newValue
            return node
        }
        return balancing(node)
    }

    //remove in tree with root in node
    private fun removeAVL(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
        if (node == null) {
            return null
        }
        if (key < node.key) {
            node.leftChild = removeAVL(node.leftChild, key)
        } else if (key > node.key) {
            node.rightChild = removeAVL(node.rightChild, key)
        } else {
            size--
            val left = node.leftChild
            val right = node.rightChild
            if (right == null) {
                if (node === root) {
                    root = left
                }
                return left
            }
            val minNode = findMin(right)
            minNode.rightChild = removeMin(right)
            minNode.leftChild = left
            if (node === root) {
                root = balancing(minNode)
            }
            return balancing(minNode)
        }
        return balancing(node)
    }

    //find in tree with root in node
    private fun findAVL(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
        if (node == null) {
            return null
        }
        if (key < node.key) {
            return findAVL(node.leftChild, key)
        } else if (key > node.key) {
            return findAVL(node.rightChild, key)
        } else {
            return node
        }
    }

    override fun insert(key: K, newValue: V) {
        insertAVL(root, key, newValue)
    }

    override fun remove(key: K) {
        removeAVL(root, key)
    }

    override fun find(key: K): V? {
        return findAVL(root, key)?.value
    }
}
