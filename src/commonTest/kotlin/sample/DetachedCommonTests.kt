package sample

import co.touchlab.stately.freeze
import co.touchlab.stately.isNative
import co.touchlab.testhelp.concurrency.ThreadOperations
import co.touchlab.testhelp.printStackTrace
import kotlin.math.floor
import kotlin.test.Test

const val SESSIONIZE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

class DetachedCommonTests {

  @Test
  fun dateMess() {
    if (!isNative) {
      return
    }

    val formatter: DateFormatHelper

    formatter = DateFormatHelper(SESSIONIZE_DATE_FORMAT).freeze()

    val dateString = "2018-11-19T08:00:00".freeze()

    val ops = ThreadOperations {}
    ops.exe {
      try {
        detach {
          println("Date mils $dateString")
          formatter.toDate(dateString)
          /*Date(
              floor(formatter.toDate(dstring)).toLong() * 1000L
          )*/
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }

    ops.run(1)
  }
}