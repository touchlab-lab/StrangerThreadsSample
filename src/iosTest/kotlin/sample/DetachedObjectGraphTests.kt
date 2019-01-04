package sample

import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.attach
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class DetachedObjectGraphTests{

  data class SomeData(val s:String)

  @Test
  fun detach(){
    val ptr = DetachedObjectGraph {SomeData("Hi")}.asCPointer()
    assertEquals("Hi", DetachedObjectGraph<SomeData>(ptr).attach().s)
  }

  @Test
  fun detachFails(){
    val data = SomeData("Nope")
    assertFails { DetachedObjectGraph {data} }
  }

}