package com.lbg;

import com.lbg.meeting.schedule.service.MeetingSchedulerServiceImpl;
import com.lbg.meeting.schedule.utils.MeetingScheduleOutputUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author vidhan
 */

@RunWith(MockitoJUnitRunner.class)
public class MeetingScheduleOutputUtilTest {

    @InjectMocks
    private MeetingScheduleOutputUtil meetingScheduleOutputUtil;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        meetingScheduleOutputUtil = new MeetingScheduleOutputUtil(new MeetingSchedulerServiceImpl());
    }


    @Test
    public void shouldPrintMeetingSchedule() {
        String meetingRequest = "0900 1800\n"
                + "2016-07-18 10:17:06 EMP001\n"
                + "2016-07-21 09:00 2\n"
                + "2016-07-18 12:34:56 EMP002\n"
                + "2016-07-21 09:00 2\n"
                + "2016-07-18 09:28:23 EMP003\n"
                + "2016-07-22 14:00 2\n"
                + "2016-07-18 11:23:45 EMP004\n"
                + "2016-07-22 16:00 1\n"
                + "2016-07-15 17:29:12 EMP005\n"
                + "2016-07-21 16:00 3\n";

        String actualScheduledMeetings  = meetingScheduleOutputUtil.generateOutput(meetingRequest);

        String expectedScheduledMeetings = "2016-07-21\n" + "09:00 11:00 EMP001\n"
                + "2016-07-22\n" + "14:00 16:00 EMP003\n"
                + "16:00 17:00 EMP004\n";

        assertEquals(actualScheduledMeetings, expectedScheduledMeetings);

    }
}
