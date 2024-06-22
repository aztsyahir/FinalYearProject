package com.heroku.java.DAO.Admin;

import com.heroku.java.MODEL.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.stereotype.Repository;

@Repository
public class ValidateDAO {
    private final DataSource dataSource;

    public ValidateDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Player> getIndividual(int edid) throws SQLException {
        ArrayList<Player> players = new ArrayList<>();
        String sql = "SELECT p.playerid, p.playername, p.playerstats " +
                "FROM individual i " +
                "JOIN player p ON i.playerid = p.playerid " +
                "WHERE i.eventdetailid = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, edid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int playerid = resultSet.getInt("playerid");
                String playername = resultSet.getString("playername");
                int playerstats = resultSet.getInt("playerstats");

                Player player = new Player(playerid, playername, playerstats);
                players.add(player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // You may want to handle this differently in a real application
        }
        return players;
    }

    public int getTotalPlayersRegistered(int edid) throws SQLException {
        int totalPlayers = 0;
        String sql = "SELECT COUNT(*) AS total_players " +
                "FROM individual " +
                "WHERE eventdetailid = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, edid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalPlayers = resultSet.getInt("total_players");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Handle this exception appropriately in your application
        }

        return totalPlayers;
    }

    public void approveITopRank(int playerid, int edid, int adminid) throws SQLException {
        String sql = "UPDATE individual SET registrationstatus = 'APPROVED', adminid = ? " +
                "WHERE playerid = ? AND eventdetailid = ?";
        String sql2 = "UPDATE eventdetail SET edstatus = 'CLOSE' WHERE eventdetailid = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statement2 = connection.prepareStatement(sql2);

            statement.setInt(1, adminid);
            statement.setInt(1, playerid);
            statement.setInt(2, edid);
            statement.executeUpdate();

            statement2.setInt(1, edid);
            statement2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // You may want to handle this differently in a real application
        }
    }

    public ArrayList<Team> getTeam(int edid) throws SQLException {
        ArrayList<Team> teams = new ArrayList<>();
        String sql = "SELECT teamid, teamname, teamstats " +
                "FROM team  WHERE eventdetailid = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, edid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int teamid = resultSet.getInt("teamid");
                String teamname = resultSet.getString("teamname");
                int teamstats = resultSet.getInt("teamstats");

                Team team = new Team(teamid, teamname, teamstats);
                teams.add(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // You may want to handle this differently in a real application
        }
        return teams;
    }

    public int getTotalTeamsRegistered(int edid) throws SQLException {
        int totalTeams = 0;
        String sql = "SELECT COUNT(*) AS total_teams " +
                "FROM team " +
                "WHERE eventdetailid = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, edid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalTeams = resultSet.getInt("total_teams");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Handle this exception appropriately in your application
        }

        return totalTeams;
    }

    public void approveTTopRank(int teamid, int edid, int adminid) throws SQLException {
        String sql = "UPDATE team SET registrationstatus = 'APPROVED', adminid = ? " +
                "WHERE teamid = ? AND eventdetailid = ?";
        String sql2 = "UPDATE eventdetail SET edstatus = 'CLOSE' WHERE eventdetailid = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statement2 = connection.prepareStatement(sql2);

            statement.setInt(1, adminid);
            statement.setInt(2, teamid);
            statement.setInt(3, edid);
            statement.executeUpdate();

            statement2.setInt(1, edid);
            statement2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // You may want to handle this differently in a real application
        }
    }

    public EventDetail getEventDetail(int eventdetailid) throws SQLException {
        EventDetail eventDetail = null;
        String sql = "SELECT ed.eventdetailid, ed.edtype, ed.edcapacity, ed.edvenue, ed.edstate, ed.edstatus, ed.edstats,e.eventid, e.eventname "
                +
                "FROM eventdetail ed " +
                "JOIN event e ON ed.eventid = e.eventid " +
                "WHERE ed.eventdetailid = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventdetailid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int edid = resultSet.getInt("eventdetailid");
                String edtype = resultSet.getString("edtype");
                int edcapacity = resultSet.getInt("edcapacity");
                String edvenue = resultSet.getString("edvenue");
                String edstate = resultSet.getString("edstate");
                String edstatus = resultSet.getString("edstatus");
                int edstats = resultSet.getInt("edstats");
                int eventid = resultSet.getInt("eventid");
                String eventname = resultSet.getString("eventname");

                eventDetail = new EventDetail(edid, edtype, edcapacity, edvenue, edstate, edstatus, edstats, eventid,
                        eventname);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // You may want to handle this differently in a real application
        }

        return eventDetail;
    }
}
