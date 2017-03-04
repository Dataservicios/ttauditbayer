package dataservicios.com.ttauditbayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.util.GlobalConstant;

/**
 * Created by Jaime on 12/04/2016.
 */
public class PremiacionFoto extends Activity {

    private Activity MyActivity;
    private Button bt_guardar, bt_photo;

    private Integer store_id, road_id;
    private DatabaseHelper db;
    private ProgressDialog pDialog;

    private int poll_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.premiacion_foto);
        getActionBar().setTitle("Foto de Premiación");
        MyActivity = (Activity) this;

        bt_guardar = (Button) findViewById(R.id.btGuardar);
        bt_photo = (Button) findViewById(R.id.btPhoto);

        // bt_registrar = (Button) findViewById(R.id.btRegistrar);

        //tv_auditScore = (TextView) findViewById(R.id.tvAuditScore);

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");
        road_id = bundle.getInt("road_id");
        poll_id = bundle.getInt("poll_id");

        db =  new DatabaseHelper(MyActivity);


        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);



        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

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
