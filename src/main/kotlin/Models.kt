data class Meeting(
    val id: String,
    val title: String,
    val invitees: List<Attendee>,
    val period: Period
)

sealed class Attendee(open val email: String, open val fullName: String) {
    class Required(override val email: String, override val fullName: String) : Attendee(email, fullName)
    class Optional(override val email: String, override val fullName: String) : Attendee(email, fullName)
}