package dataservicios.com.ttauditbayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dataservicios.com.ttauditbayer.Services.UploadService;
import dataservicios.com.ttauditbayer.adapter.ImageAdapter;
import dataservicios.com.ttauditbayer.util.GlobalConstant;

//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.ContentBody;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.InputStreamBody;

/**
 * Created by user on 06/02/2015.
 */
public class AndroidCustomGalleryActivity extends Activity {


    private static final int TAKE_PICTURE = 1;
    private String mCurrentPhotoPath;
    private static final String JPEG_FILE_PREFIX = "_Bayer_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";




    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private ImageAdapter imageAdapter;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    ArrayList<String> names_file = new ArrayList<String>();
    Activity MyActivity ;
    String store_id,product_id,tipo,url_insert_image,poll_id ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        MyActivity = (Activity) this;

        Bundle bundle = getIntent().getExtras();



        store_id = bundle.getString("store_id");
        product_id = bundle.getString("product_id");
        poll_id = bundle.getString("poll_id");
        url_insert_image = bundle.getString("url_insert_image");
        tipo = bundle.getString("tipo");


        getFromSdcard();

        final GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter(MyActivity,f);
        imagegrid.setAdapter(imageAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }


        Button btn_photo = (Button)findViewById(R.id.take_photo);
        Button btn_upload = (Button)findViewById(R.id.save_images);
        // Register the onClick listener with the implementation above
        btn_photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                // create intent with ACTION_IMAGE_CAPTURE action
               // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                Bundle bundle = getIntent().getExtras();
                String idPDV = bundle.getString("idPDV");

                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = String.format("%06d", Integer.parseInt(store_id)) + "_" + GlobalConstant.company_id  +JPEG_FILE_PREFIX + timeStamp;
                File albumF = getAlbumDir();
                // to save picture remove comment
                File file = new File(albumF,imageFileName+JPEG_FILE_SUFFIX);

                Uri photoPath = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);

                mCurrentPhotoPath = getAlbumDir().getAbsolutePath();

                // start camera activity
                startActivityForResult(intent, TAKE_PICTURE);

            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

//                Bundle bundle = getIntent().getExtras();
//                String idPDV = bundle.getString("idPDV");
//                String idPoll = bundle.getString("idPoll");
//                String tipo = bundle.getString("tipo");


                File file= new File(Environment.getExternalStorageDirectory().toString()+"/Pictures/" + getAlbumName());
                if (file.isDirectory()) {

                    int position =0;
                    int contador = 0;
                    int holder_counter=0;
                    names_file.clear();

                    if (listFile.length>0){
                        //for (int i = 0; i < listFile.length; i++){
                        int total = imageAdapter.getCount();
                        int count = imagegrid.getAdapter().getCount();

                        for (int i = 0; i < count; i++) {

                            //LinearLayout itemLayout = (LinearLayout)imagegrid.getChildAt(i); // Find by under LinearLayout
                            RelativeLayout itemLayout = (RelativeLayout)imagegrid.getChildAt(i); // Find by under LinearLayout

                            CheckBox checkbox = (CheckBox)itemLayout.findViewById(R.id.itemCheckBox);
                            if(checkbox.isChecked())
                            {
                                contador ++;
                                // Log.d("Item "+String.valueOf(i), checkbox.getTag().toString());
                                //Toast.makeText(MyActivity,checkbox.getTag().toString() ,Toast.LENGTH_LONG).show();

                                if (  listFile[i].getName().substring(0,6).equals(String.format("%06d", Integer.parseInt(store_id)) )) {
                                    String name = listFile[i].getName();
                                    names_file.add(name);
                                    //  holder_counter++;

                                    try {
                                        copyFile(getAlbumDir() + "/" + listFile[i].getName(), getAlbumDirTemp() + "/" + listFile[i].getName());
                                        copyFile(getAlbumDir() + "/" + listFile[i].getName(), getAlbumDirBackup() + "/" + listFile[i].getName());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                listFile[i].delete();

                            }
                        }

                        if(contador > 0){

                           // Toast.makeText(MyActivity, "Seleccionó " + String.valueOf(contador) + " imágenes", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(MyActivity, "Debe selecionar una imagen", Toast.LENGTH_LONG).show();
                            return;
                        }


                        //return;

                    } else {

                        Toast toast;
                        toast = Toast.makeText(MyActivity, "No ha ninguna imagen", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                }

                Intent intent = new Intent(MyActivity, UploadService.class);
                //Log.i("FOO", uri.toString());
                Bundle argPDV = new Bundle();

                argPDV.putString("store_id",store_id );
                argPDV.putString("product_id",product_id );
                argPDV.putString("poll_id",poll_id );
                argPDV.putString("company_id",String.valueOf(GlobalConstant.company_id));
                argPDV.putString("url_insert_image",url_insert_image );
                argPDV.putString("tipo",tipo);

                intent.putStringArrayListExtra("names_file", names_file);
                intent.putExtras(argPDV);
                startService(intent);
                finish();

            }
        });
    }


    //Enviar a AgenteDetailActivity


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = getIntent().getExtras();
        String idPDV = bundle.getString("idPDV");

        // getting values from selected ListItem
        String aid = idPDV;
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to previous screen when app icon in action bar is clicked

                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
//                Intent intent = new Intent(this, AgenteDetailActivity.class);
//                Bundle bolsa = new Bundle();
//                bolsa.putString("id", aid);
//                intent.putExtras(bolsa);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
       // return GlobalConstant.albunName;
    }

    private String getAlbunNameTemp(){
        return  getString(R.string.album_name_temp);
    }

    private String getAlbunNameBackup(){
        return  getString(R.string.album_name_backup);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }


    private File getAlbumDirBackup() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbunNameBackup());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d(getAlbunNameBackup(), "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            }
        }
    }


    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            galleryAddPic();
            mCurrentPhotoPath = null;
            Bundle bundle = getIntent().getExtras();

            Intent i = new Intent( AndroidCustomGalleryActivity.this , AndroidCustomGalleryActivity.class);
            Bundle bolsa = new Bundle();

            bolsa.putString("store_id",store_id );
            bolsa.putString("product_id",product_id );
            bolsa.putString("url_insert_image",url_insert_image );
            bolsa.putString("poll_id",poll_id );
            bolsa.putString("tipo",tipo);


            i.putExtras(bolsa);
            startActivity(i);
            finish();
        }

    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }


    public void getFromSdcard()
    {
        Bundle bundle = getIntent().getExtras();
        String store_id = bundle.getString("store_id");

        //File file= new File(Environment.getExternalStorageDirectory().toString()+ GlobalConstant.directory_images);
        File file= new File(Environment.getExternalStorageDirectory().toString()+ GlobalConstant.directory_images + getAlbumName());

        if (file.isDirectory())
        {
            listFile = file.listFiles();

            for (int i = 0; i < listFile.length; i++)
            {
                if (  listFile[i].getName().substring(0,6).equals(String.format("%06d", Integer.parseInt(store_id)) ))
                {
                    f.add(listFile[i].getAbsolutePath());
                }

            }
        }
    }




    public void copyFile(String selectedImagePath, String string) throws IOException {
        InputStream in = new FileInputStream(selectedImagePath);
        OutputStream out = new FileOutputStream(string);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
//        Toast customToast = new Toast(getBaseContext());
//        customToast = Toast.makeText(getBaseContext(), "Image Transferred", Toast.LENGTH_LONG);
//        customToast.setGravity(Gravity.CENTER| Gravity.CENTER, 0, 0);
//        customToast.show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
}
