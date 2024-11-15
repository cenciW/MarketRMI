package client.utils;

import contracts.ServerInterface;
import entitites.Product;
import entitites.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

public class MenuHandler {

    private BufferedReader br;
    private ServerInterface serverInterface;
    private User user;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");




//    this.id = id;
//        this.name = name;
//        this.price = price;
//        this.marketName = marketName;
//        this.dateInserted = dateInserted;
////        this.dateLastModified = dateLastModified;
//        this.user = user;

    private void addProduct() {
        try {
            Product product = new Product();

            System.out.println("Insira um Produto: ");

            System.out.print("Nome do produto: ");
            product.setName(br.readLine());

            do{
                double price;
                try{
                    System.out.print("Preço do produto: ");
                    price = Double.parseDouble(br.readLine());
                    product.setPrice(price);
                    break;

                }catch (NumberFormatException e){
                    System.out.println("ERRO: O preço do produto deve ser um número.");
                }

            }while(true);

            System.out.print("Nome do mercado que tem o produto: ");
            product.setMarketName(br.readLine());

            ZonedDateTime nowInUTC = ZonedDateTime.now();
            Date d = Date.from(nowInUTC.toInstant());
            product.setDateInserted(d);
            product.setDateLastModified(d);

            product.setUser(user);

            serverInterface.addProduct(product, user);

        } catch (RemoteException e) {
            System.err.println("Ocorreu um erro na comunicação com o servidor " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de I/O ao inserir o produto: " + e.getMessage());
        }
    }

    private void listProducts() throws RemoteException {
        System.out.println("Listar produtos");
        for (Product allProduct : serverInterface.getAllProducts()) {
            System.out.println(allProduct.writeLineFile());
        }
    }

    private void updatePriceProduct() {
        System.out.println("Atualizar produto");
    }

    public MenuHandler(BufferedReader br, ServerInterface serverInterface, User user) {
        this.br = br;
        this.serverInterface = serverInterface;
        this.user = user;
    }


    public void startMenu() throws RemoteException {
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
