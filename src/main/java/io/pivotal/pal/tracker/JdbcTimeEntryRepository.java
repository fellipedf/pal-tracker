package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(MysqlDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry any) {
        String sql = "INSERT INTO time_entries(project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, any.getProjectId());
            ps.setLong(2, any.getUserId());
            ps.setDate(3, Date.valueOf(any.getDate()));
            ps.setInt(4, any.getHours());
            return ps;
        }, generatedKeyHolder);
        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String selectQuery = "SELECT id, project_id, user_id, date, hours  FROM time_entries WHERE id=?;";
        return this.jdbcTemplate.query(selectQuery, new Object[]{timeEntryId}, (rs, rowNum) -> new TimeEntry(
                rs.getLong("id"),
                rs.getLong("project_id"),
                rs.getLong("user_id"),
                rs.getDate("date").toLocalDate(),
                rs.getInt("hours")
        )).stream().findFirst().orElse(null);
    }

    @Override
    public List<TimeEntry> list() {
        return this.jdbcTemplate.query("SELECT * FROM time_entries;", (rs, rowNum) -> new TimeEntry(
                rs.getLong("id"),
                rs.getLong("project_id"),
                rs.getLong("user_id"),
                rs.getDate("date").toLocalDate(),
                rs.getInt("hours")
        ));
    }

    @Override
    public TimeEntry update(long createdId, TimeEntry any) {
        this.jdbcTemplate.update("UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?",
                any.getProjectId(),
                any.getUserId(),
                any.getDate(),
                any.getHours(),
                createdId);
        return find(createdId);
    }

    @Override
    public void delete(long timeEntryId) {
        this.jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?;", timeEntryId);
    }
}
