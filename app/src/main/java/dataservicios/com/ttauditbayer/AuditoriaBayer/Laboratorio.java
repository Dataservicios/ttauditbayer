package dataservicios.com.ttauditbayer.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataservicios.com.ttauditbayer.Model.PollProductStore;
import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParser;
import dataservicios.com.ttauditbayer.util.JSONParserX;
import dataservicios.com.ttauditbayer.util.SessionManager;

/**
 * Created by Jaime on 9/07/2016.
 */
public class Laboratorio extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = "Aspirina";
    private SessionManager session;

    private Switch sw_recomienda, sw_stock ;
    private LinearLayout ly_stock,ly_productos;
    private Button bt_photo, bt_guardar;
    private EditText etPrecioPanado, etPrecioPanadolF, etPrecioAspirina;

    private Integer user_id, company_id,store_id,rout_id,audit_id, poll_id, poll_id_2,poll_id_3;
    private String tipo,cadenaruc,fechaRuta, comentario="" ,comentarioOtros="";

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    RadioGroup rgOpt1,rgOpt2,rgOpt3;
    RadioButton rbA1,rbB1,rbC1;
    RadioButton rbA2,rbB2,rbC2;
    RadioButton rbA3,rbB3;

    String opt1="",opt2="",opt3="";
    String coment1="", coment2="",coment3="";
    EditText etComent1,etComent2,etComent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laboratorio);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Dosis");



//        rgOpt1=(RadioGroup) findViewById(R.id.rgOpt1);
//        rgOpt2=(RadioGroup) findViewById(R.id.rgOpt2);
//        rgOpt3=(RadioGroup) findViewById(R.id.rgOpt3);
//
//        rbA1=(RadioButton) findViewById(R.id.rbA1);
//        rbB1=(RadioButton) findViewById(R.id.rbB1);
//        rbC1=(RadioButton) findViewById(R.id.rbC1);
//
//
//        rbA2=(RadioButton) findViewById(R.id.rbA2);
//        rbB2=(RadioButton) findViewById(R.id.rbB2);
//        rbC2=(RadioButton) findViewById(R.id.rbC2);
//
//        rbA3=(RadioButton) findViewById(R.id.rbA3);
//        rbB3=(RadioButton) findViewById(R.id.rbB3);
        //rbC3=(RadioButton) findViewById(R.id.rbC3);

        etComent1=(EditText) findViewById(R.id.etComent1);
        etComent2=(EditText) findViewById(R.id.etComent2);
        etComent3=(EditText) findViewById(R.id.etComent3);

        bt_guardar = (Button) findViewById(R.id.btGuardar);



        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("store_id");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");
        //product_id =bundle.getInt("product_id");

//        poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        poll_id = 438; //¿Qué tipo de premios les entrega los laboratorios que los visitan?
        poll_id_2 = 439; //¿Qué laboratorios?
        poll_id_3 = 440; //¿Cada cuánto tiempo?



        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;


        db = new DatabaseHelper(getApplicationContext());
        PollProductStore pps = new PollProductStore();




        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Finalizar");
                builder.setMessage("Está seguro que desea finalizar: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        coment1 = String.valueOf(etComent1.getText()).trim() ;
                        coment2 = String.valueOf(etComent2.getText()).trim() ;
                        coment3 = String.valueOf(etComent3.getText()).trim() ;

                        new auditPoll().execute();

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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    class auditPoll extends AsyncTask<Void, Integer , Boolean> {
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

            InsertAuditPollsProduct(poll_id,0,0,coment1);
            InsertAuditPollsProduct(poll_id_2,0,0,coment2);
            InsertAuditPollsProduct(poll_id_3,0,0,coment3);

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted
            hidepDialog();
            if (result){
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
            params.put("poll_id",String.valueOf(poll_id));
            params.put("store_id", String.valueOf(store_id));
            params.put("product_id", "0");
            params.put("sino","0");
            params.put("coment", String.valueOf(comentario));
            params.put("result", String.valueOf(result));
            params.put("company_id", String.valueOf(GlobalConstant.company_id));
            params.put("idroute", String.valueOf(rout_id));
            params.put("idaudit", String.valueOf(audit_id));
            params.put("user_id", String.valueOf(user_id));
            params.put("status", String.valueOf(status));


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
}

