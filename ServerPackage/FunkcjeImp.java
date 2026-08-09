package ServerPackage;

import java.rmi.RemoteException;
import java.util.*;

public class FunkcjeImp implements Funkcje {
    private final Map<String, MessageObserver> activeClients = new HashMap<>();
    private final List<String> messageHistory = new ArrayList<>();

    @Override
    public synchronized void recieveMessage(String message, String id, String time) throws RemoteException {
        String newMessage = time + ":\t" + id + ":\t" + message;
        messageHistory.add(newMessage);
        notifyObservers(newMessage);
    }

    @Override
    public synchronized void addObserver(MessageObserver observer, String id) throws RemoteException {
        activeClients.put(id, observer);
        for(String msg : messageHistory){
            observer.sendmessage(msg);
        }
        broadcastActiveUsers();
    }

    @Override
    public synchronized void removeObserver(String id) throws RemoteException{
        activeClients.remove(id);
        broadcastActiveUsers();
    }

    public synchronized List<String> getActiveUsers() throws RemoteException{
        return new ArrayList<>(activeClients.keySet());
    }

    private void notifyObservers(String msg) throws RemoteException {
        List<Map.Entry<String, MessageObserver>> clientsCopy;

        synchronized(this){
            clientsCopy = new ArrayList<>(activeClients.entrySet());
        }

        List<String> toRemove = new ArrayList<>();

        for(Map.Entry<String, MessageObserver> entry : clientsCopy){
            try{
                entry.getValue().sendmessage(msg);
            }catch (RemoteException e){
                System.out.println("Usunięto klienta: " + entry.getKey());
                toRemove.add(entry.getKey());
            }
        }

        if(!toRemove.isEmpty()){
            synchronized(this) {
                for(String clientId : toRemove){
                    activeClients.remove(clientId);
                }
            }
            broadcastActiveUsers();
        }
    }

    private void broadcastActiveUsers(){
        String userList = "ACTIVE_USERS: " + String.join(",", activeClients.keySet());
        for(MessageObserver observer : activeClients.values()){
            try{
                observer.sendmessage(userList);
            }catch (RemoteException e){}
        }
    }
}
