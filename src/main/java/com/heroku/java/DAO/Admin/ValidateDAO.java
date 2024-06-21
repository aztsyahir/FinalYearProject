package com.heroku.java.DAO.Admin;

import com.heroku.java.MODEL.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import java.sql.Date;
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

            public EventDetail getEventDetail(int edid) throws SQLException {
                EventDetail eventDetail = null;
                String sql = "SELECT ed.edtype, ed.edcapacity, ed.edvenue, ed.edstate, ed.edstatus, ed.edstats,e.eventid, e.eventname " +
                             "FROM eventdetail ed " +
                             "JOIN event e ON ed.eventid = e.eventid " +
                             "WHERE ed.eventdetailid = ?";
        
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement statement = connection.prepareStatement(sql)) {
                
                    statement.setInt(1, edid);
                    ResultSet resultSet = statement.executeQuery();
                
                    if (resultSet.next()) {
                        String edtype = resultSet.getString("edtype");
                        int edcapacity = resultSet.getInt("edcapacity");
                        String edvenue = resultSet.getString("edvenue");
                        String edstate = resultSet.getString("edstate");
                        String edstatus = resultSet.getString("edstatus");
                        int edstats = resultSet.getInt("edstats");
                        int eventid = resultSet.getInt("eventid");
                        String eventname = resultSet.getString("eventname");
                
                        eventDetail = new EventDetail(edid, edtype, edcapacity, edvenue, edstate, edstatus, edstats,eventid, eventname);
                    }
                
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e; // You may want to handle this differently in a real application
                }
                
                return eventDetail;
            }
}
