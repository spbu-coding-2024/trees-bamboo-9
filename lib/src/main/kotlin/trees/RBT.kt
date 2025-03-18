package trees

import nodes.*

class RBT<K : Comparable<K>, V> : Tree<K, V, RBNode<K, V>>() {
    override fun find(key: K): V? {
        val node = findNode(key)
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
        } else if (node.rightChild==null || node.leftChild==null) {
            deleteBlackNodeWithOneChild(node)
        } else if (isBlackNode(node)) {
            deleteBlackNodeWithoutChildren(node)
        }
    }

    protected fun initTree(firstKey: K, fvalue: V) {
        root = RBNode(key = firstKey, value = fvalue)
        size++
    }

    protected fun initNewNode(newKey: K, value: V): RBNode<K, V>? {
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
        val uncleNode: RBNode<K, V>? = findUncleNode(parentNode)
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
            val rch = node.rightChild
            if (rch != null) {
                val nn = universalSwap(node, rch)
                nn.parent?.rightChild = null
            }
        } else {
            val rch = node.leftChild
            if (rch != null) {
                val nn = universalSwap(node, rch)
                nn.parent?.leftChild = null
            }
        }
    }

    private fun deleteBlackNodeWithoutChildren(node: RBNode<K, V>) {
        newBalance(node)
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
        val parentNode: RBNode<K, V>? = node.parent
        val uncleNode: RBNode<K, V>? = findUncleNode(parentNode)
        val grandFatherNode: RBNode<K, V>? = parentNode?.parent
        uncleNode?.color = Color.BLACK
        parentNode?.color = Color.BLACK
        if (grandFatherNode != null) {
            grandFatherNode.color = Color.RED
            chekRoot(grandFatherNode)
        }
        if (grandFatherNode!=null  && !isBlackNode(grandFatherNode.parent)) {
            balanceAfterInsert(grandFatherNode)
        }
    }

    private fun balanceIfUncleRightChild(node: RBNode<K, V>) {
        var parentNode: RBNode<K, V>? = node.parent
        if (parentNode?.leftChild?.key != node.key) {
            rotateLeft(node)
            parentNode = node
        }
        parentNode.color = Color.BLACK
        parentNode.parent?.color = Color.RED
        rotateRight(parentNode)
        chekRoot(parentNode)
    }

    private fun balanceIfUncleLeftChild(node: RBNode<K, V>) {
        var parentNode: RBNode<K, V>? = node.parent
        if (parentNode?.rightChild?.key != node.key) {
            rotateRight(node)
            parentNode = node
        }
        parentNode.color = Color.BLACK
        parentNode.parent?.color = Color.RED
        rotateLeft(parentNode)
        chekRoot(parentNode)
    }

    private fun replaceNodeWithMaxNodeOfLeftSubtree(node: RBNode<K, V>, leftch: RBNode<K, V>): RBNode<K, V> {
        var vsnode: RBNode<K, V> = leftch
        var rch = vsnode.rightChild
        while (rch != null) {
            vsnode = rch
            rch = vsnode.rightChild
        }
        val newNode = universalSwap(node, vsnode)
        return newNode
    }

    private fun newBalance(node: RBNode<K, V>) {
        if (node.parent == null) return
        val bro:RBNode<K,V>?
        if (isLeftChild(node)) {
            bro=node.parent?.rightChild
        } else {
            bro=node.parent?.leftChild
        }
        if (bro != null) {
            if (!isBlackNode(bro)) {
                balanceIfRedBrother(bro)
                newBalance(node)
            } else {
                if (isBlackNode(bro.leftChild) && isBlackNode(bro.rightChild)) {
                    balanceIfBrotherHasTwoBlackChildren(bro)
                } else {
                    balanceIfBrotherHasOneRedChild(bro)
                }
            }
        }
    }

    private fun isBlackNode(node: RBNode<K,V>?) : Boolean {
        return (node==null || (node.color == Color.BLACK))
    }

    private fun balanceIfBrotherHasTwoBlackChildren(bro: RBNode<K, V>) {
        bro.color = Color.RED
        val par = bro.parent
        if (par!=null) {
            if (isBlackNode(par)) {
                newBalance(par)
            } else {
                par.color = Color.BLACK
            }
        }

    }

    private fun balanceIfRedBrother(bro: RBNode<K, V>) {
        bro.color = Color.BLACK
        bro.parent?.color = Color.RED
        if (isLeftChild(bro)) {
            rotateRight(bro)
        } else {
            rotateLeft(bro)
        }
        chekRoot(bro)
    }

    private fun balanceIfBrotherHasOneRedChild(bro: RBNode<K, V>) {
        if (isLeftChild(bro)) {
            if (!isBlackNode(bro.leftChild)) {
                fixik(bro)
            } else if (!isBlackNode(bro.rightChild)) {
                val rch = bro.rightChild
                if (rch != null) {
                    rch.color = Color.BLACK
                    bro.color = Color.RED
                    rotateLeft(rch)
                    fixik(rch)
                }
            }
        } else {
            if (!isBlackNode(bro.rightChild)) {
                fixik2(bro)
            } else if (!isBlackNode(bro.leftChild)) {
                val lch = bro.leftChild
                if (lch != null) {
                    lch.color = Color.BLACK
                    bro.color = Color.RED
                    rotateRight(lch)
                    fixik2(lch)
                }
            }
        }
    }

    private fun universalSwap(node1: RBNode<K, V>, node2: RBNode<K, V>): RBNode<K, V> {
        uswap(node1, node2.key, node2.value)
        return uswap(node2, node1.key, node1.value)
    }

    private fun findUncleNode(parent: RBNode<K, V>?): RBNode<K, V>? {
        if (isLeftChild(parent)) {
            return parent?.parent?.rightChild
        } else {
            return parent?.parent?.leftChild
        }
    }

    private fun fixik(bro: RBNode<K, V>) {
        bro.color = bro.parent?.color ?: Color.BLACK
        bro.parent?.color = Color.BLACK
        bro.leftChild?.color = Color.BLACK
        rotateRight(bro)
        chekRoot(bro)
    }

    private fun fixik2(bro: RBNode<K, V>) {
        bro.color = bro.parent?.color ?: Color.BLACK
        bro.parent?.color = Color.BLACK
        bro.rightChild?.color = Color.BLACK
        rotateLeft(bro)
        chekRoot(bro)
    }

    private fun chekRoot(node: RBNode<K, V>) {
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
        switchParrentsChild(isLeftChild(parent), node)
        parent?.parent = node
    }

    private fun rotateRight(node: RBNode<K, V>) {
        val parent = node.parent
        node.rightChild?.parent = parent
        parent?.leftChild = node.rightChild
        node.rightChild = parent
        node.parent = parent?.parent
        switchParrentsChild(isLeftChild(parent), node)
        parent?.parent = node
    }

    private fun switchParrentsChild(isLeftChild: Boolean, node: RBNode<K, V>) {
        if (isLeftChild) {
            node.parent?.leftChild = node
        } else {
            node.parent?.rightChild = node
        }
    }

    private fun uswap(node1: RBNode<K, V>, nkey: K, nval: V): RBNode<K, V> {
        val newNode = RBNode(nkey, nval)
        newNode.color = node1.color
        newNode.leftChild = node1.leftChild
        newNode.rightChild = node1.rightChild
        newNode.parent = node1.parent
        newNode.leftChild?.parent = newNode
        newNode.rightChild?.parent = newNode
        if (isLeftChild(node1)) {
            newNode.parent?.leftChild = newNode
        } else {
            newNode.parent?.rightChild = newNode
        }
        chekRoot(newNode)
        return newNode
    }
}
