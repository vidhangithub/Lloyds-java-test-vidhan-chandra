package com.lbg.meeting.schedule.pojos;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Map;
import java.util.Set;

/**
 * @author vidhan
 */
public class MeetingsSchedule {

    private LocalTime officeStartTime;

    private LocalTime officeFinishTime;

    private Map<LocalDate, Set<Meeting>> meetings;

    public MeetingsSchedule(LocalTime officeStartTime,
                            LocalTime officeFinishTime, Map<LocalDate, Set<Meeting>> meetings) {
        this.officeStartTime = officeStartTime;
        this.officeFinishTime = officeFinishTime;
        this.meetings = meetings;
    }

    public LocalTime getOfficeStartTime() {
        return officeStartTime;
    }

    public LocalTime getOfficeFinishTime() {
        return officeFinishTime;
    }

    public Map<LocalDate, Set<Meeting>> getMeetings() {
        return meetings;
    }

}
