package com.lbg;

import com.lbg.meeting.schedule.pojos.Meeting;
import com.lbg.meeting.schedule.pojos.MeetingsSchedule;
import com.lbg.meeting.schedule.service.MeetingSchedulerServiceImpl;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author vidhan
 */
@RunWith(MockitoJUnitRunner.class)
public class MeetingSchedulerTest {
    private String meetingRequest;

    @InjectMocks
    private MeetingSchedulerServiceImpl meetingSchedulerServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_nullInputReturnsNull() {
        //Behaviour
        meetingRequest = null;

        //System under test
        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);

        //asserts
        assertEquals(null, bookings);
    }

    @Test
    public void test_invalidInputReturnsNull() {
        //Behaviour
        meetingRequest = "0900 1800\n" + "2021-03-17 10:17:06 EMP001\n";

        //System under test
        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);

        //asserts
        assertEquals(null, bookings);
    }

    @Test
    public void test_ParseOfficeHours() {
        //Behaviour
        meetingRequest = "0900 1800\n";

        //System under test
        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);

        //asserts
        assertEquals(bookings.getOfficeStartTime().getHourOfDay(), 9);
        assertEquals(bookings.getOfficeStartTime().getMinuteOfHour(), 0);
        assertEquals(bookings.getOfficeFinishTime().getHourOfDay(), 18);
        assertEquals(bookings.getOfficeFinishTime().getMinuteOfHour(), 00);
    }

    @Test
    public void test_ParseSingleMeetingRequest() {
        meetingRequest = "0900 1800\n" + "2021-03-17 10:17:06 EMP001\n"
                + "2021-03-21 09:00 2\n";
        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);

        LocalDate meetingDate = new LocalDate(2021, 3, 21);

        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Meeting meeting = bookings.getMeetings().get(meetingDate)
                .toArray(new Meeting[0])[0];
        assertEquals("EMP001", meeting.getEmployeeId());
        assertEquals(9, meeting.getStartTime().getHourOfDay());
        assertEquals(0, meeting.getStartTime().getMinuteOfHour());
        assertEquals(11, meeting.getFinishTime().getHourOfDay());
        assertEquals(0, meeting.getFinishTime().getMinuteOfHour());
    }


    @Test
    public void test_meetingRequestOutsideOfficeHours() {
        meetingRequest = "0900 1800\n" + "2021-03-15 17:29:12 EMP005\n"
                + "2021-03-21 16:00 3\n";
        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);

        assertEquals(0, bookings.getMeetings().size());

    }

    @Test
    public void test_ProcessMeetingsInChronologicalOrderOfSubmission() {
        meetingRequest = "0900 1800\n" + "2021-03-17 10:17:06 EMP001\n"
                + "2021-03-21 09:00 2\n" + "2021-03-16 12:34:56 EMP002\n"
                + "2021-03-21 09:00 2\n";

        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);

        LocalDate meetingDate = new LocalDate(2021, 3, 21);

        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Meeting meeting = bookings.getMeetings().get(meetingDate)
                .toArray(new Meeting[0])[0];
        assertEquals("EMP001", meeting.getEmployeeId());
        assertEquals(9, meeting.getStartTime().getHourOfDay());
        assertEquals(0, meeting.getStartTime().getMinuteOfHour());
        assertEquals(11, meeting.getFinishTime().getHourOfDay());
        assertEquals(0, meeting.getFinishTime().getMinuteOfHour());
    }

    @Test
    public void test_GroupMeetingsChronologically() {
        meetingRequest = "0900 1800\n" + "2021-03-17 10:17:06 EMP004\n"
                + "2021-03-22 16:00 1\n" + "2021-03-16 09:28:23 EMP003\n"
                + "2021-03-22 14:00 2\n";

        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);
        LocalDate meetingDate = new LocalDate(2021, 3, 22);

        assertEquals(1, bookings.getMeetings().size());
        assertEquals(2, bookings.getMeetings().get(meetingDate).size());
        Meeting[] meetings = bookings.getMeetings().get(meetingDate)
                .toArray(new Meeting[0]);

        assertEquals("EMP003", meetings[0].getEmployeeId());
        assertEquals(14, meetings[0].getStartTime().getHourOfDay());
        assertEquals(0, meetings[0].getStartTime().getMinuteOfHour());
        assertEquals(16, meetings[0].getFinishTime().getHourOfDay());
        assertEquals(0, meetings[0].getFinishTime().getMinuteOfHour());

        assertEquals("EMP004", meetings[1].getEmployeeId());
        assertEquals(16, meetings[1].getStartTime().getHourOfDay());
        assertEquals(0, meetings[1].getStartTime().getMinuteOfHour());
        assertEquals(17, meetings[1].getFinishTime().getHourOfDay());
        assertEquals(0, meetings[1].getFinishTime().getMinuteOfHour());
    }
    @Test
    public void test_meetingsShouldNotOverlap_if_overlaps_schedule_earlier_submitted_request() {
        meetingRequest = "0900 1800\n" + "2021-03-17 10:17:06 EMP001\n"
                + "2021-03-21 09:00 2\n" + "2011-03-16 12:34:56 EMP002\n"
                + "2021-03-21 10:00 1\n";

        MeetingsSchedule bookings = meetingSchedulerServiceImpl.schedule(meetingRequest);
        LocalDate meetingDate = new LocalDate(2021, 3, 21);

        assertEquals(1, bookings.getMeetings().size());
        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Meeting[] meetings = bookings.getMeetings().get(meetingDate)
                .toArray(new Meeting[0]);
        assertEquals("EMP001", meetings[0].getEmployeeId());
        assertEquals(9, meetings[0].getStartTime().getHourOfDay());
        assertEquals(0, meetings[0].getStartTime().getMinuteOfHour());
        assertEquals(11, meetings[0].getFinishTime().getHourOfDay());
        assertEquals(0, meetings[0].getFinishTime().getMinuteOfHour());
    }

}
