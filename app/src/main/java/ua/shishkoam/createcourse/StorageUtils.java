package ua.shishkoam.createcourse;

import android.os.Build;
import android.os.Environment;

import java.io.File;


public class StorageUtils {
    public StorageUtils() {
    }

    public static File getInternalSDCard() {
        File standard = Environment.getExternalStorageDirectory();
        String var1 = Build.MODEL;
        byte var2 = -1;
        switch(var1.hashCode()) {
            case 152150337:
                if (var1.equals("XtremePQ70")) {
                    var2 = 1;
                }
                break;
            case 1394949604:
                if (var1.equals("Sigma_Xtreme_PQ27")) {
                    var2 = 0;
                }
        }

        File sdcard1;
        switch(var2) {
            case 0:
                if (Environment.isExternalStorageRemovable()) {
                    sdcard1 = new File("/storage/sdcard0");
                    if (sdcard1.exists() && sdcard1.canWrite()) {
                        return sdcard1;
                    }
                }

                return standard;
            case 1:
                if (Environment.isExternalStorageRemovable()) {
                    sdcard1 = new File("/storage/sdcard1");
                    if (sdcard1.exists() && sdcard1.canWrite()) {
                        return sdcard1;
                    }
                }

                return standard;
            default:
                return standard;
        }
    }
}
