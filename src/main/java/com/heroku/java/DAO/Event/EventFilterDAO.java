package com.heroku.java.DAO.Event;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.stereotype.Repository;

@Repository
public class EventFilterDAO {

    private DataSource dataSource;

    public EventFilterDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Event> searchEventsByName(String Eventname) throws SQLException {
        ArrayList<Event> events = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM event JOIN eventdetail ON event.eventid = eventdetail.eventid WHERE event.eventname LIKE ? AND eventdetail.edstatus= 'OPEN' ORDER BY eventdetail.eventdetailid DESC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "%" + Eventname + "%");

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int eventid = resultSet.getInt("eventid");
                        String eventname = resultSet.getString("eventname");
                        String edtype = resultSet.getString("edtype");
                        int edcapacity = resultSet.getInt("edcapacity");
                        String edvenue = resultSet.getString("edvenue");
                        String edstate = resultSet.getString("edstate");
                        Date eddate = resultSet.getDate("eddate");
                        Date edlastdate = resultSet.getDate("edlastdate");
                        String edstatus = resultSet.getString("edstatus");
                        int edstats = resultSet.getInt("edstats");

                        byte[] edimgbyte = resultSet.getBytes("edimg");
                        String edimgbase64 = Base64.getEncoder().encodeToString(edimgbyte);
                        String edimage = "data:image/jpeg;base64," + edimgbase64;

                        EventDetail ed = new EventDetail(eventid, eventname, edtype, edcapacity, edvenue, edstate,
                                eddate,
                                edlastdate, edstatus, edstats, null, null, edimage);
                        Event event = new Event(eventid, eventname);
                        event.setEventDetail(ed);
                        events.add(event);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving Event: " + e.getMessage());
        }
        return events;
    }

    public ArrayList<Event> FilterEvent(String EventType, Date StartDate, Date EndDate, String EventStates,
            int EventStats)
            throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM event JOIN eventdetail ON event.eventid = eventdetail.eventid WHERE eventdetail.edstatus = 'OPEN'";

            if (EventType != null) {
                sql += "AND eventdetail.edtype = ? ";
            }
            if (StartDate != null && EndDate != null) {
                sql += "AND eventdetail.eddate BETWEEN ? AND ? ";
            } else if (StartDate != null) {
                sql += " AND eventdetail.eddate >= ? ";
            }
            if (EventStates != null) {
                sql += "AND eventdetail.edstate = ? ";
            }
            if (EventStats != 0) {
                sql += "AND eventdetail.edstats >= ? ";
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int parameterIndex = 1;
                if (EventType != null) {
                    statement.setString(parameterIndex++, EventType);
                }
                if (StartDate != null && EndDate != null) {
                    statement.setDate(parameterIndex++, StartDate);
                    statement.setDate(parameterIndex++, EndDate);
                } else if (StartDate != null) {
                    statement.setDate(parameterIndex++, StartDate);
                }
                if (EventStates != null) {
                    statement.setString(parameterIndex++, EventStates);
                }
                if (EventStats != 0) {
                    statement.setInt(parameterIndex++, EventStats);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int eventid = resultSet.getInt("eventid");
                        String eventname = resultSet.getString("eventname");
                        String edtype = resultSet.getString("edtype");
                        int edcapacity = resultSet.getInt("edcapacity");
                        String edvenue = resultSet.getString("edvenue");
                        String edstate = resultSet.getString("edstate");
                        Date eddate = resultSet.getDate("eddate");
                        Date edlastdate = resultSet.getDate("edlastdate");
                        String edstatus = resultSet.getString("edstatus");
                        int edstats = resultSet.getInt("edstats");

                        byte[] edimgbyte = resultSet.getBytes("edimg");
                        String edimgbase64 = Base64.getEncoder().encodeToString(edimgbyte);
                        String edimage = "data:image/jpeg;base64," + edimgbase64;

                        EventDetail ed = new EventDetail(eventid, eventname, edtype, edcapacity, edvenue, edstate,
                                eddate,
                                edlastdate, edstatus, edstats, null, null, edimage);
                        Event event = new Event(eventid, eventname);
                        event.setEventDetail(ed);
                        events.add(event);
                    }
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error retrieving Event: " + e.getMessage());
        }
        return events;
    }
}
