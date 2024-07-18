package com.heroku.java.DAO.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerStatsDAO {
    private final DataSource dataSource;

    public PlayerStatsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void updatePlayerStats(int playerid, int totalPercentage) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE player SET playerstats = ? WHERE playerid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, totalPercentage);
            statement.setInt(2, playerid);
            statement.executeUpdate();
            connection.close();
        }
    }
}
