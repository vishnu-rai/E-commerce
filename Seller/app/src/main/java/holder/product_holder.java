package holder;

import java.util.List;

public class product_holder {

    public product_holder(){}

    String Brand,Mrp,Category,Description,Image,Item,Name,Price,Shop_id,product_id,Stock;
    List<String> Size,list_image;

    public product_holder(String brand, String mrp, String category, String description, String image, String item, String name, String price, String shop_id, String product_id, String stock, List<String> size, List<String> list_image) {
        Brand = brand;
        Mrp = mrp;
        Category = category;
        Description = description;
        Image = image;
        Item = item;
        Name = name;
        Price = price;
        Shop_id = shop_id;
        this.product_id = product_id;
        Stock = stock;
        Size = size;
        this.list_image = list_image;
    }

    public List<String> getList_image() {
        return list_image;
    }

    public void setList_image(List<String> list_image) {
        this.list_image = list_image;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getMrp() {
        return Mrp;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
    }

    public List<String> getSize() {
        return Size;
    }

    public void setSize(List<String> size) {
        Size = size;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getShop_id() {
        return Shop_id;
    }

    public void setShop_id(String shop_id) {
        Shop_id = shop_id;
    }
}
