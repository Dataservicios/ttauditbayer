package dataservicios.com.ttauditbayer.Model;

/**
 * Created by Jaime Eduardo on 29/09/2015.
 */
public class Product {

    private int id;

    private String name;
    private String eam;
    private String image;
    private int category_id;
    private String category_name;
    private int company_id;
    private int active;

    public Product() {

    }

    public Product(int id, String name, String eam, String image , int company_id , int category_id, String category_name) {
        this.id = id;
        this.name = name;
        this.eam = eam;
        this.image = image;
        this.category_name = category_name;
        this.setCompany_id(company_id);
    }

    /**
     *
     * @return id
     */

    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getEam() {
        return eam;
    }

    /**
     *
     * @param eam
     */
    public void setEam(String eam) {
        this.eam = eam;
    }

    /**
     *
     * @return
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    /**
     *
     * @return category_id
     */
    public int getCategory_id() {
        return category_id;
    }

    /**
     *
     * @param category_id
     */
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    /**
     *
     * @return category_name
     */
    public String getCategory_name() {
        return category_name;
    }

    /**
     *
     * @param category_name
     */
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    /**
     * Get Setatus Publicity
     * @return
     */
    public int getActive() {
        return active;
    }

    /**
     * Set estatus publicity
     * @param active
     */
    public void setActive(int active) {
        this.active = active;
    }
}
