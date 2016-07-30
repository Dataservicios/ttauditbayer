package dataservicios.com.ttauditbayer.Model;

/**
 * Created by Jaime Eduardo on 04/10/2015.
 */
public class PresenceProduct {
    private  int id;
    private int store_id;
    private int category_id;
    private int product_id;
    private String producto_code;

    public void PresenceProduct(){

    }

    public void PresenceProduct(int id, int store_id, int category_id , int product_id){

        this.id = id;
        this.store_id = store_id;
        this.category_id = category_id;
        this.product_id = product_id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_Code() {
        return producto_code;
    }

    public void setProduct_Code(String producto_code) {
        this.producto_code = producto_code;
    }


}
