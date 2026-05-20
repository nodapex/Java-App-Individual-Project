package com.bptn.car_management_system.entities;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// this is the reserved class for performing administrative tasks such as managing users,
// setting car lot capacity, viewing car slot usage, tickets, bills and how much is generated..
// in line with Objective Oriented concepts and SOLID principles, the principle of inheritance
// is demonstrated here and in  other parts of the code
public class Admin extends User{

    private Map<Integer, User> userMap = new HashMap<>();
    private Set<User> userSet = new HashSet<>();
    private double billRate;

    // Method to add a user to the userMap (using userId as the key)
    public void addUser(User user) {
        userMap.put(user.getUserId(), user);
        userSet.add(user);
    }

    // Method to remove a user from the userMap and userSet
    public void removeUser(int userId) {
        userMap.remove(userId);
        userSet.removeIf(user -> user.getUserId() == userId);
    }

    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    // Method to get users by role
    public Collection<User> getUsersByRole(String role) {
        return userMap.values().stream()
                .filter(user -> user.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }


    // Method to list users with a particular condition, using lambda expression
    public void listUsersWithCondition() {
        userMap.values().stream()
                .filter(user -> user.getRole().equalsIgnoreCase("admin"))
                .forEach(user -> System.out.println(user.getUsername()));
    }

    // Method to get a set of all users (without duplicates)
    public Set<User> getAllUsersSet() {
        return userSet;
    }


    public void manageParkingSlots(){
        // work in progress
    }

    public void setBillRate(double rate) {
        Billing billing = new Billing();
        this.billRate = rate;
        System.out.println("The bill rate has been set to " + rate + " per hour.");

        // work in progress...
    }
    

    public double getBillRate(){
        return this.billRate;
        // work in progess
    }


}


