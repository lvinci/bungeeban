package com.github.lucavinci.bungeeban.util;

import java.sql.*;

/**
 * Represents an sql connection in {@link com.github.lucavinci.bungeeban.BungeeBan}
 */
public class SQLConnection {

    private final String hostname;
    private final int port;
    private final String username;
    private final String password;
    private final String databaseName;
    private final Type type;
    private final String databaseFile;

    // Keeps the connection instance, null if connection is not active
    private Connection connection;

    /**
     * Create a new {@link SQLConnection} instance for SQLite
     *
     * @param databaseFile file path to the sqlite database file
     */
    public SQLConnection(final String databaseFile) {
        this.type = Type.SQLITE;
        this.hostname = "";
        this.port = 0;
        this.username = "";
        this.password = "";
        this.databaseName = "";
        this.databaseFile = databaseFile;
    }

    /**
     * Create a new {@link SQLConnection} instance for MySQL.
     *
     * @param hostname     hostname of the mysql server
     * @param port         port that the mysql server is running on
     * @param username     username of the user that is used to access the mysql server
     * @param password     password of the user
     * @param databaseName name of the database
     */
    public SQLConnection(final String hostname, final int port, final String username, final String password,
                         final String databaseName) {
        this.type = Type.MYSQL;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
        this.databaseFile = "";
    }

    /**
     * Check whether the connection is currently open or not.
     *
     * @return true if the connection is open, false otherwise
     */
    public boolean isConnected() {
        return this.connection != null;
    }

    /**
     * Attempts to open the connection in case it is not already open.
     * You can check whether this operation was successful using the isConnected method.
     */
    public void openConnection() {
        // Skip opening the connection if it is already open
        if (this.isConnected()) return;
        try {
            // Establish SQL connection using jdbc
            this.connection = DriverManager.getConnection(this.buildConnectionString());
        } catch (SQLException e) {
            // Log the sql error that occurred in the console
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection in case it is currently open.
     */
    public void closeConnection() {
        // Skip closing the connection if it is not open
        if (!this.isConnected()) return;
        // Attempt to close the sql connection and print errors
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes the given sql query and returns the resulting rows of the query.
     *
     * @param query      sql query string
     * @param parameters prepared statement parameters
     * @return result of the query or null if an exception occurred
     */
    public ResultSet query(final String query, final String... parameters) {
        try {
            // Prepare an sql statement with the given sql query string
            final PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            // Set the given parameters in the prepared statement
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setString(i, parameters[i]);
            }
            // Execute the sql query and return the results
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return null because an exception occurred and thus no results are available
        return null;
    }

    /**
     * Executes the given sql update as a prepared statement and returns the amount of rows that were updated.
     *
     * @param query      sql query string
     * @param parameters prepared statement parameters
     * @return amount of rows that were affected by this update
     */
    public int update(final String query, final String... parameters) {
        try {
            // Prepare an sql statement with the given sql query string
            final PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            // Set the given parameters in the prepared statement
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setString(i, parameters[i]);
            }
            // Execute the update and return how many rows were affected
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return 0 because an error occurred and thus 0 rows were affected
        return 0;
    }

    /**
     * Builds the jdbc connection string that is used to connect to the database.
     * For MySQL the given credentials are used, SQLite uses the given filename.
     *
     * @return built jdbc connection string
     */
    private String buildConnectionString() {
        // If the type of this connection is sqlite, create sqlite connection string
        if (this.type == Type.SQLITE) {
            return "jdbc:sqlite:" + this.databaseFile;
        }
        // Create connection string for mysql
        return "jdbc:mysql://" + this.hostname + ":" + this.port + "/" +
                this.databaseName + "?user=" + this.username + "&password=" + this.password;
    }

    /**
     * Represents the database type of this connection
     */
    public enum Type {
        MYSQL, SQLITE
    }

}
