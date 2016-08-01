package dataservicios.com.ttauditbayer;

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
import android.view.Window;
import android.widget.Button;
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

import dataservicios.com.ttauditbayer.Model.Audit;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParser;
import dataservicios.com.ttauditbayer.util.JSONParserX;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * Created by Jaime Eduardo on 10/10/2015.
 */
public class Premiacion extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Activity MyActivity;
    private Button bt_guardar, bt_photo;
    private TextView tv_mensaje, tv_auditScore;
    private Integer store_id, road_id;
    private DatabaseHelper db;
    private List<Audit> audits = new ArrayList<Audit>() ;
    private Switch sw_RecibePremio;
    private LinearLayout lyControles;
    private EditText etComentario;

    private ProgressDialog pDialog;
    int  is_recibe_premio=0;
    String comentario;
    private int poll_id, audit_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premiacion);
        MyActivity = (Activity) this;

        bt_guardar = (Button) findViewById(R.id.btGuardar);
        //bt_photo = (Button) findViewById(R.id.btPhoto);
        sw_RecibePremio = (Switch) findViewById(R.id.swRecibePremio);
        lyControles = (LinearLayout)findViewById(R.id.lyControles);
        etComentario = (EditText) findViewById(R.id.etComentario);
       // bt_registrar = (Button) findViewById(R.id.btRegistrar);
        tv_mensaje = (TextView) findViewById(R.id.tvMensaje);
        //tv_auditScore = (TextView) findViewById(R.id.tvAuditScore);

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");
        road_id = bundle.getInt("road_id");
        poll_id = 446;
        audit_id = 14;
        db =  new DatabaseHelper(MyActivity);


        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);



        String mensaje ="Tienda Premiada";



        sw_RecibePremio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    is_recibe_premio = 1;
//                    bt_photo.setVisibility(View.VISIBLE);
//                    bt_photo.setEnabled(true);
                     lyControles.setVisibility(View.INVISIBLE);
                     etComentario.setText("");

                } else {
                    is_recibe_premio = 0;
//                    bt_photo.setVisibility(View.INVISIBLE);
//                    bt_photo.setEnabled(false);
                    lyControles.setVisibility(View.VISIBLE);
                    etComentario.setText("");
                }
            }
        });


        tv_mensaje.setText(String.valueOf(mensaje)) ;
        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar");
                builder.setMessage("Está seguro de guardar esta información ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        comentario = etComentario.getText().toString();
                        db.deleteAllAudits();
                        db.deleteAllAuditForStoreId(store_id);
                        // finish();
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

//        bt_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takePhoto();
//            }
//        });

    }

    private void takePhoto() {

        Intent i = new Intent( MyActivity, AndroidCustomGalleryActivity.class);
        Bundle bolsa = new Bundle();

        bolsa.putString("store_id",String.valueOf(store_id));
        bolsa.putString("product_id","0");
        bolsa.putString("poll_id",String.valueOf(poll_id));
        bolsa.putString("company_id",String.valueOf(GlobalConstant.company_id));
        bolsa.putString("url_insert_image", GlobalConstant.dominio + "/insertImagesProductPoll");
        bolsa.putString("tipo", "1");
        i.putExtras(bolsa);
        startActivity(i);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

           if(!InsertAuditPolls(poll_id, road_id , store_id ,audit_id, 0 , is_recibe_premio, comentario)) return false;

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted
            hidepDialog();
            if (result){
                // loadLoginActivity();
                if(is_recibe_premio == 1){
                    Bundle argRuta = new Bundle();
                    argRuta.clear();
                    argRuta.putInt("store_id", store_id);
                    argRuta.putInt("road_id", road_id );
                    argRuta.putInt("poll_id", poll_id);

                    Intent intent;
                    //intent = new Intent(MyActivity, Product.class);
                    intent = new Intent(MyActivity, PremiacionFoto.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                }
                finish();
            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente",Toast.LENGTH_LONG).show();
            }

        }
    }

    private boolean InsertAuditPolls(Integer poll_id,Integer road_id, Integer store_id , Integer audit_id ,int status , int result,String comentario  ) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();
            params.put("poll_id", String.valueOf(poll_id));
            params.put("store_id", String.valueOf(store_id));
            params.put("product_id", "0");
            params.put("sino", "1");
            params.put("coment", String.valueOf(comentario));
            params.put("result", String.valueOf(result));
            params.put("company_id", String.valueOf(GlobalConstant.company_id));
            params.put("idroute", String.valueOf(road_id));
            params.put("idaudit", String.valueOf(audit_id));
            params.put("status", String.valueOf(status));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPollsProduct" ,"POST", params);
            // check your log for json response
            Log.d(LOG_TAG, json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente" );
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(MyActivity, "No se puede volver atras, presione el botón finalizar, para modificar los datos póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
}
