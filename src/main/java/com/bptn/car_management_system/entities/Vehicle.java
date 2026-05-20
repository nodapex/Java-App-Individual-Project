package com.bptn.car_management_system.entities;

public class Vehicle {

	private String plateNumber;
	private int userId;
	private String type;
	private String dateCreated;

	public Vehicle(String plateNumber, int userId, String type, String dateCreated) {
		this.plateNumber = plateNumber;
		this.userId = userId;
		this.type = type;
		this.dateCreated = dateCreated;
	}

	// Getters & Setters
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

}