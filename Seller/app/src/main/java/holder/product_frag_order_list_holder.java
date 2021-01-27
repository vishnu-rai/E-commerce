package holder;

import java.util.List;

public class product_frag_order_list_holder {

    public product_frag_order_list_holder(){}

    String Name,Brand,Category,Description,Image,Item,Total_price,Shop_id,Status,Address_id,Date,User_id;
    List<String> Product_id;

    public product_frag_order_list_holder(String name, String brand, String category, String description, String image, String item, String total_price, String shop_id, String status, String address_id, String date, String user_id, List<String> product_id) {
        Name = name;
        Brand = brand;
        Category = category;
        Description = description;
        Image = image;
        Item = item;
        Total_price = total_price;
        Shop_id = shop_id;
        Status = status;
        Address_id = address_id;
        Date = date;
        User_id = user_id;
        Product_id = product_id;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAddress_id() {
        return Address_id;
    }

    public void setAddress_id(String address_id) {
        Address_id = address_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public List<String> getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(List<String> product_id) {
        Product_id = product_id;
    }

    public String getTotal_price() {
        return Total_price;
    }

    public void setTotal_price(String total_price) {
        Total_price = total_price;
    }

    public String getShop_id() {
        return Shop_id;
    }

    public void setShop_id(String shop_id) {
        Shop_id = shop_id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
