import KlientPackage.KlientGUIMulti;
import ServerPackage.ServerGUI;

import javax.swing.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int ileKlientow = 1;

        System.out.println("Witaj w menu SimpleRMIChat. Ilu klientów GUI chcesz uruchomić?: ");
        try {
            ileKlientow = scanner.nextInt();
            scanner.nextLine();

            if (ileKlientow < 1) {
                System.out.println("Podano niepoprawną liczbę klientów. Uruchamiam 1 klienta.");
                ileKlientow = 1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Niepoprawne dane wejściowe. Uruchamiam 1 klienta.");
            ileKlientow = 1;
            scanner.nextLine();
        }

        new Thread(() -> new ServerGUI()).start();

        int finalIleKlientow = ileKlientow;
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < finalIleKlientow; i++) {
                int klientNr = i + 1;
                String username = JOptionPane.showInputDialog(null,
                        "Podaj nazwę użytkownika dla klienta " + klientNr + ":",
                        "Nazwa użytkownika",
                        JOptionPane.QUESTION_MESSAGE);

                if (username == null || username.trim().isEmpty()) {
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
