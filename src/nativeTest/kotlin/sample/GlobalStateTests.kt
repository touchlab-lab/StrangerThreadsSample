package sample

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.native.concurrent.isFrozen
import kotlin.test.*

class GlobalStateTests{

  val worker = Worker.start()

  @Test
  fun globalVal(){
    assertFalse(globalState.isFrozen)
    worker.execute(TransferMode.SAFE,{}){
      assertFails {
        println(globalState.s)
      }
    }.result
  }

  @Test
  fun globalObject(){
    assertTrue(GlobalObject.isFrozen)
    val wval = worker.execute(TransferMode.SAFE, {}){
      GlobalObject
    }.result
    assertSame(GlobalObject, wval)
  }

  @Test
  fun thGlobalVal(){
    assertFalse(thGlobalState.isFrozen)
    val wval = worker.execute(TransferMode.SAFE,{}){
      thGlobalState.freeze()
    }.result

    assertNotSame(thGlobalState, wval)
  }

  @Test
  fun sharedGlobalVal(){
    assertTrue(sharedGlobalState.isFrozen)
    val wval = worker.execute(TransferMode.SAFE,{}){
      sharedGlobalState.freeze()
    }.result

    assertSame(sharedGlobalState, wval)
  }

  @Test
  fun localObject(){
    assertFalse(LocalObject.isFrozen)
    val wval = worker.execute(TransferMode.SAFE, {}){
      LocalObject.freeze()
    }.result

    assertNotSame(LocalObject, wval)
  }
}

val globalState = SomeData("Hi")

data class SomeData(val s:String)

object GlobalObject{
  val someData = SomeData("arst")
}

@ThreadLocal
val thGlobalState = SomeData("Hi")

@SharedImmutable
val sharedGlobalState = SomeData("Hi")

@ThreadLocal
object LocalObject{
  val someData = SomeData("arst")
}