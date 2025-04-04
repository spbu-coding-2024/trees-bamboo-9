package trees

import nodes.BSNode

class BSTree<K : Comparable<K>, V> : Tree<K, V, BSNode<K, V>>() {
    override fun insert(key: K, newValue: V) {
        if (root == null) {
            root = BSNode(key, newValue)
            size++
            return
        }

        var currNode = root ?: throw IllegalStateException("Root of initialized tree became null")
        var isAncFound = false
        val newNode = BSNode(key, newValue)

        while (!isAncFound) {
            if (key < currNode.key) {
                if (currNode.leftChild == null) {
                    currNode.leftChild = newNode
                    isAncFound = true
                } else currNode = currNode.leftChild ?: throw IllegalStateException("Existing vertex became null")
            } else if (key > currNode.key) {
                if (currNode.rightChild == null) {
                    currNode.rightChild = newNode
                    isAncFound = true
                } else currNode = currNode.rightChild ?: throw IllegalStateException("Existing vertex became null")
            } else {
                currNode.value = newValue
                return
            }
        }
        size++
    }

    override fun remove(key: K) {
        var parent = root ?: throw NoSuchElementException("Key not in tree: $key")
        var rmNode = root ?: throw NoSuchElementException("Key not in tree: $key")

        findForRemove(key, rmNode, parent).let {
            parent = it.first
            rmNode = it.second
        }

        val isLeft = parent.key > key

        when {
            rmNode.leftChild == null && rmNode.rightChild == null -> {
                if (key == root?.key) {
                    root = null
                } else {
                    if (isLeft) parent.leftChild = null
                    else parent.rightChild = null
                }
            }

            rmNode.leftChild != null && rmNode.rightChild == null -> {
                if (key == root?.key) {
                    root = rmNode.leftChild
                } else {
                    if (isLeft) parent.leftChild = rmNode.leftChild
                    else parent.rightChild = rmNode.leftChild
                }
            }

            rmNode.rightChild != null && rmNode.leftChild == null -> {
                if (key == root?.key) {
                    root = rmNode.rightChild
                } else {
                    if (isLeft) parent.leftChild = rmNode.rightChild
                    else parent.rightChild = rmNode.rightChild
                }
            }

            rmNode.rightChild?.leftChild == null -> {
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

            else -> {
                var replacement = rmNode.rightChild ?: throw IllegalStateException("Existing vertex became null")
                var rpParent = rmNode

                while (replacement.leftChild != null) {
                    rpParent = replacement
                    replacement = replacement.leftChild ?: throw IllegalStateException("Existing vertex became null")
                }
                rpParent.leftChild = replacement.rightChild
                replacement.let {
                    it.leftChild = rmNode.leftChild
                    it.rightChild = rmNode.rightChild
                }

                if (key == root?.key) {
                    root = replacement
                }
                if (isLeft) parent.leftChild = replacement
                else parent.rightChild = replacement
            }
        }
        size--
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
            when {
                key > removingNode1.key -> {
                    parent1 = removingNode1
                    removingNode1 = removingNode1.rightChild ?: throw NoSuchElementException("Key not in tree: $key")
                }

                key < removingNode1.key -> {
                    parent1 = removingNode1
                    removingNode1 = removingNode1.leftChild ?: throw NoSuchElementException("Key not in tree: $key")
                }

                else -> isFound = true
            }
        }
        return Pair(parent1, removingNode1)
    }

    override fun find(key: K): V? {
        var currNode = root ?: return null
        var isFound = false

        while (!isFound) {
            when {
                key > currNode.key -> currNode = currNode.rightChild ?: return null
                key < currNode.key -> currNode = currNode.leftChild ?: return null
                else -> isFound = true
            }
        }

        return currNode.value
    }
}
