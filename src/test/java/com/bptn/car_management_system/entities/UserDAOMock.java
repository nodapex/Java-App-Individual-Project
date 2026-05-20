package com.bptn.car_management_system.entities;

public class UserDAOMock {

    // this is just to mock the actual database user creation
    public boolean createUser(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            return false;  // Fail if any of the parameters are null
        }

        // mocking inserting the user (no actual database interaction)
        System.out.println("User created with username: " + username + ", role: " + role);

        // Return true to simulate a successful user creation
        return true;
    }

    public boolean createUser(String username, String firstname, String lastname, String email, String password, String role, String defaultVehicle) {
        if (username == null || password == null || role == null || firstname == null || lastname == null || email == null || defaultVehicle == null) {
            return false;  // Fail if any parameter is null
        }

        // Simulate inserting the user (no actual database interaction)
        System.out.println("User created: " + username + ", " + role);

        // Return true to simulate a successful user creation
        return true;
    }
}
