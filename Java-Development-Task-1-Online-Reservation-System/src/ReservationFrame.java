
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class ReservationFrame extends JFrame {

    private JTextField passengerField, trainNumberField;
    private JTextField trainNameField, dateField;
    private JTextField sourceField, destinationField;

    private JComboBox<String> classBox;
    private JLabel routePreview;

    private final Color NAVY = new Color(15, 23, 42);
    private final Color TEAL = new Color(13, 148, 136);
    private final Color BG = new Color(241, 245, 249);
    private final Color TEXT = new Color(30, 41, 59);

    public ReservationFrame() {

        setTitle("RailConnect | Online Reservation System");
        setSize(1050, 720);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // LEFT SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(new EmptyBorder(30, 18, 25, 18));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("RAILCONNECT");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 21));

        JLabel tagline = new JLabel("Your journey, simplified");
        tagline.setForeground(new Color(148, 163, 184));
        tagline.setFont(new Font("Arial", Font.PLAIN, 11));

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(tagline);
        sidebar.add(Box.createVerticalStrut(55));

        JLabel menuLabel = new JLabel("MAIN MENU");
        menuLabel.setForeground(new Color(148, 163, 184));
        menuLabel.setFont(new Font("Arial", Font.BOLD, 11));
        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(15));

        JButton bookNav = navButton("  Book Ticket", true);
        JButton cancelNav = navButton("  Cancel Ticket", false);

        sidebar.add(bookNav);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(cancelNav);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = navButton("  Logout", false);
        sidebar.add(logoutButton);

        bookNav.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "You are on the booking page.")
        );

        cancelNav.addActionListener(e -> new CancellationFrame());

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        root.add(sidebar, BorderLayout.WEST);

        // MAIN CONTENT
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(30, 35, 30, 35));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel welcome = new JLabel("Welcome to RailConnect!");
        welcome.setFont(new Font("Arial", Font.BOLD, 26));
        welcome.setForeground(NAVY);

        JLabel subtitle = new JLabel(
            "Book your next journey with comfort and ease."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(welcome);
        heading.add(Box.createVerticalStrut(8));
        heading.add(subtitle);

        JLabel status = new JLabel("  ● SYSTEM ONLINE  ");
        status.setForeground(new Color(15, 118, 110));
        status.setOpaque(true);
        status.setBackground(new Color(204, 251, 241));
        status.setBorder(new EmptyBorder(9, 8, 9, 8));
        status.setFont(new Font("Arial", Font.BOLD, 11));

        header.add(heading, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);

        // BOOKING CARD
        JPanel card = new JPanel(new BorderLayout(15, 20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(25, 28, 25, 28)
        ));

        JLabel formTitle = new JLabel("Book Your Train Ticket");
        formTitle.setFont(new Font("Arial", Font.BOLD, 21));
        formTitle.setForeground(NAVY);

        JLabel formSubtitle = new JLabel(
            "Enter your journey details below to reserve your seat."
        );
        formSubtitle.setForeground(new Color(100, 116, 139));
        formSubtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel cardHeader = new JPanel();
        cardHeader.setOpaque(false);
        cardHeader.setLayout(new BoxLayout(cardHeader, BoxLayout.Y_AXIS));
        cardHeader.add(formTitle);
        cardHeader.add(Box.createVerticalStrut(7));
        cardHeader.add(formSubtitle);

        card.add(cardHeader, BorderLayout.NORTH);

        // FORM FIELDS
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        passengerField = createField("Enter passenger name");
        trainNumberField = createField("Example: 12001");

        trainNameField = createField("Train name appears here");
        trainNameField.setEditable(false);
        trainNameField.setBackground(new Color(248, 250, 252));

        dateField = createField("YYYY-MM-DD");
        sourceField = createField("Departure city");
        destinationField = createField("Arrival city");

        classBox = new JComboBox<>(new String[]{
            "Sleeper", "AC 3 Tier", "AC 2 Tier", "AC First Class"
        });

        styleCombo(classBox);

        JButton fetchButton = actionButton("SEARCH TRAIN", TEAL);

        JPanel trainPanel = new JPanel(new BorderLayout(8, 0));
        trainPanel.setOpaque(false);
        trainPanel.add(trainNumberField, BorderLayout.CENTER);
        trainPanel.add(fetchButton, BorderLayout.EAST);

        addFormField(form, "Passenger Name", passengerField, 0, 0);
        addFormField(form, "Train Number", trainPanel, 1, 0);
        addFormField(form, "Train Name", trainNameField, 0, 1);
        addFormField(form, "Travel Class", classBox, 1, 1);
        addFormField(form, "Journey Date", dateField, 0, 2);
        addFormField(form, "From Station", sourceField, 1, 2);
        addFormField(form, "To Station", destinationField, 0, 3);

        card.add(form, BorderLayout.CENTER);

        // ROUTE PREVIEW
        routePreview = new JLabel(
            "  Your route will appear here: From → To"
        );
        routePreview.setOpaque(true);
        routePreview.setBackground(new Color(240, 253, 250));
        routePreview.setForeground(new Color(15, 118, 110));
        routePreview.setBorder(new EmptyBorder(14, 12, 14, 12));
        routePreview.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel bottom = new JPanel(new BorderLayout(10, 15));
        bottom.setOpaque(false);

        bottom.add(routePreview, BorderLayout.NORTH);

        JButton bookButton = actionButton("CONFIRM BOOKING", TEAL);
        JButton clearButton = actionButton(
            "CLEAR FORM", new Color(71, 85, 105)
        );

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(clearButton);
        actions.add(bookButton);

        bottom.add(actions, BorderLayout.SOUTH);
        card.add(bottom, BorderLayout.SOUTH);

        content.add(card, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);

        // INTERACTIVE EVENTS
        fetchButton.addActionListener(e -> fetchTrain());
        bookButton.addActionListener(e -> bookTicket());
        clearButton.addActionListener(e -> clearFields());

        DocumentListener routeListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateRoute(); }
            public void removeUpdate(DocumentEvent e) { updateRoute(); }
            public void changedUpdate(DocumentEvent e) { updateRoute(); }
        };

        sourceField.getDocument().addDocumentListener(routeListener);
        destinationField.getDocument().addDocumentListener(routeListener);

        setContentPane(root);
        setVisible(true);
    }

    private JTextField createField(String placeholder) {

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(230, 42));
        field.setToolTipText(placeholder);

        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            new EmptyBorder(8, 10, 8, 10)
        ));

        return field;
    }

    private void styleCombo(JComboBox<String> combo) {

        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(230, 42));
    }

    private JButton actionButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(12, 17, 12, 17));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color original = color;

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(original.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(original);
            }
        });

        return button;
    }

    private JButton navButton(String text, boolean active) {

        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(190, 43));
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(active ? TEAL : NAVY);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 10, 12, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        return button;
    }

    private void addFormField(
        JPanel panel, String labelText,
        JComponent component, int column, int row
    ) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 9, 9, 9);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = column * 2;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(TEXT);

        gbc.gridwidth = 2;
        gbc.weightx = 1;

        JPanel fieldPanel = new JPanel(new BorderLayout(0, 8));
        fieldPanel.setOpaque(false);
        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(component, BorderLayout.CENTER);

        gbc.gridx = column;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Use one label + one input in each form column.
        JPanel wrapper = new JPanel(new BorderLayout(0, 7));
        wrapper.setOpaque(false);
        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(component, BorderLayout.CENTER);

        gbc.gridx = column * 2;
        gbc.gridwidth = 2;
        panel.add(wrapper, gbc);

        gbc.gridx = column * 2 + 2;
    }

    private void updateRoute() {

        String from = sourceField.getText().trim();
        String to = destinationField.getText().trim();

        if (from.isEmpty() && to.isEmpty()) {
            routePreview.setText(
                "  Your route will appear here: From → To"
            );
        } else {
            routePreview.setText(
                "  JOURNEY: " +
                (from.isEmpty() ? "From" : from) +
                "  →  " +
                (to.isEmpty() ? "To" : to)
            );
        }
    }

    private void fetchTrain() {

        String number = trainNumberField.getText().trim();

        if (!number.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid numeric train number.");
            return;
        }

        String sql = "SELECT train_name FROM trains WHERE train_number = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(number));

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    trainNameField.setText(rs.getString("train_name"));

                    JOptionPane.showMessageDialog(this,
                        "Train found successfully!",
                        "Train Search",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    trainNameField.setText("");

                    JOptionPane.showMessageDialog(this,
                        "No train found for this number.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Database Error: " + e.getMessage());
        }
    }

    private void bookTicket() {

        String passenger = passengerField.getText().trim();
        String trainNumber = trainNumberField.getText().trim();
        String trainName = trainNameField.getText().trim();
        String classType = (String) classBox.getSelectedItem();
        String journeyDate = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();

        if (passenger.isEmpty() || trainNumber.isEmpty()
                || trainName.isEmpty() || journeyDate.isEmpty()
                || source.isEmpty() || destination.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Please complete all booking details.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!trainNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                "Train number must contain only digits.");
            return;
        }

        if (source.equalsIgnoreCase(destination)) {
            JOptionPane.showMessageDialog(this,
                "Source and destination cannot be the same.");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(journeyDate);

            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                    "Journey date cannot be in the past.");
                return;
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Please use YYYY-MM-DD date format.");
            return;
        }

        String pnr = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();

    String sql = "INSERT INTO reservations " +
    "(pnr, passenger_name, train_number, train_name, " +
    "class_type, journey_date, source, destination, username, status) " +
    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
ps.setString(1, pnr);
ps.setString(2, passenger);
ps.setInt(3, Integer.parseInt(trainNumber));
ps.setString(4, trainName);
ps.setString(5, classType);
ps.setString(6, journeyDate);
ps.setString(7, source);
ps.setString(8, destination);
ps.setString(9, Session.currentUser);
ps.setString(10, "BOOKED");
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "YOUR TICKET IS CONFIRMED!\n\n" +
                "Passenger: " + passenger +
                "\nTrain: " + trainName +
                "\nJourney: " + source + " → " + destination +
                "\nDate: " + journeyDate +
                "\nClass: " + classType +
                "\n\nPNR: " + pnr,
                "Booking Successful",
                JOptionPane.INFORMATION_MESSAGE);

            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Booking Error: " + e.getMessage());
        }
    }

    private void clearFields() {

        passengerField.setText("");
        trainNumberField.setText("");
        trainNameField.setText("");
        dateField.setText("");
        sourceField.setText("");
        destinationField.setText("");
        classBox.setSelectedIndex(0);

        updateRoute();
    }
}
