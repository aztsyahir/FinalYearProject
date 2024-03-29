package com.heroku.java.DAO.Player;

import com.heroku.java.MODEL.Player;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerSignUpDAO {
    private final DataSource dataSource;

    public PlayerSignUpDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Player PlayerSignUp(Player player) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO player (playername, playeremail, playergender, playerage, playerpassword) VALUES (?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String playername = player.getPlayername();
            String playeremail = player.getPlayeremail();
            String playergender = player.getPlayergender();
            int playerage = player.getPlayerage();
            String playerpassword = player.getPlayerpassword();

            statement.setString(1, playername);
            statement.setString(2, playeremail);
            statement.setString(3, playergender);
            statement.setInt(4, playerage);
            statement.setString(5, playerpassword);
            statement.executeUpdate();
        }
        return player;
    }
}
