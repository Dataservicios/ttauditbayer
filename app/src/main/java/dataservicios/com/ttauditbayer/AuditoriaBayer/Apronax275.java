package dataservicios.com.ttauditbayer.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParser;
import dataservicios.com.ttauditbayer.util.SessionManager;

/**
 * Created by Jaime on 4/05/2016.
 */
public class Apronax275 extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = Apronax275.class.getSimpleName();
    private SessionManager session;

    private Switch swStock ;
    private Button bt_guardar;
    private EditText et_Comentario,etComent2,etComent1;
    private TextView tv_Pregunta;
    private LinearLayout lySi, lyNo;

    private String tipo,cadenaruc, fechaRuta, comentario="", type, region;

    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id2, poll_id3;

    int  is_stock=0 ;


    private DatabaseHelper db;

    private ProgressDialog pDialog;

    private RadioGroup rgOpt1;
    private RadioGroup rgOpt2;
    private  String opt1="",opt2="",opt="";

    private RadioButton[] radioButton1Array;
    private RadioButton[] radioButton2Array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.apronax_275);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Apronax 275");


        swStock = (Switch) findViewById(R.id.swStock);

        lySi = (LinearLayout) findViewById(R.id.lySi);
        lyNo = (LinearLayout) findViewById(R.id.lyNo);

        rgOpt1=(RadioGroup) findViewById(R.id.rgOpt1);
        rgOpt2=(RadioGroup) findViewById(R.id.rgOpt2);

        radioButton1Array = new RadioButton[] {
                (RadioButton) findViewById(R.id.rbA1),
                (RadioButton) findViewById(R.id.rbB1),
                (RadioButton) findViewById(R.id.rbC1),
                (RadioButton) findViewById(R.id.rbD1),
        };
        radioButton2Array = new RadioButton[] {
                (RadioButton) findViewById(R.id.rbA2),
                (RadioButton) findViewById(R.id.rbB2),
                (RadioButton) findViewById(R.id.rbC2),
                (RadioButton) findViewById(R.id.rbD2),
        };

        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);
        bt_guardar = (Button) findViewById(R.id.btGuardar);

        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        etComent1 = (EditText) findViewById(R.id.etComent1);
        etComent2 = (EditText) findViewById(R.id.etComent2);

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("store_id");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");



        //audit_id = 4; //Inicio de auditoría Alicorp
        poll_id = 212;
        poll_id2 = 215; //¿En que caso recomienda Apronax 275? (SI)
        poll_id3 = 216; //¿Por qué no ha comprado Apronax 275? (NO)


        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        //tv_Pregunta.setText("¿Se encuentra abierto el establecimiento?");

//        for (int x = 0; x < radioButton1Array.length; x++) {
//            radioButton1Array[x].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // DO stuff here........................
//                }
//            });
//
//        }

        rgOpt2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButton2Array[3].isChecked())
                {
                    etComent2.setEnabled(true);
                    etComent2.setVisibility(View.VISIBLE);
                    etComent2.setText("");
                }
                else
                {
                    etComent2.setEnabled(false);
                    etComent2.setVisibility(View.INVISIBLE);
                    etComent2.setText("");
                }
            }
        });

        rgOpt1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButton1Array[3].isChecked())
                {
                    etComent1.setEnabled(true);
                    etComent1.setVisibility(View.VISIBLE);
                    etComent1.setText("");
                }
                else
                {
                    etComent1.setEnabled(false);
                    etComent1.setVisibility(View.INVISIBLE);
                    etComent1.setText("");
                }
            }
        });


        swStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_stock = 1;

                    lyNo.setEnabled(false);
                    lySi.setEnabled(true);
                    lySi.setVisibility(View.VISIBLE);
                    lyNo.setVisibility(View.INVISIBLE);


                    rgOpt1.clearCheck();
                    rgOpt2.clearCheck();

                } else {
                    is_stock = 0;
                    lyNo.setEnabled(true);
                    lySi.setEnabled(false);
                    lySi.setVisibility(View.INVISIBLE);
                    lyNo.setVisibility(View.VISIBLE);


                    rgOpt1.clearCheck();
                    rgOpt2.clearCheck();
                }
            }
        });



        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(swStock.isChecked()){
                    long id1 = rgOpt1.getCheckedRadioButtonId();
                    if (id1 == -1){
                        Toast.makeText(MyActivity,"Debe seleccionar una opción" , Toast.LENGTH_LONG).show();
                        return;
                    } else{
                        for (int x = 0; x < radioButton1Array.length; x++) {
                            if(id1 ==  radioButton1Array[x].getId())  opt = poll_id2.toString() + radioButton1Array[x].getTag();
                        }

                    }

                    comentario = String.valueOf(etComent1.getText()) ;
                } else {
                    long id2 = rgOpt2.getCheckedRadioButtonId();
                    if (id2 == -1){
                        Toast.makeText(MyActivity,"Debe seleccionar una opción" , Toast.LENGTH_LONG).show();
                        return;
                    } else{
                        for (int x = 0; x < radioButton2Array.length; x++) {
                            if(id2 ==  radioButton2Array[x].getId())  opt = poll_id3.toString() + radioButton1Array[x].getTag();
                        }
                    }

                    comentario = String.valueOf(etComent2.getText()) ;

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new loadPoll().execute();
                        dialog.dismiss();
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


    class loadPoll extends AsyncTask<Void, Integer , Boolean> {
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

                InsertAuditPollsProduct(String.valueOf(poll_id),"0",String.valueOf(is_stock),"");

                if(is_stock == 0){
                    InsertAuditPollsOtions(poll_id3,0,opt,"",comentario);
                } else if(is_stock == 1) {
                    InsertAuditPollsOtions(poll_id2,0,opt,"",comentario);
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


                    Bundle argRuta = new Bundle();
                    argRuta.clear();
                    argRuta.putInt("company_id", company_id);
                    argRuta.putInt("store_id",store_id);
                    argRuta.putString("tipo", tipo);
                    argRuta.putString("cadenaruc", cadenaruc);
                    argRuta.putString("fechaRuta", fechaRuta);
                    argRuta.putInt("audit_id", audit_id);
                    argRuta.putInt("rout_id", rout_id);

                    Intent intent;
                    //intent = new Intent(MyActivity, Product.class);
                    intent = new Intent(MyActivity, MaterialPromocional.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                    finish();



                hidepDialog();

            }
        }
    }





    private void InsertAuditPollsProduct(String poll_id, String status , String result,String comentario) {
        int success;
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("poll_id",String.valueOf(poll_id)));
            params.add(new BasicNameValuePair("store_id", String.valueOf(store_id)));
            params.add(new BasicNameValuePair("product_id", "0"));
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

    private void InsertAuditPollsOtions(int poll_id, int status, String opciones, String result,String comentario) {
        int success;
        try {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("poll_id", String.valueOf(poll_id)));
            params.add(new BasicNameValuePair("store_id", String.valueOf(store_id)));
            params.add(new BasicNameValuePair("product_id", "0"));
            params.add(new BasicNameValuePair("options", "1"));
            params.add(new BasicNameValuePair("limits", "0"));
            params.add(new BasicNameValuePair("media", "0"));
            params.add(new BasicNameValuePair("coment", "1"));
            params.add(new BasicNameValuePair("coment_options", "0"));
            // params.add(new BasicNameValuePair("coment_options", "0"));
            params.add(new BasicNameValuePair("comentario_options", ""));
            params.add(new BasicNameValuePair("limite", ""));
            params.add(new BasicNameValuePair("opcion", opciones));
            params.add(new BasicNameValuePair("sino", "0"));
            params.add(new BasicNameValuePair("product", "0"));


            params.add(new BasicNameValuePair("comentario", String.valueOf(comentario)));
            params.add(new BasicNameValuePair("result", result));
            params.add(new BasicNameValuePair("idCompany", String.valueOf(GlobalConstant.company_id)));
            params.add(new BasicNameValuePair("idRuta", String.valueOf(rout_id)));
            params.add(new BasicNameValuePair("idAuditoria", String.valueOf(audit_id)));
            params.add(new BasicNameValuePair("product_id", ""));
            params.add(new BasicNameValuePair("status",  String.valueOf(status)));


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }





    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
