package dataservicios.com.ttauditbayer.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dataservicios.com.ttauditbayer.Model.Audit;
import dataservicios.com.ttauditbayer.Model.Encuesta;
import dataservicios.com.ttauditbayer.Model.PollProductStore;
import dataservicios.com.ttauditbayer.Model.PresenceProduct;
import dataservicios.com.ttauditbayer.Model.Product;
import dataservicios.com.ttauditbayer.Model.ProductScore;
import dataservicios.com.ttauditbayer.Model.User;


/**
 * Created by usuario on 12/02/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 13;
    // Database Name
    private static final String DATABASE_NAME = "db-bayer";
    // Table Names
    protected static final String TABLE_POLL = "poll";
    protected static final String TABLE_USER = "user";
    protected static final String TABLE_PRODUCTS = "products";
    protected static final String TABLE_PRODUCTS_SCORE = "products_score";
    protected static final String TABLE_POLLS_PRODUCT_STORE = "polls_product_store";
    protected static final String TABLE_PRESENCE_PRODUCTS = "presense_products";
    protected static final String TABLE_MEDIAS = "medias";
    protected static final String TABLE_AUDITS = "audits";



    //Name columns common
    protected static final String KEY_ID = "id";
    protected static final String KEY_NAME = "name";

    //Name columns user
    protected static final String KEY_EMAIL = "email";
    protected static final String KEY_PASSWORD = "password";

    //Name columns poll
    protected static final String KEY_QUESTION = "name_question";
    protected static final String KEY_ID_AUDITORIA = "auditoria_id";

    //Name columns Products
    protected static final String KEY_CODE = "code";
    protected static final String KEY_IMAGEN = "image";
    protected static final String KEY_COMPANY_ID = "company_id";
    protected static final String KEY_CATEGORY_ID = "category_id";
    protected static final String KEY_CATEGORY_NAME = "category_name";

    //Name column Presense Product
    protected static final String KEY_STORY_ID = "store_id";
    protected static final String KEY_PRODUCT_ID = "product_id";

    //Name column  TABLE_POLLS_PRODUCT_STORE
    protected static final String KEY_STORE_TYPE= "store_type";

    //Name column  products_score
    protected static final String KEY_TOTAL_PRODUCTS= "total_products";
    protected static final String KEY_TOTAL_EXHIBITIONS= "total_exhibitions";
    protected static final String KEY_AWARDS= "awards";

    //Name column Table product
    protected  static final String KEY_ACTIVE = "active";

    //Name column Audit
    protected static final String KEY_SCORE = "score";

    //Name column Table medias
    protected   static final String KEY_TIPO = "tipo";
    protected   static final String KEY_NAME_FILE = "archivo";
    protected   static final String KEY_TYPE = "type";
    protected   static final String KEY_MONTO = "monto";
    protected   static final String KEY_RAZON_SOCIAL = "razon_social";

    protected   static final String KEY_CATEGORY_PRODUCT_ID = "category_product_id";

    protected static final String KEY_STORE = "store_id";
    protected static final String KEY_STORE_ID = "store_id";
    protected static final String KEY_POLL_ID = "poll_id";
    protected static final String KEY_PUBLICITY_ID = "publicity_id";
    protected static final String KEY_DATE_CREATED= "created_at";
    protected static final String KEY_DATE_UPDATE= "update_at";

    //Nname


    // Table Create Statements
    // eNCUESTA table create statement
    private static final String CREATE_TABLE_POLL = "CREATE TABLE "
            + TABLE_POLL + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_AUDITORIA + " INTEGER,"
            + KEY_QUESTION  + " TEXT " + ")";

    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PASSWORD + " TEXT " + ")";

    // Products table create statement
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
            + TABLE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_CODE + " TEXT,"
            + KEY_IMAGEN + " TEXT, "
            + KEY_COMPANY_ID + " INTEGER, "
            + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_ACTIVE + " INTEGER, "
            + KEY_CATEGORY_NAME + " TEXT " + ")";

    // Presence product create table statemnt
    private static final String CREATE_TABLE_PRESENCE_PRODUCTS  = "CREATE TABLE "
            + TABLE_PRESENCE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STORY_ID + " INTEGER, "
            + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_CODE + " STRING, "
            + KEY_PRODUCT_ID + " INTEGER )";


    private static final String CREATE_TABLE_AUDITS  = "CREATE TABLE "
            + TABLE_AUDITS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT ,"
            + KEY_STORY_ID + " INTEGER, "
            + KEY_SCORE + " INTEGER )";

    private static final String CREATE_TABLE_PRODUCTS_SCORE  = "CREATE TABLE "
            + TABLE_PRODUCTS_SCORE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STORY_ID + " INTEGER ,"
            + KEY_TOTAL_PRODUCTS + " INTEGER , "
            + KEY_AWARDS + " INTEGER , "
            + KEY_TOTAL_EXHIBITIONS + " INTEGER )";

    private static final String CREATE_TABLE_POLLS_PRODUCT_STORE  = "CREATE TABLE "
            + TABLE_POLLS_PRODUCT_STORE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCT_ID + " INTEGER ,"
            + KEY_QUESTION + " TEXT , "
            + KEY_STORE_TYPE + " TEXT )";

    private static final String CREATE_TABLE_MEDIAS  = "CREATE TABLE "
            + TABLE_MEDIAS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STORE + " INTEGER ,"
            + KEY_POLL_ID + " INTEGER, "
            + KEY_PUBLICITY_ID + " INTEGER, "
            + KEY_PRODUCT_ID + " INTEGER, "
            + KEY_CATEGORY_PRODUCT_ID + " INTEGER, "
            + KEY_COMPANY_ID + " INTEGER, "
            + KEY_NAME_FILE + " TEXT, "
            + KEY_MONTO + " TEXT, "
            + KEY_RAZON_SOCIAL + " TEXT, "
            + KEY_TYPE + " INTEGER, "
            + KEY_DATE_CREATED + " TEXT )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_POLL);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_PRESENCE_PRODUCTS);
        db.execSQL(CREATE_TABLE_AUDITS);
        db.execSQL(CREATE_TABLE_POLLS_PRODUCT_STORE);
        db.execSQL(CREATE_TABLE_PRODUCTS_SCORE);
        db.execSQL(CREATE_TABLE_MEDIAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESENCE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLLS_PRODUCT_STORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIAS);

        // create new tables
        onCreate(db);
    }

//-------------------------------------------------------------------------//
    // ------------------------ "POLS" table methods ----------------//
    //------------------------------------------------------------------//
    /*
     * Creating a Encuesta
     */
    public long createEncuesta(Encuesta encuesta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, encuesta.getId());
        values.put(KEY_ID_AUDITORIA, encuesta.getIdAuditoria());
        values.put(KEY_QUESTION, encuesta.getQuestion());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
         db.insert(TABLE_POLL, null, values);

        long todo_id = encuesta.getId();
        return todo_id;
    }

    /*
     * get single Encuesta
     */
    public Encuesta getEncuesta(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_POLL + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Encuesta pd = new Encuesta();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setIdAiditoria(c.getInt(c.getColumnIndex(KEY_ID_AUDITORIA)));
        pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
        return pd;
    }

    /*
     * get single Encuesta por Auditoria
     */
    public Encuesta getEncuestaAuditoria(long idAuditoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_POLL + " WHERE "
                + KEY_ID_AUDITORIA + " = " + idAuditoria;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Encuesta pd = new Encuesta();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setIdAiditoria(c.getInt(c.getColumnIndex(KEY_ID_AUDITORIA)));
        pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
        return pd;
    }

    /**
     * getting all Encuesta
     * */
    public List<Encuesta> getAllEncuesta() {
        List<Encuesta> encuesta = new ArrayList<Encuesta>();
        String selectQuery = "SELECT  * FROM " + TABLE_POLL;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Encuesta pd = new Encuesta();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setIdAiditoria(c.getInt((c.getColumnIndex(KEY_ID_AUDITORIA))));
                pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));

                // adding to todo list
                encuesta.add(pd);
            } while (c.moveToNext());
        }
        return encuesta;
    }

    /*
     * getting Encuesta count
     */
    public int getEncuestaCount() {
        String countQuery = "SELECT  * FROM " + TABLE_POLL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    /*
     * Updating a Encuesta
     */
    public int updateEncuesta(Encuesta encuesta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, encuesta.getQuestion());
        // updating row
        return db.update(TABLE_POLL, values, KEY_ID + " = ?",
                new String[] { String.valueOf(encuesta.getId()) });
    }

    /*
     * Deleting a Encuesta
     */
    public void deleteEncuesta(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POLL, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /*
     * Deleting all Encuesta
     */
    public void deleteAllEncuesta() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POLL, null, null);

    }


    //-----------------------------------------------------------//
// ------------------------ "POLL_PRODUCT_STORE" table methods ----------------//
    //--------------------------------------------------

    /*
     * Creating a PollProductStore
     */
    public long createPollProductStore(PollProductStore pollProductStore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

       // values.put(KEY_ID, pollProductStore.getId());
        values.put(KEY_PRODUCT_ID, pollProductStore.getIdProduct());
        values.put(KEY_QUESTION, pollProductStore.getQuestion());
        values.put(KEY_STORE_TYPE, pollProductStore.getTypeStore());


        // insert row
        long todo_id = db.insert(TABLE_POLLS_PRODUCT_STORE, null, values);
       // db.insert(TABLE_POLL, null, values);

        //long todo_id = pollProductStore.getId();
        return todo_id;
    }



    /*
     * get single PollProductStore
     */
    public PollProductStore getPollProductStore(long idProduct , String typeStore) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_POLLS_PRODUCT_STORE + " WHERE "
                + KEY_PRODUCT_ID + " = " +  idProduct  + " AND " + KEY_STORE_TYPE + " = '" + typeStore + "'";
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        PollProductStore pd = new PollProductStore();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setIdProduct(c.getInt(c.getColumnIndex(KEY_PRODUCT_ID)));
        pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
        pd.setTypeStore((c.getString(c.getColumnIndex(KEY_STORE_TYPE))));
        return pd;
    }

    public List<PollProductStore> getAllPollProductStore() {
        List<PollProductStore> pollProductStore = new ArrayList<PollProductStore>();
        String selectQuery = "SELECT  * FROM " + TABLE_POLLS_PRODUCT_STORE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PollProductStore pd = new PollProductStore();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setIdProduct((c.getInt(c.getColumnIndex(KEY_PRODUCT_ID))));
                pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
                pd.setTypeStore((c.getString(c.getColumnIndex(KEY_STORE_TYPE))));

                // adding to todo list
                pollProductStore.add(pd);
            } while (c.moveToNext());
        }
        return pollProductStore;
    }
    /*
     * Deleting a PollProductStore
     */
    public void deletePollProductStore(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POLLS_PRODUCT_STORE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /*
     * Deleting all PollProductStore
     */
    public void deleteAllPollProductStore() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POLLS_PRODUCT_STORE, null, null);

    }


    //---------------------------------------------------------------//
    // ------------------------ "USER" table methods ----------------//
    //---------------------------------------------------------------//
    /*
     * Creating a USER
     */
    /*
     * Creating a USER
     */
    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_USER, null, values);

        long todo_id = user.getId();
        return todo_id;
    }



    /*
     * get single User id
     */
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        User pd = new User();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
        return pd;
    }

    /**
     *
     * @param name
     * @return
     */
    public User getUserName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_NAME + " = " + name;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        User pd = new User();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pd.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
        pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
        return pd;
    }

    /**
     *
     * @param email
     * @return
     */
    public User getUserEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_EMAIL + " = " + email;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        User pd = new User();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pd.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
        pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
        return pd;
    }

    /**
     * getting all User
     * */
    public List<User> getAllUser() {
        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User pd = new User();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pd.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
                pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
                // adding to todo list
                users.add(pd);
            } while (c.moveToNext());
        }
        return users;
    }
    /*
         * getting User count
         */
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }
    /*
     * Updating a User
     */
    public int updatePedido(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASSWORD, user.getPassword());
        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }


    /*
     * Deleting a User
     */
    public void deleteUser(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting all User
     */
    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null );

    }

    //---------------------------------------------------------------//
    // ------------------------ "PRODUCTS" table methods ----------------//
    //---------------------------------------------------------------//
    /*
     * Creating a PRODUCTS
     */
    /*
     * Creating a PRODUCTS
     */
    public long createProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, product.getId());
        values.put(KEY_NAME, product.getName());
        values.put(KEY_CODE, product.getEam());
        values.put(KEY_IMAGEN, product.getImage());
        values.put(KEY_COMPANY_ID, product.getCompany_id());
        values.put(KEY_ACTIVE, product.getActive());
        values.put(KEY_CATEGORY_NAME, product.getCategory_name());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_PRODUCTS, null, values);

        long todo_id = product.getId();
        return todo_id;
    }



    /*
     * get single User id
     */
    public Product getProduct(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Product pd = new Product();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        pd.setEam((c.getString(c.getColumnIndex(KEY_CODE))));
        pd.setImage((c.getString(c.getColumnIndex(KEY_IMAGEN))));
        pd.setCompany_id((c.getInt(c.getColumnIndex(KEY_COMPANY_ID))));
        pd.setCategory_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_ID))));
        pd.setActive((c.getInt(c.getColumnIndex(KEY_ACTIVE))));
        pd.setCategory_name((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));

        return pd;
    }

    /**
     *
     * @param name
     * @return
     */
    public Product getProductName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE "
                + KEY_NAME + " = " + name;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Product pd = new Product();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pd.setEam((c.getString(c.getColumnIndex(KEY_CODE))));
        pd.setImage((c.getString(c.getColumnIndex(KEY_IMAGEN))));
        pd.setCompany_id((c.getInt(c.getColumnIndex(KEY_COMPANY_ID))));
        pd.setCategory_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_ID))));
        pd.setActive((c.getInt(c.getColumnIndex(KEY_ACTIVE))));
        pd.setCategory_name((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));
        return pd;
    }

    /**
     *
     * @param code
     * @return
     */
    public Product getProductCode(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE "
                + KEY_CODE + " = " + code;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        Product pd = new Product();

        if (c.getCount() > 0) {
            c.moveToFirst();

            pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
            pd.setEam((c.getString(c.getColumnIndex(KEY_CODE))));
            pd.setImage((c.getString(c.getColumnIndex(KEY_IMAGEN))));
            pd.setCompany_id((c.getInt(c.getColumnIndex(KEY_COMPANY_ID))));
            pd.setCategory_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_ID))));
            pd.setActive((c.getInt(c.getColumnIndex(KEY_ACTIVE))));
            pd.setCategory_name((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));
        }
        return pd;

    }

    /**
     * getting all products
     * */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Product pd = new Product();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pd.setEam((c.getString(c.getColumnIndex(KEY_CODE))));
                pd.setImage((c.getString(c.getColumnIndex(KEY_IMAGEN))));
                pd.setCompany_id((c.getInt(c.getColumnIndex(KEY_COMPANY_ID))));
                pd.setCategory_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_ID))));
                pd.setActive((c.getInt(c.getColumnIndex(KEY_ACTIVE))));
                pd.setCategory_name((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));
                // adding to todo list
                products.add(pd);
            } while (c.moveToNext());
        }
        return products;
    }
    /*
         * getting User count
         */
    public int getProductCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }
    /*
     * Updating a User
     */
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_CODE, product.getEam());
        values.put(KEY_COMPANY_ID, product.getCompany_id());
        values.put(KEY_IMAGEN, product.getImage());
        values.put(KEY_CATEGORY_ID, product.getCategory_id());
        values.put(KEY_ACTIVE, product.getActive());
        values.put(KEY_CATEGORY_NAME, product.getCategory_name());


        // updating row
        return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
    }

    public int updateProductActive(int product_id,int active ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, active);


        // updating row
        return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ? ", new String[] { String.valueOf(product_id) });
    }

    public int updateAllProductActive(int active ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, active);


        // updating row
        return db.update(TABLE_PRODUCTS, values, null, null);
    }


    /*
     * Deleting a User
     */
    public void deleteProduct(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting all User
     */
    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null );

    }
//---------------------------------------------------------------//
    // ------------------------ "AUDIT" table methods ----------------//
    //------

    /**
     * Create Audit
     * @param audit
     * @return
     */
    public long createAudit(Audit audit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

       // values.put(KEY_ID, audit.getId());
        values.put(KEY_NAME, audit.getName());
        values.put(KEY_STORY_ID, audit.getStore_id());
        values.put(KEY_SCORE, audit.getScore());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_AUDITS, null, values);

        long todo_id = audit.getId();
        return todo_id;
    }
    public Audit getAudit(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Audit pd = new Audit();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        pd.setScore((c.getInt(c.getColumnIndex(KEY_SCORE))));
        pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORY_ID))));


        return pd;
    }




    /**
     * Update Audit
     * @param audit
     * @return
     */
    public int updateAudit(Audit audit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, audit.getId());
        values.put(KEY_NAME, audit.getName());
        values.put(KEY_STORY_ID, audit.getStore_id());
        values.put(KEY_SCORE, audit.getScore());


        // updating row
        return db.update(TABLE_AUDITS, values, KEY_ID + " = ?", new String[] { String.valueOf(audit.getId()) });
    }

    public int updateAuditScore(int audit_id,int score , int store_id ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,  audit_id);
        values.put(KEY_SCORE, score);


        // updating row
        return db.update(TABLE_AUDITS, values, KEY_ID + " = ? and " + KEY_STORY_ID + " = ? ", new String[] { String.valueOf(audit_id) , String.valueOf(store_id)  });
    }

    /**
     * Delete all Audits
     */
    public void deleteAllAudits() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AUDITS, null, null );
    }


    /**
     *
     * @param store_id
     */
    public void deleteAllAuditForStoreId(int store_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AUDITS, KEY_STORY_ID + " = ? ", new String[] { String.valueOf(store_id)  } );
    }

    /**
     * .Return List Audits
     * @return
     */
    public List<Audit> getAllAudits() {
        List<Audit> audit = new ArrayList<Audit>();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDITS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Audit pd = new Audit();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORY_ID))));
                pd.setScore((c.getInt(c.getColumnIndex(KEY_SCORE))));
                // adding to todo list
                audit.add(pd);
            } while (c.moveToNext());
        }
        return audit;
    }

    /**
     *
     * @param store_id
     * @return
     */
    public List<Audit> getAllAuditsForStoreId(long store_id) {
        List<Audit> audit = new ArrayList<Audit>();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE " + KEY_STORY_ID+ " = " + store_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Audit pd = new Audit();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORY_ID))));
                pd.setScore((c.getInt(c.getColumnIndex(KEY_SCORE))));
                // adding to todo list
                audit.add(pd);
            } while (c.moveToNext());
        }
        return audit;
    }





    /**
     *
     * @param id
     * @return
     */

    public int getCountAuditForId(int id) {
        String countQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE " +  KEY_ID  + " = " + id  ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }

    public int getCountAuditForIdForStoreId(int id,int store_id) {
        String countQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE " +  KEY_ID  + " = " + id + " AND " + KEY_STORY_ID  + " = " + store_id  ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }


    //---------------------------------------------------------------//
    // ------------------------ "PRESENSE PRODUCTS" table methods ----------------//
    //---------------------------------------------------------------//


    /**
     *
     * @param presenseProduct
     * @return is
     */
    public long createPresenseProduct(PresenceProduct presenseProduct) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(KEY_ID, product.getId());
        values.put(KEY_STORY_ID, presenseProduct.getStore_id());
        values.put(KEY_CATEGORY_ID, presenseProduct.getCategory_id());
        values.put(KEY_PRODUCT_ID, presenseProduct.getProduct_id());
        values.put(KEY_CODE, presenseProduct.getProduct_Code());


        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        long id = db.insert(TABLE_PRESENCE_PRODUCTS, null, values);

        //long todo_id = presenseProduct.getId();
        return id;
    }

    /**
     * Return List Presenc Products
     * @return
     */
    public List<PresenceProduct> getAllPresenceProduct() {
        List<PresenceProduct> products = new ArrayList<PresenceProduct>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRESENCE_PRODUCTS ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PresenceProduct pd = new PresenceProduct();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setProduct_id((c.getInt(c.getColumnIndex(KEY_PRODUCT_ID))));

                pd.setCategory_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_ID))));
                pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORY_ID))));
                // adding to todo list
                products.add(pd);
            } while (c.moveToNext());
        }
        return products;
    }

    /**
     *
     * @param store_id
     * @return
     */
    public List<PresenceProduct> getAllPresenceProductForStoreId(long store_id) {
        List<PresenceProduct> products = new ArrayList<PresenceProduct>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRESENCE_PRODUCTS  + " WHERE "  + KEY_STORY_ID + " = " + store_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PresenceProduct pd = new PresenceProduct();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setProduct_id((c.getInt(c.getColumnIndex(KEY_PRODUCT_ID))));

                pd.setCategory_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_ID))));
                pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORY_ID))));
                // adding to todo list
                products.add(pd);
            } while (c.moveToNext());
        }
        return products;
    }


    /**
     * Retun total presence products selected
     * @return
     */
    public int getCountPresenseProduct() {
        String countQuery = "SELECT  * FROM " + TABLE_PRESENCE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }

    /**
     *
     * @param category_id
     * @return count
     */
    public int getCountPresenseProductForCategory(int category_id) {
        String countQuery = "SELECT  * FROM " + TABLE_PRESENCE_PRODUCTS + " WHERE " +  KEY_CATEGORY_ID  + " = " + category_id ;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }

    public int getCountPresenseProductForCode(String code_product) {
        String countQuery = "SELECT  * FROM " + TABLE_PRESENCE_PRODUCTS + " WHERE " +  KEY_CODE  + " = " + code_product ;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }



    /**
     * Delete all Presense  Product
     */
    public void deleteAllPresenseProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESENCE_PRODUCTS, null, null );

    }

    /**
     *
     * @param store_id
     */
    public void deletePresenseProductForStoreId(int store_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESENCE_PRODUCTS, KEY_STORY_ID + " = ? ",  new String[] { String.valueOf(store_id) } );

    }


    //---------------------------------------------------------------//
    // ------------------------ "products_score" table methods ----------------//
    //---------------------------------------------------------------//

    /**
     *
     * @param productScore
     * @return
     */
    public long createProductScore(ProductScore productScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
       // values.put(KEY_ID, product.getId());
        values.put(KEY_STORY_ID, productScore.getStoreId());
        values.put(KEY_TOTAL_PRODUCTS, productScore.getTotalProducts());
        values.put(KEY_TOTAL_EXHIBITIONS, productScore.getTotalExhibitions());
        values.put(KEY_AWARDS, productScore.getAwards());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_PRODUCTS_SCORE, null, values);

        long todo_id = productScore.getId();
        return todo_id;
    }


    /**
     *
     * @param idStore
     * @return
     */
    public ProductScore getProductScoreForStore(long idStore) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS_SCORE + " WHERE "
                + KEY_STORY_ID + " = " + idStore;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        ProductScore pd = new ProductScore();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setStoreId(c.getInt(c.getColumnIndex(KEY_STORY_ID)));
        pd.setTotalProducts((c.getInt(c.getColumnIndex(KEY_TOTAL_PRODUCTS))));
        pd.setTotalExhibitions((c.getInt(c.getColumnIndex(KEY_TOTAL_EXHIBITIONS))));
        pd.setAwards((c.getInt(c.getColumnIndex(KEY_AWARDS))));

        return pd;
    }


    /**
     *
     * @return
     */
    public List<ProductScore> getAllProductsScore() {
        List<ProductScore> productScores = new ArrayList<ProductScore>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS_SCORE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProductScore pd = new ProductScore();
                pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                pd.setStoreId(c.getInt(c.getColumnIndex(KEY_STORY_ID)));
                pd.setTotalProducts((c.getInt(c.getColumnIndex(KEY_TOTAL_PRODUCTS))));
                pd.setTotalExhibitions((c.getInt(c.getColumnIndex(KEY_TOTAL_EXHIBITIONS))));
                pd.setAwards((c.getInt(c.getColumnIndex(KEY_AWARDS))));
                // adding to todo list
                productScores.add(pd);
            } while (c.moveToNext());
        }
        return productScores;
    }
    /*
         * getting User count
         */
    public int getProductScoreCountForStore(long idStore) {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS_SCORE + " WHERE "  + KEY_STORY_ID + " = " + idStore;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }

    /**
     *
     * @param storeId
     * @param totalProducts
     * @return
     */
    public int updateProductScoreForTotalProducts(int storeId,int totalProducts) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL_PRODUCTS, totalProducts);

        // updating row
        return db.update(TABLE_PRODUCTS_SCORE, values, KEY_STORY_ID + " = ? ", new String[] { String.valueOf(storeId) });
    }

    /**
     *
     * @param storeId
     * @param totalExhibitions
     * @return
     */
    public int updateProductScoreForTotalExhibitions(int storeId,int totalExhibitions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL_EXHIBITIONS, totalExhibitions);

        // updating row
        return db.update(TABLE_PRODUCTS_SCORE, values, KEY_STORY_ID + " = ? ", new String[] { String.valueOf(storeId) });
    }

    public int updateProductScoreForAwards(int storeId,int awards) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AWARDS, awards);

        // updating row
        return db.update(TABLE_PRODUCTS_SCORE, values, KEY_STORY_ID + " = ? ", new String[] { String.valueOf(storeId) });
    }

    /**
     *
     */
    public void deleteAllProductsScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS_SCORE, null, null );

    }



    public static boolean checkDataBase(Context context) {
        SQLiteDatabase checkDB = null;
        try {
            File database=context.getDatabasePath(DATABASE_NAME);
            if (database.exists()) {
                Log.i("Database", "Found");
                String myPath = database.getAbsolutePath();
                Log.i(LOG, myPath);
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
                //return true;
            } else {
                // Database does not exist so copy it from assets here
                Log.i(LOG, "Not Found");
                //return false;
            }
        } catch(SQLiteException e) {
            Log.i(LOG, "Not Found");
        } finally {
            if(checkDB != null) {
                checkDB.close();
            }
        }
        return checkDB != null ? true : false;
    }

}
