package com.heroku.java.DAO.Player;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import com.heroku.java.MODEL.Player;

@Repository
public class PlayerSignInDAO {
    private final DataSource dataSource;

    public PlayerSignInDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Player PlayerSignIn(String playeremail, String playerpassword) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT playerid, playername FROM player WHERE playeremail = ? AND playerpassword = ?";

            final var statement = connection.prepareStatement(sql);
            statement.setString(1, playeremail);
            statement.setString(2, playerpassword);

            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int playerid = resultSet.getInt("playerid");
                String playername = resultSet.getString("playername");
                return new Player(playerid, playername, playeremail, playerpassword);
            }

        }
        return null;
    }
}
