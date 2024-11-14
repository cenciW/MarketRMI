package client;

import contracts.ServerInterface;
import contracts.ClientInterface;
import entitites.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {
    protected Client() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try (
                //I/O initializing
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ) {
            //looking for the server
            ServerInterface serverInterface = (ServerInterface) Naming.lookup("rmi://localhost:6666/market");

            //here server and client are connected
            Client client = new Client();

            String username, password;
            String messageFromServer = "";
            String messageToServer = "";


            System.out.print("Insira seu nome de usuário:\n:>");
            username = br.readLine();
            System.out.print("Insira sua senha:\n:>");
            password = br.readLine();

            User user = new User(username, password, client);


            //buscar no arquivo de texto com o file handler

            boolean auth = serverInterface.login(user, client);
            if(auth) {
                System.out.println("Usuário autenticado com sucesso!");
            }else{
                System.out.println("Usuario não encontrado");
                System.exit(0);
            }


            while (!messageToServer.equalsIgnoreCase("sair")) {
                System.out.print("Introduza a mensagem que deseja enviar ao servidor:\n:> ");
                messageToServer = br.readLine();

                serverInterface.printOnServer(username, messageToServer);
            }
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erro ao tentar ler o texto introduzido: " + e.getMessage());
        } catch (NotBoundException e) {
            System.err.println("O nome não está registrado no servidor RMI." + e.getMessage());
        }
    }


    @Override
    public void printOnClient(String msgFromServer) throws RemoteException {
        System.out.println("O servidor enviou: " + msgFromServer);
        System.out.print("Introduza uma mensagem para enviar ao servidor\n:> ");
    }
}
