package trees

import nodes.BSNode

class BSTree<K : Comparable<K>, V> : Tree<K, V, BSNode<K, V>>() {
    override fun insert(key: K, newValue: V) {
        if (size == 0) {
            root = BSNode(key, newValue)
            size++
            return
        } else {
            var currNode = root ?: return
            var isAncFound = false
            val newNode = BSNode(key, newValue)
            while (!isAncFound) {
                if (key < currNode.key) {
                    if (currNode.leftChild == null) {
                        currNode.leftChild = newNode
                        isAncFound = true
                    } else currNode = currNode.leftChild ?: (root ?: return)
                } else if (key > currNode.key) {
                    if (currNode.rightChild == null) {
                        currNode.rightChild = newNode
                        isAncFound = true
                    } else currNode = currNode.rightChild ?: (root ?: return)
                } else {
                    currNode.value = newValue
                }
            }
        }
    }

    override fun remove(key: K) {}

    override fun find(key: K): V? {
        if (size == 0) return null
        var currNode = root
        var isFound = false
        while (!isFound) {
            if (currNode != null) {
                if (key > currNode.key) {
                    if (currNode.rightChild == null) return null
                    else currNode = currNode.rightChild ?: root
                } else if (key < currNode.key) {
                    if (currNode.leftChild == null) return null
                    else currNode = currNode.leftChild ?: root
                } else {
                    isFound = true
                }
            }
        }
        return currNode?.value
    }
}