package server.controllers;

import entitites.Product;
import entitites.User;
import server.handlers.FileHandler;

import javax.swing.plaf.ProgressBarUI;
import java.io.File;
import java.rmi.RemoteException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static server.Server.dateFormat;

public class ProductController {
    FileHandler fileHandler;
    public ProductController() {
        fileHandler = new FileHandler(new File("src/server/database/productsList.txt").getAbsolutePath());
    }

    public Product insertProduct(Product product, User user) throws RemoteException {
        if(product.isValid()){
            //conseguiu instanciar o Objeto
            fileHandler.saveProduct(product);
            return product;

        }else{
            return null;
        }

    }

    public List<Product> getAllProducts() throws RemoteException {
        //ler todas as linhas do file handler
        List<Product> products = new ArrayList<>();

        products = fileHandler.readAllProducts();

        return products;
    }

    public Product updateProduct(Product product) throws RemoteException {
        return product;
    }



    public Product getProduct(String productName) throws RemoteException {
        return null;
    }

    public List<Product> getProductByMarket(String marketName) throws RemoteException {
        return null;
    }

}
