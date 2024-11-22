package server.controllers;

import entitites.Product;
import entitites.User;
import server.handlers.FileHandler;
import server.interfaces.IProductController;
import server.utils.Cache;
import utils.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class ProductController implements IProductController{
    FileHandler fileHandler;
    public ProductController() {
        fileHandler = new FileHandler(new File("src/server/database/productsList.txt").getAbsolutePath());

        System.out.println("Preenchendo Cache de produtos: ");
        Cache.productsList = this.getAllProducts();
        System.out.println("-------------------------------");
    }

    private boolean existsProduct(Product product) {
        ArrayList<Product> list = Cache.productsList;

        return list.stream().anyMatch(p -> {
            boolean b = p.getName().equalsIgnoreCase(product.getName()) && p.getMarketName().equalsIgnoreCase(product.getMarketName());
            return b;
        });
    }

    public Product insertProduct(Product product, User user) throws Exception {
        if(product.isValid()){
            //conseguiu instanciar o Objeto

            if (existsProduct(product)) throw new Exception("Produto não encontrado");


            fileHandler.saveProduct(product);
            Cache.productsList.add(product);
            return product;

        }else{
            return null;
        }

    }

    public ArrayList<Product> getAllProducts() {
        //ler todas as linhas do file handler
        ArrayList<Product> products;

        products = fileHandler.readAllProducts();
        Cache.productsList = products;

        return products;
    }

    public Product updateProduct(Product product) throws Exception {

        //Procurar produto

        Optional<Product> oldOptionalProduct = getProduct(product.getName()).stream()
                .filter(p -> p.getMarketName().equalsIgnoreCase(product.getMarketName())).findFirst();

        if (oldOptionalProduct.isEmpty()) throw new Exception("Produto não encontrado com nome {" + product.getName() + "} no mercado {" + product.getMarketName() + "}");



        Product oldToNewProduct = oldOptionalProduct.get();



        oldToNewProduct.setDateLastModified(DateUtils.getUtcNow());
        oldToNewProduct.setUser(product.getUser());
        oldToNewProduct.setPrice(product.getPrice());

        System.out.println("Produto atualizado " + oldToNewProduct.toString());

        fileHandler.updateProductPrice(oldToNewProduct);



        return product;
    }



    public ArrayList<Product> getProduct(String productName) {

        return new ArrayList<>(
                Cache.productsList.stream()
                        .filter(product -> product.getName().toLowerCase().contains(productName.toLowerCase()))
                        .toList()
        );
    }

    public ArrayList<Product> getProductByMarket(String marketName) {
        ArrayList<Product> productsReturn = new ArrayList<>();

        for (Product product : Cache.productsList) {
            if(product.getMarketName().contains(marketName)){
                productsReturn.add(product);
            }
        }

        return productsReturn;
    }

}
