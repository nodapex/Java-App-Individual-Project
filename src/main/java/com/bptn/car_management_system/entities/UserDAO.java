package com.bptn.car_management_system.entities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// User DAO for CRUD operations
public class UserDAO extends BaseDAO {

    // prep for JUnit Test
    private Connection connection;

    public UserDAO() {

    }

    // utilized for JUnit testing purposes
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // method that validates user login
    // demonstrates Liskov Substitution Principle (LSP) in the way User and Admin classes are used
    public static User validateLoginDetails(String username, String password) throws SQLException {

        User user; // this is an object of a super class, from which Admin class is derived

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("select * from user where lower(username) = ?")
        ) {

            // get password hash:
            // the program used hashed password and does not store plain text password in the
            // database in line with best practice
            String hashedPassword = DbUtilityDAO.hashPassword(password);

            // get the data
            stmt.setString(1, username.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (Objects.equals(rs.getString("password"), hashedPassword)) {
                    System.out.println("Login details found in the database for: " + username);

                    // LSP demonstrated here
                    if (rs.getString("role").equals("admin")) {
                        user = new Admin(); // user, an object of a superclass User is being substituted with an object of its subclass without affecting the correctness of the program.
                    } else {
                        user = new User();
                    }

                    user.setUserId(rs.getInt("userId"));
                    user.setUsername(rs.getString("username"));
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setDateCreated(rs.getString("dateCreated"));
                    user.setDefaultVehicle(rs.getString("defaultVehicle"));
                    return user;
                } else {
                    System.out.println("Incorrect username or password for " + username);
                    return null;
                }
            } else {
                System.out.println("No login details found for user: " + username);
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // check if username already exists
    public static boolean usernameExists(String username) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select * from user where lower(username) = ?")
        ) {
            // get the data
            stmt.setString(1, username.toLowerCase());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                System.out.println("Username " + username + " already exists in the database!");
                return true;
            } else {
                System.out.println("Username  " + username + " does not exist in the database ");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // check if user is admin
    public static boolean isAdmin(String username) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select * from user where lower(username) = ? and role = 'admin'")
        ) {
            // get the data
            stmt.setString(1, username.toLowerCase());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                System.out.println("Username " + username + " is Admin!");
                return true;
            } else {
                System.out.println("Username  " + username + " is not Admin!");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // overloaded method for JUnit testing purpose
    public User validateLoginDetails(String username, String password, String hashedPassword) throws SQLException {
        String sql = "SELECT * FROM \"user\" WHERE LOWER(username) = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //String hashedPassword = DbUtilityDAO.hashPassword(password);
            stmt.setString(1, username.toLowerCase());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getString("password").equals(hashedPassword)) {
                    User user = new User();
                    user.setUserId(rs.getInt("userId"));
                    user.setUsername(rs.getString("username"));
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setDateCreated(rs.getString("dateCreated"));
                    user.setDefaultVehicle(rs.getString("defaultVehicle"));
                    return user;
                }
            }
        }
        return null;
    }

    public void createUser(String username, String password, String role) {
        String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            // Hash the user's password before storing it
            String hashedPassword = DbUtilityDAO.hashPassword(password);

            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createUser(String username, String firstname, String lastname, String email, String password, String role, String defaultVehicle) {
        String sql = """ 
                INSERT INTO user (username, firstname, lastname, email, password, role, defaultVehicle) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Hash the user's password before storing it
            String hashedPassword = DbUtilityDAO.hashPassword(password);

            // Set the parameters for the insert query
            statement.setString(1, username);
            statement.setString(2, firstname);
            statement.setString(3, lastname);
            statement.setString(4, email);
            statement.setString(5, hashedPassword); // store the hash
            statement.setString(6, role); // default is user
            statement.setString(7, defaultVehicle);

            // Execute the insert
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("User created successfully!");
                return true;
            } else {
                System.out.println("User creation failed.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateUserPassword(int userId, String password) {
        String sql = "UPDATE user SET password = ? WHERE userId = ?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            // Hash the user's password before storing it
            String hashedPassword = DbUtilityDAO.hashPassword(password);
            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserRole(int userId, String role) {
        String sql = "UPDATE user SET role = ? WHERE userId = ?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, role);
            statement.setInt(2, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(int id, String username, String firstname, String lastname, String email, String password, String role, String defaultVehicle) {
        String sql = "UPDATE user SET username = ?, firstname = ?, lastname = ?, email = ?, password = ?, role = ?, defaultVehicle = ? WHERE userId = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // Hash the user's password before storing it
            String hashedPassword = DbUtilityDAO.hashPassword(password);

            // Set the parameters for the update query
            statement.setString(1, username);
            statement.setString(2, firstname);
            statement.setString(3, lastname);
            statement.setString(4, email);
            statement.setString(5, hashedPassword);
            statement.setString(6, role);
            statement.setString(7, defaultVehicle);
            statement.setLong(8, id); // Set the id as the condition for the update

            // Execute the update
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
                return true;
            } else {
                System.out.println("User not found, update failed.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM user WHERE userId = ?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get user using id
    public User getUser(int userId) {
        String sql = "SELECT * FROM user WHERE userId = ?";
        User user = new User();

        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setDefaultVehicle(rs.getString("defaultVehicle"));
                user.setDateCreated(rs.getString("dateCreated"));

                System.out.println("User: " + user.getUsername() + " Role: " + user.getRole());
            } else {
                System.out.println("User record not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // get user using username
    public User getUser(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        User user = new User();

        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setDefaultVehicle(rs.getString("defaultVehicle"));
                user.setDateCreated(rs.getString("dateCreated"));

                System.out.println("User: " + user.getUsername() + " Role: " + user.getRole());
            } else {
                System.out.println("User record not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<User> getUsers() {
        int recordCount = 0;
        List<User> users = new ArrayList<>();
        String sqlQuery = "SELECT * FROM User";

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sqlQuery)) {

            while (rs.next()) {
                recordCount++;
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setDefaultVehicle(rs.getString("defaultVehicle"));
                user.setDateCreated(rs.getString("dateCreated"));
                users.add(user);
            }

            if (recordCount == 0) {
                System.out.println("No record found!");
                return new ArrayList<User>();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}