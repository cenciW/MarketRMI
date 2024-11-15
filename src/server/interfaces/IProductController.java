package server.interfaces;

import entitites.Product;
import entitites.User;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IProductController{
    Product insertProduct(Product product, User user) throws RemoteException;
    ArrayList<Product> getAllProducts() throws RemoteException;
    Product updateProduct(Product product) throws RemoteException;
    Product getProduct(String productName) throws RemoteException;
    ArrayList<Product> getProductByMarket(String marketName) throws RemoteException;
}
