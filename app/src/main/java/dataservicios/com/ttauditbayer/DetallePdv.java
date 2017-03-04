package dataservicios.com.ttauditbayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dataservicios.com.ttauditbayer.AuditoriaBayer.BayerOpenClose;
import dataservicios.com.ttauditbayer.Model.Audit;
import dataservicios.com.ttauditbayer.Model.ProductScore;
import dataservicios.com.ttauditbayer.SQLite.DatabaseHelper;
import dataservicios.com.ttauditbayer.app.AppController;
import dataservicios.com.ttauditbayer.util.AuditUtil;
import dataservicios.com.ttauditbayer.util.GPSTracker;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParserX;
import dataservicios.com.ttauditbayer.util.SessionManager;

/**
 * Created by usuario on 14/01/2015.
 */
public class DetallePdv extends FragmentActivity {
   // private static final String URL_PDVS = "http://www.dataservicios.com/webservice/pdvs.php";
    private GoogleMap map;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LOG_TAG = DetallePdv.class.getSimpleName();

    private ViewGroup linearLayout;
    private Button bt;
    private LocationManager locManager;
    private LocationListener locListener;
    private double latitude ;
    private double longitude ;
    private double lat ;
    private double lon ;
    private Integer company_id;
    private Marker MarkerNow;
    private ProgressDialog pDialog;

    private JSONObject params,paramsCordenadas;
    private SessionManager session;
    private String email_user, id_user, name_user;
    private int idPDV, IdRuta, idCompany , audit_id;
    private String fechaRuta, tipo , cadenaruc;

    EditText pdvs1,pdvsAuditados1,porcentajeAvance1;
    TextView tvTienda,tvDireccion ,tvRepresentante , tvPDVSdelDia , tv_cadena_ruc, tv_tipo,  tv_CodClient, tv_Referencia;
    Button btGuardarLatLong, btCerrarAudit, btEditStore;
    Activity MyActivity = (Activity) this;

    private DatabaseHelper db;

    Audit mAudit ;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_pdv);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Detalle de PDV");
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        session = new SessionManager(MyActivity);
        map =((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        pdvs1 = (EditText)  findViewById(R.id.etPDVS);
        pdvsAuditados1 = (EditText)  findViewById(R.id.etPDVSAuditados);
        porcentajeAvance1 = (EditText)  findViewById(R.id.etPorcentajeAvance);
        tvTienda = (TextView)  findViewById(R.id.tvTienda);
        tvDireccion = (TextView)  findViewById(R.id.tvDireccion);
        tvRepresentante = (TextView)  findViewById(R.id.tvRepresentante);
        tv_CodClient = (TextView)  findViewById(R.id.tvCodCliente);
        tv_Referencia = (TextView)  findViewById(R.id.tvReferencia);
        tvPDVSdelDia = (TextView)  findViewById(R.id.tvPDVSdelDia);
        tv_cadena_ruc= (TextView)  findViewById(R.id.tvCadenaRuc);
        tv_tipo= (TextView)  findViewById(R.id.tvTipo);


//        tvLong = (TextView) findViewById(R.id.tvlogitud);
//        tvLat = (TextView) findViewById(R.id.tvLatitud);
        btGuardarLatLong = (Button) findViewById(R.id.btGuardarLatLong);
        btCerrarAudit = (Button) findViewById(R.id.btCerrarAuditoria);
        btEditStore = (Button) findViewById(R.id.btEditStore);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        email_user = user.get(SessionManager.KEY_EMAIL);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);

        db = new DatabaseHelper(getApplicationContext());

        gpsTracker = new GPSTracker(MyActivity);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);


        btGuardarLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paramsCordenadas = new JSONObject();
                try {
                    paramsCordenadas.put("latitud", lat);
                    paramsCordenadas.put("longitud", lon);
                    paramsCordenadas.put("id", idPDV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                guardarCoordenadas();
            }
        });

        btCerrarAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de cerrar la auditoría: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = sdf.format(c.getTime());
                        GlobalConstant.fin = strDate;


                        new loadPoll().execute();

                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                builder.show();
                builder.setCancelable(false);


            }
        });

        btEditStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Bundle argRuta = new Bundle();
                argRuta.clear();
                //argRuta.putInt("company_id", GlobalConstant.company_id);
                argRuta.putInt("store_id",idPDV);
                argRuta.putString("direccion", tvDireccion.getText().toString());
                argRuta.putString("referencia", tv_Referencia.getText().toString());
                argRuta.putString("storeName", tvTienda.getText().toString());

                intent = new Intent(MyActivity, EditStore.class);
                intent.putExtras(argRuta);
                startActivity(intent);
            }
        });

        linearLayout = (ViewGroup) findViewById(R.id.lyControles);

        params = new JSONObject();
        Bundle bundle = getIntent().getExtras();
        idPDV= bundle.getInt("idPDV");
        IdRuta= bundle.getInt("idRuta");
        fechaRuta= bundle.getString("fechaRuta");
        tvPDVSdelDia.setText(fechaRuta);
        company_id = GlobalConstant.company_id;


//        db.deletePresenseProductForStoreId(idPDV);
//        db.deletePresensePublicitytForStoreId(idPDV);
//        db.updatePublicityDesactive();

        try {
            params.put("id", idPDV);
            params.put("idRoute", IdRuta);
            params.put("company_id", company_id);

            //Enviando

            params.put("iduser", id_user);

            //params.put("id_pdv",idPDV);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cargaPdvs();
        //cargarAditorias();
        cargarAditorias();

    }



    private void mostrarPosicion(Location loc) {
        if(loc != null)
        {

            latitude=loc.getLatitude();
            longitude= loc.getLongitude();

            Log.i("Posicion: ", String.valueOf(latitude + " - " + String.valueOf(longitude) + " - "));
        }
        else
        {

            Log.i("SIN Data", "No hay datos para mostrar");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
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


    class loadPoll extends AsyncTask<Void , Integer , Boolean> {
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



            String time_close = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
            mAudit = new Audit();
            mAudit.setCompany_id(GlobalConstant.company_id);
            mAudit.setStore_id(idPDV);
            mAudit.setId(0);
            mAudit.setRoute_id(IdRuta);
            mAudit.setUser_id(Integer.valueOf(id_user));
            mAudit.setLatitude_close(String.valueOf(gpsTracker.getLatitude()));
            mAudit.setLongitude_close(String.valueOf(gpsTracker.getLongitude()));
            mAudit.setLatitude_open(String.valueOf(GlobalConstant.latitude_open));
            mAudit.setLongitude_open(String.valueOf(GlobalConstant.longitude_open));
            mAudit.setTime_open(GlobalConstant.inicio);
            mAudit.setTime_close(time_close);


            if(!AuditUtil.closeAuditRoadAll(mAudit)) return false;


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                Bundle argument = new Bundle();
                argument.clear();
                argument.putInt("store_id", idPDV);
                argument.putInt("road_id", IdRuta);

                ProductScore ps = new ProductScore();
                ps = db.getProductScoreForStore(idPDV);


                if(ps.getAwards() == 1) {
                    Intent intent = new Intent(MyActivity,Premiacion.class);
                    intent.putExtras(argument);
                    startActivity(intent);
                }

                finish();
            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
    }



    private void cargaPdvs(){
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , GlobalConstant.dominio + "/JsonRoadDetail" ,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");


                            if (success == 1) {
//
                                JSONArray ObjJson;
                                ObjJson = response.getJSONArray("roadsDetail");
                                // looping through All Products
                                if(ObjJson.length() > 0) {
                                    for (int i = 0; i < ObjJson.length(); i++) {
                                        try {
                                            JSONObject obj = ObjJson.getJSONObject(i);
                                            tvTienda.setText(obj.getString("fullname"));
                                            tvDireccion.setText(obj.getString("address"));
                                            tvRepresentante.setText(obj.getString("district"));
                                            tv_CodClient.setText(String.valueOf(idPDV));
                                            tv_Referencia.setText(obj.getString("urbanization"));
                                            tvRepresentante.setText(obj.getString("district"));
                                            tv_cadena_ruc.setText(obj.getString("cadenaRuc"));
                                            tv_tipo.setText(obj.getString("type"));

                                            //tvLat.setText(obj.getString("latitude"));
                                            //tvLong.setText(obj.getString("longitude"));
                                            latitude= Double.valueOf(obj.getString("latitude"))  ;
                                            longitude= Double.valueOf(obj.getString("longitude"));
                                            tipo = obj.getString("type") ;
                                            cadenaruc =obj.getString("cadenaRuc");
                                            map.clear();
                                            map.setMyLocationEnabled(true);
                                            MarkerNow = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Mi Ubicación")
                                                    //.snippet("Population: 4,137,400")
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_bayer)));
                                            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(15).build();
                                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                            // Establezco un listener para ver cuando cambio de posicion
                                            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                                                public void onMyLocationChange(Location pos) {
                                                    // TODO Auto-generated method stub
                                                    // Extraigo la Lat y Lon del Listener
                                                    lat = pos.getLatitude();
                                                    lon = pos.getLongitude();
                                                    // Muevo la camara a mi posicion
                                                    //CameraUpdate cam = CameraUpdateFactory.newLatLng(new LatLng(lat, lon));
                                                    //map.moveCamera(cam);
                                                    // Notifico con un mensaje al usuario de su Lat y Lon
                                                    //Toast.makeText(MyActivity,"Lat: " + lat + "\nLon: " + lon, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidepDialog();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }


    private void cargarAditorias(){
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST ,  GlobalConstant.dominio + "/JsonAuditsForStore" ,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");
                            idCompany =response.getInt("company");
                            if (success == 1) {
                                JSONArray agentesObjJson;
                                //db.deleteAllAudits();
                                agentesObjJson = response.getJSONArray("audits");
                                // looping through All Products
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable
                                    String idAuditoria = obj.getString("id");
                                    String auditoria = obj.getString("fullname");

                                    //int totalAudit = db.getCountAuditForId(Integer.valueOf(idAuditoria));
                                    int totalAudit = db.getCountAuditForIdForStoreId(Integer.valueOf(idAuditoria), Integer.valueOf(idPDV));
                                    List<Audit> audits1 = new ArrayList<Audit>();
                                    audits1=db.getAllAudits();
                                    if(totalAudit==0) {
                                        Audit audit = new Audit();
                                        audit.setId(Integer.valueOf(idAuditoria));
                                        audit.setName(auditoria);
                                        audit.setStore_id(idPDV);
                                        audit.setScore(0);
                                        db.createAudit(audit);
                                    }


                                    audits1=db.getAllAudits();

                                    int status = obj.getInt("state");
                                    bt = new Button(MyActivity);
                                    LinearLayout ly = new LinearLayout(MyActivity);
                                    ly.setOrientation(LinearLayout.VERTICAL);
                                    ly.setId(i+'_');
                                    LayoutParams params = new LayoutParams(
                                            LayoutParams.FILL_PARENT,
                                            LayoutParams.FILL_PARENT
                                    );
                                    params.setMargins(0, 10, 0, 10);
                                    ly.setLayoutParams(params);
                                    bt.setBackgroundColor(getResources().getColor(R.color.color_base));
                                    bt.setTextColor(getResources().getColor(R.color.counter_text_bg));
                                    bt.setText(auditoria);


                                    if(status==1) {
                                        Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_check_on);
                                        img.setBounds( 0, 0, 60, 60 );  // set the image size
                                        bt.setCompoundDrawables( img, null, null, null );
                                        bt.setBackgroundColor(getResources().getColor(R.color.color_bottom_buttom_pressed));
                                        bt.setTextColor(getResources().getColor(R.color.color_base));
                                        bt.setEnabled(false);
                                    }  else {
                                        Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_check_off);
                                        img.setBounds( 0, 0, 60, 60 );  // set the image size
                                        bt.setCompoundDrawables( img, null, null, null );
                                    }
                                    if(GlobalConstant.global_close_audit==1){

                                        bt.setBackgroundColor(getResources().getColor(R.color.color_bottom_buttom_pressed));
                                        bt.setTextColor(getResources().getColor(R.color.color_base));
                                        bt.setEnabled(false);
                                    }
                                    //bt.setBackground();
                                    bt.setId(Integer.valueOf(idAuditoria));
                                    bt.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                                    bt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Toast.makeText(getActivity(), j  , Toast.LENGTH_LONG).show();
                                            Button button1 = (Button) v;
                                            String texto = button1.getText().toString();
                                            //Toast toast=Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT);
                                            Toast toast;
                                            toast = Toast.makeText(MyActivity, texto + ":" + button1.getId(), Toast.LENGTH_LONG);
                                            toast.show();
                                            //int idBoton = Integer.valueOf(idAuditoria);
                                            Intent intent;
                                            int idAuditoria = button1.getId();

                                            Bundle argRuta = new Bundle();
                                            argRuta.clear();
                                            argRuta.putInt("company_id", idCompany);
                                            argRuta.putInt("idPDV",idPDV);
                                            argRuta.putString("tipo", tipo);
                                            argRuta.putInt("idRuta", IdRuta);
                                            argRuta.putString("fechaRuta", fechaRuta);
                                            argRuta.putString("cadenaruc",cadenaruc);
                                            argRuta.putInt("idAuditoria",idAuditoria);


                                            switch (idAuditoria) {
                                                case 14:
                                                    //db.deleteAllPresenseProduct();
                                                   // intent = new Intent("com.dataservicios.ttauditcolgate.PRESENCIAPRODUCTO");
                                                   // intent = new Intent(MyActivity, Product.class);
                                                    intent = new Intent(MyActivity, BayerOpenClose.class);
                                                    intent.putExtras(argRuta);
                                                    startActivity(intent);

                                                    break;
                                                case 3:
                                                    intent = new Intent("com.dataservicios.ttauditcolgate.PRESENCIAMATERIAL");
                                                    intent.putExtras(argRuta);
                                                    startActivity(intent);

                                                    break;
                                                case 13:
                                                    intent = new Intent("com.dataservicios.ttauditcolgate.FACTURACION");
                                                    intent.putExtras(argRuta);
                                                    startActivity(intent);
                                                    break;

                                            }
                                        }
                                    });
                                    ly.addView(bt);
                                    linearLayout.addView(ly);
                                }
                                GlobalConstant.global_close_audit=0;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidepDialog();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }


    private void guardarCoordenadas(){

        showpDialog();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , GlobalConstant.dominio+"/updatePositionStore" ,paramsCordenadas,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");
                            if (success == 1) {
//
                                map.clear();
                                MarkerNow = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Mi Ubicación")
                                        //.snippet("Population: 4,137,400")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map)));
//        Mostrando información del Market ni bien carga el mapa
                                // MarkerNow.showInfoWindow();
//        Ajuste de la cámara en el mayor nivel de zoom es posible que incluya los límites
                                map.setMyLocationEnabled(true);
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(15).build();
                                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidepDialog();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }


    private boolean insertCloseAudit(int store_id, int road_id, int user_id, double lat , double lon ) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();


            params.put("latitud_close", String.valueOf(lat));
            params.put("longitud_close", String.valueOf(lon));
            params.put("latitud_open", String.valueOf(GlobalConstant.latitude_open));
            params.put("longitud_open",  String.valueOf(GlobalConstant.longitude_open));
            params.put("tiempo_inicio", String.valueOf( GlobalConstant.inicio));
            params.put("tiempo_fin", String.valueOf( GlobalConstant.fin));
            params.put("tduser", String.valueOf(user_id));
            params.put("id", String.valueOf(store_id));
            params.put("idruta", String.valueOf(road_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/insertaTiempo" ,"POST", params);
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


    private void insertaTiemporAuditoriaXX(JSONObject parametros) {
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , GlobalConstant.dominio + "/insertaTiempo" ,parametros,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");
                            if (success == 1) {
                                Log.d("DATAAAA", response.toString());
                                Toast.makeText(MyActivity, "Se ", Toast.LENGTH_LONG).show();
                                Bundle argument = new Bundle();
                                argument.clear();
                                argument.putInt("store_id", idPDV);
                                argument.putInt("road_id", IdRuta);

                                ProductScore ps = new ProductScore();
                                ps = db.getProductScoreForStore(idPDV);


                                if(ps.getAwards() == 1) {
                                    Intent intent = new Intent(MyActivity,Premiacion.class);
                                    intent.putExtras(argument);
                                    startActivity(intent);
                                }

                                finish();

                            } else {
                                Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidepDialog();
                    }
                }
        );


        AppController.getInstance().addToRequestQueue(jsObjRequest);


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
