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
<img src="src/images/server_start.png" alt="Server Start" width="600">

### Client Start
<img src="src/images/client_start.png" alt="Client Start" width="600">

### Client 'adm' Login
<img src="src/images/3_client_login.png" alt="Client adm Login" width="600">

### Server Feedback to Client 'adm' Login
<img src="src/images/4_server_feedback.png" alt="Server Feedback adm" width="600">

### Client 'ze' Login
<img src="src/images/5_client_login2.png" alt="Client ze Login" width="600">

### Server Feedback to Client 'ze' Login
<img src="src/images/6_server_feedback.png" alt="Client ze Login" width="600">

### Client 'ze' Insert Product
<img src="src/images/7_client_insert_product.png" alt="Client Insert" width="600">

### Menu to choose type of list
<img src="src/images/8_lists_menu_client.png" alt="ListsMenu" width="600">

### List all products
<img src="src/images/9_lists_all_products_client.png" alt="ListsMenu" width="600">

### List products by market
<img src="src/images/10_lists_product_by_market_client.png" alt="ListsMenu" width="600">

### List product by name
<img src="src/images/11_lists_product_by_name_client.png" alt="ListsMenu" width="600">

### Client 'ze' update product
<img src="src/images/12_update_product_client.png" alt="ListsMenu" width="600">

### Notify All Clients update product
<img src="src/images/13_notify_all_clients_when_update.png" alt="ListsMenu" width="600">

### Server Logs
<img src="src/images/14_server_logs.png" alt="ListsMenu" width="600">
<img src="src/images/15_server_logs.png" alt="ListsMenu" width="600">




## Contact
For any questions or suggestions, please contact:
- José Augusto Cenci Castilho: 
  - [dev.zecenci@gmail.com]
  - [https://www.linkedin.com/in/jose-augusto-cenci-castilho-94282420a/]
- Jadir Pires de Borba Junior:
  - [jadirjunior8@gmail.com]
  - [https://www.linkedin.com/in/jadir-borba-junior/]
