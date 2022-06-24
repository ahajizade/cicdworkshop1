import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class Period(
    val startTime: Date,
    val duration: Long,
    val unit: TimeUnit
) {
    val endTime: Date
        get() = Date(this.startTime.time + this.unit.toMillis(this.duration))
}

interface TimeManager {
    fun overlaps(period1: Period, period2: Period): Boolean
    fun getDate(str: String, format: String): Date
}

class TimeManagerImpl : TimeManager {
    override fun overlaps(period1: Period, period2: Period): Boolean {
        val period1Range = period1.startTime.time until period1.endTime.time
        return period2.startTime.time in period1Range || period2.endTime.time in period1Range
    }

    override fun getDate(str: String, format: String): Date {
        return SimpleDateFormat(format).parse(str)
    }
}