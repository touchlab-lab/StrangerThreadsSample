package sample

import kotlin.native.concurrent.*
import kotlin.test.*

class FreezeTests{

  data class SomeState(val s:String)

  @Test
  fun freeze(){
    val state = SomeState("Hello")
    state.freeze()
    assertTrue(state.isFrozen)
  }

  data class MoreState(val someState: SomeState, val a:String)

  @Test
  fun recursiveFreeze(){
    val moreState = MoreState(SomeState("child"), "me")
    moreState.freeze()
    assertTrue(moreState.someState.isFrozen)
  }

  val worker = Worker.start()

  @Test
  fun lambdaFail(){
    var count = 0

    val job: () -> Int = {
      for (i in 0 until 10) {
        count++
      }
      count
    }

    val future = worker.execute(TransferMode.SAFE, { job.freeze() }){
      it()
    }

    assertNull(future.result)
    assertEquals(0, count)
    assertTrue(count.isFrozen)
  }

  @Test
  fun atomicCount(){
    val count = AtomicInt(0)

    val job: () -> Int = {
      for (i in 0 until 10) {
        count.increment()
      }
      count.value
    }

    val future = worker.execute(TransferMode.SAFE, { job.freeze() }){
      it()
    }

    assertEquals(10, future.result)
    assertEquals(10, count.value)
    assertTrue(count.isFrozen)
  }

  @Test
  fun ensureNeverFrozen()
  {
    val noFreeze = SomeData("Warm")
    noFreeze.ensureNeverFrozen()
    assertFails {
      noFreeze.freeze()
    }
  }
}