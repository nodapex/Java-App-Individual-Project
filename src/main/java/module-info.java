module com.bptn.car_management_system {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires jbcrypt;


    opens com.bptn.car_management_system to javafx.fxml;
    exports com.bptn.car_management_system;
    exports com.bptn.car_management_system.controllers;
    opens com.bptn.car_management_system.controllers to javafx.fxml;
    exports com.bptn.car_management_system.archive;
    opens com.bptn.car_management_system.archive to javafx.fxml;
}