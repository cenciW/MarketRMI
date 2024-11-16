package server.interfaces;

import entitites.Product;
import entitites.User;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IProductController{
    Product insertProduct(Product product, User user) throws Exception;
    ArrayList<Product> getAllProducts();
    Product updateProduct(Product product) throws Exception;
    ArrayList<Product> getProduct(String productName);
    ArrayList<Product> getProductByMarket(String marketName);
}
