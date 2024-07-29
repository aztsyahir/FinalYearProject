package com.heroku.java.DAO.Event;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class EventCreateDAO {
    private final DataSource dataSource;

    public EventCreateDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String EventCreate(Event event, EventDetail ed) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO event (eventname) VALUES (?) RETURNING eventid";
            final var statement = connection.prepareStatement(sql);

            statement.setString(1, event.getEventname());
            final var resultSet = statement.executeQuery();

            int eventid = 0;
            if (resultSet.next()) {
                eventid = resultSet.getInt("eventid");
            }

            String sql2 = "INSERT INTO eventdetail (edtype,edcapacity,edvenue,edstate,eddate,edlastdate,edstatus,edstats,edimg,eventid) VALUES (?,?,?,?,?,?,?,?,?,?)";
            final var statement2 = connection.prepareStatement(sql2);

            statement2.setString(1, ed.getEdtype());
            statement2.setInt(2, ed.getEdcapacity());
            statement2.setString(3, ed.getEdvenue());
            statement2.setString(4, ed.getEdstate());
            statement2.setDate(5, ed.getEddate());
            statement2.setDate(6, ed.getEdlastdate());
            statement2.setString(7, "OPEN");
            statement2.setInt(8, ed.getEdstats());
            statement2.setBytes(9, ed.getEdimgbyte());
            statement2.setInt(10, eventid);
            statement2.executeUpdate();
            connection.close();
        }
        return null;
    }

    public String NewEventSet(Event event, EventDetail ed) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            int eventid = event.getEventid();

            String sql2 = "INSERT INTO eventdetail (edtype, edcapacity, edvenue, edstate, eddate, edlastdate, edstatus, edstats, edimg , eventid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            final var statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, ed.getEdtype());
            statement2.setInt(2, ed.getEdcapacity());
            statement2.setString(3, ed.getEdvenue());
            statement2.setString(4, ed.getEdstate());
            statement2.setDate(5, ed.getEddate());
            statement2.setDate(6, ed.getEdlastdate());
            statement2.setString(7, "OPEN");
            statement2.setInt(8, ed.getEdstats());
            statement2.setBytes(9, ed.getEdimgbyte());
            statement2.setInt(10, eventid);
            statement2.executeUpdate();
            connection.close();
        }
        return null;
    }

    public boolean CheckEventName(String eventname) throws SQLException {
        String sql = "SELECT COUNT(*) FROM event WHERE eventname = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventname);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
