package com.bptn.car_management_system.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ticket extends BaseEntity{

	private int ticketId;
	private int slotNumber;
	private String plateNumber;
	private long checkInTime;
	//private String dateCreated;

	private String dateExpired;
	private String status;

	// Constructor for new Ticket creation
	public Ticket(String plateNumber, String slotNumber) {
		this.plateNumber = plateNumber;
		this.slotNumber = Integer.parseInt(slotNumber);
		this.status = "active";
		this.checkInTime = System.currentTimeMillis();
		saveToDatabase();
	}

	// Overloaded constructor to rebuild Ticket from DB query
	public Ticket(int ticketId, String plateNumber, String slotNumber, long checkInTime, String dateCreated, String status) {
		this.ticketId = ticketId;
		this.plateNumber = plateNumber;
		this.slotNumber = Integer.parseInt(slotNumber);
		this.checkInTime = checkInTime;
		this.dateCreated = dateCreated;
		this.status = status;
	}

	// For display to user
	public Ticket(int ticketId, String plateNumber, int slotNumber,String status, String dateCreated, String dateExpired) {
		this.ticketId = ticketId;
		this.plateNumber = plateNumber;
		this.slotNumber = slotNumber;
		this.status = status;
		this.dateCreated = dateCreated;
		this.dateExpired = dateExpired;

	}

	@Override
	public void saveToDatabase() {
		String sql = "INSERT INTO Ticket (plateNumber, checkInTime, status, slotNumber) VALUES (?, ?, ?, ?)";

		try (Connection conn = getConnection();
				PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			statement.setString(1, plateNumber);
			statement.setLong(2, checkInTime);
			statement.setString(3, "active"); //status
			statement.setInt(4, slotNumber); //status
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				this.ticketId = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Expire ticket
	public void invalidateTicket() {
		String sql = "update ticket set status = 'expired', dateExpired = ? where ticketId = ?";
		try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, this.dateExpired);
			statement.setInt(2, this.ticketId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Getters & Setters
	public int getTicketId() {
		return ticketId;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public String getDateExpired() {
		return dateExpired;
	}

	public void setDateExpired(String dateExpired) {
		this.dateExpired = dateExpired;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public long getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(long checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}