package IteratorTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import trees.*


class IteratorTest {
    private lateinit var tree: RBT<Int,Int>

    @BeforeEach
    fun setup() {
        tree = RBT()
    }

    @Test
    fun testIteratorOnIncreaseOrder(){
        for (i in 1..10000) {
            tree.insert(i,i)
        }
        val ar=Array<Int>(10000,{0})
        var i=0
        for (el in tree){
            ar[i]=el.key
            i++
        }
        for (i in 1..9999) {
            assert(ar[i]>ar[i-1])
        }
    }

    @Test
    fun testIteratorWithNullRoot(){
        var c=0
        for (el in tree){
            c++
        }
        assertEquals(0,c)
    }
}