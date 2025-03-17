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
                    } else currNode = currNode.leftChild ?: return
                } else if (key > currNode.key) {
                    if (currNode.rightChild == null) {
                        currNode.rightChild = newNode
                        isAncFound = true
                    } else currNode = currNode.rightChild ?: return
                } else {
                    currNode.value = newValue
                    isAncFound = true
                }
            }
        }
    }

    override fun remove(key: K) {
        var removingNode = root ?: return
        var isFound = false
        var parent = root ?: return

        while (!isFound) {
            if (key > removingNode.key) {
                parent = removingNode
                removingNode = removingNode.rightChild ?: return
            } else if (key < removingNode.key) {
                parent = removingNode
                removingNode = removingNode.leftChild ?: return
            } else {
                isFound = true
            }
        }

        var isLeft = false
        if (parent.key > key) isLeft = true

        if (removingNode.leftChild == null && removingNode.rightChild == null) {
            if (isLeft) parent.leftChild = null
            else parent.rightChild = null
        } else if (removingNode.leftChild != null && removingNode.rightChild == null) {
            if (isLeft) parent.leftChild = removingNode.leftChild
            else parent.rightChild = removingNode.leftChild
        } else if (removingNode.rightChild != null && removingNode.leftChild == null) {
            if (isLeft) parent.leftChild = removingNode.rightChild
            else parent.rightChild = removingNode.rightChild
        } else {
            if (removingNode.rightChild?.leftChild == null) {
                removingNode.rightChild?.leftChild = removingNode.leftChild
                if (key == root?.key) {
                    root = removingNode.rightChild
                } else {
                    parent.rightChild = removingNode
                }
                size--
                return
            }

            var replacement = removingNode.rightChild ?: return
            var rpParent = removingNode

            while (replacement.leftChild != null) {
                rpParent = replacement
                replacement = replacement.leftChild ?: return
            }

            rpParent.leftChild = replacement.rightChild
            replacement.leftChild = removingNode.leftChild
            replacement.rightChild = removingNode.rightChild

            if (isLeft) parent.leftChild = replacement
            else parent.rightChild = replacement
        }
        size--
        return
    }

    override fun find(key: K): V? {
        var currNode = root ?: return null
        var isFound = false

        while (!isFound) {
            if (key > currNode.key) {
                currNode = currNode.rightChild ?: return null
            } else if (key < currNode.key) {
                currNode = currNode.leftChild ?: return null
            } else {
                isFound = true
            }
        }

        return currNode.value
    }
}
