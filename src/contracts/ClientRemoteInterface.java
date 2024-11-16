package contracts;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemoteInterface extends Remote {
    void printOnClient(String messageToClients) throws RemoteException;
}
