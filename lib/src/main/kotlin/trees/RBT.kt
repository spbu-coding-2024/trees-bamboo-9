package binaryTrees

import Nodes.*
import jdk.javadoc.internal.doclets.toolkit.util.DocPath.parent
import kotlin.collections.ArrayDeque

enum class Mode {
    ADD, REMOVE
}

class RBT<K : Comparable<K>, V> : Tree<K, V>, Iterable<RBNode<K, V>> { //Red Black Tree
    private var size:Int = 0
    private var root: RBNode<K, V>? = null
    override fun iterator(): Iterator<RBNode<K, V>> {
        return object : Iterator<RBNode<K, V>> {
            var wasRootInQueue = false
            val rootCopy = root
            val queue: ArrayDeque<RBNode<K, V>> = ArrayDeque()
            override fun hasNext(): Boolean {
                if (!wasRootInQueue) {
                    if (rootCopy != null) {
                        queue.add(rootCopy)
                        wasRootInQueue = true
                        bfs(rootCopy)
                    } else {
                        return false
                    }
                }
                return queue.isNotEmpty()
            }
            override fun next(): RBNode<K, V> {
                return queue.removeFirst()
            }

            fun bfs(node: RBNode<K, V>) {
                val leftChild = node.leftChild
                val rightChild = node.rightChild
                if(leftChild!=null){
                    queue.add(leftChild)
                    bfs(leftChild)
                }
                if(rightChild!=null){
                    queue.add(rightChild)
                    bfs(rightChild)
                }
            }
        }
    }

    internal fun root(): RBNode<K, V>? = root
    fun size():Int = size
    override fun insert(newKey: K, value: V) {
        if (size == 0) {
            initTree(newKey, value)
        } else {
            val newNode = initNewNode(newKey, value)
            if (newNode != null) {
                size++
                if (newNode.parent?.color == Color.RED) {
                    balanceAfterInsert(newNode)
                }
            }
        }
    }

    override fun remove(newKey: K): V? {
        val node = findNode(Mode.REMOVE, newKey)
        if (node == null) {
            return null
        }
        size--
        if (size == 0 ) {
            root = null
            return node.value
        }
        if (node.leftChild != null && node.rightChild != null) {
            replaceNodeWithMaxNodeOfLeftSubtree(node)
        }
        if (node.rightChild == null && node.leftChild == null && node.color == Color.RED) {
            deleteRedNode(node)
        } else if (node.rightChild != null || node.leftChild != null && node.color == Color.BLACK) {
            deleteBlackNodeWithOneChild(node)
        } else if (node.rightChild == null && node.leftChild == null && node.color == Color.BLACK) {
            deleteBlackNodeWithoutChildren(node)
        }
        return node.value
    }

    protected fun initTree(firstKey: K, fvalue: V) {
        root = RBNode(key = firstKey, value = fvalue)
        size++
    }

    protected fun initNewNode(newKey: K, value: V): RBNode<K, V>? {
        val newNode: RBNode<K, V>? = findNode(Mode.ADD, newKey, value)
        return newNode
    }

    fun findNode(mode: Mode, key: K, value: V? = null): RBNode<K, V>? {
        if(root==null) return null
        var currentNode = root
        var isFinded = false
        while (true) {
            if (currentNode != null) {
                if (key < currentNode.key) {
                    if (mode == Mode.ADD && currentNode.leftChild == null && value != null) {
                        return addNewLeftNode(currentNode, key, value)
                    }
                    currentNode = currentNode.leftChild ?: return null
                } else if (key > currentNode.key) {
                    if (mode == Mode.ADD && currentNode.rightChild == null && value != null) {
                        return addNewRightNode(currentNode, key, value)
                    }
                    currentNode = currentNode.rightChild ?: return null
                } else {
                    if (mode == Mode.ADD && value != null) {
                        currentNode.value = value
                        return null
                    }
                    return currentNode
                }
            }
        }
    }

    protected fun balanceAfterInsert(node: RBNode<K, V>) {
        val uncleNode: RBNode<K, V>?
        val parentNode: RBNode<K, V>? = node.parent
        if (isLeftChild(parentNode)) {
            uncleNode = parentNode?.parent?.rightChild
        } else {
            uncleNode = parentNode?.parent?.leftChild
        }
        if (uncleNode?.color == Color.BLACK || uncleNode == null) {
            balanceIfBlackUncle(node)
        } else {
            balanceIfRedUncle(node)
        }
    }

    fun deleteRedNode(node: RBNode<K, V>) {
        if (node.parent?.leftChild?.key == node.key) {
            node.parent?.leftChild = null
        } else {
            node.parent?.rightChild = null
        }
    }

    fun deleteBlackNodeWithOneChild(node: RBNode<K, V>) {
        if (node.rightChild != null) {
            if (node.key == root?.key) {
                root = node.rightChild
            }
            swap(node.rightChild, node)
            node.parent?.rightChild = null
        } else {
            if (node.key == root?.key) {
                root = node.leftChild
            }
            swap(node.leftChild, node)
            node.parent?.leftChild = null
        }
    }

    fun deleteBlackNodeWithoutChildren(node: RBNode<K, V>) {
        newBalance(node)
        if (node.parent?.leftChild?.key == node.key) {
            node.parent?.leftChild = null
        } else {
            node.parent?.rightChild = null
        }
    }

    fun balanceIfBlackUncle(node: RBNode<K, V>) {
        if (isLeftChild(node.parent)) {
            balanceIfUncleRightChild(node)
        } else {
            balanceIfUncleLeftChild(node)
        }
    }

    fun balanceIfRedUncle(node: RBNode<K, V>) {//сократи
        val uncleNode: RBNode<K, V>?
        val parentNode: RBNode<K, V>? = node.parent
        val grandFatherNode: RBNode<K, V>? = parentNode?.parent
        if(isLeftChild(parentNode)) {
            uncleNode = parentNode?.parent?.rightChild
        }else{
            uncleNode = parentNode?.parent?.leftChild
        }
        uncleNode?.color = Color.BLACK
        parentNode?.color = Color.BLACK
        if(grandFatherNode!=null){
            grandFatherNode.color = Color.RED
            chekRoot(grandFatherNode)
        }
        if (grandFatherNode?.key != root?.key && grandFatherNode?.parent?.color == Color.RED) {
            balanceAfterInsert(grandFatherNode)
        }
    }
    fun balanceIfUncleRightChild(node: RBNode<K, V>) {
        var parentNode: RBNode<K, V>? = node.parent
        if (parentNode?.leftChild?.key != node.key) {
            rotateLeft(node)
            parentNode = node
        }
        parentNode.color = Color.BLACK
        parentNode.parent?.color  = Color.RED
        fixTree2(parentNode)
    }

    fun balanceIfUncleLeftChild(node: RBNode<K, V>) {
        var parentNode: RBNode<K, V>? = node.parent
        if (parentNode?.rightChild?.key != node.key) {
            rotateRight(node)
            parentNode = node
        }
        parentNode.color = Color.BLACK
        parentNode.parent?.color  = Color.RED
        fixTree1(parentNode)
    }

    fun replaceNodeWithMaxNodeOfLeftSubtree(node: RBNode<K, V>) {
        var vsnode = node.leftChild
        while (vsnode?.rightChild != null) {
            vsnode = vsnode.rightChild
        }
        if (vsnode != null) {
            swap(node, vsnode)
            if (node.key == root?.key) {
                root = vsnode
            }
        }
    }

    fun fixTree1(node: RBNode<K,V> , repeatCount:Int = 0, colorSwapCount:Int = 0) {
        repeat(repeatCount) {
            rotateRight(node)
        }
        rotateLeft(node)
        repeat(colorSwapCount){
            changeColor1(node)
        }
        chekRoot(node)
    }
    fun fixTree2(node: RBNode<K,V>, repeatCount:Int = 0, colorSwapCount:Int = 0) {
        repeat(repeatCount) {
            rotateLeft(node)
        }
        rotateRight(node)
        repeat(colorSwapCount){
            changeColor1(node)
        }
        chekRoot(node)
    }

    fun chekRoot(node: RBNode<K, V>) {
        if (node.parent == null) {
            root = node
            node.color = Color.BLACK
        }
    }

    fun changeColor1(node: RBNode<K, V>) {
        node.color = Color.RED
        node.leftChild?.color = Color.BLACK
        node.rightChild?.color = Color.BLACK
    }

    fun addNewLeftNode(currentNode: RBNode<K,V>, key: K, value: V) : RBNode<K,V>? {
        currentNode.leftChild = RBNode(key, value)
        currentNode.leftChild?.parent = currentNode
        return currentNode.leftChild
    }

    fun addNewRightNode(currentNode: RBNode<K,V>, key: K,value: V) : RBNode<K,V>? {
        currentNode.rightChild = RBNode(key, value)
        currentNode.rightChild?.parent = currentNode
        return currentNode.rightChild
    }

    fun swapColors(node1: RBNode<K, V>, node2: RBNode<K, V>) {
        val c1 = node1.color
        val c2 = node2.color
        node1.color = c2
        node2.color = c1
    }
    fun swap(node1: RBNode<K, V>?, node2: RBNode<K, V>) {
        if (node1 != null) {
            swapColors(node1, node2)
            if (isChild(node1, node2)) {
                swapChildAndParent(node1, node2)
                return
            } else if (isChild(node2, node1)) {
                swapChildAndParent(node2, node1)
                return
            }else{
                simpleSwap(node1, node2)
            }
        }
    }

    fun simpleSwap(node1: RBNode<K,V>,node2: RBNode<K, V>){
        val parent1 = node1.parent
        val lch1 = node1.leftChild
        val rch1 = node1.rightChild
        node1.leftChild = node2.leftChild
        node1.rightChild = node2.rightChild
        node1.parent = node2.parent
        node1.leftChild?.parent = node1
        node1.rightChild?.parent = node1
        lch1?.parent = node2
        rch1?.parent = node2
        node2.leftChild = lch1
        node2.rightChild = rch1
        switchParrentsChild(isLeftChild(node2),node1)
        node2.parent = parent1
        if (parent1?.leftChild?.key == node1.key) {
            node2.parent?.leftChild = node2
        } else {
            node2.parent?.rightChild = node2
        }
    }

    fun isLeftChild(node: RBNode<K, V>?): Boolean {
        if (node?.parent?.leftChild?.key == node?.key) {
            return true
        }
        return false
    }

    fun rotateLeft(node: RBNode<K, V>) {
        val parent = node.parent
        node.leftChild?.parent = parent
        parent?.rightChild = node.leftChild
        node.leftChild = parent
        node.parent = parent?.parent
        switchParrentsChild(isLeftChild(parent), node)
        parent?.parent = node
    }

    fun rotateRight(node: RBNode<K, V>) {
        val parent = node.parent
        node.rightChild?.parent = parent
        parent?.leftChild = node.rightChild
        node.rightChild = parent
        node.parent = parent?.parent
        switchParrentsChild(isLeftChild(parent), node)
        parent?.parent = node
    }

    fun isChild(node1: RBNode<K, V>?, node2: RBNode<K, V>?): Boolean {
        if (node1?.parent == node2) {
            return true
        }
        return false
    }

    fun swapChildAndParent(node1: RBNode<K, V>?, node2: RBNode<K, V>?) {
        if (node1 != null && node2 != null) {
            if (isLeftChild(node1)) {
                swapLeftChildWithParent(node1, node2)
            } else {
                swapRightChildWithParent(node1,node2)
            }
        }
    }

    fun swapLeftChildWithParent(node1: RBNode<K, V>, node2: RBNode<K, V>) {
        node2.leftChild = node1.leftChild
        node1.leftChild = node2
        node1.parent = node2.parent
        node2.leftChild?.parent = node2
        val rch = node1.rightChild
        node1.rightChild = node2.rightChild
        node1.rightChild?.parent = node1
        node2.rightChild = rch
        rch?.parent = node2
        switchParrentsChild(isLeftChild(node2), node1)
        node2.parent = node1
    }

    fun swapRightChildWithParent(node1: RBNode<K, V>, node2: RBNode<K, V>) {
        node2.rightChild = node1.rightChild
        node1.rightChild = node2
        node1.parent = node2.parent
        node2.rightChild?.parent = node2
        val rch = node1.leftChild
        node1.leftChild = node2.leftChild
        node1.leftChild?.parent = node1
        node2.leftChild = rch
        rch?.parent = node2
        switchParrentsChild(isLeftChild(node2), node1)
        node2.parent = node1
    }

    fun switchParrentsChild(isLeftChild: Boolean, node: RBNode<K, V>) {
        if (isLeftChild) {
            node.parent?.leftChild = node
        } else {
            node.parent?.rightChild = node
        }
    }

    fun newBalance(node: RBNode<K, V>) {
        if (node.parent == null) return
        val bro = findBro(node)
        if (bro != null) {
            if (bro.color == Color.RED) {
                bro.color = Color.BLACK
                bro.parent?.color = Color.RED
                if (isLeftChild(bro)) {
                    rotateRight(bro)
                } else {
                    rotateLeft(bro)
                }
                chekRoot(bro)
                newBalance(node)
            } else {
                if ((bro.rightChild?.color == Color.BLACK || bro.rightChild == null) && (bro.leftChild?.color == Color.BLACK || bro.leftChild == null)) {
                    bro.color = Color.RED
                    val par = bro.parent
                    if (par?.color == Color.BLACK) {
                        newBalance(par)
                    }else{
                        par?.color = Color.BLACK
                    }
                } else {
                    if (isLeftChild(bro)) {
                        if (bro.leftChild?.color == Color.RED) {
                            fixik(bro)
                        } else if (bro.rightChild?.color == Color.RED) {
                            val rch = bro.rightChild
                            if (rch != null) {
                                rch.color = Color.BLACK
                                bro.color = Color.RED

                                rotateLeft(rch)
                                fixik(rch)
                            }
                        }
                    } else {
                        if (bro.rightChild?.color == Color.RED) {
                            fixik2(bro)
                        } else if (bro.leftChild?.color == Color.RED) {
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
            }
        }
    }

    fun fixik2(bro: RBNode<K, V>){
        bro.color=bro.parent?.color?:Color.BLACK
        bro.parent?.color=Color.BLACK
        bro.rightChild?.color=Color.BLACK
        rotateLeft(bro)
        chekRoot(bro)
    }

    fun fixik(bro:RBNode<K, V>){
        bro.color=bro.parent?.color?:Color.BLACK
        bro.parent?.color=Color.BLACK
        bro.leftChild?.color=Color.BLACK
        rotateRight(bro)
        chekRoot(bro)
    }

    fun findBro(node: RBNode<K, V>): RBNode<K, V>? {
        if(isLeftChild(node)){
            return node.parent?.rightChild
        }else{
            return node.parent?.leftChild
        }
    }
}
