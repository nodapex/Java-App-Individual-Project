package com.bptn.car_management_system.archive;

import java.sql.*;

public class DbConnection {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }



    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            String createTables = """
                        CREATE TABLE IF NOT EXISTS ParkingSlot (
                            slotNumber INTEGER PRIMARY KEY,
                            isOccupied BOOLEAN
                        );
                    """;
            stmt.executeUpdate(createTables);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //

    public void checkOutVehicle(String plateNumber) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT checkInTime FROM Ticket WHERE plateNumber = ? ORDER BY id DESC LIMIT 1");
        ) {
            stmt.setString(1, plateNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long checkInTime = rs.getLong("checkInTime");
                long duration = (System.currentTimeMillis() - checkInTime) / (1000 * 60 * 60);
                double fee = Math.max(1, duration) * 5.0;

                System.out.println("Vehicle checked out. Total fee: $" + fee);

                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Ticket WHERE plateNumber = ?")) {
                    deleteStmt.setString(1, plateNumber);
                    deleteStmt.executeUpdate();
                }
            } else {
                System.out.println("No active ticket found for this vehicle.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


//
class DatabaseManager {
    private static final String URL = "jdbc:sqlite:plutonine_db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Not used here yet
    public static void initializeDatabase() {
        try (
                Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            String createParkingSlots = """
                    CREATE TABLE IF NOT EXISTS ParkingSlot (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        isOccupied BOOLEAN DEFAULT FALSE)
                    """;
            stmt.execute(createParkingSlots);

            String createVehicles = """
                    CREATE TABLE IF NOT EXISTS Vehicle (
                                plateNumber TEXT PRIMARY KEY,
                                type TEXT)
                    """;
            stmt.execute(createVehicles);

            String createTickets = """
                    CREATE TABLE IF NOT EXISTS Ticket (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        plateNumber TEXT,
                                        checkInTime INTEGER,
                                        FOREIGN KEY (plateNumber) REFERENCES Vehicle(plateNumber))
                    """;
            stmt.execute(createTickets);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}