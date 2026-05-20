package com.bptn.car_management_system.entities;

import java.sql.*;

import static com.bptn.car_management_system.entities.BaseDAO.getConnection;

public class ParkingSlot {
	private int slotNumber;
	private Boolean isOccupied;
	private String vehiclePlate; // To identify the parked vehicle

	public ParkingSlot() {
	}

	public ParkingSlot(int slotNumber) {
		this.slotNumber = slotNumber;
		this.isOccupied = false;
		this.vehiclePlate = null;
	}

	public ParkingSlot(int slotNumber, boolean isOccupied, String vehiclePlate) {
		this.slotNumber = slotNumber;
		this.isOccupied = isOccupied;
		this.vehiclePlate = vehiclePlate;
	}

	public int updateSlotStatus() {
		String sql = """
                update parkingSlot
                set isOccupied = ?, vehiclePlate = ?
                WHERE slotNumber = ?
                """;

		try (Connection conn = getConnection();
			 PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			statement.setBoolean(1, this.isOccupied);
			statement.setString(2, this.vehiclePlate);
			statement.setInt(3, this.slotNumber);
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				int retVal = rs.getInt(1);
				return retVal;
			}

			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}


	// Getters & Setters
	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public boolean getIsOccupied() {
		return isOccupied;
	}

	// Overloadded version to return a string value indicating availability of slot
	public String getIsAvailable() {
		return isOccupied ? "Occupied" : "Available";
	}

	public void setIsOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public String getVehiclePlate() {
		return vehiclePlate;
	}

	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}


}