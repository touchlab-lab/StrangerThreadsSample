package sample

import co.touchlab.stately.annotation.ThreadLocal
import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.freeze
import co.touchlab.stately.isFrozen
import co.touchlab.stately.isNative
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StatelyTest{
  data class SomeData(val s:String)

  @Test
  fun freeze(){
    if(!isNative)
      return

    val data = SomeData("Hello")
    assertFalse(data.isFrozen())
    data.freeze()
    assertTrue(data.isFrozen())

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

  @Test
  fun threadLocal()
  {
    assertFalse(globalData.isFrozen())
  }
}

@ThreadLocal
val globalData = StatelyTest.SomeData("arst")