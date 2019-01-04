package sample

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class WorkerTest {
  val worker = Worker.start()

  data class JobArg(val a: String)

  @Test
  fun simpleProducer() {
    worker.execute(TransferMode.SAFE, { JobArg("Hi") }) {
      println(it)
    }
  }

  @Test
  fun frameReferenceFails() {
    val valArg = JobArg("Hi")
    assertFails {
      worker.execute(TransferMode.SAFE, { valArg }) {
        println(it)
      }
    }
  }

  class ArgHolder(var arg:JobArg?){
    fun getAndClear():JobArg{
      val temp = arg!!
      arg = null
      return temp
    }
  }

  @Test
  fun stillVisible() {
    val holder = ArgHolder(JobArg("Hi"))
    assertFails {
      worker.execute(TransferMode.SAFE, { holder.getAndClear() }) {
        println(it)
      }
    }
  }

  fun makeInstance() = ArgHolder(JobArg("Hi"))

  @Test
  fun canDetach() {
    val holder = makeInstance()
    worker.execute(TransferMode.SAFE, { holder.getAndClear() }) {
      println(it)
    }
  }

  @Test
  fun frozenFtw() {
    val valArg = JobArg("Hi").freeze()
    worker.execute(TransferMode.SAFE, { valArg }) {
      println(it)
    }
  }

  @Test
  fun backgroundStuff() {
    val future = worker.execute(TransferMode.SAFE, { 1_000_000 }) {
      var count = 0

      for(i in 1..it){
        //Do some long stuff
        count++
      }

      count
    }

    assertEquals(1_000_000, future.result)
  }

  @Test
  fun stringsFrozen() {
    val valString = "Hello"
    worker.execute(TransferMode.SAFE, { valString }) {
      println(it)
    }
  }

}