interface BookingDatabase {
    fun save(meeting: Meeting)
    fun getMeetings(): List<Meeting>
}

class BookingDatabaseImpl: BookingDatabase {
    private var meetings: ArrayList<Meeting> = arrayListOf()

    override fun save(meeting: Meeting) {
        this.meetings.add(meeting)
    }

    override fun getMeetings(): List<Meeting> {
        return meetings
    }
}