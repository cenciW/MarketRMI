package server;


import contracts.ClientInterface;
import server.utils.FileHandler;
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
import java.util.ArrayList;
import java.util.Optional;

public class Server extends UnicastRemoteObject implements ServerInterface {
    static ArrayList<User> usersList = new ArrayList<User>();
    static FileHandler file;

    public Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        file = new FileHandler(new File("src/server/database/usersList.txt").getAbsolutePath());
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ) {
            //Instancing Server
            Server server = new Server();
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
                    user.clientInterface.printOnClient(messageToClients);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao tentar ler o texto introduzido: " + e.getMessage());
        }
    }

    @Override
    public boolean login(User user, ClientInterface clientInterface) throws RemoteException {

        Optional<User> userOptional = file.searchUserOnFile(user);
        boolean ret = false;
        if(userOptional.isEmpty()) {
            System.out.println("Cliente: " + user.getUsername() + " tentativa de login não autorizada");
            ret = false;
        } else {
            ret = true;
            System.out.println("Cliente: " + user.getUsername() + " entrou no servidor.");
            usersList.add(new User(user.getUsername(), user.getPassword(), clientInterface));
        }

        System.out.print("Introduza a mensagem que deseja enviar para todos os clientes:\n:> ");
        return ret;
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
