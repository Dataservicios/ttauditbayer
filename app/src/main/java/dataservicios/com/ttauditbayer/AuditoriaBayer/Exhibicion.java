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
import dataservicios.com.ttauditbayer.Model.PollDetail;
import dataservicios.com.ttauditbayer.Model.ProductScore;
import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.AuditUtil;
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
    int  is_sino=0;

    int totalValores ;

    int vA=0,vB=0,vC=0,vD=0,vE=0,vF=0,vG=0,vH=0,vI=0,vJ=0,vK=0,vL=0;
    String oA="",oB="",oC="",oD="",oE="",oF="",oG="" ,oH="" ,oI="" , oJ="", oK="", oL="";
    String opt1="";

    private CheckBox[] checkBoxArray;

    private PollDetail pollDetail;

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

        checkBoxArray = new CheckBox[] {
                (CheckBox) findViewById(R.id.cbA),
                (CheckBox) findViewById(R.id.cbB),
                (CheckBox) findViewById(R.id.cbC),
                (CheckBox) findViewById(R.id.cbD),
                (CheckBox) findViewById(R.id.cbE),
                (CheckBox) findViewById(R.id.cbF),
                (CheckBox) findViewById(R.id.cbG),
                (CheckBox) findViewById(R.id.cbH),
        };






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

        //poll_id = 557 ; //¿Tiene exhibición Bayer?
        poll_id = GlobalConstant.poll_id[1] ; //¿Tiene exhibición Bayer?


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

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
                    ViewGroup.LayoutParams params = lySi.getLayoutParams();
                    params.height = 1400;
                    lySi.setLayoutParams(new LinearLayout.LayoutParams(params));
                    for (int x = 0; x < checkBoxArray.length; x++) {
                       checkBoxArray[x].setChecked(false) ;
                    }

                } else if (rbNo.getId()==checkedId) {

                    ViewGroup.LayoutParams params = lySi.getLayoutParams();
                    params.height = 2;
                    lySi.setLayoutParams(new LinearLayout.LayoutParams(params));

                    for (int x = 0; x < checkBoxArray.length; x++) {
                        checkBoxArray[x].setChecked(false) ;
                    }

                }


            }
        });




        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opt1="";
                totalValores=0;
                totalOption="";
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
                        is_sino=1;
                        comentario = String.valueOf(et_comentario.getText());

                    } else if (id == rbNo.getId()) {
                        result = 0;
                        is_sino=0;

                        //comentario = String.valueOf(et_comentarioNo.getText());
                    }
                }


                if (checkBoxArray[0].isChecked()) {
                    vA = 1;
                    oA = String.valueOf(poll_id) + "a";
                }
                if (checkBoxArray[1].isChecked()) {
                    vB = 1;
                    oB = String.valueOf(poll_id) + "b";
                }
                if (checkBoxArray[2].isChecked()) {
                    vC = 1;
                    oC = String.valueOf(poll_id) + "c";
                }
                if (checkBoxArray[3].isChecked()) {
                    vD = 1;
                    oD = String.valueOf(poll_id) + "d";
                }
                if (checkBoxArray[4].isChecked()) {
                    vE = 1;
                    oE = String.valueOf(poll_id) + "e";
                }
                if (checkBoxArray[5].isChecked()) {
                    vF = 1;
                    oF = String.valueOf(poll_id) + "f";
                }
                if (checkBoxArray[6].isChecked()) {
                    vG = 1;
                    oG = String.valueOf(poll_id) + "g";
                }
                //Se repite la "k" por que es otros para SI
                if (checkBoxArray[7].isChecked()) {
                    vH = 1;
                    oH = String.valueOf(poll_id) + "h";
                }


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

                for (int x = 0; x < checkBoxArray.length; x++) {
                    if(checkBoxArray[x].isChecked())  {
                        opt1 = opt1 + poll_id.toString() + checkBoxArray[x].getTag() + "|";

                    }
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
//                        if(is_sino==1) {
//
//                            ps = db.getProductScoreForStore(store_id);
//                            int total_exhibidores = 0 ;
//                            total_exhibidores = 1  + ps.getTotalExhibitions();
//                            db.updateProductScoreForTotalExhibitions(store_id,total_exhibidores);
//                        }

                        //***************************** end ***********************


                       // comentario = String.valueOf(etComent.getText()) ;

                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(1);
                        pollDetail.setOptions(1);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(1);
                        pollDetail.setComment(0);
                        pollDetail.setResult(is_sino);
                        pollDetail.setLimite("0");
                        pollDetail.setComentario("");
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
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
           // if(!InsertAuditPollsOtions(poll_id,0,result,String.valueOf(comentario))) return false;
            if(!AuditUtil.insertPollDetail(pollDetail)) return false;


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
                        if(is_sino==1) {

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
