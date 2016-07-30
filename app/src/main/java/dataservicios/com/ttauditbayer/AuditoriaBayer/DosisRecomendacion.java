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
import dataservicios.com.ttauditbayer.util.SessionManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Jaime on 3/04/2016.
 */

public class DosisRecomendacion extends Activity {

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosis_recomendacion);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Dosis");



        rgOpt1=(RadioGroup) findViewById(R.id.rgOpt1);
        rgOpt2=(RadioGroup) findViewById(R.id.rgOpt2);
        rgOpt3=(RadioGroup) findViewById(R.id.rgOpt3);

        rbA1=(RadioButton) findViewById(R.id.rbA1);
        rbB1=(RadioButton) findViewById(R.id.rbB1);
        rbC1=(RadioButton) findViewById(R.id.rbC1);


        rbA2=(RadioButton) findViewById(R.id.rbA2);
        rbB2=(RadioButton) findViewById(R.id.rbB2);
        rbC2=(RadioButton) findViewById(R.id.rbC2);

        rbA3=(RadioButton) findViewById(R.id.rbA3);
        rbB3=(RadioButton) findViewById(R.id.rbB3);
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

        poll_id = 160; //Precio por unidad de Panadol 500mg
        poll_id_2 = 161; //Precio por unidad de  Panadol Forte
        poll_id_3 = 162; //Precio por unidad de  Aspirina 500mg



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



                        long id1 = rgOpt1.getCheckedRadioButtonId();
                        if (id1 == -1){
                            Toast.makeText(MyActivity,"Debe seleccionar una opción" , Toast.LENGTH_LONG).show();
                            return;
                        }
                        else{
                            if(id1 == rbA1.getId())  opt1 = poll_id.toString() + "a";
                            if(id1 == rbB1.getId())  opt1 = poll_id.toString() + "b";
                            if(id1 == rbC1.getId())  opt1 = poll_id.toString() + "c";
                        }

                        long id2 = rgOpt2.getCheckedRadioButtonId();
                        if (id2 == -1){
                            Toast.makeText(MyActivity,"Debe seleccionar una opción" , Toast.LENGTH_LONG).show();
                            return;
                        }
                        else{
                            if(id2 == rbA2.getId())  opt2 = poll_id_2.toString() + "a";
                            if(id2 == rbB2.getId())  opt2 = poll_id_2.toString() + "b";
                            if(id2 == rbC2.getId())  opt2 = poll_id_2.toString() + "c";
                        }


                        long id3 = rgOpt3.getCheckedRadioButtonId();
                        if (id2 == -1){
                            Toast.makeText(MyActivity,"Debe seleccionar una opción" , Toast.LENGTH_LONG).show();
                            return;
                        }
                        else{
                            if(id3 == rbA3.getId())  opt3 = poll_id_3.toString() + "a";
                            if(id3 == rbB3.getId())  opt3 = poll_id_3.toString() + "b";
                        }



                        coment1 = String.valueOf(etComent1.getText()) ;
                        coment2 = String.valueOf(etComent2.getText()) ;
                        coment3 = String.valueOf(etComent3.getText()) ;



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
            //cargaTipoPedido();
            InsertAuditPollsOtions(poll_id,0,opt1,"",coment1 );
            InsertAuditPollsOtions(poll_id_2,0,opt2,"",coment2 );
            InsertAuditPollsOtions(poll_id_3,0,opt3,"",coment3 );
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
                //new loadPublicity().execute();
                //Intent intent = new Intent(MyActivity, LoginActivity.class);
                //startActivity(intent);

                hidepDialog();

//                Bundle argPDV = new Bundle();
//                argPDV.putInt("store_id", Integer.valueOf(store_id));
//                argPDV.putInt("company_id", Integer.valueOf(company_id));
//                argPDV.putInt("rout_id", Integer.valueOf(rout_id));
//                argPDV.putString("tipo", String.valueOf(tipo));
//                argPDV.putString("cadenaruc", String.valueOf(cadenaruc));
//                //argPDV.putInt("product_id", Integer.valueOf(product_id));
//                argPDV.putString("fechaRuta", fechaRuta);
//                argPDV.putInt("audit_id", audit_id);
//
//                Intent intent = new Intent(MyActivity, DosisRecomendacion.class);

                finish();


            }
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
            params.add(new BasicNameValuePair("user_id",String.valueOf(user_id)));
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
}

