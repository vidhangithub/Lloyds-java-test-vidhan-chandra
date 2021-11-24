package com.lbg.meeting.schedule.utils;

import com.lbg.meeting.schedule.service.MeetingSchedulerService;
import com.lbg.meeting.schedule.service.MeetingSchedulerServiceImpl;
import com.lbg.meeting.schedule.pojos.Meeting;
import com.lbg.meeting.schedule.pojos.MeetingsSchedule;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.Set;

/**
 * @author vidhan
 */
public class MeetingScheduleOutputUtil {

    private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

    private MeetingSchedulerService meetingSchedulerService;

    public MeetingScheduleOutputUtil(MeetingSchedulerService MeetingSchedulerService) {
        this.meetingSchedulerService = new MeetingSchedulerServiceImpl();
    }

    public String generateOutput(String meetingRequest) {
        MeetingsSchedule meetingsScheduleBooked = meetingSchedulerService.schedule(meetingRequest);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, Set<Meeting>> meetingEntry : meetingsScheduleBooked.getMeetings().entrySet()) {
            LocalDate meetingDate = meetingEntry.getKey();
            sb.append(dateFormatter.print(meetingDate)).append("\n");
            Set<Meeting> meetings = meetingEntry.getValue();
            for (Meeting meeting : meetings) {
                sb.append(timeFormatter.print(meeting.getStartTime())).append(" ");
                sb.append(timeFormatter.print(meeting.getFinishTime())).append(" ");
                sb.append(meeting.getEmployeeId()).append("\n");
            }
        }
        return sb.toString();
    }
}
