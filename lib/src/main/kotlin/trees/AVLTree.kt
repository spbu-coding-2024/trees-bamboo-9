package trees

import nodes.AVLNode

class AVLTree<K: Comparable<K>, V>: Tree<K, V, AVLNode<K, V>>() {
    override fun insert(key: K, newValue: V) {}
    override fun remove(key: K) {}
    override fun find(key: K): V? {}
}