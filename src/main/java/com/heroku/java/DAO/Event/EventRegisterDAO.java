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
public class EventRegisterDAO {
    private final DataSource dataSource;

    public EventRegisterDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Event IRegisterEventView(int edid, int playerid) throws SQLException {
        Event event = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT e.eventid, e.eventname, ed.edtype " +
                    "FROM eventdetail ed " +
                    "JOIN event e ON ed.eventid = e.eventid " +
                    "WHERE ed.eventdetailid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, edid);
            final var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int eventid = resultSet.getInt("eventid");
                String eventname = resultSet.getString("eventname");
                String edtype = resultSet.getString("edtype");

                EventDetail ed = new EventDetail(eventid, eventname, edid, edtype);
                event = new Event(eventid, eventname);
                event.setEventDetail(ed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }
}
