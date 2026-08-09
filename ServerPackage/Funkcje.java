package ServerPackage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Funkcje extends Remote {
    void recieveMessage(String messege, String id, String time) throws RemoteException;
    void addObserver(MessageObserver observer, String id) throws RemoteException;
    void removeObserver(String id) throws RemoteException;
    List<String> getActiveUsers() throws RemoteException;
}
