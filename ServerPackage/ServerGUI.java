package ServerPackage;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerGUI {
    private final JTextArea logArea;

    public ServerGUI() {
        JFrame frame = new JFrame("Serwer RMI");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);

        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        frame.add(new JScrollPane(logArea), BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startServer();
    }

    private void startServer() {
        try {
            FunkcjeImp serverrun = new FunkcjeImp() {
                @Override
                public void recieveMessage(String msg, String userId, String godzina) throws RemoteException {
                    super.recieveMessage(msg, userId, godzina);
                    String logEntry = userId + ": " + msg;
                    logArea.append(userId + ": " + msg + "\n");
                    LogWriter.logMessage(userId, msg, godzina);
                }
            };

            Registry registry = LocateRegistry.createRegistry(5559);
            Funkcje stub = (Funkcje) UnicastRemoteObject.exportObject(serverrun, 0);
            registry.rebind("MessageManager", stub);
            logArea.append("Server runs...\n");
        } catch (Exception e) {
            logArea.append("Server error: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerGUI());
    }
}
