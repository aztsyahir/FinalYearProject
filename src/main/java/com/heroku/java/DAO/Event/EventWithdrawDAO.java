package com.heroku.java.DAO.Event;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import org.springframework.stereotype.Repository;

@Repository
public class EventWithdrawDAO {
    private final DataSource dataSource;

    public EventWithdrawDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void WithdrawEvent(int registrationid) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE registration SET registrationstatus =? WHERE registrationid =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "WITHDRAWN");
            statement.setInt(2, registrationid);
            statement.executeUpdate();
        }
    }
}
