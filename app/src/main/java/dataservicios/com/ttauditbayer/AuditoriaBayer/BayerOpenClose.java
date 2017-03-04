package dataservicios.com.ttauditbayer.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dataservicios.com.ttauditbayer.AndroidCustomGalleryActivity;
import dataservicios.com.ttauditbayer.MainActivity;
import dataservicios.com.ttauditbayer.Model.Audit;
import dataservicios.com.ttauditbayer.Model.PollDetail;
import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.AuditUtil;
import dataservicios.com.ttauditbayer.util.AvailableSpaceHandler;
import dataservicios.com.ttauditbayer.util.GPSTracker;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParser;
import dataservicios.com.ttauditbayer.util.JSONParserX;
import dataservicios.com.ttauditbayer.util.SessionManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * Created by Jaime on 15/02/2016.
 */
public class BayerOpenClose extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = BayerOpenClose.class.getSimpleName();
    private SessionManager session;

    private Switch swSiNo  ;
    private Button bt_photo, bt_guardar;
    private EditText etComent;
    private TextView tv_Pregunta;

    private String tipo,cadenaruc, fechaRuta, comentario="";

    private Integer user_id, company_id,store_id,rout_id,road_id,audit_id, product_id, poll_id;

    int  is_sino=0;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    private PollDetail pollDetail;
    private Audit mAudit;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bayer_open_close);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");

        swSiNo = (Switch) findViewById(R.id.swSiNo);

        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);

        bt_guardar = (Button) findViewById(R.id.btGuardar);
        bt_photo = (Button) findViewById(R.id.btPhoto);

        etComent = (EditText) findViewById(R.id.etComent);

        gpsTracker = new GPSTracker(MyActivity);

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("idPDV");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("idRuta");
        road_id = bundle.getInt("idRuta");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("idAuditoria");
        product_id =bundle.getInt("product_id");

        poll_id = GlobalConstant.poll_id[0]; //¿Se encuentra abierto el establecimiento?

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        tv_Pregunta.setText("¿Se encuentra abierto el establecimiento?");

        swSiNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    is_sino = 1;
                    bt_photo.setVisibility(View.INVISIBLE);
                    bt_photo.setEnabled(false);
                } else {
                    is_sino = 0;
                    bt_photo.setVisibility(View.VISIBLE);
                    bt_photo.setEnabled(true);
                }
            }
        });


        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        comentario = String.valueOf(etComent.getText()) ;

                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(1);
                        pollDetail.setOptions(0);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(1);
                        pollDetail.setComment(0);
                        pollDetail.setResult(is_sino);
                        pollDetail.setLimite("0");
                        pollDetail.setComentario(comentario);
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCommentOptions(0);
                        pollDetail.setSelectdOptions("");
                        pollDetail.setSelectedOtionsComment("");
                        pollDetail.setPriority("0");

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

    private void takePhoto() {

        // AvailableSpaceHandler space = new  AvailableSpaceHandler();

        Long total =AvailableSpaceHandler.getExternalAvailableSpaceInMB();

//        if (total > 200) {
//
//        }

        Intent i = new Intent( MyActivity, AndroidCustomGalleryActivity.class);
        Bundle bolsa = new Bundle();

        bolsa.putString("store_id", String.valueOf(store_id));
        bolsa.putString("product_id", String.valueOf("0"));
        bolsa.putString("publicities_id", String.valueOf("0"));
        bolsa.putString("poll_id", String.valueOf(poll_id));
        bolsa.putString("sod_ventana_id", String.valueOf("0"));
        bolsa.putString("company_id", String.valueOf(GlobalConstant.company_id));
        bolsa.putString("category_product_id", "0");
        bolsa.putString("monto","");
        bolsa.putString("razon_social","");
        bolsa.putString("url_insert_image", GlobalConstant.dominio + "/insertImagesProductPollAlicorp");
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

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
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
            if(is_sino == 1) {
                if(!AuditUtil.insertPollDetail(pollDetail)) return false;
            } else{

                String time_close = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
                mAudit = new Audit();
                mAudit.setCompany_id(GlobalConstant.company_id);
                mAudit.setStore_id(store_id);
                mAudit.setId(audit_id);
                mAudit.setRoute_id(road_id);
                mAudit.setUser_id(user_id);
                mAudit.setLatitude_close(String.valueOf(gpsTracker.getLatitude()));
                mAudit.setLongitude_close(String.valueOf(gpsTracker.getLongitude()));
                mAudit.setLatitude_open(String.valueOf(GlobalConstant.latitude_open));
                mAudit.setLongitude_open(String.valueOf(GlobalConstant.longitude_open));
                mAudit.setTime_open(GlobalConstant.inicio);
                mAudit.setTime_close(time_close);

                if(!AuditUtil.insertPollDetail(pollDetail)) return false;
                if(!AuditUtil.closeAuditRoadStore(mAudit)) return false;
               // if(!AuditUtil.closeAuditRoadAll(mAudit)) return false;
            }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();
                if(is_sino==1) {
                    Bundle argRuta = new Bundle();
                    argRuta.clear();
                    argRuta.putInt("company_id", company_id);
                    argRuta.putInt("idPDV",store_id);
                    argRuta.putString("tipo", tipo);
                    argRuta.putString("tipo", tipo);
                    argRuta.putString("cadenaruc", cadenaruc);
                    argRuta.putString("fechaRuta", fechaRuta);
                    argRuta.putInt("idAuditoria", audit_id);
                    argRuta.putInt("rout_id", rout_id);

                    Intent intent;
                    intent = new Intent(MyActivity, Exhibicion.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);

//                    Bundle argPDV = new Bundle();
//                    argPDV.putInt("store_id", Integer.valueOf(store_id));
//                    argPDV.putInt("road_id", Integer.valueOf(rout_id));
//                    Intent intent = new Intent(MyActivity, EncuestaRepLegalActivity.class);
//                    intent.putExtras(argPDV);
//                    startActivity(intent);

                    finish();

                } else if(is_sino==0){
                    finish();
                }
            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente",Toast.LENGTH_LONG).show();
            }

            hidepDialog();
        }
    }



}
