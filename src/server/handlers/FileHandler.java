package server.handlers;

import entitites.Product;
import entitites.User;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FileHandler {
    private final String filePath;
    private File file;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");

    public FileHandler(String filePath) {
        this.filePath = filePath;
        file = new File(filePath);
        checkAndCreateFile();
    }

    // Método para verificar e criar o arquivo caso ele não exista
    private void checkAndCreateFile() {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo: " + e.getMessage());
        }
    }

    //USERS ***************************************************
    public Optional<User> searchUserOnFile(User user) {
        try (
                BufferedReader br = new BufferedReader(new FileReader(file))
        ) {
            String st;

            boolean isValid = false;
            while ((st = br.readLine()) != null) {
                String[] separated = st.split(";");

                if (separated[0].equals(user.getUsername()) && separated[1].equals(user.getPassword())) {
                    isValid = true;
                }

            }

            if (!isValid) {
                return Optional.empty();
            }

            return Optional.of(user);

        }  catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + e.getMessage());
            return Optional.empty();
        } catch (IOException e) {
            System.err.println("FileHandler: Erro de I/O na leitura dos dados: " + e.getMessage());
            return Optional.empty();
        }

    }


    //PRODUCTS ***************************************************

    // Método para gravar um produto no arquivo
    public void saveProduct(Product product) {
        try (FileWriter fw = new FileWriter(filePath, true); // Append no arquivo
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(product.writeLineFile());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o produto: " + e.getMessage());
        }
    }

    // Método para atualizar o preço de um produto
    public void updateProductPrice(String productName, double newPrice) {
        List<Product> products = readAllProducts();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : products) {
                if (product.getName().equals(productName)) {
                    product.setPrice(newPrice);
                    product.setDateLastModified(new Date()); // Atualiza a data de modificação
                }
                bw.write(product.writeLineFile());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar o preço do produto: " + e.getMessage());
        }
    }

    // Método para ler todos os produtos
    public ArrayList<Product> readAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        int cont = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(cont == 0){
                    cont++;
                    continue;
                }
                Product product = parseProduct(line);
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler os produtos: " + e.getMessage());
        }
        return products;
    }

    // Método para converter uma linha do arquivo em um objeto Utils.Product
    private Product parseProduct(String line) {
        try {
            //adm;10.0;market;18:57:03H15-11-2024;18:57:03H15-11-2024;adm
            String[] fields = line.split(";");
            String name = fields[0];
            double price = Double.parseDouble(fields[1]);
            String marketName = fields[2];

            Date dateInserted = dateFormat.parse(fields[3].replace("H", " "));
            Date dateLastModified = dateFormat.parse(fields[4].replace("H", " "));

            String username = fields[5];

            return new Product(name, price, marketName, dateInserted, dateLastModified, username);
        } catch (ParseException | NumberFormatException e) {
            System.out.println("Erro ao converter a linha em um produto: " + e.getMessage());
            return null;
        }
    }
}
