package KlientPackage;

import ServerPackage.Funkcje;
import ServerPackage.MessageObserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Klient extends UnicastRemoteObject implements MessageObserver {
    private String clientId;

    public Klient(String clientId) throws RemoteException {
        super();
        this.clientId = clientId;
    }

    @Override
    public void sendmessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public void start() {
    try {
        Funkcje manager = (Funkcje) Naming.lookup("//localhost:5558/Serverrun");
        manager.addObserver(this, clientId);

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String message = scanner.nextLine();
            String timeString = LocalDateTime.now().toString();
            manager.recieveMessage(message, clientId, timeString);
        }
    }catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Podaj numer klienta: ");
            int numer = scanner.nextInt();
            scanner.nextLine();

            String clientId = "Klient " + numer;
            new Klient(clientId).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
