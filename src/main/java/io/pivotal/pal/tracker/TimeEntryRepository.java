package io.pivotal.pal.tracker;

import java.sql.SQLException;
import java.util.List;

public interface TimeEntryRepository {
    TimeEntry create(TimeEntry any);

    TimeEntry find(long timeEntryId);

    List<TimeEntry> list();

    TimeEntry update(long createdId, TimeEntry any);

    void delete(long timeEntryId);
}
