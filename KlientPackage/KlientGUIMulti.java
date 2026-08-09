package KlientPackage;

import ServerPackage.Funkcje;
import ServerPackage.MessageObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KlientGUIMulti extends UnicastRemoteObject implements MessageObserver {
    private Funkcje manager;
    private JTextArea chatArea;
    private JTextField inputField;
    private final String username;
    private static int aktywniKlienci = 0;
    private DefaultListModel<String> userListModel = new DefaultListModel<>();
    private JList<String> userList;

    public KlientGUIMulti(String username) throws RemoteException {
        super();
        this.username=username;
        synchronized (KlientGUIMulti.class){
            aktywniKlienci++;
        }
        SwingUtilities.invokeLater(() -> {
            createUI();
            connectToServer();
        });
    }

    private void createUI() {
        JFrame frame = new JFrame("Klient RMI - " + username);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JToggleButton themeToggle = new JToggleButton("☀️");//jasny motyw
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(themeToggle);
        frame.add(topPanel, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBorder(BorderFactory.createTitledBorder("Aktywni użytkownicy"));
        userList.setPreferredSize(new Dimension(150, 0));
        frame.add(new JScrollPane(userList), BorderLayout.EAST);

        inputField = new JTextField();
        inputField.setEnabled(true);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Wpisz wiadomość: "), BorderLayout.NORTH);
        bottomPanel.add(inputField, BorderLayout.CENTER);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(bottomPanel, BorderLayout.CENTER);

        frame.add(bottomContainer, BorderLayout.SOUTH);

        inputField.addActionListener((ActionEvent _) -> {
            String message = inputField.getText();
            try {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedNow = now.format(formatter);
                manager.recieveMessage(message, username, formattedNow);
                inputField.setText("");
            } catch (RemoteException remoteException) {
                chatArea.append("Błąd wysyłania wiadomości\n");
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try{
                    if(manager != null){
                        manager.removeObserver(username);
                    }
                }catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void windowClosed(java.awt.event.WindowEvent e){
                try{
                    if(manager != null){
                        manager.removeObserver(username);
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                synchronized (KlientGUIMulti.class){
                    aktywniKlienci--;
                    if(aktywniKlienci == 0){
                        System.exit(0);
                    }
                }
            }
        });

        themeToggle.addActionListener(e -> {
            if (themeToggle.isSelected()) {
                themeToggle.setText("🌙");
                setDarkMode(frame);
            } else {
                themeToggle.setText("☀️");
                setLightMode(frame);
            }
        });

        setLightMode(frame);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> inputField.requestFocusInWindow());
    }

    private void setDarkMode(JFrame frame) {
        Color background = Color.BLACK;
        Color foreground = Color.WHITE;

        frame.getContentPane().setBackground(background);

        chatArea.setBackground(background);
        chatArea.setForeground(foreground);

        inputField.setBackground(background);
        inputField.setForeground(foreground);

        userList.setBackground(background);
        userList.setForeground(foreground);

        userList.setSelectionBackground(Color.DARK_GRAY);
        userList.setSelectionForeground(Color.WHITE);
    }

    private void setLightMode(JFrame frame) {
        Color background = Color.WHITE;
        Color foreground = Color.BLACK;

        frame.getContentPane().setBackground(background);

        chatArea.setBackground(background);
        chatArea.setForeground(foreground);

        inputField.setBackground(background);
        inputField.setForeground(foreground);

        userList.setBackground(background);
        userList.setForeground(foreground);

        userList.setSelectionBackground(Color.LIGHT_GRAY);
        userList.setSelectionForeground(Color.BLACK);
    }

    private void connectToServer() {
        try {
            manager = (Funkcje) Naming.lookup("rmi://localhost:5559/MessageManager");
            manager.addObserver(this, username);
            chatArea.append("Connected with server\n");
        } catch (Exception e) {
            chatArea.append("Failed connection with server.\n");
        }
    }

    @Override
    public void sendmessage(String message) {
        if (message.startsWith("ACTIVE_USERS:")) {
            String usersCSV = message.substring("ACTIVE_USERS:".length());
            String[] users = usersCSV.split(",");
            SwingUtilities.invokeLater(() -> {
                userListModel.clear();
                java.util.List<String> userList = java.util.Arrays.asList(users);
                userList.sort(String.CASE_INSENSITIVE_ORDER);
                for (String user : users) {
                    if (!user.isBlank()) userListModel.addElement(user);
                }
            });
        } else {
            SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog(null, "Ile klientów chcesz uruchomić?", "Liczba klientów", JOptionPane.QUESTION_MESSAGE);
            int liczbaKlientow = 1;
            try {
                if (input != null && !input.trim().isEmpty()) {
                    liczbaKlientow = Integer.parseInt(input.trim());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Niepoprawna liczba. Uruchamiam 1 klienta.", "Uwaga", JOptionPane.WARNING_MESSAGE);
            }

            for (int i = 0; i < liczbaKlientow; i++) {
                final int klientNr = i + 1;
                SwingUtilities.invokeLater(() -> {
                    String username = JOptionPane.showInputDialog(null, "Podaj nazwę użytkownika dla klienta " + klientNr + ":", "Nazwa użytkownika", JOptionPane.QUESTION_MESSAGE);
                    if (username == null || username.trim().isEmpty()) {
                        username = "Użytkownik " + klientNr;
                    }
                    try {
                        new KlientGUIMulti(username.trim());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Błąd uruchomienia klienta " + klientNr + ": " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });
    }

}
