package com.heroku.java.DAO.Event;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Date;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Base64;

@Repository
public class EventDetailDAO {
    private final DataSource dataSource;

    public EventDetailDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Event> getEventDetail(int eventid) throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM event JOIN eventdetail ON event.eventid = eventdetail.eventid WHERE event.eventid = ? ORDER BY eventdetailid DESC";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, eventid);
            final var resultSet = statement.executeQuery();

            while (resultSet.next()) {

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
        }
        return events;
    }

}
