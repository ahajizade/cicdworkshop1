import java.util.UUID

interface BookingService {
    fun book(
        title: String,
        invitees: List<Attendee>,
        period: Period
    ): Meeting

    fun isAvailable(period: Period): Boolean
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
}

object NotAvailableTimeSlotException : Throwable()