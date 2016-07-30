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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Jaime on 15/03/2016.
 */
public class Supradyn extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = "Supradyn";
    private SessionManager session;

    private Switch sw_recomienda, sw_stock ;
    private LinearLayout ly_stock,ly_productos;

    private Button bt_photo, bt_guardar;
    private EditText et_Comentario, et_ComentarioOtros, etProducto ,etTienda, etA,etB,etC, etD,etE;
    private TextView tv_ComentarioOtros;
    private TextView tv_Pregunta, tvStock;
    private CheckBox cbTienda,cbProducto , cbA,cbB,cbC,cbD,cbE ;

    private String tipo,cadenaruc,fechaRuta, comentario="" ,comentarioOtros="";
    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id_2,poll_id_3;

    int  is_exhibidor=0, is_recomieda =0 , stock=0;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    String totalOption="";
    int totalValores ;
    int vTienda=0,vProducto=0,vA=0,vB=0,vC=0,vD=0,vE=0;
    String oTienda="",oProducto="",oA="",oB="",oC="",oD="", oE="";
    String pTienda="",pProducto="",pA="",pB="",pC="",pD="", pE="";

    private EditText[] editTextArray;
    private CheckBox[] checkBoxArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.supradyn);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Supradin");


        sw_recomienda = (Switch) findViewById(R.id.swRecomienda);
        sw_stock = (Switch) findViewById(R.id.swStock);
        tvStock = (TextView) findViewById(R.id.tvStock);

        cbTienda = (CheckBox) findViewById(R.id.cbTienda);
        cbProducto = (CheckBox) findViewById(R.id.cbProducto);
        cbA = (CheckBox) findViewById(R.id.cbA);
        cbB = (CheckBox) findViewById(R.id.cbB);
        cbC = (CheckBox) findViewById(R.id.cbC);
        cbD = (CheckBox) findViewById(R.id.cbD);
        cbE = (CheckBox) findViewById(R.id.cbE);

        etTienda = (EditText) findViewById(R.id.etTienda);
        etProducto = (EditText) findViewById(R.id.etProducto);
        etA = (EditText) findViewById(R.id.etA);
        etB = (EditText) findViewById(R.id.etB);
        etC = (EditText) findViewById(R.id.etC);
        etD = (EditText) findViewById(R.id.etD);
        etE = (EditText) findViewById(R.id.etE);


        editTextArray = new EditText[] {
                (EditText) findViewById(R.id.etTienda),
                (EditText) findViewById(R.id.etProducto),
                (EditText) findViewById(R.id.etA),
                (EditText) findViewById(R.id.etB),
                (EditText) findViewById(R.id.etC),
                (EditText) findViewById(R.id.etD),
                (EditText) findViewById(R.id.etE),
        };
        checkBoxArray = new CheckBox[] {
                (CheckBox) findViewById(R.id.cbTienda),
                (CheckBox) findViewById(R.id.cbProducto),
                (CheckBox) findViewById(R.id.cbA),
                (CheckBox) findViewById(R.id.cbB),
                (CheckBox) findViewById(R.id.cbC),
                (CheckBox) findViewById(R.id.cbD),
                (CheckBox) findViewById(R.id.cbE),
        };

        ly_stock = (LinearLayout) findViewById(R.id.lyStock);
        ly_productos = (LinearLayout) findViewById(R.id.lyProductos);

        et_Comentario = (EditText) findViewById(R.id.etComentario);

        et_ComentarioOtros = (EditText) findViewById(R.id.etComentarioOtros);
        tv_ComentarioOtros = (TextView) findViewById(R.id.tvComentarioOtros);

        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);

        bt_guardar = (Button) findViewById(R.id.btGuardar);
        // bt_photo = (Button) findViewById(R.id.btPhoto);

        et_Comentario = (EditText) findViewById(R.id.etComentario);

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

        poll_id = 196; //SE RECOMIENDA EL PRODUCTO
        poll_id_2 = 197; //QUE PRODUCTO RECOMENDO
        poll_id_3 = 198; //STOcK



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
                cbTienda.setText("Biocord ");
            }
            if(cadenaruc.equals("Fasa-Mifarma")){
                cbTienda.setText("Infor x30");

            }
            if(cadenaruc.equals("ARCANGEL")){
                cbTienda.setText("Infor x60");
            }

            if(cadenaruc.equals("B&S")){
                cbTienda.setText("Vitaenergy");
            }

            etTienda.setText("0");
            etTienda.setEnabled(false);
            etTienda.setVisibility(View.VISIBLE);

        } else if(tipo.equals("HORIZONTAL")) {

            cbTienda.setEnabled(false);
            cbTienda.setVisibility(View.INVISIBLE);

            etTienda.setText("0");
            etTienda.setEnabled(false);
            etTienda.setVisibility(View.INVISIBLE);
        }

        cbProducto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    tvStock.setVisibility(View.INVISIBLE);
                    sw_stock.setEnabled(false);
                    sw_stock.setVisibility(View.INVISIBLE);

                    etProducto.setText("1");
                    etProducto.setEnabled(true);
                    // etProducto.setFocusable(true);
                    etProducto.requestFocus();
                } else{
                    tvStock.setVisibility(View.VISIBLE);
                    sw_stock.setEnabled(true);
                    sw_stock.setVisibility(View.VISIBLE);

                    etProducto.setText("0");
                    etProducto.setEnabled(false);
                }
            }
        });

        cbTienda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    etTienda.setText("1");
                    etTienda.setEnabled(true);
                    etTienda.requestFocus();
                } else{
                    etTienda.setText("0");
                    etTienda.setEnabled(false);
                }
            }
        });

        cbA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    etA.setText("1");
                    etA.setEnabled(true);
                    etA.requestFocus();
                } else{
                    etA.setText("0");
                    etA.setEnabled(false);
                }
            }
        });

        cbB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    etB.setText("1");
                    etB.setEnabled(true);
                    etB.requestFocus();
                } else{
                    etB.setText("0");
                    etB.setEnabled(false);
                }
            }
        });

        cbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    etC.setText("1");
                    etC.setEnabled(true);
                    etC.requestFocus();
                } else{
                    etC.setText("0");
                    etC.setEnabled(false);
                }
            }
        });

        cbD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    etD.setText("1");
                    etD.setEnabled(true);
                    etD.requestFocus();
                } else{
                    etD.setText("0");
                    etD.setEnabled(false);
                }
            }
        });

        cbE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {

                    etE.setText("1");
                    etE.setEnabled(true);
                    etE.requestFocus();
                    // perform logic
                    tv_ComentarioOtros.setVisibility(View.VISIBLE);
                    et_ComentarioOtros.setEnabled(true);
                    et_ComentarioOtros.setVisibility(View.VISIBLE);



                } else{
                    etE.setText("0");
                    etE.setEnabled(false);

                    tv_ComentarioOtros.setVisibility(View.INVISIBLE);
                    et_ComentarioOtros.setEnabled(false);
                    et_ComentarioOtros.setVisibility(View.INVISIBLE);
                }

            }
        });
//        sw_recomienda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    is_recomieda = 1;
//                    ly_stock.setEnabled(false);
//                    ly_stock.setVisibility(View.INVISIBLE);
//
//                    ly_productos.setVisibility(View.INVISIBLE);
//                    ly_productos.setEnabled(false);
//
//                    cbTienda.setChecked(false);
//                    cbA.setChecked(false);
//                    cbB.setChecked(false);
//                    cbC.setChecked(false);
//                   cbD.setChecked(false);
//
//                } else {
//                    is_recomieda = 0;
//                    //if(product_id == 534){
//                        ly_stock.setEnabled(true);
//                        ly_stock.setVisibility(View.VISIBLE);
//                    //}
//
//                    ly_productos.setVisibility(View.VISIBLE);
//                    ly_productos.setEnabled(true);
//
//                    cbTienda.setChecked(false);
//                    cbA.setChecked(false);
//                    cbB.setChecked(false);
//                    cbC.setChecked(false);
//                    cbD.setChecked(false);
//                }
//            }
//        });

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

//        bt_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takePhoto();
//            }
//        });

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------------Verificando que haya seleccionado 1 elemento o max 3
                int contado_control=0;
                for (int x = 0; x < checkBoxArray.length; x++) {
                    if (checkBoxArray[x].isChecked()) contado_control++;
                }
                if (contado_control > 3) {
                    Toast.makeText(MyActivity, "Debe seleccionar maximo 3 opciones", Toast.LENGTH_LONG).show();
                    return;
                }
                if (contado_control < 1) {
                    Toast.makeText(MyActivity, "Debe seleccionar 1 opción como mínimo", Toast.LENGTH_LONG).show();
                    return;
                }
                //-------------------------------------------------------

                //--------------------- Verificando que ingrese valores iguales ------------------------
                for (int x = 0; x < checkBoxArray.length; x++) {

                    if(checkBoxArray[x].isChecked()) {
                        int valor;
                        if( editTextArray[x].getText().equals(""))  valor = 0 ; else valor = Integer.valueOf(String.valueOf(editTextArray[x].getText())) ;
                        if (valor > 3) {
                            Toast.makeText(MyActivity, "El valor de prioridad debe ser entre 1 y 3", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (valor < 1) {
                            Toast.makeText(MyActivity, "El valor de prioridad debe iniciar en 1", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (int i = 0; i < editTextArray.length; i++) {

                            if(x != i){
                                int nuevo_valor;
                                nuevo_valor = Integer.valueOf(String.valueOf(editTextArray[i].getText()));
                                if(valor == nuevo_valor  ){
                                    Toast.makeText(MyActivity, "No se puede ingresar prioridades iguales", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }

                        }
                    }
                }

                if (cbProducto.isChecked()) {
                    int prioridad=0;
                    prioridad = Integer.valueOf(etProducto.getText().toString());
                    if(etProducto.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe marcar almenos una opción", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else if (prioridad == 1 ||prioridad == 2 || prioridad == 3) {
                        is_recomieda=1;
                        vProducto = 1;
                        oProducto = String.valueOf(poll_id_2) + "ac" + "-" + etProducto.getText().toString();
                        //pProducto = etProducto.getText().toString();
                    }

                }

                if (cbTienda.isChecked()) {
                    if(cbTienda.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        if(cadenaruc.equals("INKAFARMA")){
                            vTienda = 1;
                            oTienda = String.valueOf(poll_id_2) + "o";

                        }
                        if(cadenaruc.equals("Fasa-Mifarma")){
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

                        oTienda = oTienda + "-" +  etTienda.getText().toString();
                    }
                }

                if (cbA.isChecked()) {
                    if(etA.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vA = 1;
                        oA = String.valueOf(poll_id_2) + "s" + "-" + etA.getText().toString();
                    }
                }
                if (cbB.isChecked()) {
                    if(etB.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vB = 1;
                        oB = String.valueOf(poll_id_2) + "t"  + "-" + etB.getText().toString();
                        pB = etB.getText().toString();
                    }

                }
                if (cbC.isChecked()) {
                    if(etC.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vC = 1;
                        oC = String.valueOf(poll_id_2) + "u" + "-" + etC.getText().toString();
                        pC = etC.getText().toString();
                    }

                }

                if (cbD.isChecked()) {
                    if(etD.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vD = 1;
                        oD = String.valueOf(poll_id_2) + "w" + "-" + etD.getText().toString();
                        pD = etD.getText().toString();
                    }
                }


                if (cbE.isChecked()) {
                    if(etE.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vE = 1;
                        oE = String.valueOf(poll_id_2) + "y" + "-" + etE.getText().toString();
                        pE = etE.getText().toString();
                    }
                }

                totalValores = vProducto + vTienda + vA + vB + vC + vD + vE ;
                totalOption = oProducto + "|" + oTienda + "|" + oA + "|" + oB + "|" + oC + "|" + oD + "|" + oE;

                if(is_recomieda==0){
                    if(totalValores==0){

                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
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

            InsertAuditPollsOtions(String.valueOf(poll_id_2), "0", "0", "");
            if(is_recomieda==0){


                //Enviando por defecto estock segun el swich que marco
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

