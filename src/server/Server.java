package server;


import entitites.Product;
import server.controllers.ProductController;
import server.controllers.UserController;
import server.handlers.FileHandler;
import contracts.ServerRemoteInterface;
import entitites.User;
import server.utils.Cache;
import utils.DateUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Semaphore;

public class Server extends UnicastRemoteObject implements ServerRemoteInterface {
    static FileHandler file;
    static ProductController productController;
    static UserController userController;
    private final Semaphore semaphore;

    //número máximo de semafóros para adquirir
    public static int permits = 1;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    public Server(int permits) throws RemoteException {
        super();
        this.semaphore = new Semaphore(permits, true);
    }

    public void acquireSemaphore(User user) throws RemoteException {
        try {
            System.out.println("[SERVER]: Semáforos disponíveis para adquirir: " + semaphore.availablePermits());
            semaphore.acquire();
            System.out.println("[SERVER]: User: ["+user.getUsername() +"]:" +" Adquiriu o semaforo com sucesso (disponíveis): " + semaphore.availablePermits());
        } catch (InterruptedException e) {
            throw new RemoteException("[SERVER]: Falha ao adquirir o semaforo: " + e.getMessage());
        }
    }

    public void releaseSemaphore(User user) throws RemoteException {
        semaphore.release();
        System.out.println("[SERVER]: User: ["+user.getUsername() +"] liberou o semaforo (disponíveis): " + semaphore.availablePermits());
    }

    private void notifyAllUsers(Product product) {
        String messageToClients = "O user " + product.getUser()
                + " atualizou o preço do produto {" + product.getName() + "} no mercado {"
                + product.getMarketName() + "} para: €" + product.getPrice();

        for (User user : Cache.usersList) {
            //agora to enviando para todos os clientes
            try {
                user.getClientInterface().printOnClient(messageToClients);
            } catch (RemoteException e) {
                System.err.println("Ocorreu um erro ao notificar o cliente " + user.getUsername() + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        file = new FileHandler(new File("src/server/database/usersList.txt").getAbsolutePath());
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ) {
            productController = new ProductController();
            userController = new UserController();

            //Instancing Server
            Server server = new Server(permits);
            //creating registry
            LocateRegistry.createRegistry(6666);
            //rebind
            Naming.rebind("rmi://localhost:6666/market", server);
            System.out.println("[SERVER]: Server preparado para receber clientes.");
        } catch (IOException e) {
            System.err.println("[SERVER]: Erro ao tentar ler o texto introduzido: " + e.getMessage());
        }
    }

    @Override
    public boolean login(User user) throws RemoteException {
        return userController.login(user);
    }


    @Override
    public void addProduct(Product product, User user) throws RemoteException {
        acquireSemaphore(user);
        try {
            productController.insertProduct(product, user);
            System.out.println("[SERVER]: O " + user.getUsername() + " adicionou o produto: " + product.writeLineFile());
            user.getClientInterface().printOnClient("\nAdicionando produto:\n" + product.writeLineFile());
        } catch (Exception e) {
            System.out.println("[SERVER]: Já existe um produto com nome {" + product.getName() + "} no mercado {" + product.getMarketName() + "}");
            user.getClientInterface().printOnClient("Já existe um produto com nome {" + product.getName() + "} no mercado {" + product.getMarketName() + "}");
        }


        releaseSemaphore(user);
    }

    @Override
    public ArrayList<Product> getAllProducts(User user) throws RemoteException {
        ArrayList<Product> productsList = new ArrayList<>();

        acquireSemaphore(user);
        productsList = productController.getAllProducts();

        System.out.println("[SERVER]: Listagem de todos os produtos: ");
        for (Product product : productsList) {
            System.out.println(product.writeLineFile());
        }
        releaseSemaphore(user);

        return productsList;
    }

    @Override
    public ArrayList<Product> getProductByName(String productName) throws RemoteException{
        return productController.getProduct(productName);
    }


    @Override
    public ArrayList<Product> getProductByMarketName(User user, String marketName) {
        ArrayList<Product> productsList = new ArrayList<>();
        try {
            acquireSemaphore(user);
            System.out.println("[SERVER]: Listagem de produtos do mercado: " + marketName);
            productsList = productController.getProductByMarket(marketName);
            for (Product product : productsList) {
                System.out.println(product.writeLineFile());
            }
            releaseSemaphore(user);
        } catch (RemoteException e) {
            System.out.println("[SERVER]: Erro: " + e.getMessage());
        }
        if (productsList.isEmpty()) {
            System.out.println("[SERVER]: Lista de produtos vazia.");
            return new ArrayList<>();
        }
        return productsList;
    }



    @Override
    public void updateProduct(Product product, User user) throws RemoteException {
        acquireSemaphore(user);
        try {
            Product newProduct = productController.updateProduct(product);

            if (newProduct != null) {
                notifyAllUsers(newProduct);
            }
        } catch (Exception e) {
            System.out.println("[SERVER]: Ocorreu um erro ao atualizar o produto: " + e.getMessage());
            user.getClientInterface().printOnClient("Ocorreu um erro ao atualizar o produto: " + e.getMessage());
        }
        releaseSemaphore(user);
    }
}
