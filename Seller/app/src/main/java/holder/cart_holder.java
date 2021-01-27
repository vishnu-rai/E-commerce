package holder;

public class cart_holder {

    public cart_holder(){}

    String Name,Brand,Image,Price,Shop_id,product_id;

    public cart_holder(String name, String brand, String image, String price, String shop_id, String product_id) {
        Name = name;
        Brand = brand;
        Image = image;
        Price = price;
        Shop_id = shop_id;
        this.product_id = product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getShop_id() {
        return Shop_id;
    }

    public void setShop_id(String Shop_id) {
        this.Shop_id = Shop_id;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}