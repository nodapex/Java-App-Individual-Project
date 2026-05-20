package com.bptn.car_management_system.entities;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ParkingLot {
    List<ParkingSlot> slots;
    TicketDAO ticketDAO = new TicketDAO();
    VehicleDAO vehicleDAO = new VehicleDAO();

    public ParkingLot() {
        // Load slots from the database.
        this.slots = DbUtilityDAO.getAllSlots();
    }

    public ParkingLot(int capacity) {
        slots = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            slots.add(new ParkingSlot(i));
        }
    }

    //todo: remove this method and call DbUtilityDAO.getAllSlotsDTO() directly from the controllers
    public List<SlotDTO> getSlots() {
        return DbUtilityDAO.getAllSlotsDTO();
    }

    // Book a slot: create vehicle, mark slot as occupied, create ticket.
    public int bookSlot(User user, String slotNumber, String vehicleType, String licensePlate) {

        // check if this slot is still available for booking
        try {
            if (!DbUtilityDAO.isSlotAvailble(Integer.parseInt(slotNumber))) {
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // create vehicle record and generate ticket
        vehicleDAO.createVehicle(licensePlate, user.getUserId(), vehicleType); // Save vehicle details to DB.
        Ticket ticket = ticketDAO.createTicket(licensePlate, slotNumber);

        // Update the slot as occupied
        ParkingSlot slot = new ParkingSlot(Integer.parseInt(slotNumber));
        slot.setVehiclePlate(licensePlate);
        slot.setSlotNumber(Integer.parseInt(slotNumber));
        slot.setIsOccupied(true);
        int rs = slot.updateSlotStatus();

        //return generated ticket
        System.out.println("Slot booked! Ticket ID: " + ticket.getTicketId() + ", Slot: " + slotNumber);
        return ticket.getTicketId();

    }

    // Check-out vehicle: calculate fee, free up slot, delete/invalidate ticket.
    public double checkOutVehicle(User user, int ticketId) {
        Ticket ticket = ticketDAO.getTicket(user, ticketId);
        if (ticket == null) {
            System.out.println("Invalid Ticket number!");
            return -1;
        }

        if (Objects.equals(ticket.getStatus(), "expired")) {
            System.out.println("Ticket is expired!");
            return -1;
        }

        // calculate bill due for payment
        Billing billing = new Billing();
        billing.setTicketId(ticketId);
        billing.setUserId(user.getUserId());
        double fee = billing.calculateFee(ticket.getCheckInTime());

        // invalidate ticket
        ticket.setStatus("expired");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        ticket.setDateExpired(formattedDate);
        ticket.invalidateTicket();

        // Update the slot as unoccupied
        ParkingSlot slot = new ParkingSlot(ticket.getSlotNumber());
        //slot.setVehiclePlate(ticket.getPlateNumber());
        slot.setVehiclePlate(null); // de-allocate the slot from the vehicle
        slot.setIsOccupied(false);
        int rs = slot.updateSlotStatus();

        // return the bill amount due
        return fee;

    }
}