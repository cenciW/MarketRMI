# Project Java RMI Market

## Authors
- José Augusto Cenci Castilho
- Jadir Pires de Borba Junior

## Features
- Client-server architecture using Java RMI.
- Remote object interaction.
- Market functionalities:
    - Insert a Product
    - Update a Product
      - When Update notify all clients
    - List all products
    - List all products from one market
    - List all products with one name
    - Client Login
    - Client Logout
- Server functionalities:
  - Logs

## Project Structure
```
src/
├── client
│   ├── utils
│   │   └── MenuHandler.java
│   └── Client.java
├── contracts
│   ├── ClientRemoteInterface.java
│   └── ServerRemoteInterface.java
├── docs/
│   ├── Anotacoes.txt
│   ├── Trabalho2.pdf
├── entities
│   ├── Product.java
│   └── User.java
├── server
│   ├── controllers
│   │   ├── ProductController.java
│   │   └── UserController.java
│   ├── database
│   │   ├── productsList.txt
│   │   └── usersList.txt
│   ├── handlers
│   │   └── FileHandler.java
│   ├── interfaces
│   │   ├── IProductController.java
│   │   └── IUserController.java
│   ├── utils
│   │   ├── Cache.java
│   │   └── Server.java
├── utils
│   └── DateUtils.java
```

## Screenshots
### Server Start
![Server Start](src/images/server_start.png)

### Client Start
![Client Start](src/images/client_start.png)

### Client 'adm' Login
![Client adm Login](src/images/3_client_login.png)

### Server Feedback to Client 'adm' Login
![Server Feedback adm](src/images/4_server_feedback.png)

### Client 'ze' Login
![Client ze Login](src/images/5_client_login2.png)

### Server Feedback to Client 'ze' Login
![Client ze Login](src/images/6_server_feedback.png)

### Client 'ze' Insert Product
![Client Insert](src/images/7_client_insert_product.png)

### Menu to choose type of list
![ListsMenu](src/images/8_lists_menu_client.png)

### List all products
![ListsMenu](src/images/9_lists_all_products_client.png)

### List products by market
![ListsMenu](src/images/10_lists_product_by_market_client.png)

### List product by name
![ListsMenu](src/images/11_lists_product_by_name_client.png)

### Client 'ze' update product
![ListsMenu](src/images/12_update_product_client.png)

### Notify All Clients update product
![ListsMenu](src/images/13_notify_all_clients_when_update.png)


### Server Logs
![ListsMenu](src/images/14_server_logs.png)
![ListsMenu](src/images/15_server_logs.png)



## Contact
For any questions or suggestions, please contact:
- José Augusto Cenci Castilho: 
  - [dev.zecenci@gmail.com]
  - [https://www.linkedin.com/in/jose-augusto-cenci-castilho-94282420a/]
- Jadir Pires de Borba Junior:
  - [Email]
  - [https://www.linkedin.com/in/jadir-borba-junior/]
