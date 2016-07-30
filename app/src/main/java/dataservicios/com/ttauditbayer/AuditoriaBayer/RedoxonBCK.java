package dataservicios.com.ttauditbayer.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataservicios.com.ttauditbayer.AndroidCustomGalleryActivity;
import dataservicios.com.ttauditbayer.Model.PollProductStore;
import dataservicios.com.ttauditbayer.Model.ProductScore;
import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParser;
import dataservicios.com.ttauditbayer.util.SessionManager;

/**
 * Created by Jaime on 4/05/2016.
 */
public class RedoxonBCK extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = "Redoxon";
    private SessionManager session;

    private Switch sw_recomienda, sw_stock ;
    private LinearLayout ly_stock,ly_productos;
    private Button bt_photo, bt_guardar;
    private EditText et_Comentario, et_ComentarioOtros;
    private TextView tv_ComentarioOtros;
    private TextView tv_Pregunta;
    private CheckBox cbTienda , cbA,cbB,cbC,cbD;

    private String tipo,cadenaruc,fechaRuta, comentario="", comentarioOtros="";
    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id_2,poll_id_3;

    int  is_exhibidor=0, is_recomieda =0 , stock=0;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    String totalOption="";
    int totalValores ;
    int vTienda=0,vA=0,vB=0,vC=0,vD;
    String oTienda="",oA="",oB="",oC="",oD="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.redoxon);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Redoxon");


        sw_recomienda = (Switch) findViewById(R.id.swRecomienda);
        sw_stock = (Switch) findViewById(R.id.swStock);

        cbTienda = (CheckBox) findViewById(R.id.cbTienda);
        cbA = (CheckBox) findViewById(R.id.cbA);
        cbB = (CheckBox) findViewById(R.id.cbB);
        cbC = (CheckBox) findViewById(R.id.cbC);
        cbD = (CheckBox) findViewById(R.id.cbD);

        ly_stock = (LinearLayout) findViewById(R.id.lyStock);
        ly_productos = (LinearLayout) findViewById(R.id.lyProductos);

        et_Comentario = (EditText) findViewById(R.id.etComentario);
        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);

        bt_guardar = (Button) findViewById(R.id.btGuardar);
        // bt_photo = (Button) findViewById(R.id.btPhoto);

        et_Comentario = (EditText) findViewById(R.id.etComentario);

        et_ComentarioOtros = (EditText) findViewById(R.id.etComentarioOtros);
        tv_ComentarioOtros = (TextView) findViewById(R.id.tvComentarioOtros);

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("store_id");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");
        product_id =bundle.getInt("product_id");

//        poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        poll_id = 208; //SE RECOMIENDA EL PRODUCTO
        poll_id_2 = 209; //QUE PRODUCTO RECOMENDO
        poll_id_3 = 210; //STOcK



        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;


        db = new DatabaseHelper(getApplicationContext());
        PollProductStore pps = new PollProductStore();

        pps = db.getPollProductStore(product_id, tipo);
        tv_Pregunta.setText(pps.getQuestion());

        ly_stock.setEnabled(true);
        ly_stock.setVisibility(View.VISIBLE);

        if(tipo.equals("CADENA")) {

            if(cadenaruc.equals("INKAFARMA")){
                cbTienda.setText("Mi vic");
            }
            if(cadenaruc.equals("MIFARMA")){
                cbTienda.setText("Redoxin");


            }
            if(cadenaruc.equals("ARCANGEL")){
                cbTienda.setText("Efervit -C");
            }

            if(cadenaruc.equals("B&S")){
                cbTienda.setText("Redo-C");
            }

        } else if(tipo.equals("HORIZONTAL")) {

            cbTienda.setEnabled(false);
            cbTienda.setVisibility(View.INVISIBLE);
        }


        sw_recomienda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_recomieda = 1;
                    ly_stock.setEnabled(false);
                    ly_stock.setVisibility(View.INVISIBLE);

                    ly_productos.setVisibility(View.INVISIBLE);
                    ly_productos.setEnabled(false);

                    cbTienda.setChecked(false);
                    cbA.setChecked(false);
                    cbB.setChecked(false);
                    cbC.setChecked(false);
                    cbD.setChecked(false);

                } else {
                    is_recomieda = 0;
                    //if(product_id == 534){
                    ly_stock.setEnabled(true);
                    ly_stock.setVisibility(View.VISIBLE);
                    //}

                    ly_productos.setVisibility(View.VISIBLE);
                    ly_productos.setEnabled(true);

                    cbTienda.setChecked(false);
                    cbA.setChecked(false);
                    cbB.setChecked(false);
                    cbC.setChecked(false);
                    cbD.setChecked(false);
                }
            }
        });

        sw_stock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stock = 1;

                } else {
                    stock = 0;

                }
            }
        });

        cbD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    // perform logic
                    tv_ComentarioOtros.setVisibility(View.VISIBLE);
                    et_ComentarioOtros.setEnabled(true);
                    et_ComentarioOtros.setVisibility(View.VISIBLE);

                } else{
                    tv_ComentarioOtros.setVisibility(View.INVISIBLE);
                    et_ComentarioOtros.setEnabled(false);
                    et_ComentarioOtros.setVisibility(View.INVISIBLE);
                }

            }
        });


//        bt_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takePhoto();
//            }
//        });

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cbTienda.isChecked()) {
                    if(cadenaruc.equals("INKAFARMA")){
                        vTienda = 1;
                        oTienda = String.valueOf(poll_id_2) + "o";
                    }
                    if(cadenaruc.equals("MIFARMA")){
                        vTienda = 1;
                        oTienda = String.valueOf(poll_id_2) + "p";

                    }
                    if(cadenaruc.equals("ARCANGEL")){
                        vTienda = 1;
                        oTienda = String.valueOf(poll_id_2) + "q";
                    }

                    if(cadenaruc.equals("B&S")){
                        vTienda = 1;
                        oTienda = String.valueOf(poll_id_2) + "r";
                    }

                }

                if (cbA.isChecked()) {
                    vA = 1;
                    oA = String.valueOf(poll_id_2) + "s";
                }
                if (cbB.isChecked()) {
                    vB = 1;
                    oB = String.valueOf(poll_id_2) + "t";
                }
                if (cbC.isChecked()) {
                    vC = 1;
                    oC = String.valueOf(poll_id_2) + "u";
                }

                if (cbD.isChecked()) {
                    vD = 1;
                    oD = String.valueOf(poll_id_2) + "af";
                }



                totalValores = vTienda + vA + vB + vC + vD ;
                totalOption = oTienda + "|" + oA + "|" + oB + "|" + oC + "|" + oD ;

                if(is_recomieda==0){
                    if(totalValores==0){

                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe marcar almenos una opción", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }




                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        comentario = String.valueOf(et_Comentario.getText()) ;
                        comentarioOtros = String.valueOf(et_ComentarioOtros.getText()) ;
                        new loadPoll71().execute();
                        db.updateProductActive(product_id, 1);

                        ProductScore ps = new ProductScore();
                        if(is_recomieda==1) {

                            ps = db.getProductScoreForStore(store_id);
                            int total_products = 0 ;
                            total_products = 1  + ps.getTotalProducts();
                            db.updateProductScoreForTotalProducts(store_id,total_products);
                        }

//                        if(is_exhibidor==1) {
//
//                            ps = db.getProductScoreForStore(store_id);
//                            int total_exhibidores = 0 ;
//                            total_exhibidores = 1  + ps.getTotalExhibitions();
//                            db.updateProductScoreForTotalExhibitions(store_id,total_exhibidores);
//                        }

                        List<ProductScore> listProductScore = new ArrayList<ProductScore>();
                        listProductScore = db.getAllProductsScore();
                        Log.d(LOG_TAG, String.valueOf(listProductScore));

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                builder.setCancelable(false);




            }
        });





    }

    private void takePhoto() {

        Intent i = new Intent( MyActivity, AndroidCustomGalleryActivity.class);
        Bundle bolsa = new Bundle();



        bolsa.putString("store_id",String.valueOf(store_id));
        bolsa.putString("product_id",String.valueOf(product_id));
        bolsa.putString("poll_id",String.valueOf(poll_id));
        bolsa.putString("url_insert_image", GlobalConstant.dominio + "/insertImagesProductPoll");
        bolsa.putString("tipo", "1");
        i.putExtras(bolsa);
        startActivity(i);


    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                Intent a = new Intent(this,PanelAdmin.class);
//                //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
//            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
//            onBackPressed();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    @Override
    public void onBackPressed() {
        //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    class loadPoll71 extends AsyncTask<Void, Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            //tvCargando.setText("Cargando Product...");
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //cargaTipoPedido();

            InsertAuditPollsProduct(String.valueOf(poll_id),"0",String.valueOf(is_recomieda),comentario);

            if(is_recomieda==0){
                InsertAuditPollsOtions(String.valueOf(poll_id_2),"0","0","");

                InsertAuditPollsProduct(String.valueOf(poll_id_3), "0", String.valueOf(stock), "");
            } else if(is_recomieda==1) {

                //Enviando por defecto estock 1
                InsertAuditPollsProduct(String.valueOf(poll_id_3),"0","1","");
            }
//            Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//            startActivity(intent);
//            finish();
            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();
                //new loadPoll72().execute();

                finish();

            }
        }
    }




    private void InsertAuditPollsProduct(String poll_id, String status , String result,String comentario) {
        int success;
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("poll_id", poll_id));
            params.add(new BasicNameValuePair("store_id", String.valueOf(store_id)));
            params.add(new BasicNameValuePair("product_id", String.valueOf(product_id)));
            params.add(new BasicNameValuePair("sino", "1"));
            params.add(new BasicNameValuePair("coment", String.valueOf(comentario)));
            params.add(new BasicNameValuePair("result", result));
            params.add(new BasicNameValuePair("company_id", String.valueOf(GlobalConstant.company_id)));
            params.add(new BasicNameValuePair("idroute", String.valueOf(rout_id)));
            params.add(new BasicNameValuePair("idaudit", String.valueOf(audit_id)));
            params.add(new BasicNameValuePair("status", status));


            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPollsProduct" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                Log.d(LOG_TAG, json.getString("Ingresado correctamente"));
            }else{
                Log.d(LOG_TAG, json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void InsertAuditPollsOtions(String poll_id, String status,String result,String comentario) {
        int success;
        try {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("poll_id", poll_id));
            params.add(new BasicNameValuePair("store_id", String.valueOf(store_id)));

            params.add(new BasicNameValuePair("options", "1"));
            params.add(new BasicNameValuePair("limits", "0"));

            params.add(new BasicNameValuePair("media", "0"));


            params.add(new BasicNameValuePair("coment", "1"));
            params.add(new BasicNameValuePair("coment_options", "0"));
            // params.add(new BasicNameValuePair("coment_options", "0"));
            params.add(new BasicNameValuePair("comentario_options", ""));
            params.add(new BasicNameValuePair("limite", ""));
            params.add(new BasicNameValuePair("opcion", totalOption));

            params.add(new BasicNameValuePair("sino", "0"));
            params.add(new BasicNameValuePair("product", "1"));


            params.add(new BasicNameValuePair("comentario", String.valueOf(comentarioOtros)));
            params.add(new BasicNameValuePair("result", result));
            params.add(new BasicNameValuePair("idCompany", String.valueOf(GlobalConstant.company_id)));
            params.add(new BasicNameValuePair("idRuta", String.valueOf(rout_id)));
            params.add(new BasicNameValuePair("idAuditoria", String.valueOf(audit_id)));

            params.add(new BasicNameValuePair("product_id", String.valueOf(product_id)));
            params.add(new BasicNameValuePair("status", status));


            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPolls" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditBayer" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                Log.d(LOG_TAG, json.getString("Ingresado correctamente"));
            }else{
                Log.d(LOG_TAG, json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

