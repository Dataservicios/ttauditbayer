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
import dataservicios.com.ttauditbayer.util.JSONParserX;
import dataservicios.com.ttauditbayer.util.SessionManager;

/**
 * Created by Jaime on 15/07/2016.
 */
public class AspirinaForte extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = "Aspirina 500";
    private SessionManager session;

    private Switch sw_recomienda, sw_stock ;
    private LinearLayout ly_stock,ly_productos;
    private Button bt_photo, bt_guardar;
    private EditText et_Comentario, et_ComentarioOtros, etProducto , etA,etB,etC, etD ,etE ,etF ,etG ,etH;
    private TextView tv_ComentarioOtros;
    private TextView tv_Pregunta,tvStock;
    private CheckBox cbProducto,  cbA,cbB,cbC,cbD,cbE,cbF,cbG,cbH ;

    private String tipo,cadenaruc,fechaRuta, comentario="" ,comentarioOtros="";
    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id_2,poll_id_3;

    int  is_exhibidor=0, is_recomieda =0 , stock=0;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    String totalOption="";
    int totalValores ;
    int vProducto=0,vA=0,vB=0,vC=0,vD=0, vE=0, vF=0, vG=0, vH=0;
    String oProducto="",oA="",oB="",oC="",oD="",oE="",oF="",oG="",oH="";
    String pProducto="",pA="",pB="",pC="",pD="",pE="",pF="",pG="",pH="";

    private EditText[] editTextArray;
    private CheckBox[] checkBoxArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.aspirina_forte);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Aspirina Forte");


        //sw_recomienda = (Switch) findViewById(R.id.swRecomienda);
        tvStock =(TextView) findViewById(R.id.tvStock);
        sw_stock = (Switch) findViewById(R.id.swStock);


        cbProducto = (CheckBox) findViewById(R.id.cbProducto);
        cbA = (CheckBox) findViewById(R.id.cbA);
        cbB = (CheckBox) findViewById(R.id.cbB);
        cbC = (CheckBox) findViewById(R.id.cbC);
        cbD = (CheckBox) findViewById(R.id.cbD);
        cbE = (CheckBox) findViewById(R.id.cbE);
        cbF = (CheckBox) findViewById(R.id.cbF);
        cbG = (CheckBox) findViewById(R.id.cbG);
        cbH = (CheckBox) findViewById(R.id.cbH);

        //etTienda = (EditText) findViewById(R.id.etTienda);
        etProducto = (EditText) findViewById(R.id.etProducto);
        etA = (EditText) findViewById(R.id.etA);
        etB = (EditText) findViewById(R.id.etB);
        etC = (EditText) findViewById(R.id.etC);
        etD = (EditText) findViewById(R.id.etD);
        etE = (EditText) findViewById(R.id.etE);
        etF = (EditText) findViewById(R.id.etF);
        etG = (EditText) findViewById(R.id.etG);
        etH = (EditText) findViewById(R.id.etH);

        editTextArray = new EditText[] {

                (EditText) findViewById(R.id.etProducto),
                (EditText) findViewById(R.id.etA),
                (EditText) findViewById(R.id.etB),
                (EditText) findViewById(R.id.etC),
                (EditText) findViewById(R.id.etD),
                (EditText) findViewById(R.id.etE),
                (EditText) findViewById(R.id.etF),
                (EditText) findViewById(R.id.etG),
                (EditText) findViewById(R.id.etH),
        };
        checkBoxArray = new CheckBox[] {

                (CheckBox) findViewById(R.id.cbProducto),
                (CheckBox) findViewById(R.id.cbA),
                (CheckBox) findViewById(R.id.cbB),
                (CheckBox) findViewById(R.id.cbC),
                (CheckBox) findViewById(R.id.cbD),
                (CheckBox) findViewById(R.id.cbE),
                (CheckBox) findViewById(R.id.cbF),
                (CheckBox) findViewById(R.id.cbG),
                (CheckBox) findViewById(R.id.cbH),
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

//        poll_id = 558; //SE RECOMIENDA EL PRODUCTO
//        poll_id_2 = 559; //QUE PRODUCTO RECOMENDO
//        poll_id_3 = 560; //STOcK

        poll_id = GlobalConstant.poll_id[2]; //SE RECOMIENDA EL PRODUCTO
        poll_id_2 = GlobalConstant.poll_id[3]; //QUE PRODUCTO RECOMENDO
        poll_id_3 = GlobalConstant.poll_id[4]; //STOcK


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
                cbE.setText("Dolgramin");
                cbF.setText("Migratapcin");
                cbG.setText("Dolofac");
            }
            if(cadenaruc.equals("MIFARMA")){
                cbE.setText("Cefadol");
                cbF.setText("Digravin");
                cbG.setText("Migralivia");

            }
            if(cadenaruc.equals("ARCANGEL")){
                cbE.setText("Cefadol");
                cbF.setText("Digravin");
                cbG.setText("Miolene");
            }

            if(cadenaruc.equals("B&S")){
                cbE.setText("Cefadol");
                cbF.setText("Digravin");
                cbG.setText("Migrapac");
            }



        } else if(tipo.equals("HORIZONTAL") || tipo.equals("DETALLISTA") || tipo.equals("MINI CADENAS")  || tipo.equals("SUB DISTRIBUIDOR")) {

            cbE.setText("Migrax");
            cbF.setText("Migralivia");
            cbG.setText("Migrodorixina");
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
                if (isChecked) {
                    etB.setText("1");
                    etB.setEnabled(true);
                    etB.requestFocus();
                } else {
                    etB.setText("0");
                    etB.setEnabled(false);
                }
            }
        });

        cbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etC.setText("1");
                    etC.setEnabled(true);
                    etC.requestFocus();
                } else {
                    etC.setText("0");
                    etC.setEnabled(false);
                }
            }
        });

        cbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etC.setText("1");
                    etC.setEnabled(true);
                    etC.requestFocus();
                } else {
                    etC.setText("0");
                    etC.setEnabled(false);
                }
            }
        });

        cbD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etD.setText("1");
                    etD.setEnabled(true);
                    etD.requestFocus();
                } else {
                    etD.setText("0");
                    etD.setEnabled(false);
                }
            }
        });

        cbE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etE.setText("1");
                    etE.setEnabled(true);
                    etE.requestFocus();
                } else {
                    etE.setText("0");
                    etE.setEnabled(false);
                }
            }
        });
        cbF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etF.setText("1");
                    etF.setEnabled(true);
                    etF.requestFocus();
                } else {
                    etF.setText("0");
                    etF.setEnabled(false);
                }
            }
        });
        cbG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etG.setText("1");
                    etG.setEnabled(true);
                    etG.requestFocus();
                } else {
                    etG.setText("0");
                    etG.setEnabled(false);
                }
            }
        });

        cbH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    etH.setText("1");
                    etH.setEnabled(true);
                    etH.requestFocus();
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
                        oProducto = String.valueOf(poll_id_2) + "p" + "-" + etProducto.getText().toString(); //Aspirina Forte
                        //pProducto = etProducto.getText().toString();
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
                        oA = String.valueOf(poll_id_2) + "l" + "-" + etA.getText().toString(); //Panadol
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
                        oB = String.valueOf(poll_id_2) + "m"  + "-" + etB.getText().toString(); //Panadol Forte
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
                        oC = String.valueOf(poll_id_2) + "n" + "-" + etC.getText().toString(); //Kitadol
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
                        oD = String.valueOf(poll_id_2) + "o" + "-" + etD.getText().toString(); //Acido Acetilsalicilico
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
                        if(tipo.equals("CADENA")) {
                            if(cadenaruc.equals("INKAFARMA")) oE = String.valueOf(poll_id_2) + "cq" + "-" + etE.getText().toString(); //Dolgramin
                            if(cadenaruc.equals("MIFARMA")) oE = String.valueOf(poll_id_2) + "cr" + "-" + etE.getText().toString(); //Cefadol
                            if(cadenaruc.equals("ARCANGEL")) oE = String.valueOf(poll_id_2) + "cr" + "-" + etE.getText().toString(); //Cefadol
                            if(cadenaruc.equals("B&S")) oE = String.valueOf(poll_id_2) + "cr" + "-" + etE.getText().toString(); //Cefadol
                        } else if(tipo.equals("HORIZONTAL") || tipo.equals("DETALLISTA") || tipo.equals("MINI CADENAS")  || tipo.equals("SUB DISTRIBUIDOR")) {
                            oE = String.valueOf(poll_id_2) + "cu" + "-" + etE.getText().toString(); //Migrax
                        }
                        pE = etE.getText().toString();
                    }
                }

                if (cbF.isChecked()) {
                    if(etF.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vF = 1;
                        if(tipo.equals("CADENA")) {
                            if(cadenaruc.equals("INKAFARMA")) oF = String.valueOf(poll_id_2) + "cw" + "-" + etF.getText().toString(); //Dolgramin
                            if(cadenaruc.equals("MIFARMA")) oF = String.valueOf(poll_id_2) + "cs" + "-" + etF.getText().toString(); //Cefadol
                            if(cadenaruc.equals("ARCANGEL")) oF = String.valueOf(poll_id_2) + "cs" + "-" + etF.getText().toString(); //Cefadol
                            if(cadenaruc.equals("B&S")) oF = String.valueOf(poll_id_2) + "cs" + "-" + etF.getText().toString(); //Cefadol
                        } else if(tipo.equals("HORIZONTAL") || tipo.equals("DETALLISTA") || tipo.equals("MINI CADENAS")  || tipo.equals("SUB DISTRIBUIDOR")) {
                            oF = String.valueOf(poll_id_2) + "ct" + "-" + etF.getText().toString(); //Migrax
                        }
                        pF = etF.getText().toString();
                    }
                }

                if (cbG.isChecked()) {
                    if(etG.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vG = 1;
                        if(tipo.equals("CADENA")) {
                            if(cadenaruc.equals("INKAFARMA")) oG = String.valueOf(poll_id_2) + "i" + "-" + etG.getText().toString(); //Dolgramin
                            if(cadenaruc.equals("MIFARMA")) oG = String.valueOf(poll_id_2) + "ct" + "-" + etG.getText().toString(); //Cefadol
                            if(cadenaruc.equals("ARCANGEL")) oG = String.valueOf(poll_id_2) + "j" + "-" + etG.getText().toString(); //Cefadol
                            if(cadenaruc.equals("B&S")) oG = String.valueOf(poll_id_2) + "k" + "-" + etG.getText().toString(); //Cefadol
                        } else if(tipo.equals("HORIZONTAL") || tipo.equals("DETALLISTA") || tipo.equals("MINI CADENAS")  || tipo.equals("SUB DISTRIBUIDOR")) {
                            oG = String.valueOf(poll_id_2) + "cv" + "-" + etG.getText().toString(); //Migrax
                        }
                        pG = etG.getText().toString();
                    }
                }

                if (cbH.isChecked()) {
                    if(etH.getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, "Debe ingresar un valor numérico", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
                        vH = 1;
                        oH = String.valueOf(poll_id_2) + "ai" + "-" + etH.getText().toString(); //otros
                        pH = etH.getText().toString();
                    }
                }



                totalValores = vProducto + vA + vB + vC + vD + vE + vF+ vG+ vH;
                totalOption = oProducto + "|"  + oA + "|" + oB + "|" + oC + "|" + oD + "|" + oE+ "|" + oF+ "|" + oG+ "|" + oH;

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
            if(!InsertAuditPollsProduct(poll_id,0,is_recomieda,comentario)) return false;
            if(!InsertAuditPollsOtions(poll_id_2, 0, 0, "")) return false;
            if(is_recomieda==0){
                //Enviando por defecto estock segun el swich que marco
                if(!InsertAuditPollsProduct(poll_id_3, 0, stock, "")) return false;
            } else if(is_recomieda==1) {
                //Enviando por defecto estock 1
                if(!InsertAuditPollsProduct(poll_id_3,0,1,"")) return false;
            }
            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted
            hidepDialog();
            if (result){


                //////////////////////////////////////////////modificado///////////////////////////////////////////
                ProductScore ps = new ProductScore();
                if(is_recomieda==1) {
                    ps = db.getProductScoreForStore(store_id);
                    int total_products = 0 ;
                    total_products = 1  + ps.getTotalProducts();
                    db.updateProductScoreForTotalProducts(store_id,total_products);
                }

                //////////////////////////////////////////////end///////////////////////////////////////////

                finish();
            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente",Toast.LENGTH_LONG).show();
            }

        }
    }




    private boolean InsertAuditPollsProduct(int poll_id, int status , int result,String comentario) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("poll_id",String.valueOf( poll_id));
            params.put("store_id", String.valueOf(store_id));
            params.put("product_id", String.valueOf(product_id));
            params.put("sino", "1");
            params.put("coment", String.valueOf(comentario));
            params.put("result", String.valueOf(result));
            params.put("company_id", String.valueOf(GlobalConstant.company_id));
            params.put("idroute", String.valueOf(rout_id));
            params.put("idaudit", String.valueOf(audit_id));
            params.put("status",String.valueOf( status));



            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPollsProduct" ,"POST", params);
            // check your log for json response
            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro, registro duplicado");
                    // return json.getString("message");
                    // return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, " Error " + Log.getStackTraceString(e));
            return false;
        }

        return true;

    }



    private boolean InsertAuditPollsOtions(int poll_id, int status,int result,String comentario) {
        int success;
        try {


            HashMap<String, String> params = new HashMap<>();

            params.put("poll_id",String.valueOf(poll_id));
            params.put("store_id", String.valueOf(store_id));
            params.put("options", "1");
            params.put("limits", "0");
            params.put("media", "0");
            params.put("coment", "1");
            params.put("coment_options", "0");
            params.put("comentario_options", "");
            params.put("limite", "");
            params.put("opcion", totalOption);
            params.put("sino", "0");
            params.put("product", "1");
            params.put("comentario", String.valueOf(comentarioOtros));
            params.put("result", String.valueOf(result));
            params.put("idCompany", String.valueOf(GlobalConstant.company_id));
            params.put("idRuta", String.valueOf(rout_id));
            params.put("idAuditoria", String.valueOf(audit_id));
            params.put("product_id", String.valueOf(product_id));
            params.put("status", String.valueOf(status));


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPolls" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditBayer" ,"POST", params);
            // check your log for json response
            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro, registro duplicado");
                    // return json.getString("message");
                    // return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, " Error " + Log.getStackTraceString(e));
            return false;
        }

        return true;
    }

}

