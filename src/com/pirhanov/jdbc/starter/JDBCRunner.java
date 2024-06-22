package com.pirhanov.jdbc.starter;

import com.pirhanov.jdbc.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JDBCRunner {
    public static void main(String[] args) throws SQLException {
//        Long flightId = 2L;
//        final var ticketsByFlyId = getTicketsByFlyId(flightId);
//        System.out.println(ticketsByFlyId);

        var result = getTicketsBetween(LocalDate.of(2020, 1, 1).atStartOfDay(), LocalDateTime.now());
        System.out.println(result);
    }

    private static List<Long> getTicketsBetween(LocalDateTime from, LocalDateTime to)
            throws SQLException {
        String sql =
                """
                         select flight.id
                         from flight
                        where departure_date between ? and ?
                         """;
        List<Long> result = new ArrayList<>();

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(from));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(to));

            System.out.println(sql);
            System.out.println(preparedStatement);

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        }
        return result;
    }

    private static List<Long> getTicketsByFlyId(Long flightId) throws SQLException {
        String sql =
                """
                                 select ticket.id
                                 from ticket
                                where flight_id = ?
                        """;

        List<Long> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, flightId);

            final var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getLong(1));
            }

        }
        return result;
    }
}