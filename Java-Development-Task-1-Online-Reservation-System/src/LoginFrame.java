
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;

    private final Color NAVY = new Color(15, 23, 42);
    private final Color TEAL = new Color(13, 148, 136);
    private final Color BG = new Color(241, 245, 249);

    public LoginFrame() {

        setTitle("RailConnect | Login");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new GridLayout(1, 2));
        main.setBackground(BG);

        // LEFT WELCOME PANEL
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(NAVY);
        welcomePanel.setLayout(new BoxLayout(
            welcomePanel, BoxLayout.Y_AXIS
        ));
        welcomePanel.setBorder(
            new EmptyBorder(75, 45, 50, 35)
        );

        JLabel logo = new JLabel("RAILCONNECT");
        logo.setFont(new Font("Arial", Font.BOLD, 25));
        logo.setForeground(Color.WHITE);

        JLabel icon = new JLabel("🚆");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 65));

        JLabel welcome = new JLabel(
            "<html>Travel smarter.<br>Travel better.</html>"
        );
        welcome.setFont(new Font("Arial", Font.BOLD, 30));
        welcome.setForeground(Color.WHITE);

        JLabel description = new JLabel(
            "<html>Your journey begins here.<br>" +
            "Book tickets, manage reservations<br>" +
            "and travel with ease.</html>"
        );
        description.setFont(new Font("Arial", Font.PLAIN, 15));
        description.setForeground(new Color(203, 213, 225));

        JLabel footer = new JLabel("YOUR JOURNEY, OUR PRIORITY");
        footer.setFont(new Font("Arial", Font.BOLD, 11));
        footer.setForeground(new Color(45, 212, 191));

        welcomePanel.add(logo);
        welcomePanel.add(Box.createVerticalStrut(65));
        welcomePanel.add(icon);
        welcomePanel.add(Box.createVerticalStrut(25));
        welcomePanel.add(welcome);
        welcomePanel.add(Box.createVerticalStrut(20));
        welcomePanel.add(description);
        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(footer);

        // RIGHT LOGIN PANEL
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BG);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(365, 410));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(30, 28, 30, 28)
        ));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Login to continue your journey."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = new JLabel("USERNAME");
        styleLabel(userLabel);

        usernameField = createField("Enter your username");

        JLabel passLabel = new JLabel("PASSWORD");
        styleLabel(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 42));
        passwordField.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42)
        );
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(203, 213, 225)
            ),
            new EmptyBorder(8, 10, 8, 10)
        ));

        showPassword = new JCheckBox("Show password");
        showPassword.setBackground(Color.WHITE);
        showPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        showPassword.setForeground(new Color(71, 85, 105));
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        char defaultEcho = passwordField.getEchoChar();

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEcho);
            }
        });

        JButton loginButton = createButton(
            "LOGIN TO YOUR ACCOUNT", TEAL
        );

        JButton clearButton = createButton(
            "CLEAR", new Color(71, 85, 105)
        );

        JLabel message = new JLabel(" ");
        message.setFont(new Font("Arial", Font.PLAIN, 12));
        message.setForeground(new Color(220, 38, 38));
        message.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accountInfo = new JLabel(
            "Secure railway reservation access"
        );
        accountInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        accountInfo.setForeground(new Color(148, 163, 184));
        accountInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));

        card.add(userLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(20));

        card.add(passLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(8));

        card.add(showPassword);
        card.add(Box.createVerticalStrut(15));

        card.add(loginButton);
        card.add(Box.createVerticalStrut(10));
        card.add(clearButton);
        card.add(Box.createVerticalStrut(12));

        card.add(message);
        card.add(Box.createVerticalGlue());
        card.add(accountInfo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(card, gbc);

        main.add(welcomePanel);
        main.add(loginPanel);

        setContentPane(main);

        // BUTTON ACTIONS
        loginButton.addActionListener(e -> login(message));

        passwordField.addActionListener(e -> login(message));

        usernameField.addActionListener(e -> passwordField.requestFocus());

        clearButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            message.setText(" ");
            showPassword.setSelected(false);
            passwordField.setEchoChar(defaultEcho);
            usernameField.requestFocus();
        });

        setVisible(true);
        usernameField.requestFocusInWindow();
    }

    private JTextField createField(String placeholder) {

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 42));
        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42)
        );
        field.setToolTipText(placeholder);

        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(203, 213, 225)
            ),
            new EmptyBorder(8, 10, 8, 10)
        ));

        return field;
    }

    private void styleLabel(JLabel label) {

        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(new Color(71, 85, 105));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(13, 15, 13, 15));
        button.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 45)
        );
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void login(JLabel message) {

        String username = usernameField.getText().trim();
        String password = new String(
            passwordField.getPassword()
        );

        if (username.isEmpty() || password.isEmpty()) {
            message.setForeground(new Color(220, 38, 38));
            message.setText("Please enter username and password.");
            return;
        }

        String sql =
            "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

    // Store the logged-in username
    Session.currentUser = username;

    JOptionPane.showMessageDialog(
        this,
        "Welcome to RailConnect, " + username + "!"
    );

    dispose();
    new ReservationFrame();

}
                 else {

                    message.setForeground(
                        new Color(220, 38, 38)
                    );
                    message.setText(
                        "Incorrect username or password."
                    );

                    passwordField.setText("");
                    passwordField.requestFocus();
                }
            }

        } catch (Exception e) {

            message.setForeground(new Color(220, 38, 38));
            message.setText("Unable to connect to database.");

            JOptionPane.showMessageDialog(
                this,
                "Login Error: " + e.getMessage()
            );
        }
    }
}

