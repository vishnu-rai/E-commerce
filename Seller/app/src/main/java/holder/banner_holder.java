package holder;

public class banner_holder {

    public banner_holder(){}

    String shop_id,status,image;

    public banner_holder(String shop_id, String status, String image) {
        this.shop_id = shop_id;
        this.status = status;
        this.image = image;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
