package contracts;

import entitites.Product;
import entitites.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRemoteInterface extends Remote {
    //enviamos a interface do cliente para conseguir comunicar
    //cliente se liga o servidor, por isso ele tem que se registrar para conseguir comunicar
    //interface do cliente para o server para o server conseguir chamar os métodos do cliente

    boolean login(User user) throws RemoteException;
    void addProduct(Product product, User user) throws RemoteException;
    ArrayList<Product> getAllProducts(User user) throws RemoteException;
    ArrayList<Product> getProductByMarketName(User user, String marketName) throws RemoteException;
    ArrayList<Product> getProductByName(String productName) throws RemoteException;
    void updateProduct(Product product, User user) throws RemoteException;
}
