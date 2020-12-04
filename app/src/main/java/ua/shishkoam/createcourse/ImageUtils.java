package ua.shishkoam.createcourse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


public class ImageUtils {
    public static final  int AVATAR_SIZE = 256;

    public static final int DEFAULT_IMAGE_QUALITY = 60;
    public static final int DEFAULT_IMAGE_RESOLUTION = 0;
    public static final int MAX_IMAGE_RESOLUTION = 2048;

    public static Bitmap rescaleAvatar(String path) {
        AtomicBoolean scaled = new AtomicBoolean(false);
        Bitmap bitmap = bmpFromPath(path, scaled, AVATAR_SIZE);
        if (bitmap != null && scaled.get())
            saveImage(bitmap, path);
        return bitmap;
    }

    public static Bitmap bmpFromPath(String path, AtomicBoolean rescaled) {
        return bmpFromPath(path, rescaled, MAX_IMAGE_RESOLUTION);
    }

    public static Bitmap bmpFromPath(String path, AtomicBoolean rescaled, int size) {
        Bitmap b = null;

        if (path == null) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {

            //Decode image size
            FileInputStream fis = new FileInputStream(file);
            BitmapFactory.decodeStream(fis, null, options);
            fis.close();

            int scale = 1;
            if (options.outHeight > size || options.outWidth > size) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(size /
                        (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));

            }

            if (rescaled != null) {
                rescaled.set(scale != 1);
            }

            //Decode with inSampleSize. Make image smaller if we need it
            BitmapFactory.Options finalOptions = new BitmapFactory.Options();
            finalOptions.inSampleSize = scale;
            fis = new FileInputStream(file);
            b = BitmapFactory.decodeStream(fis, null, finalOptions);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }

    public static File saveUnnamedImage(Bitmap bmp, String dir) {
        File file = null;
        FileOutputStream out = null;
        try {
            File directory = new File(dir);
            directory.mkdirs();
            file = new File(dir, "image"
                    + String.valueOf(System.currentTimeMillis()) + ".jpg");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File saveImage(Bitmap bmp, String path) {
        File file = null;
        FileOutputStream out = null;
        try {
            file = new File(path);
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static int getBitmapHash(Bitmap bmp){
        if (bmp == null)
            return 0;
        int hash = 31; //or a higher prime at your choice
        for(int x = 0; x < bmp.getWidth(); x++){
            for (int y = 0; y < bmp.getHeight(); y++){
                hash += (bmp.getPixel(x,y) + 31);
            }
        }
        return hash;
    }

    public static byte[] bmpToByteArray(Bitmap bitmap, int sizeRestriction, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap scaled = null;
        if (sizeRestriction > 0 && bitmap.getHeight() > sizeRestriction) {
            int scaledWidth = bitmap.getWidth() * sizeRestriction / bitmap.getHeight();
            scaled = Bitmap.createScaledBitmap(bitmap, scaledWidth, sizeRestriction, false);
            bitmap.recycle();
        } else
            scaled = bitmap;
        scaled.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        return stream.toByteArray();
    }

    public static Bitmap bmpFromPath(String path) {
        return bmpFromPath(path, null);
    }
}
