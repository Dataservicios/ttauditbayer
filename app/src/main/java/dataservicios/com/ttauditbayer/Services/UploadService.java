package dataservicios.com.ttauditbayer.Services;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dataservicios.com.ttauditbayer.AlbumStorageDirFactory;
import dataservicios.com.ttauditbayer.BaseAlbumDirFactory;
import dataservicios.com.ttauditbayer.FroyoAlbumDirFactory;
import dataservicios.com.ttauditbayer.R;
import dataservicios.com.ttauditbayer.util.AndroidMultiPartEntity;
import dataservicios.com.ttauditbayer.util.GlobalConstant;
import dataservicios.com.ttauditbayer.util.JSONParser;
//Subida de Archivos
public class UploadService extends IntentService {

    public static final int NOTIFICATION_ID=1;
    private int totalMessages = 0;
    long totalSize = 0;
    private NotificationManager mNotificationManager;
    private Notification notification;
    private Context context = this;

    ArrayList<String> names_file = new ArrayList<String>();
    private static final String url_upload_image = GlobalConstant.dominio + "/uploadImagesAuditPrueba";
    // private static final String url_insert_image = GlobalConstant.dominio + "/insertImagesPublicities";
    private String url_insert_image ;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    String store_id,product_id,poll_id,tipo;

    public UploadService(String name) {
        super(name);
    }
    public UploadService(){
        super("UploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       // notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        //Uri uri  = intent.getData();
        names_file =intent.getStringArrayListExtra("names_file");
        store_id=intent.getStringExtra("store_id");
        product_id=intent.getStringExtra("product_id");
        url_insert_image=intent.getStringExtra("url_insert_image");
        poll_id=intent.getStringExtra("poll_id");
        tipo=intent.getStringExtra("tipo");
        //Log.i("FOO", uri.toString());
        new ServerUpdate().execute(names_file);
    }
    class ServerUpdate extends AsyncTask<ArrayList<String>,String,Boolean> {
        //ProgressDialog pDialog;
        @Override
        protected Boolean doInBackground(ArrayList<String>... arg0) {
            Uri uri;
            int lastPercent = 0;
            //uri=arg0[0];
            //Subiendo la foto
            for (int i = 0; i < arg0[0].size(); i++) {
                String foto = arg0[0].get(i);

                if (uploadFoto(getAlbumDirTemp().getAbsolutePath() + "/" + foto) ) {
                    File file = new File(getAlbumDirTemp().getAbsolutePath() + "/" + foto);
                    file.delete();
                } else {
                    return false;
                }

            }
           return true;


        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // new loadInsert().execute(names_file);

            if (result){
                Toast.makeText(context,"Se guardó correctamente la imágen en el servidor",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"Error no se pudo guardar la imágen, consulta su backup de imagenes",Toast.LENGTH_SHORT).show();
                updateNotification();
            }
        }
    }



    //Metodo que escala la imágen
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


    private boolean uploadFoto(String imag){
        File file = new File(imag);
        Bitmap bbicon;
         HttpResponse resp;
        HttpClient httpClient = new DefaultHttpClient();
        Log.i("FOO", "Notification started");
        bbicon= BitmapFactory.decodeFile(String.valueOf(file));
        Bitmap scaledBitmap;
        if(Build.MODEL.equals("MotoG3")){
             scaledBitmap = rotateImage(scaleDown(bbicon, 450 , true),0);
        } else {
             scaledBitmap = rotateImage(scaleDown(bbicon, 450 , true),90);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);

        InputStream in = new ByteArrayInputStream(bos.toByteArray());
        // InputStream in = new ByteArrayInputStream(bos.toByteArray());
        //If you are stuck with HTTPClient 4.0, use InputStreamBody instead:
        //ContentBody foto = new InputStreamBody(in, "image/jpeg", file.getName());
        //ContentBody foto = new FileBody(file, "image/jpeg");
        //Use a ByteArrayBody instead (available since HTTPClient 4.1), despite its name it takes a file name, too:
        ContentBody foto = new ByteArrayBody(bos.toByteArray(), file.getName());
        HttpClient httpclient = new DefaultHttpClient();

        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(url_upload_image);
        //MultipartEntity mpEntity = new MultipartEntity();
        AndroidMultiPartEntity mpEntity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
            @Override
            public void transferred(long num) {
                //notification.contentView.setProgressBar(R.id.progressBar1, 100,(int) ((num / (float) totalSize) * 100), true);
               // notificationManager.notify(1, notification);
            }
        });




        httppost.setEntity(mpEntity);

        try {

            totalSize =  mpEntity.getContentLength();
            mpEntity.addPart("fotoUp", foto);
            mpEntity.addPart("archivo", new StringBody(String.valueOf(file.getName())));
            mpEntity.addPart("store_id", new StringBody(String.valueOf(store_id)));
            mpEntity.addPart("product_id", new StringBody(String.valueOf(product_id)));
            mpEntity.addPart("poll_id", new StringBody(String.valueOf(poll_id)));
            mpEntity.addPart("company_id", new StringBody(String.valueOf(GlobalConstant.company_id)));
            mpEntity.addPart("tipo", new StringBody(String.valueOf(tipo)));

            Log.i("FOO", "About to call httpClient.execute");
            resp = httpClient.execute(httppost);
           // resp.getEntity().getContent();


            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            } else {

                Log.i("FOO", "Screw up with http - " + resp.getStatusLine().getStatusCode());
                return  false ;
            }
            //resp.getEntity().consumeContent();
            //return true;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  false ;
        }
        return true;
    }



    private String getAlbunNameTemp(){
        return  getString(R.string.album_name_temp);
    }


    private File getAlbumDirTemp() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbunNameTemp());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d(getAlbunNameTemp(), "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }





    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        mBuilder.setContentTitle("Alicorp Bodegas");
        mBuilder.setContentText("Fotos truncadas Alicorp.");
        mBuilder.setTicker("Notificación de fotos truncadas Alert!");
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(R.drawable.ic_bayer_active);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mBuilder.setNumber(++totalMessages);
        // Intent resultIntent = new Intent(this, NotificationClass.class);
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // stackBuilder.addParentStack(NotificationClass.class);
        // stackBuilder.addNextIntent(resultIntent);
        // PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        // mBuilder.setContentIntent(resultPendingIntent);

        String pathFile = Environment.getExternalStorageDirectory().toString()+"/Pictures/" + getAlbunNameTemp()  ;
        File filePath = new File(pathFile);

        if (filePath.isDirectory()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri myUri = Uri.parse(String.valueOf(filePath));
            intent.setDataAndType(myUri, "resource/folder");
            //startActivity(intent);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            //stackBuilder.addParentStack(NotificationClass.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
