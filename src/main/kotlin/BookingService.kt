import java.util.*
import java.util.concurrent.TimeUnit

interface BookingService {
    fun book(
        title: String,
        invitees: List<Attendee>,
        period: Period
    ): Meeting

    fun isAvailable(period: Period): Boolean
    fun suggestNextPeriod(period: Period): Period
}

class BookingServiceImpl(
    private val timeManager: TimeManager,
    private val localDataSource: BookingDatabase
) : BookingService {

    override fun book(title: String, invitees: List<Attendee>, period: Period): Meeting {
        if (isAvailable(period).not()) {
            throw NotAvailableTimeSlotException
        }

        val newId = UUID.randomUUID().toString()
        val meeting = Meeting(
            id = newId,
            title = title,
            invitees = invitees,
            period = period
        )

        localDataSource.save(meeting)

        return meeting
    }

    override fun isAvailable(period: Period): Boolean {
        return localDataSource.getMeetings().isEmpty() || localDataSource.getMeetings().all { periodCompare ->
            timeManager.overlaps(periodCompare.period, period).not()
        }
    }
    override fun suggestNextPeriod(period: Period): Period {
        var newStartTime = period.startTime.time
        val endLimit = timeManager.endOfWorkDay(period.startTime).time - period.unit.toMillis(period.duration)

        while (newStartTime < endLimit) {
            newStartTime += TimeUnit.MINUTES.toMillis(5) // skip each 5 minutes

            val newPeriod =  Period(
                startTime = Date(newStartTime),
                duration = period.duration,
                unit = period.unit
            )

            if (isAvailable(newPeriod)) {
                return newPeriod
            }
        }

        throw NotAvailableTimeSlotException
    }
}

object NotAvailableTimeSlotException : Throwable()
