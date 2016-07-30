package dataservicios.com.ttauditbayer.Model;

/**
 * Created by Jaime on 11/02/2016.
 */
public class ProductScore {
    private int id;
    private int storeId;
    private int totalProducts;
    private int totalExhibitions;
    private int awards ;


    public ProductScore() {

    }

    public ProductScore(int id, int storeId, int totalProducts, int totalExhibitions, int awards) {


        this.id= id;
        this.storeId= storeId;
        this.totalProducts= totalProducts;
        this.totalExhibitions= totalExhibitions;
        this.awards =awards;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalExhibitions() {
        return totalExhibitions;
    }

    public void setTotalExhibitions(int totalExhibitions) {
        this.totalExhibitions = totalExhibitions;
    }

    public int getAwards() {
        return awards;
    }

    public void setAwards(int awards) {
        this.awards = awards;
    }






}
