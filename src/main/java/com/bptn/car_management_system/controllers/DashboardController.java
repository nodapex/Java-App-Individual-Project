package com.bptn.car_management_system.controllers;

import com.bptn.car_management_system.MainApplication;
import com.bptn.car_management_system.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;

//import com.gluonhq.charm.glisten.control.Icon;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


import java.io.IOException;
import java.util.List;

import static com.bptn.car_management_system.controllers.LoginController.actionConfirmed;
import static com.bptn.car_management_system.controllers.LoginController.showConfirmDialog;

public class DashboardController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Pane pnl_ViewParkingLots,
            pnl_BookSlots,
            pnl_CheckoutVehicle,
            pnl_TransHistory,
            pnl_UpdateProfile,
            pnl_ViewTickets,
            pnl_ViewBills,
            pnl_About,
            pnl_ViewAdmin,
            pnl_Logout;

    @FXML
    Button mni_ViewParkingLots,
            mni_BookSlots,
            mni_CheckoutVehicle,
            mni_TransHistory,
            mni_UpdateProfile,
            mni_ViewTickets,
            mni_ViewBills,
            mni_About,
            mni_Logout,
            mni_ViewAdmin;

    // Book Slot
    @FXML
    private Label lblBookingTicketNumber;

    @FXML
    private TextField txtBookingVehiclePlate, txtBookingVehicleType, txtSlotNumberToBook;

    @FXML
    private Button btnBookSlot;

    // Checkout Slot
    @FXML
    private Label lblTicketNumberCheckoutMessage;

    @FXML
    private TextField txtTicketNumberCheckout;

    @FXML
    private Button btnTicketNumberCheckout;

    // Display Ticket Data //
    @FXML
    private TableView<Ticket> tblViewTickets;

    @FXML
    private TableColumn<Ticket, String>
            ticketPlateNumberColumn,
            ticketStatusColumn,
            ticketDateCreatedColumn,
            ticketDateExpiredColumn;

    @FXML
    private TableColumn<Ticket, Integer>
            ticketNumberColumn,
            ticketSlotNumberColumn;

    // Display Bills //
    @FXML
    private TableView<Billing> tblViewBills;

    @FXML
    private Button btnLoadBills;

    @FXML
    private Label lblTotalRevenue, lblTotalRevenueLabel;

    @FXML
    private TableColumn<Billing, String>
            billDateCreatedColumn;
    @FXML
    private TableColumn<Billing, Integer>
            billTicketIdColumn;

    @FXML
    private TableColumn<Billing, Long>
            billTimeColumn;
    @FXML
    private TableColumn<Billing, Double>
            billAmountColumn;

    // Admin Screen
    @FXML
    private Button btnSetBillRate;

    @FXML
    private Label lblAdminMessage, lblWelcome;

    @FXML
    private TextField txtBillRate;

    @FXML
    private TableView<SlotDTO> tblViewParkingSlotsDTO;

    @FXML
    private TableColumn<SlotDTO, String>
            usernameDtoColumn,
            firstnameColumn,
            lastnameColumn,
            vehiclePlateNoColumn,
            slotNoColumn,
            parkingStatusColumn;

    private User user;


    public void handleMenuActionClick(ActionEvent event) {
        if (event.getSource() == mni_ViewParkingLots) {
            pnl_ViewParkingLots.toFront();

        } else if (event.getSource() == mni_BookSlots) {
            pnl_BookSlots.toFront();

        } else if (event.getSource() == mni_CheckoutVehicle) {
            pnl_CheckoutVehicle.toFront();

        } else if (event.getSource() == mni_TransHistory) {
            pnl_TransHistory.toFront();

        } else if (event.getSource() == mni_UpdateProfile) {
            pnl_UpdateProfile.toFront();

        } else if (event.getSource() == mni_ViewTickets) {
            pnl_ViewTickets.toFront();

        } else if (event.getSource() == mni_ViewBills) {
            pnl_ViewBills.toFront();

        } else if (event.getSource() == mni_About) {
            pnl_About.toFront();

        } else if (event.getSource() == mni_Logout) {
            switchToLoginScreen(event);

        } else if (event.getSource() == mni_ViewAdmin) {
            // check if the user has admin privilege
            setAdminAccessControl();
        } else {
            // do nothing
        }
    }

    // used to navigate to login screen
    public void switchToLoginScreen(ActionEvent event) {
        try {
            // Go back to login screen
            showConfirmDialog("logout");
            if (actionConfirmed) {
                root = FXMLLoader.load(MainApplication.class.getResource("LoginScreen.fxml"));
            } else {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaxWidth(520);
        stage.setMaxHeight(400);
        stage.centerOnScreen();

        stage.show();
        stage.setMaxWidth(520);
        stage.setMaxHeight(400);
    }

    // Load Parking Slots
    @FXML
    private TableView<ParkingSlot> tblViewParkingSlots;

    @FXML
    private TableColumn<ParkingSlot, Integer> slotNumberColumn;

    @FXML
    private TableColumn<ParkingSlot, String> isOccupiedColumn;

    @FXML
    private TableColumn<ParkingSlot, String> vehiclePlateColumn;

    public void initialize() {

        // initialize menu icons
        initMenuIcons();

        // Initialize ParkingSlots view table columns to map the properties
        slotNumberColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSlotNumber()).asObject());
        isOccupiedColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIsAvailable()));
        vehiclePlateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getVehiclePlate()));

        // Load ParkingSlot data from the SQLite database into the TableView
        DbUtilityDAO.loadParkingSlot(tblViewParkingSlots);

        // Display ticket info - Initialize columns to map the properties
        ticketNumberColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTicketId()).asObject());
        ticketSlotNumberColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSlotNumber()).asObject());

        ticketPlateNumberColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPlateNumber()));
        ticketStatusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        ticketDateCreatedColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateCreated()));
        ticketDateExpiredColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateExpired()));

        // Display bills info - Initialize columns to map the properties
        billTicketIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTicketId()).asObject());
        ticketSlotNumberColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSlotNumber()).asObject());
        billAmountColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getAmount()).asObject());
        billTimeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getBillingTime()).asObject());
        billDateCreatedColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateCreated()));

        // Call the method to setup double-click event handler for ParkingSlot TableView
        setParkingSlotTableViewEventHandler();

        // Call the method to setup double-click event handler for Tickets TableView
        setViewTicketsTableEventHandler();

        // Initialize Admin Screen view table columns to map the properties
        usernameDtoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        firstnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFirstname()));
        lastnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLastname()));
        vehiclePlateNoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getVehiclePlate()));
        slotNoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSlotNumber()));
        parkingStatusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getParkingStatus()));

        // Load Admin screen TableView data from the SQLite database
        displayAdminScreenUserDtoAction();
    }

    private void initMenuIcons() {

        // Logout
        ImageView iconLogout = new ImageView(new Image(getClass().getResourceAsStream("/images/iconLogout.png")));
        iconLogout.setFitWidth(20);
        iconLogout.setFitHeight(20);
        iconLogout.setPreserveRatio(true);

        mni_Logout.setGraphic(iconLogout);
        mni_Logout.setContentDisplay(ContentDisplay.LEFT);
        mni_Logout.setGraphicTextGap(5);

        // About
        ImageView iconAbout = new ImageView(new Image(getClass().getResourceAsStream("/images/iconAbout.png")));
        iconAbout.setFitWidth(20);
        iconAbout.setFitHeight(20);
        iconAbout.setPreserveRatio(true);

        mni_About.setGraphic(iconAbout);
        mni_About.setContentDisplay(ContentDisplay.LEFT);
        mni_About.setGraphicTextGap(5);

        // View Parking Sots
        ImageView iconViewSlots = new ImageView(new Image(getClass().getResourceAsStream("/images/iconViewSlots.png")));
        iconViewSlots.setFitWidth(32);
        iconViewSlots.setFitHeight(32);
        iconViewSlots.setPreserveRatio(true);

        mni_ViewParkingLots.setGraphic(iconViewSlots);
        mni_ViewParkingLots.setContentDisplay(ContentDisplay.LEFT);
        mni_ViewParkingLots.setGraphicTextGap(5);

        // Book Slots
        ImageView iconBookSlots = new ImageView(new Image(getClass().getResourceAsStream("/images/iconBookSlot.png")));
        //ImageView iconBookSlots = new ImageView(new Image(getClass().getResourceAsStream("/images/iconBookSlot2.png")));
        iconBookSlots.setFitWidth(24);
        iconBookSlots.setFitHeight(24);
        iconBookSlots.setPreserveRatio(true);

        mni_BookSlots.setGraphic(iconBookSlots);
        mni_BookSlots.setContentDisplay(ContentDisplay.LEFT);
        mni_BookSlots.setGraphicTextGap(5);

        // Checkout Vehicle
        ImageView iconCheckout = new ImageView(new Image(getClass().getResourceAsStream("/images/iconCheckout.png")));
        iconCheckout.setFitWidth(24);
        iconCheckout.setFitHeight(24);
        iconCheckout.setPreserveRatio(true);

        mni_CheckoutVehicle.setGraphic(iconCheckout);
        mni_CheckoutVehicle.setContentDisplay(ContentDisplay.LEFT);
        mni_CheckoutVehicle.setGraphicTextGap(5);

        // Tickets
        ImageView iconTickets = new ImageView(new Image(getClass().getResourceAsStream("/images/iconTickets.png")));
        iconTickets.setFitWidth(32);
        iconTickets.setFitHeight(32);
        iconTickets.setPreserveRatio(true);

        mni_ViewTickets.setGraphic(iconTickets);
        mni_ViewTickets.setContentDisplay(ContentDisplay.LEFT);
        mni_ViewTickets.setGraphicTextGap(5);

        // Bills
        ImageView iconBills = new ImageView(new Image(getClass().getResourceAsStream("/images/iconBills.png")));
        iconBills.setFitWidth(24);
        iconBills.setFitHeight(24);
        iconBills.setPreserveRatio(true);

        mni_ViewBills.setGraphic(iconBills);
        mni_ViewBills.setContentDisplay(ContentDisplay.LEFT);
        mni_ViewBills.setGraphicTextGap(3);

        // Admin
        ImageView iconAdmin = new ImageView(new Image(getClass().getResourceAsStream("/images/iconAdmin.png")));
        iconAdmin.setFitWidth(20);
        iconAdmin.setFitHeight(20);
        iconAdmin.setPreserveRatio(true);

        mni_ViewAdmin.setGraphic(iconAdmin);
        mni_ViewAdmin.setContentDisplay(ContentDisplay.LEFT);
        mni_ViewAdmin.setGraphicTextGap(3);

    }

    // Setup double-click event handler for ParkingSlot TableView
    public void setParkingSlotTableViewEventHandler() {
        // Set RowFactory for double-click
        tblViewParkingSlots.setRowFactory(evt -> {
            TableRow<ParkingSlot> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ParkingSlot clickedRow = row.getItem();

                    // for deugging purposes only..
                    System.out.println("\n\nDouble-clicked on \nSlot number: " + clickedRow.getSlotNumber() +
                                       " \nAvailability: " + clickedRow.getIsAvailable() +
                                       " \nVehicle License Plate: " + (clickedRow.getVehiclePlate() == null ? "N/A" : clickedRow.getVehiclePlate())
                    );

                    // pass the selected row values to the slot booking form and show the form screen...
                    txtSlotNumberToBook.setText(String.valueOf(clickedRow.getSlotNumber()));

                    txtBookingVehicleType.requestFocus();
                    pnl_BookSlots.toFront();
                }
            });
            return row;
        });
    }

    // Setup double-click event handler for Ticket TableView
    public void setViewTicketsTableEventHandler() {
        // Set RowFactory for double-click
        tblViewTickets.setRowFactory(evt -> {
            TableRow<Ticket> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Ticket clickedRow = row.getItem();

                    // for debugging purposes only...
                    System.out.println("\n\nDouble-clicked on Ticket number: " + clickedRow.getTicketId());

                    // pass the selected row values to the vehicle checkout form and show the screen...
                    txtTicketNumberCheckout.setText(String.valueOf(clickedRow.getTicketId()));
                    txtTicketNumberCheckout.requestFocus();
                    pnl_CheckoutVehicle.toFront();
                }
            });
            return row;
        });

    }

    // set the info of the successfully logged-in user, passed from login view
    public void setLoginInfo(User user) {

        // LSP is applied here because the user object could be either of User or Admin type depending on the role of the user that has logged in
        this.user = user;

        // Set user's default vehicle plate number
        if (user != null) {
            txtBookingVehiclePlate.setText(user.getDefaultVehicle());
        }

        // check if the user has admin privilege and apply appropriate settings
        if (user != null) {
            lblWelcome.setText("Welcome, " + user.getFirstname() + "!");
            setAdminAccessControl();
        }
    }


    // method to handling actual booking of available slot
    public void bookSlotAction(ActionEvent event) {

        // validate user entries
        if (txtBookingVehiclePlate.getText() == null) {
            lblBookingTicketNumber.setText("Please enter your vehicle plate number");
            txtBookingVehiclePlate.requestFocus();
            return;
        }
        if (txtBookingVehiclePlate.getText().isEmpty()) {
            lblBookingTicketNumber.setText("Please enter your vehicle plate number");
            txtBookingVehiclePlate.requestFocus();
            return;
        }

        if (txtBookingVehicleType.getText().isEmpty()) {
            lblBookingTicketNumber.setText("Please enter your vehicle type/model");
            txtBookingVehicleType.requestFocus();
            return;
        }

        if (txtSlotNumberToBook.getText().isEmpty()) {
            lblBookingTicketNumber.setText("Please enter the slot number you want to book");
            txtSlotNumberToBook.requestFocus();
            return;
        }

        // generate ticket number for the booked slot and notify the user
        ParkingLot parkingLot = new ParkingLot();
        int ticketNumber = parkingLot.bookSlot(user,
                txtSlotNumberToBook.getText(),
                txtBookingVehicleType.getText(),
                txtBookingVehiclePlate.getText());

        if (ticketNumber == 0) {
            // slot is not available for booking
            lblBookingTicketNumber.setText("Slot " + txtSlotNumberToBook.getText() + " not available for booking! \nPlease pick another slot.");

        } else {
            lblBookingTicketNumber.setText("Your booking for slot " + txtSlotNumberToBook.getText() + " is confirmed! \nYour ticket number is: " + ticketNumber);

            // Update the parking slots view to reflect current availability by reloading the data from the database
            DbUtilityDAO.loadParkingSlot(tblViewParkingSlots);

        }
    }

    // checkout a parked vehicle
    public void checkoutSlotAction(ActionEvent event) {

        // validate user entries
        if (txtTicketNumberCheckout.getText().isEmpty()) {
            lblTicketNumberCheckoutMessage.setText("Please enter the ticket number to checkout");
            txtTicketNumberCheckout.requestFocus();
            return;
        }

        // check out the user
        ParkingLot parkingLot = new ParkingLot();
        double fee = parkingLot.checkOutVehicle(user, Integer.parseInt(txtTicketNumberCheckout.getText()));

        if (fee < 0) {
            lblTicketNumberCheckoutMessage.setText("Invalid ticket number! Please enter a valid ticket number.");
            return;
        }

        lblTicketNumberCheckoutMessage.setText("Your fee is $" + fee + " Thank you for your patronage!");

        // Refresh the Tickets view table in the background
        displayTicketInfo(event);

        // Also refresh the parking slots view table in the background
        DbUtilityDAO.loadParkingSlot(tblViewParkingSlots);
    }

    // Display Ticket Data
    public void displayTicketInfo(ActionEvent event) {
        // Display Ticket Info
        DbUtilityDAO.loadTickets(user, tblViewTickets);
    }

    // displaying bills data
    public void displayBillsAction(ActionEvent event) {
        DbUtilityDAO.loadBills(user, tblViewBills);

        // calculate total revenue generated up till date
        double totalAmount = DbUtilityDAO.getTotalRevenue();
        lblTotalRevenue.setText("CAD$" + String.valueOf(totalAmount));
    }

    public void setBillRateAction(ActionEvent event) {
        if (txtBillRate.getText().isEmpty()) {
            lblAdminMessage.setText("Please enter a billing rate!");
            txtBillRate.requestFocus();
            return;
        }

        //set the new rate
        boolean bResult = DbUtilityDAO.setBillRate(Double.parseDouble(txtBillRate.getText()));
        if (bResult) {
            lblAdminMessage.setText("Billing rate has been updated successfully!");
        } else {
            lblAdminMessage.setText("Unable to update billing rate! Please, try again later.");
        }
    }

    public void displayAdminScreenUserDtoAction() {

        List<SlotDTO> slotDTOList = DbUtilityDAO.getAllSlotsDTO();


        if (!slotDTOList.isEmpty()) {
            // alternative, using stream and lambda:
            slotDTOList.stream().forEach(c -> tblViewParkingSlotsDTO.getItems().add(c));

        }
    }

    private void setAdminAccessControl() {
        // check if the user has admin privilege
        if (user.getRole().equals("admin")) {
            lblTotalRevenue.setVisible(true);
            lblTotalRevenueLabel.setVisible(true);
            mni_ViewAdmin.setVisible(true);
            pnl_ViewAdmin.setVisible(true);
            pnl_ViewAdmin.toFront();
        } else {
            // user has no admin privilege
            lblTotalRevenue.setVisible(false);
            lblTotalRevenueLabel.setVisible(false);
            mni_ViewAdmin.setVisible(false);
            pnl_ViewAdmin.setVisible(false);
        }
    }

}
