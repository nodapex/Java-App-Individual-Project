package com.bptn.car_management_system.entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SlotDAOMock {
    // prep for JUnit Test
    private Connection connection;


    public SlotDAOMock(Connection connection) {
        this.connection = connection;
    }

    public List<SlotDTO> getAllSlotsDTO() {
        int recordCount = 0;
        List<SlotDTO> slots = new ArrayList<>();

        String sqlQuery = """
                select user.UserId, user.username, user.firstname, user.lastname, vehicle.type, parkingSlot.slotNumber, parkingSlot.vehiclePlate, parkingSlot.isOccupied from parkingSlot
                inner join vehicle on parkingSlot.vehiclePlate = vehicle.plateNumber
                inner join user on vehicle.userId = user.UserId;
                """;

        // use try-with-resources statement to ensure that resources are automatically closed
        // at the end of the statement. This eliminates the need for a finally block to manually close resources, reducing boilerplate code and preventing resource leaks.
        try (
             Statement statement = connection.createStatement();
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

    public List<SlotDTO> getEmptySlotsDTO() {
        int recordCount = 0;
        List<SlotDTO> slots = new ArrayList<>();

        return slots;
    }

}
