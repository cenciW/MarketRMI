package contracts;

import entitites.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    //metodo que o cliente utiliza no servidor para escrever coisas no servidor
    void printOnServer(String clientName, String msgFromClient) throws RemoteException;

    //enviamos a interface do cliente para conseguir comunicar
    //cliente se liga o servidor, por isso ele tem que se registrar para conseguir comunicar
    //interface do cliente para o server para o server conseguir chamar os métodos do cliente
    boolean login(User user, ClientInterface clientInterface) throws RemoteException;

}
