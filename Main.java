import KlientPackage.KlientGUIMulti;
import ServerPackage.ServerGUI;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Witaj w menu SimpleRMIChat. Ilu klientówGUI chcesz uruchomić?: ");
        int ileKlientow = scanner.nextInt();
        scanner.nextLine();

        new Thread(() -> new ServerGUI()).start();

        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < ileKlientow; i++) {
                int klientNr = i + 1;
                String username = JOptionPane.showInputDialog(null,
                        "Podaj nazwę użytkownika dla klienta " + klientNr + ":",
                        "Nazwa użytkownika",
                        JOptionPane.QUESTION_MESSAGE);
                if(username == null || username.trim().isEmpty()){
                    username = "GUI Klient " + klientNr;
                }

                final String finalUsername = username.trim();
                SwingUtilities.invokeLater(() -> {
                    try {
                        new KlientGUIMulti(finalUsername);
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Błąd uruchomienia klienta " + klientNr + ": " + e.getMessage(),
                                "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });
    }
}
