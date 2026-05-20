package com.bptn.car_management_system.entities;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlotDAOTest {
    private static Connection connection;
    private static SlotDAOMock slotDAO;

    @BeforeAll
    static void setUp() throws Exception {
        // Connect to the sqlite test database
        connection = BaseDAO.getTestConnection();

        // Initialize DAO with test database connection
        slotDAO = new SlotDAOMock(connection);

    }

    @AfterAll
    static void tearDown() throws Exception {
        connection.close(); // Close database connection after tests
    }

    @Test
    void testGetAllSlotsDTO_ReturnsCorrectData() throws Exception {
        List<SlotDTO> slots = slotDAO.getAllSlotsDTO();

        assertNotNull(slots);
        assertFalse(slots.isEmpty(), "Slots list should not be empty");

        SlotDTO slot = slots.get(0);
        assertEquals("mary", slot.username);
        assertEquals("Mary", slot.firstname);
        assertEquals("Anderson", slot.lastname);
        assertEquals("Honda Accord", slot.vehicleType);
        assertEquals("MAR9348", slot.vehiclePlate);
        assertEquals("Occupied", slot.parkingStatus);
    }

    @Test
    void testGetAllSlotsDTO_EmptyResult() throws Exception {
        // Clear the parkingSlot table for this test
        try (Statement stmt = connection.createStatement()) {
            //stmt.execute("DELETE FROM parkingSlot");
        }

        List<SlotDTO> slots = slotDAO.getEmptySlotsDTO();
        assertNotNull(slots);
        assertTrue(slots.isEmpty(), "Slots list should be empty");
    }
}
