package com.heroku.java.DAO.Event;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Member;
import com.heroku.java.MODEL.Team;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public Event RegisterEventView(int edid, int playerid) throws SQLException {
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

    public Event IRegisterEvent(int edid, int playerid) throws SQLException {
        Event event = null;
        try (Connection connection = dataSource.getConnection()) {
            String individualSql = "INSERT INTO individual ( registrationstatus, eventdetailid, playerid) VALUES ( ?, ?, ?)";
            try (PreparedStatement individualStatement = connection.prepareStatement(individualSql)) {

                individualStatement.setString(1, "PENDING");
                individualStatement.setInt(2, edid);
                individualStatement.setInt(3, playerid);
                // Execute individual insert
                individualStatement.executeUpdate();
            }
        }
        return event;
    }

    public Team TRegisterEvent(int edid, int playerid) throws SQLException {
        Team team = null;
        try (Connection connection = dataSource.getConnection()) {
            String teamSql = "INSERT INTO team (registrationstatus, eventdetailid, playerid) VALUES (?, ?, ?) RETURNING teamid";
            try (PreparedStatement statement = connection.prepareStatement(teamSql)) {
                statement.setString(1, "PENDING");
                statement.setInt(2, edid);
                statement.setInt(3, playerid);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int teamid = resultSet.getInt("teamid");
                        team = new Team(teamid);
                    }
                }
            }
        }
        return team;
    }

    public Member addmember( int playerid, int teamid,Team team) throws SQLException {
        Member member = null;
        try (Connection  connection = dataSource.getConnection()) {
            String sql = "INSERT INTO member (playerid, teamid) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

                statement.setInt(1, playerid);
                statement.setInt(2, teamid);
                statement.executeUpdate();

            member = new Member(playerid, teamid);
    }catch (SQLException e) {
        e.printStackTrace();
    }
    return member;
}

    public void updateTeamRegister(Team team) throws SQLException {
        try (Connection connection = dataSource.getConnection()){
            int teamid = team.getTeamid();
        String Sql2 = "UPDATE team SET teamname = ?, teamstats = ? WHERE teamid =?";
        final var statement2 = connection.prepareStatement(Sql2);
        statement2.setString(1, team.getTeamname());
        statement2.setInt(2, team.getTeamstats());
        statement2.setInt(3, teamid);
        statement2.executeUpdate();
    }
}

    public boolean isPlayerRegistered(int edid, int playerid) throws SQLException {
        String sql = "SELECT COUNT(*) FROM individual WHERE eventdetailid = ? AND playerid = ?";
        String sql2 = "SELECT COUNT(*) FROM team WHERE eventdetailid = ? AND playerid = ?";
        Connection connection = dataSource.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, edid);
            statement.setInt(2, playerid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(sql2)) {
            statement.setInt(1, edid);
            statement.setInt(2, playerid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
