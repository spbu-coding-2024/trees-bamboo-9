package trees

import nodes.Node

abstract class Tree<K : Comparable<K>, V, treeNode : Node<K, V, treeNode>> : Iterable<treeNode> {
    var root: treeNode? = null
        internal set
        public get

    abstract fun insert(key: K, newValue: V)

    abstract fun remove(key: K)

    abstract fun find(key: K): V?

    override fun iterator(): Iterator<treeNode> {
        return object : Iterator<treeNode> {
            private var wasRootInQueue = false
            private val rootCopy = root
            private val queue: ArrayDeque<treeNode> = ArrayDeque()
            override fun hasNext(): Boolean {
                if (!wasRootInQueue) {
                    if (rootCopy != null) {
                        wasRootInQueue = true
                        dfs(rootCopy)
                    } else return false
                }
                return queue.isNotEmpty()
            }

            override fun next(): treeNode {
                return queue.removeFirst()
            }

            fun dfs(root: treeNode) {
                val queueForDFS:ArrayDeque<treeNode> = ArrayDeque()
                queueForDFS.add(root)
                val visitedKeys : HashSet<K> = hashSetOf<K>()
                while (!queueForDFS.isEmpty()) {
                    val node =queueForDFS.first()
                    val left=node.leftChild
                    val right=node.rightChild
                    if(left!=null ){
                        if(!visitedKeys.contains(left.key)) {
                            queueForDFS.addFirst(left)
                            continue
                        }
                    }
                    if(right!=null){
                        if(!visitedKeys.contains(right.key)) {
                            visitedKeys.add(node.key)
                            queueForDFS.removeFirst()
                            queue.add(node)
                            queueForDFS.addFirst(right)
                            continue
                        }
                    }
                    visitedKeys.add(node.key)
                    queueForDFS.removeFirst()
                    queue.add(node)
                }
            }
        }
    }
}
