package trees

import nodes.Node

abstract class Tree<K : Comparable<K>, V, treeNode : Node<K, V, treeNode>> : Iterable<treeNode> {
    internal var size: Int = 0
    var root: treeNode? = null
        protected set
        public get

    abstract fun insert(key: K, newValue: V)

    abstract fun remove(key: K)

    abstract fun find(key: K): V?

    override fun iterator(): Iterator<treeNode> {
        return object : Iterator<treeNode> {
            var wasRootInQueue = false
            val rootCopy = root
            val queue: ArrayDeque<treeNode> = ArrayDeque()
            override fun hasNext(): Boolean {
                if (!wasRootInQueue) {
                    if (rootCopy != null) {
                        queue.add(rootCopy)
                        wasRootInQueue = true
                        bfs(rootCopy)
                    } else return false
                }
                return queue.isNotEmpty()
            }

            override fun next(): treeNode {
                return queue.removeFirst()
            }

            fun bfs(node: treeNode) {
                val leftChild = node.leftChild
                val rightChild = node.rightChild
                if (leftChild != null) {
                    queue.add(leftChild)
                    bfs(leftChild)
                }
                if (rightChild != null) {
                    queue.add(rightChild)
                    bfs(rightChild)
                }
            }
        }
    }
}
