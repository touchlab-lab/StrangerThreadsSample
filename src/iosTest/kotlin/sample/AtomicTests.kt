package sample

import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.native.concurrent.isFrozen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class AtomicTests{

  data class SomeData(val s:String)

  @Test
  fun atomicReference(){
    val someData = SomeData("Hello").freeze()
    val ref = AtomicReference(someData)
    assertEquals("Hello", ref.value.s)
  }

  @Test
  fun notFrozen(){
    val someData = SomeData("Hello")
    assertFails {
      AtomicReference(someData)
    }
  }

  class Wrapper(someData: SomeData){
    val reference = AtomicReference(someData)
  }

  @Test
  fun swapReference(){
    val initVal = SomeData("First").freeze()
    val wrapper = Wrapper(initVal).freeze()
    assertTrue(wrapper.isFrozen)
    assertEquals(wrapper.reference.value.s, "First")
    wrapper.reference.value = SomeData("Second").freeze()
    assertEquals(wrapper.reference.value.s, "Second")
  }

}