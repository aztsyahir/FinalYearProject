package com.heroku.java.DAO.Player;

import java.sql.Connection;
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
}