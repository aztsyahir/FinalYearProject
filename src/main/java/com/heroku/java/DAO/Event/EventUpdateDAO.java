package com.heroku.java.DAO.Event;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Base64;

@Repository
public class EventUpdateDAO {
    private final DataSource dataSource;

    public EventUpdateDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Event DisplayEvent(int eventdetailid, int eventid) throws SQLException {
        Event event = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM event JOIN eventdetail ON event.eventid = eventdetail.eventid WHERE eventdetail.eventdetailid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, eventdetailid);
            final var resultSet = statement.executeQuery();

            if (resultSet.next()) {

                String eventname = resultSet.getString("eventname");
                int edid = resultSet.getInt("eventdetailid");
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

                EventDetail ed = new EventDetail(eventid, eventname, edid, edtype, edcapacity, edvenue, edstate, eddate,
                        edlastdate, edstatus, edstats, null, null, edimage);
                event = new Event(eventid, eventname);
                event.setEventDetail(ed);

            }
        }
        return event;

    }

    public void EventUpdate(Event event, EventDetail ed, int edid) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            int eventid = event.getEventid();
            String sql = "UPDATE event SET eventname =? WHERE eventid =?";
            final var statement = connection.prepareStatement(sql);

            statement.setString(1, event.getEventname());
            statement.setInt(2, eventid);
            statement.executeUpdate();

            String sql2 = "UPDATE eventdetail SET edtype = ?, edcapacity = ?, edvenue = ?, edstate = ?, eddate = ?, edlastdate = ?, edstats = ?, edimg = ? WHERE eventdetailid =?";
            final var statement2 = connection.prepareStatement(sql2);

            statement2.setString(1, ed.getEdtype());
            statement2.setInt(2, ed.getEdcapacity());
            statement2.setString(3, ed.getEdvenue());
            statement2.setString(4, ed.getEdstate());
            statement2.setDate(5, ed.getEddate());
            statement2.setDate(6, ed.getEdlastdate());
            statement2.setInt(7, ed.getEdstats());
            statement2.setBytes(8, ed.getEdimgbyte());
            statement2.setInt(9, edid);
            statement2.executeUpdate();
            connection.close();
        }
    }

    public EventDetail EventUpdateimg(int eventid) throws SQLException {
        EventDetail eventDetail = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT edimg FROM eventdetail WHERE eventid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, eventid);
            final var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                byte[] edimgbyte = resultSet.getBytes("edimg");
                eventDetail = new EventDetail();
                eventDetail.setEdimgbyte(edimgbyte);
            }
            connection.close();
        }
        return eventDetail;
    }

    public EventDetail CancelEvent(int edid) throws SQLException {
        EventDetail eventDetail = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE eventdetail SET edstatus =? WHERE eventdetailid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, "CANCELLED");
            statement.setInt(2, edid);
            statement.executeUpdate();

            String sql2 = "UPDATE registration SET registrationstatus =? WHERE eventdetailid = ?";
            final var statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, "REJECTED DUE TO EVENT CANCELLATION");
            statement2.setInt(2, edid);
            statement2.executeUpdate();
            connection.close();
        }
        return eventDetail;
    }
}
