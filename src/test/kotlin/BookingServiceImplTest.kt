import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.TimeUnit

internal class BookingServiceImplTest {

    private val timeManager: TimeManager = TimeManagerImpl()
    lateinit var service: BookingService

    @BeforeEach
    fun setUp() {
        val localDataSource: BookingDatabase = BookingDatabaseImpl()

        service = BookingServiceImpl(
            timeManager = timeManager,
            localDataSource = localDataSource
        )
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun testBookSuccess() {
        val title = "Test meeting 1"
        val invitees = listOf<Attendee>(Attendee.Required(fullName = "John Doe", email = "john@company.com"))
        val period = Period(
            startTime = timeManager.getDate("10.01.2022/09:00", format = "dd.MM.yyyy/HH:mm"),
            duration = 45,
            unit = TimeUnit.MINUTES
        )

        val meeting = service.book(
            title = title,
            invitees = invitees,
            period = period
        )

        assertNotNull(meeting.id)
        assertEquals(meeting.title, title)
        assertEquals(meeting.invitees, invitees)
        assertEquals(meeting.period, period)
    }

    @Test
    fun isAvailable() {
    }

    @Test
    fun testSuggestNextPeriod() {
        val title = "Test meeting 1"
        val invitees = listOf<Attendee>(Attendee.Required(fullName = "John Doe", email = "john@company.com"))
        val period = Period(
            startTime = timeManager.getDate("10.01.2022/09:00", format = "dd.MM.yyyy/HH:mm"),
            duration = 45,
            unit = TimeUnit.MINUTES
        )

        val meeting = service.book(
            title = title,
            invitees = invitees,
            period = period
        )

        assertNotNull(meeting.id)
        assertEquals(meeting.title, title)
        assertEquals(meeting.invitees, invitees)
        assertEquals(meeting.period, period)

        val title2 = "Test meeting 2"
        val invitees2 = listOf<Attendee>(Attendee.Required(fullName = "Sarah Doe", email = "sarah@company.com"))
        var period2 = Period(
            startTime = timeManager.getDate("10.01.2022/09:30", format = "dd.MM.yyyy/HH:mm"),
            duration = 25,
            unit = TimeUnit.MINUTES
        ) // conflicting time slot

        period2 = service.suggestNextPeriod(period2)

        val meeting2 = service.book(
            title = title2,
            invitees = invitees2,
            period = period2
        )

        assertNotNull(meeting2.id)
        assertEquals(meeting2.title, title2)
        assertEquals(meeting2.invitees, invitees2)
        assertEquals(meeting2.period, period2)
    }
}

