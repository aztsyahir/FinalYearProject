package com.heroku.java.DAO.Admin;

import com.heroku.java.MODEL.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

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
                     "WHERE i.eventdetailid = ? ORDER BY p.playerstats DESC";

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
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return players;
    }

    public List<String> getIndividualEmail(int edid) throws SQLException {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT p.playeremail FROM player p JOIN individual i ON p.playerid = i.playerid WHERE i.playerid = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, edid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                emails.add(resultSet.getString("playeremail"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return emails;
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
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return totalPlayers;
    }

    public boolean approveITopRank(int playerid, int edid, int adminid) throws SQLException {
        String sql = "UPDATE individual SET registrationstatus = 'APPROVED', adminid = ? " +
                     "WHERE playerid = ? AND eventdetailid = ?";
        String sql2 = "UPDATE eventdetail SET edstatus = 'CLOSE' WHERE eventdetailid = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statement2 = connection.prepareStatement(sql2);

            statement.setInt(1, adminid);
            statement.setInt(2, playerid);
            statement.setInt(3, edid);
            int rowsUpdated = statement.executeUpdate();

            statement2.setInt(1, edid);
            int eventDetailUpdated = statement2.executeUpdate();

            connection.close();
            return rowsUpdated > 0 && eventDetailUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Integer> rejectPlayer(List<Integer> approvedPlayerIds, int edid, int adminid) throws SQLException {
        String sql = "UPDATE individual SET registrationstatus = 'REJECTED', adminid = ? " +
                     "WHERE eventdetailid = ? AND playerid NOT IN (" + approvedPlayerIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                + ")";
        List<Integer> rejectedPlayerIds = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, adminid);
            statement.setInt(2, edid);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                String selectSql = "SELECT playerid FROM individual WHERE eventdetailid = ? AND playerid NOT IN ("
                        + approvedPlayerIds.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "))
                        + ")";
                try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                    selectStatement.setInt(1, edid);
                    ResultSet resultSet = selectStatement.executeQuery();
                    while (resultSet.next()) {
                        rejectedPlayerIds.add(resultSet.getInt("playerid"));
                    }
                }
            }

            connection.close();
            return rejectedPlayerIds;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<Team> getTeam(int edid) throws SQLException {
        ArrayList<Team> teams = new ArrayList<>();
        String sql = "SELECT teamid, teamname, teamstats " +
                     "FROM team  WHERE eventdetailid = ? ORDER BY teamstats DESC";

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
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
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
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return totalTeams;
    }

    public boolean approveTTopRank(int teamid, int edid, int adminid) throws SQLException {
        String sql = "UPDATE team SET registrationstatus = 'APPROVED', adminid = ? " +
                     "WHERE teamid = ? AND eventdetailid = ?";
        String sql2 = "UPDATE eventdetail SET edstatus = 'CLOSE' WHERE eventdetailid = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statement2 = connection.prepareStatement(sql2);

            statement.setInt(1, adminid);
            statement.setInt(2, teamid);
            statement.setInt(3, edid);
            int rowsUpdated = statement.executeUpdate();

            statement2.setInt(1, edid);
            int eventDetailUpdated = statement2.executeUpdate();

            connection.close();
            return rowsUpdated > 0 && eventDetailUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<String> getMemberEmailByTeamId(int teamId) throws SQLException {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT p.playeremail FROM player p JOIN team t ON p.playerid = t.playerid WHERE teamid = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, teamId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                emails.add(resultSet.getString("playeremail"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return emails;
    }

    public List<Integer> rejectTeam(List<Integer> approvedTeamIds, int edid, int adminid) throws SQLException {
        String sql = "UPDATE team SET registrationstatus = 'REJECTED', adminid = ? " +
                "WHERE eventdetailid = ? AND teamid NOT IN (" + approvedTeamIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                + ")";
        List<Integer> rejectedTeamIds = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, adminid);
            statement.setInt(2, edid);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the IDs of the rejected teams
                String selectSql = "SELECT teamid FROM team WHERE eventdetailid = ? AND teamid NOT IN ("
                        + approvedTeamIds.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "))
                        + ")";
                try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                    selectStatement.setInt(1, edid);
                    ResultSet resultSet = selectStatement.executeQuery();
                    while (resultSet.next()) {
                        rejectedTeamIds.add(resultSet.getInt("teamid"));
                    }
                }
            }
            connection.close();
            return rejectedTeamIds;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public EventDetail getEventDetail(int eventdetailid) throws SQLException {
        EventDetail eventDetail = null;
        String sql = "SELECT ed.eventdetailid, ed.edtype, ed.edcapacity, ed.edvenue, ed.edstate, ed.edstatus, ed.edstats, ed.eddate, e.eventid, e.eventname "
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
                Date eddate = resultSet.getDate("eddate");
                int eventid = resultSet.getInt("eventid");
                String eventname = resultSet.getString("eventname");

                eventDetail = new EventDetail(edid, edtype, edcapacity, edvenue, edstate, edstatus, edstats, eddate,
                        eventid, eventname);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return eventDetail;
    }
}
