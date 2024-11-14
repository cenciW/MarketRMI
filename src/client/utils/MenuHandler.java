package client.utils;

import contracts.ServerInterface;
import entitites.Product;
import entitites.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.RemoteException;

public class MenuHandler {

    private BufferedReader br;
    private ServerInterface serverInterface;
    private User user;



    private void addProduct() {
        try {
            serverInterface.addProduct(new Product(), user);
        } catch (RemoteException e) {
            System.err.println("Ocorreu um erro na comunicação com o servidor " + e.getMessage());
        }
    }

    private void listProducts() {
        System.out.println("Listar produtos");
    }

    private void updatePriceProduct() {
        System.out.println("Atualizar produto");
    }

    public MenuHandler(BufferedReader br, ServerInterface serverInterface, User user) {
        this.br = br;
        this.serverInterface = serverInterface;
        this.user = user;
    }


    public void startMenu() {
        int item = 0;
        while (true) {
            System.out.println("=============== RMI MARKET ===============");
            System.out.println("===== Escolha uma opção para navegar =====");
            System.out.println("===== 1. Cadastrar produto ===============");
            System.out.println("===== 2. Listar produtos =================");
            System.out.println("===== 3. Atualizar produto ===============");
            System.out.println("===== 4. Sair ============================");
            System.out.println("=============== RMI MARKET ===============");
            System.out.print(":> ");

            try {
                item = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Por favor, digite um número válido");
                continue;
            } catch (IOException e) {
                System.err.println("Erro ao utilizar o buffer");
                System.exit(1);
            }

            switch (item) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    listProducts();
                    break;
                case 3:
                    updatePriceProduct();
                    break;
                case 4:
                    System.out.println("Obrigado por utilizar nosso sistema, adeus...");
                    System.exit(99);
                    break;
                default:
                    System.out.println("Operação inválida, tente novamente.");
                    break;
            }
        }


    }

}
