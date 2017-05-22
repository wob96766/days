package com.mindspree.days.lib;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.mindspree.days.AppApplication;
import com.mindspree.days.engine.ClusterEngine;
import com.mindspree.days.model.DatelineModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static android.R.attr.orientation;


public class AppUtils {

    public static int datediffinminutes(Date origin, Date target){
        Calendar calOrigin = Calendar.getInstance();
        Calendar calTarget = Calendar.getInstance();
        calOrigin.setTime(origin);
        calTarget.setTime(target);

        long diffSec = (calOrigin.getTimeInMillis() - calTarget.getTimeInMillis()) / 1000;       //초
        /*Long diffDay = diffSec/(60 * 60 * 24);*/
        Long diffMunutes = diffSec/(60);
        return diffMunutes.intValue();
    }


    public static String [] arraylistTostringarray_nooverlap(ArrayList poiList){
        Set<String> uniqKeys2 = new TreeSet<String>();

        // Convert poiList arraylist to string array
        Object[] poiListStringArraytemp = poiList.toArray(new String[poiList.size()]);
        String[] poiListStringArray = (String[]) poiListStringArraytemp;

        try {
            uniqKeys2.addAll(Arrays.asList(poiListStringArray));
        } catch (Exception e) {
            e.printStackTrace();

        }

        List<String> stringList = new ArrayList<>(uniqKeys2);
        String [] uniqKeysArray2 = new String[uniqKeys2.size()];
        for (int j = 0; j < uniqKeys2.size(); j++)
            uniqKeysArray2[j] = stringList.get(j); //


        return uniqKeysArray2;

    }

    public static int arraylistsize_nooverlap(ArrayList poiList){
        Set<String> uniqKeys2 = new TreeSet<String>();


        // Convert poiList arraylist to string array
        Object[] poiListStringArraytemp = poiList.toArray(new String[poiList.size()]);
        String[] poiListStringArray = (String[]) poiListStringArraytemp;

        try {
            uniqKeys2.addAll(Arrays.asList(poiListStringArray));
        } catch (Exception e) {
            e.printStackTrace();

        }

        return uniqKeys2.size();

    }
    public static Date getTodayDateTime(Date origin, String timeFormat){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.KOREA);
            String dateString = dateFormat.format(origin);

            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            return sdFormat.parse(dateString + timeFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            return origin;
        }
    }

    public static String getTodayDateTimeString(Date origin, String timeFormat){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            String dateString = dateFormat.format(origin);


            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date StringToDate(Date origin, String timeFormat){
        try {

            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            return sdFormat.parse(timeFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            return origin;
        }
    }

    public static Date StringToDate(String timeFormat){
        try {

            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            return sdFormat.parse(timeFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }


    public static boolean isEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
        if(emailPattern.length() < 1)
            return false;
        else
            return email.matches(emailPattern);
    }


    public static String getFacebookFormat(String dateString, boolean displayTime){
        try {
            long now = System.currentTimeMillis();
            Date today = new Date(now);

            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            Date destination = sdFormat.parse(dateString);

            if(destination.after(today))
            {
                return "조금전";
            } else {
                String strToday = dateFormat.format(today);
                String strDestination = dateFormat.format(destination);
                Date dateToday = dateFormat.parse(strToday);
                Date dateDest = dateFormat.parse(strDestination);

                long diff = dateToday.getTime() - dateDest.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if(diffDays > 0){
                    if(displayTime == false) {
                        return dateFormat.format(dateDest);
                    } else {
                        return sdFormat.format(destination);
                    }
                } else {
                    diff = today.getTime() - destination.getTime();
                    diffDays = diff / (60 * 60 * 1000);
                    if(diffDays > 0){
                        return String.format("%d 시간전", diffDays);
                    } else {
                        diffDays = diff / (60 * 1000);
                        return String.format("%d 분전", diffDays);
                    }
                }
            }
        } catch (Exception e) {
            return getDate(dateString);
        }
    }

    public static String getDate(String dateString){
        return getDateFormat(dateString, "yyyy-MM-dd HH:mm:ss.S");
    }

    public static String getDate(String dateString, String toFormat){
        return getDateFormat(dateString, "yyyy-MM-dd HH:mm:ss.S", toFormat);
    }

    public static String getTime(String dateString){
        return getTimeFormat(dateString, "yyyy-MM-dd HH:mm:ss.S");
    }

    public static String getDateFormat(String dateString, String format){

        try {
            SimpleDateFormat sdFormat = new SimpleDateFormat(format, Locale.KOREA);
            Date tempDate = sdFormat.parse(dateString);
            sdFormat = new SimpleDateFormat("EEE, d MMM yyyy");
            return sdFormat.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateFormat(String dateString, String format, String toFormat){

        try {
            SimpleDateFormat sdFormat = new SimpleDateFormat(format, Locale.KOREA);
            Date tempDate = sdFormat.parse(dateString);
            sdFormat = new SimpleDateFormat(toFormat);
            return sdFormat.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeFormat(String dateString, String format){

        try {
            SimpleDateFormat sdFormat = new SimpleDateFormat(format, Locale.KOREA);
            Date tempDate = sdFormat.parse(dateString);
            sdFormat = new SimpleDateFormat("HH:mm a");
            return sdFormat.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDistance(String distanceString)
    {
        try {
            DecimalFormat df = new DecimalFormat("#");
            Double distance = Double.parseDouble(distanceString) * 1000;
            if(distance > 1000){
                return String.format("%s km", df.format(distance/1000));
            }else {
                return String.format("%s m", df.format(distance));
            }
        }catch(Exception e){
            return String.format("%s m", "0");
        }
    }

    public static int getInteger(String stringNumber) {

        try {
            return Integer.parseInt(stringNumber);
        } catch (Exception ex){
            return 2;
        }
    }

    public static File getTempImageFile(Context context, String extension) {
        File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/temp/");
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, String.format("tempimage.%s", extension));
        return file;
    }

    public static File getTempImageFile(Context context) {
        File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/temp/");
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, "tempimage.png");
        return file;
    }

    public static String getDeviceModel(){
        return Build.MODEL;
    }

    public static int dpToPixels(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String getCurrencyText(String strNum)
    {
        try{
            if(strNum.length() > 0){
                int num = Integer.parseInt(strNum);
                return getCurrencyText(num);
            } else {
                return "0원";
            }
        } catch(Exception e)
        {
            return "0원";
        }

    }

    public static String getCurrencyText(int number)
    {
        if(number < 1)
            return "";
        else {
            DecimalFormat df = new DecimalFormat("###,###,###,###,###,###");
            return String.format(Locale.KOREA, "%s원", df.format(number));
        }
    }

    public static String getCurrencyNumber(int number)
    {
        if(number < 1)
            return "";
        else {
            DecimalFormat df = new DecimalFormat("###,###,###,###,###,###");
            return String.format(Locale.KOREA, "%s", df.format(number));
        }
    }

    public static Bitmap rotateBitmap(Bitmap bmp, int degree){
        System.gc();
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }


    public static Bitmap collage_gen(DatelineModel mDateline)
    {
        int photo_cnt=0;
        Bitmap bitmap_2photo_target =null;
        Bitmap bitmap_3photo_target =null;
        for(String imageUrl : mDateline.getPhotoList()) {
            if (imageUrl.contains("http") || imageUrl.contains("https")) {
            }else {
                Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);
                if(bitmap.getHeight()>200)
                    photo_cnt++;
            }

            if(photo_cnt==3)
                break;
        }


        Bitmap[] bitmapArray = new Bitmap[photo_cnt];
        for(int i=0;i<photo_cnt;i++){ // Becareful.... Rank 2 and 3 are processed first.
            String imageUrl =mDateline.getPhotoList().get(i).toString();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageUrl, options);

            int imgHeight = options.outHeight;
            int sample_size =2;

            if(imgHeight<2000)
                sample_size =1 ;
            else if(imgHeight >=2000)
                sample_size =2 ;

            BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
            bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap_options.inSampleSize = sample_size;
            bitmapArray[i] = BitmapFactory.decodeFile(imageUrl, bitmap_options);


            int orientation =1;
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageUrl);
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

            } catch (Exception e) {
                //e.printStackTrace();

                orientation = ExifInterface.ORIENTATION_NORMAL;
            }
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmapArray[i] = AppUtils.rotateBitmap(bitmapArray[i], 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmapArray[i] = AppUtils.rotateBitmap(bitmapArray[i], 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmapArray[i] = AppUtils.rotateBitmap(bitmapArray[i], 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }


        }


        if(photo_cnt==2){
            if( (bitmapArray[0].getWidth() >= bitmapArray[0].getHeight()) && (bitmapArray[1].getWidth() >= bitmapArray[1].getHeight()) ) {
                // Both of them are landscape . Add upd and down
                bitmap_2photo_target=AppUtils.combineImages_updown(bitmapArray[0],bitmapArray[1]);
            }else{
                // Portrait and landscape or Landscape and portrait
                bitmap_2photo_target=AppUtils.combineImages_side(bitmapArray[0],bitmapArray[1]);
            }

        }else if(photo_cnt==3) {

            if( (bitmapArray[1].getWidth() >= bitmapArray[1].getHeight()) && (bitmapArray[2].getWidth() >= bitmapArray[2].getHeight()) ) {
                // Both of them are landscape . Add upd and down
                bitmap_2photo_target=AppUtils.combineImages_updown(bitmapArray[1],bitmapArray[2]);
            }else{
                // Portrait and landscape or Landscape and portrait
                bitmap_2photo_target=AppUtils.combineImages_side(bitmapArray[1],bitmapArray[2]);
            }

            if(  bitmapArray[0].getWidth() >= bitmapArray[0].getHeight()  ){
                // Rank 1 is Landscape
                // addd up and down
                bitmap_2photo_target=AppUtils.combineImages_side(bitmapArray[1],bitmapArray[2]);
                bitmap_3photo_target=AppUtils.combineImages_updown(bitmapArray[0],bitmap_2photo_target);

            }else{
                // Rank 1 is Portrait
                // addd side by side
                bitmap_2photo_target=AppUtils.combineImages_updown(bitmapArray[1],bitmapArray[2]);
                bitmap_3photo_target=AppUtils.combineImages_side(bitmapArray[0],bitmap_2photo_target);
            }




        }

        return bitmap_3photo_target;


    }


    public static Bitmap combineImages_side(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        // Resizing
        if ( (c.getHeight() > s.getHeight()) ){
            c= Bitmap.createScaledBitmap(c,  Math.round( (float)c.getWidth() * (float)s.getHeight() / (float)c.getHeight()  ), s.getHeight(), true);
        }else{
            s= Bitmap.createScaledBitmap(s,  Math.round( (float)s.getWidth() * (float)c.getHeight() / (float)s.getHeight()  ), c.getHeight(), true);
        }


        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        Bitmap image = Bitmap.createBitmap(20, height, Bitmap.Config.ARGB_8888);
        image.eraseColor(Color.WHITE);

        cs = Bitmap.createBitmap(width+20, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(image, c.getWidth(), 0f, null);
        comboImage.drawBitmap(s, c.getWidth()+20, 0f, null);

        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/

        return cs;
    }


    public static Bitmap combineImages_updown(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        // Resizing
        if ( (c.getWidth() > s.getWidth()) ){
            c= Bitmap.createScaledBitmap(c, s.getWidth(), Math.round( (float)c.getHeight() * (float)s.getWidth() / (float)c.getWidth()  ), true);
        }else{
            s= Bitmap.createScaledBitmap(s, c.getWidth(), Math.round( (float)s.getHeight() * (float)c.getWidth() / (float)s.getWidth()  ), true);
        }


        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight() + s.getHeight();
        } else {
            width = s.getWidth();
            height = c.getHeight() + s.getHeight();
        }

        Bitmap image = Bitmap.createBitmap(width, 20, Bitmap.Config.ARGB_8888);
        image.eraseColor(Color.WHITE);

        cs = Bitmap.createBitmap(width, height+20, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(image, 0f, c.getHeight(), null);
        comboImage.drawBitmap(s, 0f, c.getHeight()+20, null);

        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/

        return cs;
    }


    public static Bitmap downsampleImageFile(String filelocation, int target_ratio_short, int target_ratio_long){
        System.gc();
        final BitmapFactory.Options options = new BitmapFactory.Options();

        ClusterEngine clusterEngine = new ClusterEngine();

        int orientation =1;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filelocation);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

        } catch (Exception e) {
            //e.printStackTrace();

            orientation = ExifInterface.ORIENTATION_NORMAL;
        }



        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filelocation, options);

        int imgHeight = options.outHeight;
        int imgWidth = options.outWidth;


        int targetW2;
        int targetH2;

        int sample_size =1;

        if(imgHeight>imgWidth){
            targetH2 = target_ratio_long;
            targetW2 = target_ratio_short;
        }else{
            targetW2 = target_ratio_long;
            targetH2 = target_ratio_short;
        }

        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap_options.inSampleSize = sample_size;
        Bitmap bitmapSource = BitmapFactory.decodeFile(filelocation, bitmap_options);

        Bitmap bmRotated = clusterEngine.rotateBitmap(bitmapSource, orientation);

        Bitmap bm= Bitmap.createScaledBitmap(bmRotated, targetW2, targetH2, true);


        return bm;
    }

    public static byte[] compressImageFile(String filelocation){
        System.gc();
        Bitmap bitmap = BitmapFactory.decodeFile(filelocation);
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] dataCompress = baos.toByteArray();
            return dataCompress;
        } else {
            return null;
        }
    }

    public static byte[] compressBitmapFile(String filelocation){
        System.gc();
        Bitmap bitmap = BitmapFactory.decodeFile(filelocation);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] dataCompress = baos.toByteArray();
        return dataCompress;
    }


    public static String getAppText(int resourceId){
        return AppApplication.getAppInstance().getResources().getString(resourceId);
    }

    public static boolean isScreenLock(Context context){
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static boolean isPackageInstalled (Activity activity, String uri) {
        PackageManager pm = activity.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static void openPlayStore(Activity activity, String uri) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + uri)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + uri)));
        }
    }




    public static boolean IsExternalMemoryAvailable()
    {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static long GetTotalInternalMemorySize()
    {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();

        return totalBlocks * blockSize;
    }

    public static long GetAvailableInternalMemorySize()
    {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();

        return availableBlocks * blockSize;
    }

    public static long GetTotalExternalMemorySize()
    {
        if(IsExternalMemoryAvailable() == true)
        {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();

            return totalBlocks * blockSize;
        }
        else
        {
            return -1;
        }
    }

    public static long GetAvailableExternalMemorySize()
    {
        if(IsExternalMemoryAvailable() == true)
        {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();

            return availableBlocks * blockSize;
        }
        else
        {
            return -1;
        }
    }


}
