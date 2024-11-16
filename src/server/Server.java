package server;


import entitites.Product;
import server.controllers.ProductController;
import server.handlers.FileHandler;
import contracts.ServerInterface;
import entitites.User;

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
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

public class Server extends UnicastRemoteObject implements ServerInterface {
    static ArrayList<User> usersList = new ArrayList<User>();
    static ArrayList<Product> productsList = new ArrayList<Product>();
    static FileHandler file;
    static ProductController productController;
    private final Semaphore semaphore;

    //número máximo de semafóros para adquirir
    public static int permits = 2;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    public Server(int permits) throws RemoteException {
        super();
        this.semaphore = new Semaphore(permits, true);
    }

    public static void main(String[] args) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        file = new FileHandler(new File("src/server/database/usersList.txt").getAbsolutePath());
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ) {
            productController = new ProductController();
            //Instancing Server
            Server server = new Server(permits);
            //creating registry
            LocateRegistry.createRegistry(6666);
            //rebind
            Naming.rebind("rmi://localhost:6666/market", server);
            System.out.println("Server preparado para receber clientes.");
            String messageToClients;

            while (true) {
                System.out.print("Introduza a mensagem que deseja enviar para todos os clientes\n:> ");
                messageToClients = br.readLine();
                for (User user : usersList) {
                    //agora to enviando para todos os clientes
                    user.getClientInterface().printOnClient(messageToClients);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao tentar ler o texto introduzido: " + e.getMessage());
        }
    }

    @Override
    public boolean login(User user) throws RemoteException {

        Optional<User> userOptional = file.searchUserOnFile(user);
        boolean ret = false;
        if (userOptional.isEmpty()) {
            System.out.println("Cliente: " + user.getUsername() + " tentativa de login não autorizada");
            ret = false;
        } else {
            ret = true;
            System.out.println("Cliente: " + user.getUsername() + " entrou no servidor.");
            usersList.add(new User(user.getUsername(), user.getPassword(), user.getClientInterface()));
        }

        System.out.print("Introduza a mensagem que deseja enviar para todos os clientes:\n:> ");
        return ret;
    }


    @Override
    public void addProduct(Product product, User user) throws RemoteException {
        acquireSemaphore(user);
        ZonedDateTime nowInUTC = ZonedDateTime.now();
        Date d = Date.from(nowInUTC.toInstant());
        String[] formatted = dateFormat.format(d).split(" ");
        String formattedFinal = formatted[0] + "H" + formatted[1];

        System.out.println("Usuário " + user.getUsername() + " irá adicionar um produto às " + formattedFinal);
        productController.insertProduct(product, user);

        System.out.println("O " + user.getUsername() + " adicionou o produto: " + product.writeLineFile());
        user.getClientInterface().printOnClient("\nAdicionando produto:\n" + product.writeLineFile());
        releaseSemaphore(user);
    }


    public ArrayList<Product> getAllProducts(User user) throws RemoteException {
        ArrayList<Product> productsList = new ArrayList<>();

        acquireSemaphore(user);
        productsList = productController.getAllProducts();

        System.out.println("Listagem de todos os produtos: ");
        for (Product product : productsList) {
            System.out.println(product.writeLineFile());
        }
        releaseSemaphore(user);

        return productsList;
    }


    @Override
    public ArrayList<Product> getProductByMarketName(User user, String marketName) {
        try {
            acquireSemaphore(user);
            System.out.println("Listagem de produtos do mercado: " + marketName);
            productsList = productController.getProductByMarket(marketName);
            for (Product product : productsList) {
                System.out.println(product.writeLineFile());
            }
            releaseSemaphore(user);
        } catch (RemoteException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        if (productsList.isEmpty()) {
            System.out.println("Lista de produtos vazia.");
            return new ArrayList<>();
        }
        return productsList;
    }

    //TODO: IMPLEMENT UPDATE PRODUCT
    @Override
    public Product updateProduct(Product product, User user) throws RemoteException {
        return null;
    }

    @Override
    public void acquireSemaphore(User user) throws RemoteException {
        try {
            System.out.println("Semáforos disponíveis para adquirir: " + semaphore.availablePermits());
            semaphore.acquire();
            System.out.println("User: ["+user.getUsername() +"]:" +" Adquiriu o semaforo com sucesso (disponíveis): " + semaphore.availablePermits());
        } catch (InterruptedException e) {
            throw new RemoteException("Falha ao adquirir o semaforo: " + e.getMessage());
        }
    }

    @Override
    public void releaseSemaphore(User user) throws RemoteException {
        semaphore.release();
        System.out.println("User: ["+user.getUsername() +"] liberou o semaforo (disponíveis): " + semaphore.availablePermits());
    }


    @Override
    public void printOnServer(String clientName, String msgFromClient) throws RemoteException {
        if (msgFromClient.equalsIgnoreCase("Sair")) {
            //o cliente saiu, entao tenho que apagar a info dele
            for (User user : usersList) {
                if (user.getUsername().equals(clientName)) {
                    usersList.remove(user);
                    break;
                }
            }
            System.out.println("O cliente " + clientName + " saiu.");

        } else {
            System.out.println("O cliente: " + clientName + " enviou: " + msgFromClient);
        }

        //server pedindo para enviarmos as mensagens aos outros clientes
        System.out.print("Introduza a mensagem que deseja enviar para todos os clientes:\n:> ");
        //se quiser sair tem que limpar a estrutura do servidor
    }
}
