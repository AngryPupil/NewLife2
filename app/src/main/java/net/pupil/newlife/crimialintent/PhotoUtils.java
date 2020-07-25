package net.pupil.newlife.crimialintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.View;

/**
 * Created by Angry on 2018/7/5.
 */

public class PhotoUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //inJustDecodeBounds：
        //如果将这个值置为true，那么在解码的时候将不会返回bitmap，只会返回这个bitmap的尺寸。这个属性的目的是，如果你只想知道一个bitmap的尺寸，但又不想将其加载到内存时。这是一个非常有用的属性。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        //outWidth和outHeight：
        //表示这个Bitmap的宽和高，一般和inJustDecodeBounds一起使用来获得Bitmap的宽高，但是不加载到内存。
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        //inSampleSize：
        //这个值是一个int，当它小于1的时候，将会被当做1处理，如果大于1，那么就会按照比例（1 / inSampleSize）缩小bitmap的宽和高、降低分辨率，
        // 大于1时这个值将会被处置为2的倍数。例如，width=100，height=100，inSampleSize=2，那么就会将bitmap处理为，width=50，height=50，宽高降为1 / 2，像素数降为1 / 4。
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, View view) {
        Point size = new Point();
        view.getDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

}
