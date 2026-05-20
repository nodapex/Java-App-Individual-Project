package com.bptn.car_management_system.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Ticket DAO for CRUD operations
public class TicketDAO extends BaseDAO {
    public Ticket createTicket(String plateNumber, String slotNumber) {
        Ticket ticket = new Ticket(plateNumber,slotNumber);
        // Ticket constructor automatically saves to DB and assigns ticketId
        return ticket;
    }

    public Ticket getTicket(User user, int ticketId) {

        String sqlQuery = """
                    select * from ticket
                    where ticketId in (
                        select ticketId
                        from ticket
                        join vehicle on ticket.plateNumber = vehicle.plateNumber
                        join user on user.userId = vehicle.userId
                        where ticket.ticketId = ? and user.userId = ?
                    );
                """;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {

            //PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            statement.setInt(1, ticketId);
            statement.setInt(2, user.getUserId());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int rowCount = rs.getInt(1);

                if (rowCount == 0) {
                    return null;
                }

                Ticket ticket = new Ticket(
                        ticketId,
                        rs.getString("plateNumber"),
                        rs.getString("slotNumber"),
                        rs.getLong("checkInTime"),
                        rs.getString("dateCreated"),
                        rs.getString("status")
                );

                return ticket;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void deleteTicket(int ticketId) {
        String sql = "DELETE FROM Ticket WHERE ticketId = ?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, ticketId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}