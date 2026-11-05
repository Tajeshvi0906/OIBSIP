
import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Database {

    private static final String URL =
        "jdbc:sqlite:database/reservation.db";

    public static Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {

        try {
            Files.createDirectories(Path.of("database"));

            try (Connection con = getConnection();
                 Statement stmt = con.createStatement()) {

                // USERS TABLE
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT NOT NULL)"
                );

                // TRAINS TABLE
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS trains (" +
                    "train_number INTEGER PRIMARY KEY, " +
                    "train_name TEXT NOT NULL)"
                );

                // RESERVATIONS TABLE
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS reservations (" +
                    "pnr TEXT PRIMARY KEY, " +
                    "passenger_name TEXT NOT NULL, " +
                    "train_number INTEGER NOT NULL, " +
                    "train_name TEXT NOT NULL, " +
                    "class_type TEXT NOT NULL, " +
                    "journey_date TEXT NOT NULL, " +
                    "source TEXT NOT NULL, " +
                    "destination TEXT NOT NULL, " +
                    "username TEXT NOT NULL DEFAULT 'admin', " +
                    "status TEXT NOT NULL DEFAULT 'BOOKED')"
                );

                // MIGRATE EXISTING DATABASE
                addColumnIfMissing(
                    con, "reservations",
                    "username",
                    "TEXT NOT NULL DEFAULT 'admin'"
                );

                addColumnIfMissing(
                    con, "reservations",
                    "status",
                    "TEXT NOT NULL DEFAULT 'BOOKED'"
                );

                // DEFAULT ADMIN USER
                try (PreparedStatement user =
                    con.prepareStatement(
                        "INSERT OR IGNORE INTO users VALUES (?, ?)")) {

                    user.setString(1, "admin");
                    user.setString(2, "admin123");
                    user.executeUpdate();
                }

                // SAMPLE TRAINS
                try (PreparedStatement train =
                    con.prepareStatement(
                        "INSERT OR IGNORE INTO trains VALUES (?, ?)")) {

                    train.setInt(1, 12001);
                    train.setString(2, "Rajdhani Express");
                    train.executeUpdate();

                    train.setInt(1, 12951);
                    train.setString(2, "Mumbai Rajdhani Express");
                    train.executeUpdate();
                }

                System.out.println(
                    "Database connected successfully!"
                );
                System.out.println(
                    "Database tables initialized successfully!"
                );
            }

        } catch (Exception e) {
            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }

    private static void addColumnIfMissing(
        Connection con,
        String table,
        String column,
        String definition
    ) throws SQLException {

        boolean exists = false;

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "PRAGMA table_info(" + table + ")")) {

            while (rs.next()) {
                if (column.equalsIgnoreCase(
                    rs.getString("name"))) {
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(
                    "ALTER TABLE " + table +
                    " ADD COLUMN " + column + " " + definition
                );
            }

            System.out.println(
                "Added database column: " + column
            );
        }
    }
}

