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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataservicios.com.ttauditbayer.AndroidCustomGalleryActivity;
import dataservicios.com.ttauditbayer.MainActivity;
import dataservicios.com.ttauditbayer.Model.ProductScore;
import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParser;
import dataservicios.com.ttauditbayer.util.JSONParserX;
import dataservicios.com.ttauditbayer.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * Created by Jaime on 24/02/2016.
 */
public class Exhibicion extends Activity{

    private Activity MyActivity = this ;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SessionManager session;
    private Button bt_photo, bt_guardar;
    private TextView tv_Pregunta, tv_comentario, tv_comentarioNo;
    private String tipo,cadenaruc,fechaRuta, comentario="";
    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id;

    private DatabaseHelper db;
    private ProgressDialog pDialog;


    private RadioGroup rgTipo;
    private RadioButton rbSi,rbNo;

    private EditText et_comentario, et_comentarioNo;
    private CheckBox cbA,cbB,cbC,cbD,cbE,cbF,cbG,cbH,cbI,cbJ,cbK,cbL;

    private LinearLayout lySi , lyNo;

    private  int result;

    String totalOption="";
    int  is_exhibidor=0;

    int totalValores ;
    int vA=0,vB=0,vC=0,vD=0,vE=0,vF=0,vG=0,vH=0,vI=0,vJ=0,vK=0,vL=0;
    String oA="",oB="",oC="",oD="",oE="",oF="",oG="" ,oH="" ,oI="" , oJ="", oK="", oL="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exhibicion);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");

        bt_guardar = (Button) findViewById(R.id.btGuardar);
        bt_photo = (Button) findViewById(R.id.btPhoto);
        et_comentario = (EditText) findViewById(R.id.etComentario);
        tv_comentario = (TextView) findViewById(R.id.tvComentario);

       // et_comentarioNo = (EditText) findViewById(R.id.etComentarioNo);
      //  tv_comentarioNo = (TextView) findViewById(R.id.tvComentarioNo);

        //DEl si
        cbA=(CheckBox) findViewById(R.id.cbA);
        cbB=(CheckBox) findViewById(R.id.cbB);
        cbC=(CheckBox) findViewById(R.id.cbC);
        cbD=(CheckBox) findViewById(R.id.cbD);
        cbE=(CheckBox) findViewById(R.id.cbE);
        cbF=(CheckBox) findViewById(R.id.cbF);
        cbG=(CheckBox) findViewById(R.id.cbG);
        cbH=(CheckBox) findViewById(R.id.cbH);


        //DEL NO
//        cbI=(CheckBox) findViewById(R.id.cbI);
//        cbJ=(CheckBox) findViewById(R.id.cbJ);
//        cbK=(CheckBox) findViewById(R.id.cbK);
//        cbL=(CheckBox) findViewById(R.id.cbL);

        lySi=(LinearLayout) findViewById(R.id.lyChkSi);
       // lyNo=(LinearLayout) findViewById(R.id.lyChkNo);
        //cbG=(CheckBox) findViewById(R.id.cbG);
        rgTipo=(RadioGroup) findViewById(R.id.rgTipo);
        rbSi=(RadioButton) findViewById(R.id.rbSi);
        rbNo=(RadioButton) findViewById(R.id.rbNo);


        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("idPDV");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("idAuditoria");
        product_id =bundle.getInt("product_id");
        poll_id = 433 ;


        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        db = new DatabaseHelper(getApplicationContext());

        rgTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);
                if (rbSi.getId()==checkedId){
                    ViewGroup.LayoutParams paramsSi = lySi.getLayoutParams();
                    paramsSi.height = 1400;
                    lySi.setLayoutParams(new LinearLayout.LayoutParams(paramsSi));
//                    ViewGroup.LayoutParams paramsNo = lyNo.getLayoutParams();
//                    paramsNo.height = 2;
//                    lyNo.setLayoutParams(new LinearLayout.LayoutParams(paramsNo));
                    cbA.setChecked(false);
                    cbB.setChecked(false);
                    cbC.setChecked(false);
                    cbD.setChecked(false);
                    cbE.setChecked(false);
                    cbF.setChecked(false);
                    cbG.setChecked(false);
                    cbH.setChecked(false);
//                    cbI.setChecked(false);
//                    cbJ.setChecked(false);
//                    cbK.setChecked(false);
//                    cbL.setChecked(false);

//                    tv_comentario.setVisibility(View.INVISIBLE);
//                    et_comentario.setEnabled(false);
//                    et_comentario.setVisibility(View.INVISIBLE);


//                    tv_comentarioNo.setVisibility(View.INVISIBLE);
//                    et_comentarioNo.setEnabled(false);
//                    et_comentarioNo.setVisibility(View.INVISIBLE);


                } else if (rbNo.getId()==checkedId) {
                    //lySi.setVisibility(View.INVISIBLE);
                    //  lyNo.setVisibility(View.VISIBLE);

//                    if(tipo.equals("HORIZONTAL")) {
//                        ViewGroup.LayoutParams paramsNo = lyNo.getLayoutParams();
//                        paramsNo.height = 760;
//                        lyNo.setLayoutParams(new LinearLayout.LayoutParams(paramsNo));
//
//                    }

                    ViewGroup.LayoutParams params = lySi.getLayoutParams();
                    params.height = 2;
                    lySi.setLayoutParams(new LinearLayout.LayoutParams(params));

                    cbA.setChecked(false);
                    cbB.setChecked(false);
                    cbC.setChecked(false);
                    cbD.setChecked(false);
                    cbE.setChecked(false);
                    cbF.setChecked(false);
                    cbG.setChecked(false);
                    cbH.setChecked(false);

//                    cbI.setChecked(false);
//                    cbJ.setChecked(false);
//                    cbK.setChecked(false);
//                    cbL.setChecked(false);

//                    tv_comentario.setVisibility(View.INVISIBLE);
//                    et_comentario.setEnabled(false);
//                    et_comentario.setVisibility(View.INVISIBLE);

//                    tv_comentarioNo.setVisibility(View.INVISIBLE);
//                    et_comentarioNo.setEnabled(false);
//                    et_comentarioNo.setVisibility(View.INVISIBLE);

                }
                //textViewChoice.setText("You Selected "+rb.getText());
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();

            }
        });

//        cbH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if ( isChecked )
//                {
//                    // perform logic
//                    tv_comentario.setVisibility(View.VISIBLE);
//                    et_comentario.setEnabled(true);
//                    et_comentario.setVisibility(View.VISIBLE);
//
//                } else{
//                    tv_comentario.setVisibility(View.INVISIBLE);
//                    et_comentario.setEnabled(false);
//                    et_comentario.setVisibility(View.INVISIBLE);
//                }
//
//            }
//        });

//        cbL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if ( isChecked )
//                {
//                    // perform logic
//                    tv_comentarioNo.setVisibility(View.VISIBLE);
//                    et_comentarioNo.setEnabled(true);
//                    et_comentarioNo.setVisibility(View.VISIBLE);
//
//                } else{
//                    tv_comentarioNo.setVisibility(View.INVISIBLE);
//                    et_comentarioNo.setEnabled(false);
//                    et_comentarioNo.setVisibility(View.INVISIBLE);
//                }
//
//            }
//        });





        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long id = rgTipo.getCheckedRadioButtonId();
                if (id == -1) {
                    //no item selected
                    //valor ="";
                    Toast toast;
                    toast = Toast.makeText(MyActivity, "Debe seleccionar una opción", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                } else {
                    if (id == rbSi.getId()) {
                        //Do something with the button
                        result = 1;
                        is_exhibidor=1;
                        comentario = String.valueOf(et_comentario.getText());

                    } else if (id == rbNo.getId()) {
                        result = 0;
                        is_exhibidor=0;

                        //comentario = String.valueOf(et_comentarioNo.getText());
                    }
                }




                if (cbA.isChecked()) {
                    vA = 1;
                    oA = String.valueOf(poll_id) + "a";
                }
                if (cbB.isChecked()) {
                    vB = 1;
                    oB = String.valueOf(poll_id) + "b";
                }
                if (cbC.isChecked()) {
                    vC = 1;
                    oC = String.valueOf(poll_id) + "c";
                }
                if (cbD.isChecked()) {
                    vD = 1;
                    oD = String.valueOf(poll_id) + "d";
                }
                if (cbE.isChecked()) {
                    vE = 1;
                    oE = String.valueOf(poll_id) + "e";
                }
                if (cbF.isChecked()) {
                    vF = 1;
                    oF = String.valueOf(poll_id) + "f";
                }
                if (cbG.isChecked()) {
                    vG = 1;
                    oG = String.valueOf(poll_id) + "g";
                }

                //Se repite la "k" por que es otros para SI
                if (cbH.isChecked()) {
                    vH = 1;
                    oH = String.valueOf(poll_id) + "h";
                }



//                if (cbI.isChecked()) {
//                    vI = 1;
//                    oI = String.valueOf(poll_id) + "h";
//                }
//                if (cbJ.isChecked()) {
//                    vJ = 1;
//                    oJ = String.valueOf(poll_id) + "i";
//                }
//
//                if (cbK.isChecked()) {
//                    vK = 1;
//                    oK = String.valueOf(poll_id) + "j";
//                }
//                //Se repite la "k" por que es otros para NO
//                if (cbL.isChecked()) {
//                    vL = 1;
//                    oL = String.valueOf(poll_id) + "k";
//                }


               // totalValores = vA + vB + vC + vD + vE + vF + vG + vH + vI + vJ + vK + vL;
                totalValores = vA + vB + vC + vD + vE + vF + vG + vH ;

                //totalOption = oA + "|" + oB + "|" + oC + "|" + oD + "|" + oE + "|" + oF + "|" + oG + "|" + oH + "|" + oI + "|" + oJ + "|" + oK + "|" + oL  ;
                totalOption = oA + "|" + oB + "|" + oC + "|" + oD + "|" + oE + "|" + oF + "|" + oG + "|" + oH ;

                if(tipo.equals("CADENA") && result==0) {
                    totalValores=1;
                }

                if(totalValores==0 && result==1){
                    Toast toast;
                    toast = Toast.makeText(MyActivity, "Debe marcar almenos una opción", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }



                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //***************************** inicio modificado ***********************

//                        ProductScore ps = new ProductScore();
//                        if(is_exhibidor==1) {
//
//                            ps = db.getProductScoreForStore(store_id);
//                            int total_exhibidores = 0 ;
//                            total_exhibidores = 1  + ps.getTotalExhibitions();
//                            db.updateProductScoreForTotalExhibitions(store_id,total_exhibidores);
//                        }

                        //***************************** end ***********************

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


        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
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



    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void takePhoto() {

        Intent i = new Intent( MyActivity, AndroidCustomGalleryActivity.class);
        Bundle bolsa = new Bundle();

        bolsa.putString("store_id",String.valueOf(store_id));
        bolsa.putString("product_id",String.valueOf(""));
        bolsa.putString("poll_id",String.valueOf(poll_id));
        bolsa.putString("url_insert_image", GlobalConstant.dominio + "/insertImagesProductPoll");
        bolsa.putString("tipo", "1");
        i.putExtras(bolsa);
        startActivity(i);


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
            if(!InsertAuditPollsOtions(poll_id,0,result,String.valueOf(comentario))) return false;

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

//***************************** inicio modificado ***********************

                        ProductScore ps = new ProductScore();
                        if(is_exhibidor==1) {

                            ps = db.getProductScoreForStore(store_id);
                            int total_exhibidores = 0 ;
                            total_exhibidores = 1  + ps.getTotalExhibitions();
                            db.updateProductScoreForTotalExhibitions(store_id,total_exhibidores);
                        }

 //***************************** end ***********************




                Bundle argRuta = new Bundle();
                argRuta.clear();
                argRuta.putInt("company_id", company_id);
                argRuta.putInt("idPDV",store_id);
                argRuta.putString("tipo", tipo);
                argRuta.putString("cadenaruc", cadenaruc);
                argRuta.putInt("idRuta", rout_id );
                argRuta.putString("fechaRuta", fechaRuta);
                argRuta.putInt("idAuditoria", audit_id);

                    Intent intent;
                    //intent = new Intent(MyActivity, Product.class);
                    intent = new Intent(MyActivity, Product.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                    finish();

            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente",Toast.LENGTH_LONG).show();
            }


        }
    }

    private boolean InsertAuditPollsOtions(int poll_id,  int status,int result,String comentario) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();
            params.put("poll_id", String.valueOf(poll_id));
            params.put("poll_id",String.valueOf(poll_id) );
            params.put("store_id", String.valueOf(store_id));
            params.put("product_id", "0");
            params.put("options", "1");
            params.put("limits", "0");
            if (result == 1 ) {
                params.put("media", "1");
            } else if( result== 0 ){
                params.put("media", "0");
            }
            params.put("coment", "1");
            params.put("coment_options", "0");
            // params.put("coment_options", "0"));
            params.put("comentario_options", "");
            params.put("limite", "");
            params.put("opcion", totalOption);
            params.put("sino", "1");
            params.put("product", "0");
            params.put("comentario", String.valueOf(comentario));
            params.put("result", String.valueOf(result));
            params.put("idCompany", String.valueOf(GlobalConstant.company_id));
            params.put("idRuta", String.valueOf(rout_id));
            params.put("idAuditoria", String.valueOf(audit_id));
            params.put("product_id", "");
            params.put("status", String.valueOf(status));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPolls" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditBayer" ,"POST", params);
            // check your log for json response
            Log.d(LOG_TAG, json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro, por duplicado u otro problema");
                    // return json.getString("message");
                   // return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, e.toString());
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
        Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
}
