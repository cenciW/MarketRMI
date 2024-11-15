package entitites;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

/*Utils.Product
+id
+name
+price (qual data foi inserido)
+lojaId (loja onde o produto tem esse preço)
+usernameId (who inserted the product)
+dateInserted*/

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private String marketName;
    //date now
    private Date dateInserted;
    private Date dateLastModified;
    private User user;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");


    public Product() {

    }
    public Product(int id, String name, double price, String marketName, Date dateInserted, /*Date dateLastModified,*/ User user) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.marketName = marketName;
        this.dateInserted = dateInserted;
//        this.dateLastModified = dateLastModified;
        this.user = user;
    }

    public Product(int id, String name, double price, String marketName, Date dateInserted, Date dateLastModified, User user) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public Date getDateInserted() {
        return dateInserted;
    }

    public void setDateInserted(Date dateInserted) {
        this.dateInserted = dateInserted;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String convertDateToString(Date d){
        String[] formattedInserted = dateFormat.format(d).split(" ");

        return formattedInserted[0] + "H" + formattedInserted[1];

    }


    public String writeLineFile() {
        return name + ";" + price + ";" + marketName + ";" + convertDateToString(this.getDateInserted()) + ";" + convertDateToString(this.getDateLastModified()) + ";" + user.getUsername();
    }

    public boolean isValid() {
        //se for null retorna false
        if(this.getName().isEmpty() || this.getMarketName().isEmpty() ||
        this.getUser() == null || this.getPrice() <= 0.0) {
            return false;
        }else {
            return true;
        }
    }
}
