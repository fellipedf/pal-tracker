package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private List<TimeEntry> timeEntries;
    private int autoincrement = 1;
    public InMemoryTimeEntryRepository() {
        this.timeEntries = new ArrayList<>();
    }

    @Override
    public TimeEntry create(TimeEntry any) {
        TimeEntry incrementedTimeEntry = new TimeEntry(autoincrement, any.getProjectId(), any.getUserId(), any.getDate(), any.getHours());
        timeEntries.add(incrementedTimeEntry);
        autoincrement += 1L;
        return incrementedTimeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return timeEntries.stream().filter(timeEntry -> timeEntry.getId() == timeEntryId).findFirst().orElse(null);
    }

    @Override
    public List<TimeEntry> list() {
        return timeEntries;
    }

    @Override
    public TimeEntry update(long createId, TimeEntry any) {

        TimeEntry toBeRemoved = timeEntries.stream().filter(timeEntry -> timeEntry.getId() == createId).findFirst().orElse(null);
        if (toBeRemoved != null) {
            timeEntries.remove(toBeRemoved);
            TimeEntry updated = new TimeEntry(createId, any.getProjectId(), any.getUserId(), any.getDate(), any.getHours());
            timeEntries.add(updated);
            return updated;
        } else {
            return null;
        }
    }

    @Override
    public void delete(long timeEntryId) {
        TimeEntry toBeRemoved = timeEntries.stream().filter(timeEntry -> timeEntry.getId() == timeEntryId).findFirst().orElse(null);
        timeEntries.remove(toBeRemoved);

    }
}
