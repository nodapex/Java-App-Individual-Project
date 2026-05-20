package com.bptn.car_management_system.entities;

import javafx.scene.control.TableView;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// This is a core utility class to handle all the more complex Db operations, e.g queries
// that affect multiple tables. It provides static methods that are called from the various
// entity classes that model the car parking system

public class DbUtilityDAO extends BaseDAO {

    // this is the method that seeds the Sqlite database by creating the core tables such as
    // user, vehicle, parkingSlot, ticket, billing,
    // it creates the required tables in the database if they do not already exist
    // thereafter, sample/demo data are inserted to the user and parkingLot tables
    // setup sample users and to initialize the capacity of the parking lot
    public static void initializeDatabase() {
        try (
                Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // user table
            String sqlUsers = """
                    create table if not exists user
                    (
                        userId         integer not null
                            constraint user_pk
                                primary key autoincrement,
                        username       TEXT    not null,
                        firstname      TEXT    not null,
                        lastname       TEXT    not null,
                        email          TEXT    not null,
                        password       TEXT    not null,
                        role           TEXT,
                        dateCreated    TEXT default (datetime('now')),
                        defaultVehicle TEXT
                    );
                    """;
            stmt.execute(sqlUsers);

            // here is seeding the user table with demo user records, this will make it easy
            // to test run the program. Additionally, new user accounts can created from the
            // login screen
            String sqlSeedUserTable = """
                    insert into user (
                      username,
                      firstname,
                      lastname,
                      email,
                      password,
                      defaultVehicle,
                      role
                    )
                    VALUES
                      ('admin', 'Admin', 'Admin', 'hathunda@gmail.com', '$2a$10$lhYxALkycPCfWCsP3N4a8OQgnaUCGz4uGIJobEyf0SB6veYr6f85m', NULL, 'admin'),
                      ('paul', 'Paul', 'Akinpelu', 'hathunda@gmail.com', '$2a$10$lhYxALkycPCfWCsP3N4a8OEQR6HcqfOsi.azDbdsqVSIMmJnbf0qa', 'PAL2025', 'admin'),
                      ('peter', 'Peter', 'Gilbert', 'peter.gilbert@gmail.com', '$2a$10$lhYxALkycPCfWCsP3N4a8Oq.yzpm21be0oRwACj4rUFYjHoJRfCx.', 'PET0234', 'user'),
                      ('mary', 'Mary', 'Anderson', 'mary.anderson@hotmail.com', '$2a$10$lhYxALkycPCfWCsP3N4a8Oq.yzpm21be0oRwACj4rUFYjHoJRfCx.', 'MAR9348', 'user');
                    
                    """;


            // only insert if the table is empty
            String sqlCheckIfUserExists = "select count(*) from user";
            ResultSet rsUsers = stmt.executeQuery(sqlCheckIfUserExists);
            if (rsUsers.next()) {
                int rowCount = rsUsers.getInt(1);
                if (rowCount == 0) {
                    System.out.println("The user table is empty.");
                    // initialize it with seed data
                    stmt.execute(sqlSeedUserTable);
                } else {
                    System.out.println("The user table is not empty.");
                }
            } else {
                System.out.println("Nothing found.");
            }

            // settings table - bill rate
            String sqlSettings = """
                    create table if not exists settings
                    (
                    	rate REAL not null
                    );
                    """;

            stmt.execute(sqlSettings);

            // seed rate into billing table
            String sqlBillRate = """
                    insert into settings (rate)
                    values (5.0);
                    """;

            // only insert if the table is empty
            String sqlCheckIfRateExists = "select count(rate) from settings";
            ResultSet rsRate = stmt.executeQuery(sqlCheckIfRateExists);
            if (rsRate.next()) {
                int rowCount = rsRate.getInt(1);
                if (rowCount == 0) { // only insert if the table is empty
                    System.out.println("The rate record is not set!");
                    // initialize it with seed data
                    stmt.execute(sqlBillRate);
                } else {
                    System.out.println("The rate record is already set!");
                }
            } else {
                System.out.println("Nothing found.");
            }


            // parkingSlots table
            String sqlParkingSlots = """                               
                    create table if not exists parkingSlot
                    (
                        slotNumber   INTEGER not null
                            primary key autoincrement,
                        isOccupied   BOOLEAN,
                        vehiclePlate TEXT
                    );
                    """;
            stmt.execute(sqlParkingSlots);

            // vehicle table
            String createVehicles = """
                    create table if not exists vehicle
                    (
                        plateNumber TEXT
                            primary key,
                        type        TEXT,
                        userId      integer
                            constraint vehicle_user_userId_fk
                                references user
                    );
                    """;
            stmt.execute(createVehicles);

            // tickets table
            String createTickets = """
                    create table if not exists ticket
                    (
                        ticketId    INTEGER                        not null
                            primary key autoincrement,
                        plateNumber TEXT
                            constraint ticket_vehicle_plateNumber_fk
                                references vehicle,
                        checkInTime INTEGER,
                        dateCreated TEXT default (datetime('now')) not null,
                        status      TEXT,
                        dateExpired TEXT,
                        slotNumber  INTEGER
                            constraint ticket_parkingSlot_slotNumber_fk
                                references parkingSlot
                    );
                    """;
            stmt.execute(createTickets);

            // billing table
            String createBilling = """
                    create table if not exists billing
                    (
                        billingId   INTEGER
                            primary key autoincrement,
                        ticketId    INTEGER
                            references ticket,
                        userId      INTEGER,
                        amount      REAL,
                        billingTime INTEGER,
                        dateCreated TEXT default (datetime('now')) not null
                    );
                    """;
            stmt.execute(createBilling);

            // initialize the parking lost to a capacity of 20 slots
            String createSlots = """
                    insert or ignore into parkingSlot (isOccupied)
                    values
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false),
                    (false);
                    """;

            // only insert if the table is empty
            String sqlCheckIfExists = "select count(*) from parkingSlot";
            ResultSet rs = stmt.executeQuery(sqlCheckIfExists);
            if (rs.next()) {
                int rowCount = rs.getInt(1);
                if (rowCount == 0) {
                    System.out.println("The parkingSlot table is empty.");
                    // initialize it with seed data
                    stmt.execute(createSlots);
                } else {
                    System.out.println("The parkingSlot table is not empty.");
                }
            } else {
                System.out.println("Nothing found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // check if slot is available for booking
    public static boolean isSlotAvailble(int slotNumber) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select * from parkingSlot where slotNumber = ? and isOccupied = false")
        ) {
            // get the data
            stmt.setInt(1, slotNumber);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                System.out.println("Slot " + slotNumber + " is available for booking!");
                return true;
            } else {
                System.out.println("Slot  " + slotNumber + " is available for booking!");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // fetch all parking slots data from the database and return as an arrayList
    public static List<ParkingSlot> getAllSlots() {
        int recordCount = 0;
        List<ParkingSlot> slots = new ArrayList<>();
        String sqlQuery = "SELECT * FROM ParkingSlot";

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                recordCount++;
                ParkingSlot slot = new ParkingSlot();
                slot.setSlotNumber(resultSet.getInt("slotNumber"));
                slot.setIsOccupied(resultSet.getBoolean("isOccupied"));
                slot.setVehiclePlate(resultSet.getString("vehiclePlate"));
                slots.add(slot);
            }

            if (recordCount == 0) {
                System.out.println("No record found!");
                return new ArrayList<ParkingSlot>();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return slots;
    }

    // method to get user info that includes their vehicle info, and parking slot info using
    // table joins query for all users. It returns an arraylist of slot DTOs
    public static List<SlotDTO> getAllSlotsDTO() {
        int recordCount = 0;
        List<SlotDTO> slots = new ArrayList<>();

        String sqlQuery = """
                select user.UserId, user.username, user.firstname, user.lastname, vehicle.type, parkingSlot.slotNumber, parkingSlot.vehiclePlate, parkingSlot.isOccupied from parkingSlot
                inner join vehicle on parkingSlot.vehiclePlate = vehicle.plateNumber
                inner join user on vehicle.userId = user.UserId;
                """;

        // use try-with-resources statement to ensure that resources are automatically closed
        // at the end of the statement. This eliminates the need for a finally block to manually close resources, reducing boilerplate code and preventing resource leaks.
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                recordCount++;
                String status = resultSet.getBoolean("isOccupied") ? "Occupied" : "Available";
                SlotDTO slotDTO = new SlotDTO();
                slotDTO.userId = resultSet.getLong("userId");
                slotDTO.username = resultSet.getString("username");
                slotDTO.firstname = resultSet.getString("firstname");
                slotDTO.lastname = resultSet.getString("lastname");
                slotDTO.vehicleType = resultSet.getString("type");
                slotDTO.vehiclePlate = resultSet.getString("vehiclePlate");
                slotDTO.slotNumber = Integer.toString(resultSet.getInt("slotNumber"));
                slotDTO.parkingStatus = status;

                slots.add(slotDTO);
                System.out.println("User: " + resultSet.getString("username") + " Status: " + status);
            }

            if (recordCount == 0) {
                System.out.println("No record found!");
                return new ArrayList<SlotDTO>();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return slots;
    }

    // method to get user info that includes their vehicle info, and parking slot info using
    // table joins query. It returns an arraylist of slot DTOs
    public static void getUserSlotDetails(long userId) {
        String sqlQuery = """
                select user.UserId, user.username, user.firstname, user.lastname, vehicle.type, parkingSlot.vehiclePlate, parkingSlot.isOccupied from parkingSlot
                inner join vehicle on parkingSlot.vehiclePlate = vehicle.plateNumber
                inner join user on vehicle.userId = user.UserId
                where user.UserId = ?;
                """;

        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getBoolean("isOccupied") ? "Occupied" : "Available";
                System.out.println("User: " + resultSet.getString("username") + " Status: " + status);
            } else {
                System.out.println("Record not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //
    public static String hashPassword(String plainPassword) {
        // Generate a salt and hash the password
        return BCrypt.hashpw(plainPassword, "$2a$10$lhYxALkycPCfWCsP3N4a8O");

    }

    // Load ParkingSlot for display to users
    public static void loadParkingSlot(TableView<ParkingSlot> tableView) {
        int recordCount = 0;
        String sqlQuery = "SELECT * FROM ParkingSlot";

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sqlQuery)) {

            tableView.getItems().clear();
            while (rs.next()) {
                // Add the data to the TableView
                tableView.getItems().add(new ParkingSlot(
                        rs.getInt("slotNumber"),
                        rs.getBoolean("isOccupied"),
                        rs.getString("vehiclePlate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // load Tickets for display to a user, only admin can see all users' tickets
    public static void loadTickets(User user, TableView<Ticket> tableView) {
        int recordCount = 0;
        String sqlQuery;

        // select all records for admin
        if ("admin".equals(user.getRole())) {
            sqlQuery = "select * from ticket";
        } else {
            // select only current user's records
            sqlQuery = """
                               select * from ticket
                               where ticketId in (
                                   select ticketId
                                   from ticket
                                            join vehicle on ticket.plateNumber = vehicle.plateNumber
                                            join user on user.userId = vehicle.userId
                                   where user.userId = """ + user.getUserId() + """
                               );
                               """;
        }

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
        ) {

            //statement.setInt(1,user.getUserId()) ;
            ResultSet rs = statement.executeQuery(sqlQuery);

            tableView.getItems().clear();
            while (rs.next()) {
                // Add the data to the TableView
                tableView.getItems().add(new Ticket(
                        rs.getInt("ticketId"),
                        rs.getString("plateNumber"),
                        rs.getInt("slotNumber"),
                        rs.getString("status"),
                        rs.getString("dateCreated"),
                        rs.getString("dateExpired")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // load billing data for display to a user, only admin can see all users' bills
    public static void loadBills(User user, TableView<Billing> tableView) {
        int recordCount = 0;
        String sqlQuery;

        if ("admin".equals(user.getRole())) {
            sqlQuery = "select * from billing"; // select all records for admin
        } else {
            // select only current user's records
            sqlQuery = "select * from billing where userId = " + user.getUserId() + ";";
        }

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
        ) {
            ResultSet rs = statement.executeQuery(sqlQuery);

            tableView.getItems().clear();
            while (rs.next()) {
                // Add the data to the TableView
                tableView.getItems().add(new Billing(
                        rs.getInt("billingId"),
                        rs.getInt("ticketId"),
                        rs.getDouble("amount"),
                        rs.getLong("billingTime"),
                        rs.getString("dateCreated")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean setBillRate(double billRate) {

        String sqlUpdateRate = "Update Settings set rate = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdateRate)) {

            stmt.setDouble(1, billRate);
            int result = stmt.executeUpdate();
            return (result > 0);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double getBillRate() {

        String sqlRate = "select rate from Settings";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlRate)) {
            double rate = rs.getDouble(1);
            return rate;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 5; // default
    }


    public static double getTotalRevenue() {

        String sqlQuery = "select sum(amount) as totalAmount from billing";

         try(
                 Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlQuery);
                 ){

             return rs.getDouble("totalAmount");

         } catch (SQLException e) {
             throw new RuntimeException(e);
         }

    }
}
