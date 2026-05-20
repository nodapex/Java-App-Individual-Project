package com.bptn.car_management_system.entities;

import java.sql.*;

// the Billing class models the billing of vehicle owners' usage of the parking lot
// just like other entity classes, it demonstrates OOP Encapsulation concept
public class Billing extends BaseEntity {

    private int billingId;
    private int ticketId;
    private int userId;
    private double amount;
    private long billingTime;
    private double hourlyRate; // Admin can adjust this rate
    //private String dateCreated;

    public Billing() {
        // fetch the current rate from the database
        this.hourlyRate = DbUtilityDAO.getBillRate();
    }

    // Parametrized constructor for bills display to user
    public Billing(int billingId, int ticketId, double amount, long billingTime, String dateCreated) {
        this.billingId = billingId;
        this.ticketId = ticketId;
        this.userId = userId;
        this.amount = amount;
        this.billingTime = billingTime;
        this.dateCreated = dateCreated;
    }

    public Billing(int billingId, int ticketId, int userId, double amount, long billingTime, String dateCreated) {
        this.billingId = billingId;
        this.ticketId = ticketId;
        this.userId = userId;
        this.amount = amount;
        this.billingTime = billingTime;
        this.dateCreated = dateCreated;
    }

    // Calculate fee due to car owner
    public double calculateFee(long checkInTime) {
        long currentTime = System.currentTimeMillis();
        long durationMinutes = (currentTime - checkInTime) / (1000 * 60);
        long durationHour = durationMinutes/60; //(currentTime - checkInTime) / (1000 * 60 * 60);
        double fee = Math.max(1, durationHour) * hourlyRate;

        // update the billing table
        this.amount = fee;
        this.billingTime = durationMinutes; // store the duration in minutes
        saveToDatabase(); //update the billing table
        return fee;
    }

    // Getters & Setters
    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getBillingId() {
        return billingId;
    }

    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getBillingTime() {
        return billingTime;
    }

    public void setBillingTime(long billingTime) {
        this.billingTime = billingTime;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public void saveToDatabase() {
        String sql = """
                insert into billing (ticketId, userId, amount, billingTime)
                values (?,?,?,?);
                """;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, this.ticketId);
            statement.setInt(2, this.userId);
            statement.setDouble(3, this.amount);
            statement.setLong(4, this.billingTime);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}