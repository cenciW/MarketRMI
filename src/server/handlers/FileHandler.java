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
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    public List<Product> readAllProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
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
            String[] fields = line.split(";");
            int id = Integer.parseInt(fields[0]);
            String name = fields[1];
            double price = Double.parseDouble(fields[2]);
            String marketName = fields[3];
            Date dateInserted = dateFormat.parse(fields[4]);
            Date dateLastModified = dateFormat.parse(fields[5]);
            // Substitua `null` pelo usuário real, conforme o design da classe `Utils.User`
            User user = null;

            return new Product(id, name, price, marketName, dateInserted, dateLastModified, user);
        } catch (ParseException | NumberFormatException e) {
            System.out.println("Erro ao converter a linha em um produto: " + e.getMessage());
            return null;
        }
    }
}
