package com.bptn.car_management_system.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Vehicle DAO for CRUD operations
public class VehicleDAO extends BaseDAO{

	public void createVehicle(String plateNumber, int userId, String type) {
		String sql = "insert or ignore into Vehicle (plateNumber, userId, type) VALUES (?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, plateNumber);
			statement.setInt(2, userId);
			statement.setString(3, type);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Vehicle getVehicle(String plateNumber) {
		String sql = "SELECT * FROM Vehicle WHERE plateNumber = ?";
		try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, plateNumber);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				return new Vehicle(
						rs.getString("plateNumber"),
						rs.getInt("userId"),
						rs.getString("type"),
						rs.getString("dateCreated"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

 	// Method to update a vehicle record
	public boolean updateVehicle(int userId, String type, String plateNumber) {
		String sql = "UPDATE vehicle SET userId = ?, type = ? WHERE plateNumber = ?";

		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {

			// Set the parameters for the update query
			statement.setInt(1, userId);
			statement.setString(2, type);
			statement.setString(3, plateNumber );

			// Execute the update
			int rowsUpdated = statement.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Vehicle updated successfully!");
				return true;
			} else {
				System.out.println("Vehicle not found, update failed.");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deleteVehicle(String plateNumber) {
		String sql = "DELETE FROM Vehicle WHERE plateNumber = ?";
		try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, plateNumber);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}