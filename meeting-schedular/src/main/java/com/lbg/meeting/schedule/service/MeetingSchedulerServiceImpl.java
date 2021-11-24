package com.lbg.meeting.schedule.service;

import com.lbg.meeting.schedule.pojos.Meeting;
import com.lbg.meeting.schedule.pojos.MeetingsSchedule;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.Integer.parseInt;

/**
 * @author vidhan
 */
public class MeetingSchedulerServiceImpl implements  MeetingSchedulerService{

    private DateTimeFormatter dateFormatter = DateTimeFormat
            .forPattern("yyyy-MM-dd");
    private DateTimeFormatter separatedTimeFormatter = DateTimeFormat
            .forPattern("HH:mm");

    @Override
    public MeetingsSchedule schedule(String meetingRequest) {
        try {
            if (meetingRequest.isBlank()) {
                System.err
                        .println(" Employee has requested for booking is not a valid input");
                return null;
            }

            String[] requestLines = meetingRequest.split("\n");

            String[] officeHoursTokens = requestLines[0].split(" ");
            LocalTime officeStartTime = new LocalTime(
                    parseInt(officeHoursTokens[0].substring(0, 2)),
                    parseInt(officeHoursTokens[0].substring(2, 4)));
            LocalTime officeFinishTime = new LocalTime(
                    parseInt(officeHoursTokens[1].substring(0, 2)),
                    parseInt(officeHoursTokens[1].substring(2, 4)));

            Map<LocalDate, Set<Meeting>> meetings = new LinkedHashMap<LocalDate, Set<Meeting>>();

            for (int i = 1; i < requestLines.length; i = i + 2) {

                String[] meetingSlotRequest = requestLines[i + 1].split(" ");
                LocalDate meetingDate = dateFormatter
                        .parseLocalDate(meetingSlotRequest[0]);

                Meeting meeting = extractMeeting(requestLines[i],
                        officeStartTime, officeFinishTime, meetingSlotRequest);
                if (meeting != null) {
                    if (meetings.containsKey(meetingDate)) {
                        Set<Meeting> alreadyScheduledMeeting = meetings.get(meetingDate);
                        Interval meetingRequestInterval = new Interval(meeting.getStartTime().toDateTimeToday(),
                                meeting.getFinishTime().toDateTimeToday());
                        for (Meeting scheduledMeeting : alreadyScheduledMeeting) {
                            Interval scheduledMeetingInterval = new Interval(scheduledMeeting.getStartTime()
                                    .toDateTimeToday(), scheduledMeeting.getFinishTime().toDateTimeToday());
                            if (scheduledMeeting.getMeetingRequestSubmissionTime().isAfter(meeting.getMeetingRequestSubmissionTime())) {
                                meetings.get(meetingDate).remove(meeting);
                                meetings.get(meetingDate).add(meeting);
                            } else if (!(meetingRequestInterval.overlaps(scheduledMeetingInterval))) {
                                meetings.get(meetingDate).remove(meeting);
                                meetings.get(meetingDate).add(meeting);
                            }
                        }
                    } else {
                        Set<Meeting> meetingsForDay = new TreeSet<Meeting>();
                        meetingsForDay.add(meeting);
                        meetings.put(meetingDate, meetingsForDay);
                    }
                }
            }

            return new MeetingsSchedule(officeStartTime, officeFinishTime,
                    meetings);
        } catch (Exception e) {
            return null;
        }

    }


    private Meeting extractMeeting(String requestLine,
                                   LocalTime officeStartTime, LocalTime officeFinishTime,
                                   String[] meetingSlotRequest) {
        String[] employeeRequest = requestLine.split(" ");
        String employeeId = employeeRequest[2];
        LocalTime meetingRequestSubmissionTime = LocalTime.parse(employeeRequest[1]);
        LocalTime meetingStartTime = separatedTimeFormatter
                .parseLocalTime(meetingSlotRequest[1]);
        LocalTime meetingFinishTime = new LocalTime(
                meetingStartTime.getHourOfDay(),
                meetingStartTime.getMinuteOfHour())
                .plusHours(parseInt(meetingSlotRequest[2]));

        if (meetingTimeOutsideOfficeHours(officeStartTime, officeFinishTime,
                meetingStartTime, meetingFinishTime)) {
            System.err.println("EmployeeId:: " + employeeId
                    + " has requested booking which is outside office hour.");
            return null;
        } else {
            return new Meeting(employeeId, meetingStartTime, meetingFinishTime, meetingRequestSubmissionTime);

        }
    }

    private boolean meetingTimeOutsideOfficeHours(LocalTime officeStartTime,
                                                  LocalTime officeFinishTime, LocalTime meetingStartTime,
                                                  LocalTime meetingFinishTime) {
        return meetingStartTime.isBefore(officeStartTime)
                || meetingStartTime.isAfter(officeFinishTime)
                || meetingFinishTime.isAfter(officeFinishTime)
                || meetingFinishTime.isBefore(officeStartTime);
    }


}
