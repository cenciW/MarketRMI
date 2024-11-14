package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void printOnClient(String messageToClients) throws RemoteException;
}
