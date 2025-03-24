package trees

import nodes.BSNode

class BSTree<K : Comparable<K>, V> : Tree<K, V, BSNode<K, V>>() {
    override fun insert(key: K, newValue: V) {
        if (size == 0) {
            root = BSNode(key, newValue)
            size++
            return
        } else {
            var currNode = root ?: throw Exception("Non-null object became null")
            var isAncFound = false
            val newNode = BSNode(key, newValue)

            while (!isAncFound) {
                if (key < currNode.key) {
                    if (currNode.leftChild == null) {
                        currNode.leftChild = newNode
                        isAncFound = true
                    } else currNode = currNode.leftChild ?: throw Exception("Non-null object became null")
                } else if (key > currNode.key) {
                    if (currNode.rightChild == null) {
                        currNode.rightChild = newNode
                        isAncFound = true
                    } else currNode = currNode.rightChild ?: throw Exception("Non-null object became null")
                } else {
                    currNode.value = newValue
                    return
                }
            }
        }
        size++
    }

    override fun remove(key: K) {
        var rmNode = root ?: return
        var parent = root ?: return

        val pair = findForRemove(key, rmNode, parent)
        parent = pair.first
        rmNode = pair.second

        var isLeft = parent.key > key

        if (rmNode.leftChild == null && rmNode.rightChild == null) {
            if (key == root?.key) {
                root = null
            } else {
                if (isLeft) parent.leftChild = null
                else parent.rightChild = null
            }
        } else if (rmNode.leftChild != null && rmNode.rightChild == null) {
            if (key == root?.key) {
                root = rmNode.leftChild
            } else {
                if (isLeft) parent.leftChild = rmNode.leftChild
                else parent.rightChild = rmNode.leftChild
            }
        } else if (rmNode.rightChild != null && rmNode.leftChild == null) {
            if (key == root?.key) {
                root = rmNode.rightChild
            } else {
                if (isLeft) parent.leftChild = rmNode.rightChild
                else parent.rightChild = rmNode.rightChild
            }
        } else {
            if (rmNode.rightChild?.leftChild == null) {
                rmNode.rightChild?.leftChild = rmNode.leftChild
                if (key == root?.key) {
                    root = root?.rightChild
                } else {
                    if (isLeft) parent.leftChild = rmNode.rightChild
                    else parent.rightChild = rmNode.rightChild
                }
                size--
                return
            }

            var replacement = rmNode.rightChild ?: throw Exception("Non-null object became null")
            var rpParent = rmNode

            while (replacement.leftChild != null) {
                rpParent = replacement
                replacement = replacement.leftChild ?: throw Exception("Non-null object became null")
            }

            rpParent.leftChild = replacement.rightChild
            replacement.leftChild = rmNode.leftChild
            replacement.rightChild = rmNode.rightChild

            if(key == root?.key) {
                root = replacement
            }
            if (isLeft) parent.leftChild = replacement
            else parent.rightChild = replacement
        }
        size--
        return
    }

    private fun findForRemove(
        key: K,
        removingNode: BSNode<K, V>,
        parent: BSNode<K, V>
    ): Pair<BSNode<K, V>, BSNode<K, V>> {
        var isFound = false
        var removingNode1 = removingNode
        var parent1 = parent
        while (!isFound) {
            if (key > removingNode1.key) {
                parent1 = removingNode1
                removingNode1 = removingNode1.rightChild ?: throw NoSuchElementException("Key not in tree")
            } else if (key < removingNode1.key) {
                parent1 = removingNode1
                removingNode1 = removingNode1.leftChild ?: throw NoSuchElementException("Key not in tree")
            } else {
                isFound = true
            }
        }
        return Pair(parent1, removingNode1)
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
