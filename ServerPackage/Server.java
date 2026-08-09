package ServerPackage;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            FunkcjeImp serverrun = new FunkcjeImp();
            Registry registry = LocateRegistry.createRegistry(5558);
            Funkcje stub =(Funkcje) UnicastRemoteObject.exportObject(serverrun, 0);
            registry.rebind("Serverrun", stub);
            System.out.println("Serwer dziala");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
