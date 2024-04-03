package com.heroku.java.DAO.Event;

import com.heroku.java.MODEL.Event;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Date;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

@Repository
public class EventCreateDAO {
    private final DataSource dataSource;

    public EventCreateDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Event EventCreate(Event event) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO event (eventname, eventtype, eventcapacity, eventvenue, eventstate, eventdate,eventlastdate eventstats, eventimg) VALUES (?,?,?,?,?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            statement.setString(1, event.getEventname());
            statement.setString(2, event.getEventtype());
            statement.setInt(3, event.getEventcapacity());
            statement.setString(4, event.getEventvenue());
            statement.setString(5, event.getEventstate());
            statement.setDate(6, event.getEventdate());
            statement.setDate(7, event.getEventlastdate());
            statement.setInt(7, event.getEventstats());
            statement.setBytes(8, event.getEventimgbytes());
            statement.executeUpdate();

        }
        return event;
    }
}
