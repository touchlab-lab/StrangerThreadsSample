package sample

import platform.Foundation.NSDateFormatter
import platform.Foundation.timeIntervalSince1970
import kotlin.math.floor
import kotlin.native.concurrent.DetachedObjectGraph

actual class Date(private val longMillis:Long) {
    actual fun toLongMillis():Long{
        return longMillis
    }
}

actual fun detach(block: () -> Any) {
    DetachedObjectGraph {block()}.asCPointer()
}

actual class DateFormatHelper actual constructor(format:String){

    val formatter: NSDateFormatter

    init
    {
        formatter = NSDateFormatter()
        formatter.dateFormat = format
    }

    actual fun toDate(s:String):Date = Date(floor(formatter.dateFromString(s)!!.timeIntervalSince1970).toLong() * 1000L)
    actual fun format(d:Date):String = d.toLongMillis().toString()//formatter.stringFromDate(d.iosDate)
}