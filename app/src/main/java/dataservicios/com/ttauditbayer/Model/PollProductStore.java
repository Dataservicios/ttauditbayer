package dataservicios.com.ttauditbayer.Model;

/**
 * Created by Jaime on 10/02/2016.
 */
public class PollProductStore {


    private String Question, TypeStore ;
    private int Id, IdProduct;

    //private ArrayList<String> genre;

    public PollProductStore() {
    }

    public PollProductStore(int Id, int IdProduct,  String Question, String TypeStore ) {
        this.Question = Question;
        this.Id= Id;
    }
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdProduct() {
        return IdProduct;
    }

    public void setIdProduct(int IdProduct) {
        this.IdProduct = IdProduct;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getTypeStore() {
        return TypeStore;
    }

    public void setTypeStore(String TypeStore) {
        this.TypeStore = TypeStore;
    }



}
