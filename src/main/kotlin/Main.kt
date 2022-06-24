import java.util.concurrent.TimeUnit

fun main() {

    val localDataSource: BookingDatabase = BookingDatabaseImpl()
    val timeManager: TimeManager = TimeManagerImpl()
    val bookingService: BookingService = BookingServiceImpl(
        localDataSource = localDataSource,
        timeManager = timeManager
    )

    bookingService.book(
        title = "Sprint daily retail mobile",
        invitees = listOf(
            Attendee.Required(fullName = "Oleg Kratov", email = "oleg@company.com"),
        ),
        period = Period(
            startTime = timeManager.getDate("12.03.2022/10:00", "dd.MM.yyyy/HH:mm"),
            duration = 15,
            unit = TimeUnit.MINUTES
        )
    )

    bookingService.book(
        title = "Sprint daily private mobile",
        invitees = listOf(
            Attendee.Required(fullName = "Alex Doe", email = "alex@company.com"),
            Attendee.Required(fullName = "Bob Doe", email = "bob@company.com")
        ),
        period = Period(
            startTime = timeManager.getDate("12.03.2022/10:15", "dd.MM.yyyy/HH:mm"),
            duration = 15,
            unit = TimeUnit.MINUTES
        )
    )

    // expected throw an exception
    bookingService.book(
        title = "Sprint daily business mobile",
        invitees = listOf(
            Attendee.Required(fullName = "Sarah Doe", email = "sarah@company.com"),
            Attendee.Optional(fullName = "John Doe", email = "john@company.com")
        ),
        period = Period(
            startTime = timeManager.getDate("12.03.2022/10:25", "dd.MM.yyyy/HH:mm"),
            duration = 15,
            unit = TimeUnit.MINUTES
        )
    )
}