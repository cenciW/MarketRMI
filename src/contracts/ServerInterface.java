package contracts;

import entitites.Product;
import entitites.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
    //metodo que o cliente utiliza no servidor para escrever coisas no servidor
    void printOnServer(String clientName, String msgFromClient) throws RemoteException;

    //enviamos a interface do cliente para conseguir comunicar
    //cliente se liga o servidor, por isso ele tem que se registrar para conseguir comunicar
    //interface do cliente para o server para o server conseguir chamar os métodos do cliente
    boolean login(User user) throws RemoteException;

    void addProduct(Product product, User user) throws RemoteException;
    ArrayList<Product> getAllProducts() throws RemoteException;
    ArrayList<Product> getProductByMarketName(String marketName) throws RemoteException;
    Product updateProduct(Product product, User user) throws RemoteException;
}
