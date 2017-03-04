package dataservicios.com.ttauditbayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import dataservicios.com.ttauditbayer.Model.Pdv;
import dataservicios.com.ttauditbayer.Model.ProductScore;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.adapter.PdvsAdapter;
import dataservicios.com.ttauditbayer.app.AppController;
import dataservicios.com.ttauditbayer.util.AuditUtil;
import dataservicios.com.ttauditbayer.util.GPSTracker;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.SessionManager;


/**
 * Created by usuario on 06/01/2015.
 */
public class PuntosVenta extends Activity {

    // Log tag
    //private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LOG_TAG = PuntosVenta.class.getSimpleName();


    EditText pdvs1,pdvsAuditados1,porcentajeAvance1;
    private TextView tvPDVSdelDía;

    // Movies json url

    private ProgressDialog pDialog;
    private List<Pdv> pdvList = new ArrayList<Pdv>();
    private ListView listView;
    private PdvsAdapter adapter;
    private int IdRuta ;
    private String fechaRuta;
    private Button bt_MapaRuta,btMapaRutasAll;

    private Activity MyActivity =  (Activity) this;
    private JSONObject params;
    private SessionManager session;
    private String email_user, id_user, name_user;

    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntos_venta);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("PDVs");
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        db = new DatabaseHelper(getApplicationContext());
       // final Activity MyActivity = (Activity) this;

        pdvs1 = (EditText) findViewById(R.id.etPDVS);
        pdvsAuditados1 = (EditText) findViewById(R.id.etPDVSAuditados);
        porcentajeAvance1 = (EditText) findViewById(R.id.etPorcentajeAvance);
        tvPDVSdelDía = (TextView) findViewById(R.id.tvPDVSdelDia);

        Bundle bundle = getIntent().getExtras();
        IdRuta= bundle.getInt("idRuta");
        fechaRuta = bundle.getString("fechaRuta");

        tvPDVSdelDía.setText(fechaRuta);


        session = new SessionManager(MyActivity);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        email_user = user.get(SessionManager.KEY_EMAIL);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);
        //Añadiendo parametros para pasar al Json por metodo POST
        params = new JSONObject();
        try {
            params.put("id", IdRuta);
            params.put("company_id", GlobalConstant.company_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bt_MapaRuta = (Button) findViewById(R.id.btMapaRuta);

        //bt_MapaRuta = (Button) findViewById(R.id.btMapaRuta);
        btMapaRutasAll = (Button) findViewById(R.id.btMapaRutaAll);

        bt_MapaRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle argRuta = new Bundle();
                argRuta.putInt("id", IdRuta);
                //Intent intent = new Intent("com.dataservicios.ttauditcolgate.MAPARUTAS");
                Intent intent = new Intent(MyActivity,MapaRuta.class);
                intent.putExtras(argRuta);
                startActivity(intent);
            }
        });

        btMapaRutasAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle argRuta = new Bundle();
                argRuta.putInt("id", IdRuta);


                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.dataservicios.ttauditrutas", "com.dataservicios.ttauditrutas.MapaRuta"));
                    intent.putExtras(argRuta);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(MyActivity,"No se encuentra instalada la aplicación", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.dataservicios.ttauditrutas"));
                    startActivity(intent);
                }finally {

                }
            }
        });
        listView = (ListView) findViewById(R.id.list);
        adapter = new PdvsAdapter(this, pdvList);

        listView.setAdapter(adapter);
        // Click event for single list row
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Obteniendo fecha y hora
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                GlobalConstant.inicio = strDate;
                Log.i("FECHA", strDate);



                //Obteniendo Ubicacion
                GPSTracker gps = new GPSTracker(MyActivity);



                // Verificar si GPS esta habilitado
                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    GlobalConstant.latitude_open = latitude;
                    GlobalConstant.longitude_open = longitude;
                    //Toast toast = Toast.makeText(getApplicationContext(), "Lat: " + String.valueOf(latitude) + "Long: " + String.valueOf(longitude), Toast.LENGTH_SHORT);
                    //toast.show();
                }else{
                    // Indicar al Usuario que Habilite su GPS
                    gps.showSettingsAlert();
                }

                // selected item
                String selected =((TextView) view.findViewById(R.id.tvId)).getText().toString();

                //Insertando el en la tabla escores
                ProductScore ps = new ProductScore();
                //ps=db.getProductScoreForStore(Integer.valueOf(selected));
                db.deleteAllProductsScore();
                int totaRows = db.getProductScoreCountForStore(Integer.valueOf(selected));
                if(totaRows == 0) {
                    ps.setStoreId(Integer.valueOf(selected));
                    ps.setTotalProducts(0);
                    ps.setTotalExhibitions(0);
                    ps.setAwards(0);
                    db.createProductScore(ps);

                    List<ProductScore> listProductScore = new ArrayList<ProductScore>();
                    listProductScore = db.getAllProductsScore();
                    Log.d(LOG_TAG, String.valueOf(listProductScore));
                }




                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();
                Bundle argPDV = new Bundle();
                argPDV.putInt("idPDV", Integer.valueOf(selected) );
                argPDV.putInt("idRuta", Integer.valueOf(IdRuta) );
                argPDV.putString("fechaRuta",fechaRuta);
                //Intent intent = new Intent("com.dataservicios.ttauditcolgate.DETALLEPDV");
                Intent intent = new Intent(MyActivity,DetallePdv.class);
                intent.putExtras(argPDV);
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);

        new loadStores().execute();
    }

    class loadStores extends AsyncTask<Void, Integer, ArrayList<Pdv>> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            //tvCargando.setText("Cargando Product...");

            pDialog = new ProgressDialog(MyActivity);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Pdv> doInBackground(Void... params) {
            // TODO Auto-generated method stub

            ArrayList<Pdv> listPdv = new ArrayList<Pdv>();
            listPdv = AuditUtil.getLisStores(IdRuta, GlobalConstant.company_id);
            return listPdv;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(ArrayList<Pdv> pdvs) {
            // dismiss the dialog once product deleted


            if (pdvs.isEmpty()) {
//                Toast.makeText(TimelineActivity.this, getResources().getString(R.string.label_tweets_not_found),
//                        Toast.LENGTH_SHORT).show();

                //pdvList.addAll(pdvs);
            } else {
//                updateListView(tweets);
//                Toast.makeText(TimelineActivity.this, getResources().getString(R.string.label_tweets_downloaded),
//                        Toast.LENGTH_SHORT).show();


                float contadorStore = pdvs.size();
                float auditadosStore = 0;


                for (int i = 0; i < pdvs.size(); i++) {

                    if(pdvs.get(i).getStatus() == 1) {
                        auditadosStore ++;
                    }
                }


                pdvs1.setText(String.valueOf(contadorStore)) ;
                pdvsAuditados1.setText(String.valueOf(auditadosStore));

                float porcentajeAvance=(auditadosStore / contadorStore) *100;
                BigDecimal big = new BigDecimal(porcentajeAvance);
                big = big.setScale(2, RoundingMode.HALF_UP);
                porcentajeAvance1.setText( String.valueOf(big) + " % ");

                pdvList.addAll(pdvs);
                adapter.notifyDataSetChanged();
            }


            hidePDialog();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
//        Intent a = new Intent(this,PanelAdmin.class);
//        //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(a);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
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

