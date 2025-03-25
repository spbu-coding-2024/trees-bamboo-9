package trees

import nodes.*

class RBT<K : Comparable<K>, V> : Tree<K, V, RBNode<K, V>>() {
    override fun find(key: K): V? {
        if(root==null) return null
        val node = findNode(key)
        if(node.key != key){
            return null
        }
        return node.value
    }

    internal fun root(): RBNode<K, V>? = root
    fun size(): Int = size
    override fun insert(key: K, newValue: V) {
        if (size == 0) {
            initTree(key, newValue)
        } else {
            val newNode = initNewNode(key, newValue)
            if (newNode != null) {
                size++
                if (newNode.parent?.color == Color.RED) {
                    balanceAfterInsert(newNode)
                }
            }
        }
    }

    override fun remove(key: K) {
        if (root == null) return
        var node = findNode(key)
        if (node.key != key) return
        size--
        if (size == 0) {
            root = null
            return
        }
        if (node.leftChild != null && node.rightChild != null) {
            val leftChild = node.leftChild
            if (leftChild != null) {
                node = replaceNodeWithMaxNodeOfLeftSubtree(node, leftChild)
            }

        }
        if (node.rightChild==null && node.leftChild==null && !isBlackNode(node)) {
            deleteRedNode(node)
        } else if (node.rightChild!=null || node.leftChild!=null && isBlackNode(node)) {
            deleteBlackNodeWithOneChild(node)
        } else {
            deleteBlackNodeWithoutChildren(node)
        }
    }

    private fun initTree(firstKey: K, fvalue: V) {//Make first node in Tree
        root = RBNode(key = firstKey, value = fvalue)
        size++
    }

    private fun initNewNode(newKey: K, value: V): RBNode<K, V>? {//Make New Node
        val newNode: RBNode<K, V> = findNode(newKey)
        if (newNode.key == newKey) {
            newNode.value = value
            return null
        } else if (newNode.key < newKey) {
            newNode.rightChild = RBNode(newKey, value)
            newNode.rightChild?.parent = newNode
            return newNode.rightChild
        } else {
            newNode.leftChild = RBNode(newKey, value)
            newNode.leftChild?.parent = newNode
            return newNode.leftChild
        }
    }

    private fun findNode(key: K): RBNode<K, V> {
        var currentNode = root
        while (true) {
            if (currentNode != null) {
                if (key < currentNode.key) {
                    currentNode = currentNode.leftChild ?: return currentNode
                } else if (key > currentNode.key) {
                    currentNode = currentNode.rightChild ?: return currentNode
                } else {
                    return currentNode
                }
            }
        }
    }

    private fun balanceAfterInsert(node: RBNode<K, V>) {
        val parentNode: RBNode<K, V>? = node.parent
        val uncleNode: RBNode<K, V>? = findBrother(parentNode)
        if (isBlackNode(uncleNode)) {
            balanceIfBlackUncle(node)
        } else {
            balanceIfRedUncle(node)
        }
    }

    private fun deleteRedNode(node: RBNode<K, V>) {
        if (node.parent?.leftChild?.key == node.key) {
            node.parent?.leftChild = null
        } else {
            node.parent?.rightChild = null
        }
    }

    private fun deleteBlackNodeWithOneChild(node: RBNode<K, V>) {
        if (node.rightChild != null) {
            val rightChild = node.rightChild
            if (rightChild != null) {
                val newNodeWithSameKey = swapNodes(node, rightChild)
                newNodeWithSameKey.parent?.rightChild = null
            }
        } else {
            val leftChild = node.leftChild
            if (leftChild != null) {
                val newNodeWithSameKey = swapNodes(node, leftChild)
                newNodeWithSameKey.parent?.leftChild = null
            }
        }
    }

    private fun deleteBlackNodeWithoutChildren(node: RBNode<K, V>) {
        balanceAfterRemove(node)
        if (node.parent?.leftChild?.key == node.key) {
            node.parent?.leftChild = null
        } else {
            node.parent?.rightChild = null
        }
    }

    private fun balanceIfBlackUncle(node: RBNode<K, V>) {
        if (isLeftChild(node.parent)) {
            balanceIfUncleRightChild(node)
        } else {
            balanceIfUncleLeftChild(node)
        }
    }

    private fun balanceIfRedUncle(node: RBNode<K, V>) {//сократи
        val parent: RBNode<K, V>? = node.parent
        val uncle: RBNode<K, V>? = findBrother(parent)
        val grandFather: RBNode<K, V>? = parent?.parent
        uncle?.color = Color.BLACK
        parent?.color = Color.BLACK
        if (grandFather != null) {
            grandFather.color = Color.RED
            checkNodeForRootAfterBalancing(grandFather)
        }
        if (grandFather!=null  && !isBlackNode(grandFather.parent)) {
            balanceAfterInsert(grandFather)
        }
    }

    private fun balanceIfUncleRightChild(node: RBNode<K, V>) {
        var parent: RBNode<K, V>? = node.parent
        if (parent?.leftChild?.key != node.key) {
            rotateLeft(node)
            parent = node
        }
        parent.color = Color.BLACK
        parent.parent?.color = Color.RED
        rotateRight(parent)
        checkNodeForRootAfterBalancing(parent)
    }

    private fun balanceIfUncleLeftChild(node: RBNode<K, V>) {
        var parent: RBNode<K, V>? = node.parent
        if (parent?.rightChild?.key != node.key) {
            rotateRight(node)
            parent = node
        }
        parent.color = Color.BLACK
        parent.parent?.color = Color.RED
        rotateLeft(parent)
        checkNodeForRootAfterBalancing(parent)
    }

    private fun replaceNodeWithMaxNodeOfLeftSubtree(node: RBNode<K, V>, leftChild: RBNode<K, V>): RBNode<K, V> {
        var currentNode: RBNode<K, V> = leftChild
        var currentRightChild = currentNode.rightChild
        while (currentRightChild != null) {
            currentNode = currentRightChild
            currentRightChild = currentNode.rightChild
        }
        val nodeWithNewPlace = swapNodes(node, currentNode)
        return nodeWithNewPlace
    }

    private fun balanceAfterRemove(node: RBNode<K, V>) {
        if (node.parent == null) return
        val brother=findBrother(node)
        if (brother != null) {
            if (!isBlackNode(brother)) {
                balanceIfRedBrother(brother)
                balanceAfterRemove(node)
            } else {
                if (isBlackNode(brother.leftChild) && isBlackNode(brother.rightChild)) {
                    balanceIfBrotherHasTwoBlackChildren(brother)
                } else {
                    balanceIfBrotherHasOneRedChild(brother)
                }
            }
        }
    }

    private fun isBlackNode(node: RBNode<K,V>?) : Boolean {
        return (node==null || (node.color == Color.BLACK))
    }

    private fun balanceIfBrotherHasTwoBlackChildren(brother: RBNode<K, V>) {
        brother.color = Color.RED
        val parent = brother.parent
        if (parent!=null) {
            if (isBlackNode(parent)) {
                balanceAfterRemove(parent)
            } else {
                parent.color = Color.BLACK
            }
        }

    }

    private fun balanceIfRedBrother(brother: RBNode<K, V>) {
        brother.color = Color.BLACK
        brother.parent?.color = Color.RED
        if (isLeftChild(brother)) {
            rotateRight(brother)
        } else {
            rotateLeft(brother)
        }
        checkNodeForRootAfterBalancing(brother)
    }

    private fun balanceIfBrotherHasOneRedChild(brother: RBNode<K, V>) {
        if (isLeftChild(brother)) {
            if (!isBlackNode(brother.leftChild)) {
                fixTreeIfBrotherLeftChild(brother)
            } else if (!isBlackNode(brother.rightChild)) {
                val rightChild = brother.rightChild
                if (rightChild != null) {
                    rightChild.color = Color.BLACK
                    brother.color = Color.RED
                    rotateLeft(rightChild)
                    fixTreeIfBrotherLeftChild(rightChild)
                }
            }
        } else {
            if (!isBlackNode(brother.rightChild)) {
                fixTreeIfBrotherRightChild(brother)
            } else if (!isBlackNode(brother.leftChild)) {
                val leftChild = brother.leftChild
                if (leftChild != null) {
                    leftChild.color = Color.BLACK
                    brother.color = Color.RED
                    rotateRight(leftChild)
                    fixTreeIfBrotherRightChild(leftChild)
                }
            }
        }
    }

    private fun swapNodes(node1: RBNode<K, V>, node2: RBNode<K, V>): RBNode<K, V> {
        makeNodeWithSameLinksAndCertainKey(node1, node2.key, node2.value)
        return makeNodeWithSameLinksAndCertainKey(node2, node1.key, node1.value)
    }

    private fun findBrother(node: RBNode<K, V>?): RBNode<K, V>? {
        if (isLeftChild(node)) {
            return node?.parent?.rightChild
        } else {
            return node?.parent?.leftChild
        }
    }

    private fun fixTreeIfBrotherLeftChild(node: RBNode<K, V>) {
        node.color = node.parent?.color ?: Color.BLACK
        node.parent?.color = Color.BLACK
        node.leftChild?.color = Color.BLACK
        rotateRight(node)
        checkNodeForRootAfterBalancing(node)
    }

    private fun fixTreeIfBrotherRightChild(node: RBNode<K, V>) {
        node.color = node.parent?.color ?: Color.BLACK
        node.parent?.color = Color.BLACK
        node.rightChild?.color = Color.BLACK
        rotateLeft(node)
        checkNodeForRootAfterBalancing(node)
    }

    private fun checkNodeForRootAfterBalancing(node: RBNode<K, V>) {
        if (node.parent == null) {
            root = node
            node.color = Color.BLACK
        }
    }

    private fun isLeftChild(node: RBNode<K, V>?): Boolean {
        return node?.parent?.leftChild?.key == node?.key
    }

    private fun rotateLeft(node: RBNode<K, V>) {
        val parent = node.parent
        node.leftChild?.parent = parent
        parent?.rightChild = node.leftChild
        node.leftChild = parent
        node.parent = parent?.parent
        switchParentsChild(isLeftChild(parent), node)
        parent?.parent = node
    }

    private fun rotateRight(node: RBNode<K, V>) {
        val parent = node.parent
        node.rightChild?.parent = parent
        parent?.leftChild = node.rightChild
        node.rightChild = parent
        node.parent = parent?.parent
        switchParentsChild(isLeftChild(parent), node)
        parent?.parent = node
    }

    private fun switchParentsChild(isLeftChild: Boolean, node: RBNode<K, V>) {
        if (isLeftChild) {
            node.parent?.leftChild = node
        } else {
            node.parent?.rightChild = node
        }
    }

    private fun makeNodeWithSameLinksAndCertainKey(node: RBNode<K, V>, nkey: K, nval: V): RBNode<K, V> {
        val newNode = RBNode(nkey, nval)
        newNode.color = node.color
        newNode.leftChild = node.leftChild
        newNode.rightChild = node.rightChild
        newNode.parent = node.parent
        newNode.leftChild?.parent = newNode
        newNode.rightChild?.parent = newNode
        if (isLeftChild(node)) {
            newNode.parent?.leftChild = newNode
        } else {
            newNode.parent?.rightChild = newNode
        }
        checkNodeForRootAfterBalancing(newNode)
        return newNode
    }
}
