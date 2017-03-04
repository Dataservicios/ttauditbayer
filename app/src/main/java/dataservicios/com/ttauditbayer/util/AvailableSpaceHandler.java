package dataservicios.com.ttauditbayer.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * This class is designed to get available space in external storage of android.
 * It contains methods which provide you the available space in different units e.g
 * bytes, KB, MB, GB. OR you can get the number of available blocks on external storage.
 *
 */

public class AvailableSpaceHandler {
    //*********
    //Variables
    /**
     * Number of bytes in one KB = 2<sup>10</sup>
     */
    public final static long SIZE_KB = 1024L;

    /**
     * Number of bytes in one MB = 2<sup>20</sup>
     */
    public final static long SIZE_MB = SIZE_KB * SIZE_KB;

    /**
     * Number of bytes in one GB = 2<sup>30</sup>
     */
    public final static long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;

    //********
    // Methods

    /**
     * @return Number of bytes available on external storage
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getExternalAvailableSpaceInBytes() {
        long availableSpace = -1L;
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            //availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
            availableSpace = (long) stat.getAvailableBlocksLong() * (long) stat.getBlockSizeLong();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableSpace;
    }


    /**
     * @return Number of kilo bytes available on external storage
     */
    public static long getExternalAvailableSpaceInKB(){
        return getExternalAvailableSpaceInBytes()/SIZE_KB;
    }
    /**
     * @return Number of Mega bytes available on external storage
     */
    public static long getExternalAvailableSpaceInMB(){
        return getExternalAvailableSpaceInBytes()/SIZE_MB;
    }

    /**
     * @return gega bytes of bytes available on external storage
     */
    public static long getExternalAvailableSpaceInGB(){
        return getExternalAvailableSpaceInBytes()/SIZE_GB;
    }

    /**
     * @return Total number of available blocks on external storage
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getExternalStorageAvailableBlocks() {
        long availableBlocks = -1L;
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            //availableBlocks = stat.getAvailableBlocks();
            availableBlocks = stat.getAvailableBlocksLong();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableBlocks;
    }
}