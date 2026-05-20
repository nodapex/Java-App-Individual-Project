package com.bptn.car_management_system.entities;

import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private static Connection connection;
    private static UserDAO userDAO;

    @BeforeAll
    static void setUp() throws Exception {
        // Connect to sqlite test database

         //connection = userDAO.getTestConnection();
        connection = BaseDAO.getTestConnection();

        // Initialize DAO with test database connection
        userDAO = new UserDAO(connection);
    }

    @AfterAll
    static void tearDown() throws Exception {
        connection.close(); // Close database connection after tests
    }

    @Test
    void testValidateLoginDetails_SuccessfulLogin() throws Exception {
        String username = "peter";
        String password = "123456";

        // Simulate password hashing function (replace with real hashing if needed)
        String hashedPassword = "$2a$10$lhYxALkycPCfWCsP3N4a8Oq.yzpm21be0oRwACj4rUFYjHoJRfCx.";
        //DbUtilityDAO.hashPassword = (p) -> hashedPassword;

        User user = userDAO.validateLoginDetails(username, password, hashedPassword);
        
        assertNotNull(user);
        assertEquals("peter", user.getUsername());
        assertEquals("Peter", user.getFirstname());
    }

    @Test
    void testValidateLoginDetails_InvalidPassword() throws Exception {
        String username = "testuser";
        String password = "wrongPassword";

        // Simulate incorrect hashed password
        //DbUtilityDAO.hashPassword = (p) -> "wrongHashedPassword";

        User user = userDAO.validateLoginDetails(username, password, null);
        assertNull(user);
    }

    @Test
    void testValidateLoginDetails_UserNotFound() throws Exception {
        String username = "nonexistentuser";
        String password = "password";

        User user = userDAO.validateLoginDetails(username, password, null);
        assertNull(user);
    }
}