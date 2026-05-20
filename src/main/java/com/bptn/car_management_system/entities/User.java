package com.bptn.car_management_system.entities;

// this is the class that models the users of the system
public class User {

  private int userId;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private String role;
  private String dateCreated;
  private String defaultVehicle;

  public User(){};

  public User(int userId, String username, String firstname, String lastname, String email, String password, String role, String dateCreated, String defaultVehicle) {
    this.userId = userId;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
    this.role = role;
    this.dateCreated = dateCreated;
    this.defaultVehicle = defaultVehicle;
  }

  public int getUserId(){
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getDefaultVehicle() {
    return defaultVehicle;
  }

  public void setDefaultVehicle(String defaultVehicle) {
    this.defaultVehicle = defaultVehicle;
  }

}
