package dataservicios.com.ttauditbayer.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;

import dataservicios.com.ttauditbayer.Model.PollDetail;
import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.AuditUtil;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.SessionManager;

public class EncuestaHorarioActivity extends Activity {

    private Activity myActivity = this ;
    private static final String LOG_TAG = EncuestaHorarioActivity.class.getSimpleName();
    private SessionManager session;
    private Button bt_guardar;
    private LinearLayout lyContent;
    private Integer user_id, company_id,store_id,road_id, poll_id;
    private DatabaseHelper db;
    private ProgressDialog pDialog;
    private String opt1="";

    private RadioGroup rgOptions;
    private RadioButton[] radioButton1Array;
    //private CheckBox[] checkBoxArray;

    private PollDetail pollDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta_horario);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Encuesta");



        bt_guardar = (Button) findViewById(R.id.btGuardar);

        Bundle bundle = getIntent().getExtras();

        store_id = bundle.getInt("store_id");
        road_id = bundle.getInt("road_id");


        company_id = GlobalConstant.company_id;
        poll_id = GlobalConstant.poll_id[9];


        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(myActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        createRadioButtonOptions();

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                opt1 = "" ;
                int aelectedOptions = 0;
                for (int x = 0; x < radioButton1Array.length; x++) {
                    if(radioButton1Array[x].isChecked()) aelectedOptions ++;
                }

                if (aelectedOptions == 0){
                    Toast.makeText(myActivity,"Seleccionar una opción " , Toast.LENGTH_LONG).show();
                    return;
                } else{
                    for (int x = 0; x < radioButton1Array.length; x++) {
                        if(radioButton1Array[x].isChecked())  {
                            opt1 = opt1 + poll_id.toString() + radioButton1Array[x].getTag() + "|";
                        }
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(0);
                        pollDetail.setOptions(1);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(0);
                        pollDetail.setComment(0);
                        pollDetail.setResult(0);
                        pollDetail.setLimite("0");
                        pollDetail.setComentario("");
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setCommentOptions(0);
                        pollDetail.setSelectdOptions(opt1);
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
            //PollDetail mPD = params[0] ;
            if(!AuditUtil.insertPollDetail(pollDetail)) return false;

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                Bundle argPDV = new Bundle();
                argPDV.putInt("store_id", Integer.valueOf(store_id));
                argPDV.putInt("road_id", Integer.valueOf(road_id));
                Intent intent = new Intent(myActivity, EncuestaRecibirInfoActivity.class);
                intent.putExtras(argPDV);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(myActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(myActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(myActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void createRadioButtonOptions(){

        lyContent = (LinearLayout) findViewById(R.id.lyContent);
        rgOptions = (RadioGroup) findViewById(R.id.rgOptions);
        rgOptions.setOrientation(LinearLayout.VERTICAL);

        radioButton1Array = new RadioButton[3];

        radioButton1Array[0] = new RadioButton(this);
        radioButton1Array[0].setText("9:00 am - 12:00 pm (Turno mañana)");
        radioButton1Array[0].setTag("a");
        //lyContent.addView(radioButton1Array[0]);
        rgOptions.addView(radioButton1Array[0]);

        radioButton1Array[1] = new RadioButton(this);
        radioButton1Array[1].setText("3:00 pm - 6:00 pm (Turno Tarde)");
        radioButton1Array[1].setTag("b");
        rgOptions.addView(radioButton1Array[1]);

        radioButton1Array[2] = new RadioButton(this);
        radioButton1Array[2].setText("6:00 pm - 9:00 pm (Turno Noche)");
        radioButton1Array[2].setTag("c");
        rgOptions.addView(radioButton1Array[2]);


    }

}

