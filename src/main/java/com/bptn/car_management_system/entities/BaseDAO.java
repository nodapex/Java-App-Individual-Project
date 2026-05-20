package com.bptn.car_management_system.entities;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {
    // method to get the path to the SQLite database in the resources folder
    private static String getDatabasePath() {
        // Get the resource URL for the SQLite database file from resources
        URL resourceUrl = DbUtilityDAO.class.getClassLoader().getResource("database.db");

        URL classLocation = DbUtilityDAO.class.getProtectionDomain().getCodeSource().getLocation();
        File targetClasses = new File(classLocation.getPath());
        //File targetFolder = targetClasses.getParentFile();

        return targetClasses.getPath() + "\\database.db";
    }

    // test db
    private static String getTestDatabasePath() {
        // Get the resource URL for the test SQLite database file from resources
        URL resourceUrl = DbUtilityDAO.class.getClassLoader().getResource("test_database.db");
        String path;

        if (resourceUrl == null) {
            //return null;
            throw new IllegalArgumentException("Database file not found in resources");
        }

        // Convert URL to URI and get the absolute file path
        URI dbUri = null;
        try {
            dbUri = resourceUrl.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return new File(dbUri).getAbsolutePath();
    }

    // establish a connection to the SQLite database
    public static Connection getConnection() {
        Connection connection = null;
        try {
            String dbUrl;
            String dbPath = getDatabasePath(); // Get the path from the resources folder

            dbUrl = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(dbUrl);

            //connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // test connection
    public static Connection getTestConnection() {
        Connection connection = null;
        try {
            String dbUrl;
            String dbPath = getTestDatabasePath(); // Get the path from the resources folder

            //test_database
            dbUrl = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(dbUrl);

            //connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

//    // Base data access class to provide db connection to other utility classes and entities
//    protected Connection getConnection(){
//        return com.bptn.car_management_system.entities.DbUtilityDAO.getConnection();
//    }
//
//    protected Connection getTestConnection(){
//        return com.bptn.car_management_system.entities.DbUtilityDAO.getTestConnection();
//    }




}
