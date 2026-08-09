package ServerPackage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageObserver extends Remote {
    void sendmessage(String message) throws RemoteException;
}
