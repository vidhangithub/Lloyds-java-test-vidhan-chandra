package com.lbg.meeting.schedule.pojos;

import org.joda.time.Interval;
import org.joda.time.LocalTime;


/**
 * @author vidhan
 * POJO to hold meeting attributes
 *
 */
public class Meeting implements Comparable<Meeting> {

    private String employeeId;

    private LocalTime meetingRequestSubmissionTime;

    private LocalTime requestStartTime;

    private LocalTime requestFinishTime;

    /**
     * @param employeeId
     * @param startTime
     * @param finishTime
     */
    public Meeting(String employeeId, LocalTime startTime, LocalTime finishTime, LocalTime meetingRequestSubmissionTime) {
        this.employeeId = employeeId;
        this.requestStartTime = startTime;
        this.requestFinishTime = finishTime;
        this.meetingRequestSubmissionTime = meetingRequestSubmissionTime;
    }

    /**
     *
     * @return String
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     *
     * @return LocalTime
     */
    public LocalTime getStartTime() {
        return requestStartTime;
    }

    /**
     *
     * @return LocalTime
     */
    public LocalTime getFinishTime() {
        return requestFinishTime;
    }

    /**
     *
     * @return LocalTime
     */
    public LocalTime getMeetingRequestSubmissionTime() {
        return meetingRequestSubmissionTime;
    }

    /**
     * @param that
     * @return int
     */
    public int compareTo(Meeting that) {
        Interval meetingInterval = new Interval(requestStartTime.toDateTimeToday(),
                requestFinishTime.toDateTimeToday());
        Interval toCompareMeetingInterval = new Interval(that.getStartTime()
                .toDateTimeToday(), that.getFinishTime().toDateTimeToday());

        if (meetingInterval.overlaps(toCompareMeetingInterval)) {
            return 0;
        } else {
            return this.getStartTime().compareTo(that.getStartTime());
        }
    }
}
