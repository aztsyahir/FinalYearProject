package com.heroku.java.DAO.Event;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Base64;

@Repository
public class EventListDAO {
    private final DataSource dataSource;

    public EventListDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Event> getEvents() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT DISTINCT e.*, ed.* " +
                    "FROM event e " +
                    "JOIN eventdetail ed ON e.eventid = ed.eventid " +
                    "LEFT JOIN registration r ON ed.eventdetailid = r.eventdetailid " +
                    "WHERE r.registrationstatus = 'PENDING' " +
                    "ORDER BY ed.eventdetailid DESC";
            final var statement = connection.createStatement();
            final var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {

                int eventid = resultSet.getInt("eventid");
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
                Event event = new Event(eventid, eventname);
                event.setEventDetail(ed);
                events.add(event);

            }
            connection.close();
            return events;
        }
    }

    public ArrayList<Event> getEventsForMonth(int month, int year) throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {

            // Update statement to set edstatus to 'CLOSE' for past events
            String updateSql = "UPDATE eventdetail " +
                    "SET edstatus = 'CLOSE' " +
                    "WHERE edlastdate < CURRENT_DATE " +
                    "AND edstatus = 'OPEN'";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.executeUpdate();
            }

            String sql = "SELECT * FROM event " +
                    "JOIN eventdetail ON event.eventid = eventdetail.eventid " +
                    "WHERE EXTRACT(MONTH FROM eventdetail.eddate) = ? " +
                    "AND EXTRACT(YEAR FROM eventdetail.eddate) = ? " +
                    "ORDER BY eventdetail.eventdetailid DESC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, month);
                statement.setInt(2, year);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int eventid = resultSet.getInt("eventid");
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

                        EventDetail ed = new EventDetail(eventid, eventname, edid, edtype, edcapacity, edvenue, edstate,
                                eddate,
                                edlastdate, edstatus, edstats, null, null, edimage);
                        Event event = new Event(eventid, eventname);
                        event.setEventDetail(ed);
                        events.add(event);
                    }
                }
            }
            connection.close();
        }
        return events;
    }

    public ArrayList<Event> getEventHistory(int playerid) throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "SELECT DISTINCT e.eventid, e.eventname, ed.edtype, ed.edcapacity, ed.edvenue, " +
                "ed.edstate, ed.eddate, ed.edlastdate, ed.edstatus, ed.edstats, ed.edimg, r.registrationstatus, " +
                "r.registrationid AS r_registrationid, i.registrationid AS i_registrationid, t.registrationid AS t_registrationid, r.playerid "
                +
                "FROM registration r " +
                "JOIN eventdetail ed ON r.eventdetailid = ed.eventdetailid " +
                "JOIN event e ON ed.eventid = e.eventid " +
                "LEFT JOIN individual i ON r.registrationid = i.registrationid " +
                "LEFT JOIN team t ON r.registrationid = t.registrationid " +
                "LEFT JOIN member m ON m.teamid = t.teamid " +
                "WHERE r.playerid = ? OR m.playerid = ? " +
                "ORDER BY r.registrationid DESC";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, playerid);
            statement.setInt(2, playerid);
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

                    String registrationstatus = resultSet.getString("registrationstatus");

                    int registrationid = resultSet.getInt("r_registrationid");
                    int iRegistrationId = resultSet.getInt("i_registrationid");
                    int tRegistrationId = resultSet.getInt("t_registrationid");

                    boolean isDirectRegistration = resultSet.getInt("playerid") == playerid;

                    EventDetail ed = new EventDetail(eventid, eventname, edtype, edcapacity, edvenue, edstate, eddate,
                            edlastdate, edstatus, edstats, edimage, registrationstatus, registrationid,
                            isDirectRegistration);
                    Event event = new Event(eventid, eventname);
                    event.setEventDetail(ed);
                    events.add(event);
                }
            }
            connection.close();
        }
        return events;
    }

    public ArrayList<Event> getAEventHistory() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM event e JOIN eventdetail ed ON e.eventid = ed.eventid ORDER BY eventdetailid DESC";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

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
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Handle the exception as needed
        }

        return events;
    }

    
}
