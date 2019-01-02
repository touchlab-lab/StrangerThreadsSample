package sample

expect class Date {
    fun toLongMillis():Long
}

expect class DateFormatHelper(format:String){
    fun toDate(s:String):Date
    fun format(d:Date):String
}

expect fun detach(block:()->Any)