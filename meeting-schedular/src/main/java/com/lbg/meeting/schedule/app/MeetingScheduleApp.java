package com.lbg.meeting.schedule.app;

import com.lbg.meeting.schedule.service.MeetingSchedulerService;
import com.lbg.meeting.schedule.utils.MeetingScheduleOutputUtil;
import com.lbg.meeting.schedule.service.MeetingSchedulerServiceImpl;

/**
 * @author vidhan
 */
public class MeetingScheduleApp {

    public static void main(String[] args) {
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
        MeetingSchedulerService meetingSchedulerService = new MeetingSchedulerServiceImpl();
        String scheduledMeetingResponse = new MeetingScheduleOutputUtil(meetingSchedulerService).generateOutput(meetingRequest);
        System.out.println(scheduledMeetingResponse);
    }
}
