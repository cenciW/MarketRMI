package client.utils;

import contracts.ServerRemoteInterface;
import entitites.Product;
import entitites.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

public class MenuHandler {

    private BufferedReader br;
    private ServerRemoteInterface serverRemoteInterface;
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

            do {
                double price;
                try {
                    System.out.print("Preço do produto: ");
                    price = Double.parseDouble(br.readLine());
                    product.setPrice(price);
                    break;

                } catch (NumberFormatException e) {
                    System.out.println("ERRO: O preço do produto deve ser um número.");
                }

            } while (true);

            System.out.print("Nome do mercado que tem o produto: ");
            product.setMarketName(br.readLine());

            //ZonedDateTime nowInUTC = ZonedDateTime.now();
            //Date d = Date.from(nowInUTC.toInstant());
            product.setDateInserted(new Date());
            product.setDateLastModified(new Date());

            product.setUser(user.getUsername());

            serverRemoteInterface.addProduct(product, user);

        } catch (RemoteException e) {
            System.err.println("Ocorreu um erro na comunicação com o servidor " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de I/O ao inserir o produto: " + e.getMessage());
        }
    }

    private void listProducts() throws RemoteException {
        System.out.println("Listar produtos");
        for (Product allProduct : serverRemoteInterface.getAllProducts(user)) {
            System.out.println(allProduct.toString());
        }
        System.out.println();
    }

    private void listProductsByMarketName(String marketName) throws RemoteException {
        System.out.println("Lista de produtos do mercado: " + marketName);

        for (Product allProduct : serverRemoteInterface.getProductByMarketName(user, marketName)) {
            System.out.println(allProduct.toString());
        }
        System.out.println();

    }

    private void listProductsByName(String productName) {
        try {
            ArrayList<Product> products = serverRemoteInterface.getProductByName(productName);

            if (products.isEmpty()) {
                System.out.println("Não há produtos registrados com o nome: " + productName);
                return;
            }

            for (Product allProduct : products) {
                System.out.println(allProduct.toString());
            }
        } catch (RemoteException e) {
            System.err.println("Ocorreu um erro ao buscar o produto: " + e.getMessage());
        }

        System.out.println();
    }

    private void updatePriceProduct() {
        try {
            String name, market;
            double price;

            System.out.println("Atualize um Produto: ");

            System.out.print("Nome do produto: ");
            name = br.readLine();

            do {
                try {
                    System.out.print("Preço do produto: ");
                    price = Double.parseDouble(br.readLine());
                    break;

                } catch (NumberFormatException e) {
                    System.out.println("ERRO: O preço do produto deve ser um número.");
                }

            } while (true);

            System.out.print("Nome do mercado que tem o produto: ");
            market = br.readLine();

            serverRemoteInterface.updateProduct(new Product(name, price, market, user), user);

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar atualizar o produto: " + e.getMessage());
        }

    }

    public MenuHandler(BufferedReader br, ServerRemoteInterface serverRemoteInterface, User user) {
        this.br = br;
        this.serverRemoteInterface = serverRemoteInterface;
        this.user = user;
    }


    public void startMenu() throws IOException {
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
                System.err.println("Por favor, digite um número válido: " + e.getMessage());
                continue;
            } catch (IOException e) {
                System.err.println("Erro ao utilizar o buffer (I/O): " + e.getMessage());
                System.exit(1);
            }

            switch (item) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    boolean showSubmenu = true;
                    while (showSubmenu) {
                        System.out.println("========== MENU DE PRODUTOS ==========");
                        System.out.println("=== 1. Listar todos os produtos ======");
                        System.out.println("=== 2. Buscar produto por Mercado ====");
                        System.out.println("=== 3. Buscar produto por Nome =======");
                        System.out.println("=== 4. Voltar ao menu principal ======");
                        System.out.println("======================================");
                        System.out.print("Escolha uma opção: ");

                        int submenuOption = Integer.parseInt(br.readLine());

                        switch (submenuOption) {
                            case 1:
                                listProducts(); // Método para listar todos os produtos
                                break;
                            case 2:
                                System.out.print("Digite o nome do mercado: ");
                                String marketName = br.readLine();

                                listProductsByMarketName(marketName);
                                break;
                            case 3:
                                System.out.print("Digite o nome do produto: ");
                                String productName = br.readLine();

                                listProductsByName(productName);
                                break;
                            case 4:
                                showSubmenu = false; // Sai do submenu
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    }
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
