
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CancellationFrame extends JFrame {

    private JTextField pnrField;
    private JTextArea detailsArea;
    private JButton cancelButton;

    public CancellationFrame() {

        setTitle("Online Reservation System - Cancellation");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel heading = new JLabel(
            "TICKET CANCELLATION",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(heading, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        pnrField = new JTextField();

        JButton fetchButton = new JButton("FETCH DETAILS");

        searchPanel.add(new JLabel("Enter PNR Number:"), BorderLayout.WEST);
        searchPanel.add(pnrField, BorderLayout.CENTER);
        searchPanel.add(fetchButton, BorderLayout.EAST);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(detailsArea);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 15));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        cancelButton = new JButton("CANCEL TICKET");
        JButton closeButton = new JButton("CLOSE");

        cancelButton.setEnabled(false);

        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        fetchButton.addActionListener(e -> fetchBooking());

        cancelButton.addActionListener(e -> cancelBooking());

        closeButton.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }

    private void fetchBooking() {

        String pnr = pnrField.getText().trim().toUpperCase();

        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a PNR number.");
            return;
        }

        String sql = "SELECT * FROM reservations WHERE pnr = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pnr);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    detailsArea.setText(
                        "BOOKING DETAILS\n\n" +
                        "PNR Number: " + rs.getString("pnr") + "\n" +
                        "Passenger Name: " + rs.getString("passenger_name") + "\n" +
                        "Train Number: " + rs.getInt("train_number") + "\n" +
                        "Train Name: " + rs.getString("train_name") + "\n" +
                        "Class Type: " + rs.getString("class_type") + "\n" +
                        "Journey Date: " + rs.getString("journey_date") + "\n" +
                        "Source: " + rs.getString("source") + "\n" +
                        "Destination: " + rs.getString("destination")
                    );

                    cancelButton.setEnabled(true);

                } else {

                    detailsArea.setText("No booking found for this PNR.");
                    cancelButton.setEnabled(false);

                    JOptionPane.showMessageDialog(this,
                        "Invalid PNR number!");
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    private void cancelBooking() {

        String pnr = pnrField.getText().trim().toUpperCase();

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel this ticket?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM reservations WHERE pnr = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pnr);

            int result = ps.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(this,
                    "Ticket cancelled successfully!");

                detailsArea.setText("This reservation has been cancelled.");
                pnrField.setText("");
                cancelButton.setEnabled(false);

            } else {

                JOptionPane.showMessageDialog(this,
                    "Booking not found!");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                "Cancellation Error: " + e.getMessage());
        }
    }
}
