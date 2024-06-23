package com.heroku.java.DAO.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public class PlayerEmailDAO {
    private final DataSource dataSource;

    public PlayerEmailDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<String> getPlayerEmail() throws SQLException {
        ArrayList<String> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT playeremail From player";
            final var statement = connection.prepareStatement(sql);
            final var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String EmailToPlayer = resultSet.getString("playeremail");
                players.add(EmailToPlayer);
            }
        }
        return players;
    }

     public ArrayList<String> getMemberEmail(int edid) throws SQLException {
        ArrayList<String> players = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT player.playeremail " +
                         "FROM player " +
                         "JOIN member ON player.playerid = member.playerid " +
                         "JOIN team ON member.teamid = team.teamid " +
                         "WHERE team.eventdetailid = ?";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, edid);
            
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String emailToMember = resultSet.getString("playeremail");
                players.add(emailToMember);
            }
        }

        return players;
    }

    public ArrayList<String> getRegisterEmail(int edid) throws SQLException {
        ArrayList<String> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT playeremail FROM player JOIN registraion ON player.playerid = registraion.playerid WHERE registraion.eventdetailid = ?";
            final var statement = connection.prepareStatement(sql);
           
            statement.setInt(1, edid);
            
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String EmailToPlayer = resultSet.getString("playeremail");
                players.add(EmailToPlayer);
            }
        }
        return players;
    }

}