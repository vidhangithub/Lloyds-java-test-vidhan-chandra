package com.lbg.meeting.schedule.service;

import com.lbg.meeting.schedule.pojos.MeetingsSchedule;

/**
 * @author vidhan
 */
public interface MeetingSchedulerService {
    MeetingsSchedule schedule(String meetingRequest);
}
