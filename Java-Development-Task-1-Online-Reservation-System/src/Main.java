
public class Main {

    public static void main(String[] args) {

        System.out.println("Starting Online Reservation System...");

        Database.initializeDatabase();

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}

