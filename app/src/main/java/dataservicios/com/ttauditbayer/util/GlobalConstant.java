package dataservicios.com.ttauditbayer.util;

import dataservicios.com.ttauditbayer.R;

/**
 * Created by usuario on 11/11/2014.
 */
public final class GlobalConstant {
   public static String dominio = "http://ttaudit.com";
   //public static String dominio = "http://appfiliaibk.com/";
   // public static String dominio = "http://192.168.1.21/ttaudit.com/backend/ttaudit1/public";
   //  public static String dominio = "http://192.168.1.40/ttaudit.com/backend/ttaudit1/public";
    public static final String LOGIN_URL = dominio + "/loginUser" ;
    public static final String KEY_USERNAME = "username";
    public static String inicio,fin;
    public static  double latitude_open, longitude_open;
    public static  int global_close_audit =0;
    public static int company_id = 35;
    //public static String albunName = "BayerPhoto";
    //public static String directory_images = "/Pictures/" +  R.string.album_name;
    public static String directory_images = "/Pictures/";
    public static String type_aplication = "android";
}
