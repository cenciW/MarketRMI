package entitites;

import contracts.ClientInterface;

import java.io.Serializable;

/*
* Utils.User (só o server pode ver)
+username (no ficheiro tem que ser criado manualmente e nao pode ser alterado via aplicacao)
+password
>Apenas uma tentativa de login, se errar sua aplicação será finalizada

>Cada user pode executar:
    getAllProducts()
    getProductByName()
    getBestSellerProductByMarketId()

>Cada user pode alterar o preço de um produto
    Ficando registrado na lista de produtos
    >userId (que alterou)
    >userName
    >dateModified

>Cada user pode adicionar novos produtos a lista de produtos
    >productName
    >productPrice
    >markedId (mercado que tem esse preço)
    >userId
    >username
    >dateInserted
    *pode conter mesmo nome de produto desde que em lojas diferentes
    >Sempre que o preço do produto for alterado todos os clientes conectados devem ser notificados (novo preço do produto é>)
*/
public class User implements Serializable {
    private String username, password;
    private ClientInterface clientInterface;
    boolean isLoggedIn = false;

    public User(){

    }
    public User(String username, String password, ClientInterface clientInterface) {
        this.username = username;
        this.password = password;
        this.clientInterface = clientInterface;
    }

    // Método para login, com limite de uma tentativa
    public boolean login(String usernameAttempt, String passwordAttempt) {
        if (!isLoggedIn) {
            if (this.username.equals(usernameAttempt) && this.password.equals(passwordAttempt)) {
                isLoggedIn = true;
                System.out.println("Login bem-sucedido para o usuário: " + username);
                return true;
            } else {
                System.out.println("Tentativa de login falhou. A aplicação será finalizada.");
                System.exit(0);
            }
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }

    public void setClientInterface(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }
}



