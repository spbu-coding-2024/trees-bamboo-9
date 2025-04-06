package nodes

abstract class Node<K : Comparable<K>, V, treeNode : Node<K, V, treeNode>>(val key: K, var value: V) {
    internal var leftChild: treeNode? = null
    internal var rightChild: treeNode? = null
}
