package com.mindspree.days.engine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.mindspree.days.AppApplication;
import com.mindspree.days.data.DBHelper;
import com.mindspree.days.model.PhotosTableModel;

import java.util.ArrayList;


public class EngineDBInterface {

    public ArrayList getPhotoListWithoutClusterId(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();

        Cursor c = db.rawQuery(" SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE cluster_id is null" +
                " ORDER BY update_date ASC;", null);

        if(c.moveToFirst()){
            do{
                file_location_name = c.getString(0);
                update_date = c.getString(1);

                PhotosTableModel photo = new PhotosTableModel();
                photo.setFile_location(file_location_name);
                photo.setUpdate_date(update_date);

                photoArrayList.add(photo);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return photoArrayList;
    }

    public ArrayList getBlurredPhotoList(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();

        Cursor c = db.rawQuery(" SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE blurring_index IS NOT NULL" +
                " ORDER BY update_date DESC;", null);

        if(c.moveToFirst()){
            do{
                file_location_name = c.getString(0);
                update_date = c.getString(1);

                PhotosTableModel photo = new PhotosTableModel();
                photo.setFile_location(file_location_name);
                photo.setUpdate_date(update_date);

                photoArrayList.add(photo);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return photoArrayList;
    }


    public ArrayList getsvmPhotoList(int class_svm){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();
        Cursor c = db.rawQuery("SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE extra_feat = " + class_svm +
                " ORDER BY update_date DESC; ", null);


        if(c.moveToFirst()){
            do{
                file_location_name = c.getString(0);
                update_date = c.getString(1);

                PhotosTableModel photo = new PhotosTableModel();
                photo.setFile_location(file_location_name);
                photo.setUpdate_date(update_date);

                photoArrayList.add(photo);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return photoArrayList;
    }

    public ArrayList getPhotoListwithoutSVMclass(int svm_class){

        int psvm_class=0;
        int nsvm_class=0;

        if(svm_class ==0){
            psvm_class =10;
        } else if(svm_class ==1){
            psvm_class =20;
        } else if(svm_class ==2) {
            psvm_class =30;
        } else if(svm_class ==3){
            psvm_class =40;
        }



        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();
        Cursor c = db.rawQuery("SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE extra_feat is null" +
                " OR extra_feat = " + nsvm_class +
                " ORDER BY update_date DESC; ", null);


        if(c.moveToFirst()){
            do{
                file_location_name = c.getString(0);
                update_date = c.getString(1);

                PhotosTableModel photo = new PhotosTableModel();
                photo.setFile_location(file_location_name);
                photo.setUpdate_date(update_date);

                photoArrayList.add(photo);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return photoArrayList;

//        return c;

    }

    public Cursor getPhotoListwithoutSVMclass_cursor(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();
        Cursor c = db.rawQuery("SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE extra_feat is null" +
                " ORDER BY update_date DESC; ", null);


        c.close();
        db.close();

        return c;

    }

    public int getNextClusterId(){

        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        ArrayList fileArrayList = new ArrayList();

        Cursor c = db.rawQuery(" SELECT " +
                " CASE WHEN cluster_id is NULL then " +
                "   1 " +
                " ELSE " +
                "   MAX(cluster_id) + 1 " +
                " END AS next_cluster_id " +
                " FROM photos ;", null);

        c.moveToFirst();
        int nextClusterId = c.getInt(0);

        c.close();
        db.close();

        return nextClusterId;
    }

    public int getLastClusterId(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        ArrayList fileArrayList = new ArrayList();

        Cursor c = db.rawQuery(" SELECT " +
                " CASE WHEN cluster_id is NULL then " +
                "   0 " +
                " ELSE " +
                "   MAX(cluster_id) " +
                " END AS next_cluster_id " +
                " FROM photos ;", null);

        c.moveToFirst();
        int nextClusterId = c.getInt(0);

        c.close();
        db.close();

        return nextClusterId;
    }

    public ArrayList getClusterIdWithoutQualityScore(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        int clusterId = 0;
        ArrayList clusterIdArrayList = new ArrayList();

        Cursor c = db.rawQuery(" SELECT cluster_id " +
                " FROM photos " +
                " WHERE cluster_id IS NOT NULL " +
                " AND quality_rank IS NULL" +
                " GROUP BY cluster_id; ", null);

        if(c.moveToFirst()){
            do{
                clusterId = c.getInt(0);

                clusterIdArrayList.add(clusterId);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return clusterIdArrayList;
    }

    public int getClusterIdWithPhotoURL(String file_location_name){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        int clusterId = 0;
        ArrayList clusterIdArrayList = new ArrayList();

        Cursor c = db.rawQuery(" SELECT cluster_id " +
                " WHERE photos = " + file_location_name +
                " GROUP BY cluster_id; ", null);

        if(c.moveToFirst()){
            do{
                clusterId = c.getInt(0);

                clusterIdArrayList.add(clusterId);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return clusterId;
    }


    public ArrayList getPhotoListForClusterId(int clusterId){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";

        ArrayList photoArrayList = new ArrayList();

        Cursor c = db.rawQuery("SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE cluster_id = " + clusterId +
                " ORDER BY update_date DESC; ", null);

        if(c.moveToFirst()){
            do{
                file_location_name = c.getString(0);
                update_date = c.getString(1);

                PhotosTableModel photo = new PhotosTableModel();
                photo.setFile_location(file_location_name);
                photo.setUpdate_date(update_date);

                photoArrayList.add(photo);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return photoArrayList;
    }

    public void updateClusterID(String file_location, int cluster_id) {
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET cluster_id = " + cluster_id +
                " WHERE file_location = '" + file_location + "';");

        db.close();
    }

    public void updateCBlurryIndex(String file_location, int blurryIndex) {
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET blurring_index = " + blurryIndex +
                " WHERE file_location = '" + file_location + "';");

        db.close();
    }

    public void updateSVMIndex(String file_location, int svm_class) {
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET extra_feat = " + svm_class +
                " WHERE file_location = '" + file_location + "';");

        db.close();
    }

    public void updateQualityScore(String file_location, double final_score){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET final_score = " + final_score +
                " , quality_rank = " + final_score +
                " WHERE file_location = '"+file_location+"';");

        db.close();

    }

    // Return Number of All Photos
    public int getNumberOfAllPhotos(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int allPhotos = 0;
        Cursor c = db.rawQuery(" SELECT count(*) " +
                " FROM photos; ", null);

        if(c.moveToFirst()){
            do{
                allPhotos = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return allPhotos;
    }

    // Return Number of clustered Photos
    public int getNumberOfClusteredPhotos(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int clusteredPhotos = 0;
        Cursor c = db.rawQuery(" SELECT count(*) " +
                " FROM photos" +
                " WHERE cluster_id IS NOT NULL; ", null);

        if(c.moveToFirst()){
            do{
                clusteredPhotos = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return clusteredPhotos;
    }

    // Return total size of photos
    public int getTotalPhotoSizeInMB(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int totalPhotoSize = 0;
        Cursor c = db.rawQuery(" SELECT sum(photo_size)/1024/1024 " +
                " FROM photos; ", null);

        if(c.moveToFirst()){
            do{
                totalPhotoSize = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return totalPhotoSize;
    }


    // Return total count of clusters with multiple photos
    public int getNumberOfBlurredPhotos(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int PhotoCnt = 0;
        Cursor c = db.rawQuery(" SELECT count(*) " +
                " FROM photos " +
                " WHERE blurring_index IS NOT NULL", null);


        if(c.moveToFirst()){
            do{
                PhotoCnt = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return PhotoCnt;


    }

    public int getSizeOfBlurredPhotosInMB(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int clusterSize = 0;
        Cursor c = db.rawQuery(" SELECT sum(photo_size)/1024/1024 " +
                " FROM photos " +
                " WHERE blurring_index IS NOT NULL" +
                " ORDER BY update_date DESC;", null);

        if(c.moveToFirst()){
            do{
                clusterSize = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return clusterSize;

    }


    // Return total count of clusters with multiple photos
    public int getNumberOfClustersWithMultiplePhotos(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int clusterCnt = 0;
        Cursor c = db.rawQuery(" SELECT SUM(cnt) FROM " +
                " (SELECT cluster_id, COUNT(*) cnt FROM photos " +
                " GROUP BY 1) " +
                " WHERE cnt > 1;", null);

        if(c.moveToFirst()){
            do{
                clusterCnt = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return clusterCnt;
    }





    public int getSizeOfClustersWithMultiplePhotosInMB(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int clusterSize = 0;
        Cursor c = db.rawQuery(" SELECT SUM(photo_size)/1024/1024 " +
                " FROM PHOTOS " +
                " WHERE cluster_id in " +
                "     (SELECT cluster_id " +
                "         FROM " +
                "         (SELECT cluster_id, COUNT(*) cnt FROM photos " +
                "     GROUP BY 1) " +
                "     WHERE cnt > 1 " +
                " ); ", null);

        if(c.moveToFirst()){
            do{
                clusterSize = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return clusterSize;

    }
    public int getSizeOfClustersWithSinglePhotosInMB(){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int clusterSize = 0;
        Cursor c = db.rawQuery(" SELECT SUM(photo_size)/1024/1024 " +
                " FROM PHOTOS " +
                " WHERE cluster_id in " +
                "     (SELECT cluster_id " +
                "         FROM " +
                "         (SELECT cluster_id, COUNT(*) cnt FROM photos " +
                "     GROUP BY 1) " +
                "     WHERE cnt == 1 " +
                " ); ", null);

        if(c.moveToFirst()){
            do{
                clusterSize = c.getInt(0);
            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return clusterSize;

    }


    // Return Number of Mega bytes available on External storage
    public int getAvailableSpaceInMB(){
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return (int)(availableSpace/SIZE_MB);
    }

    // Return Number of Mega bytes available on External storage
    public int getTotalSpaceInMB(){
        long KILOBYTE = 1024L;

        StatFs internalStatFs = new StatFs( Environment.getRootDirectory().getAbsolutePath() );
        long internalTotal;
        long internalFree;

        StatFs externalStatFs = new StatFs( Environment.getExternalStorageDirectory().getAbsolutePath() );
        long externalTotal;
        long externalFree;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = ( internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
            internalFree = ( internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
            externalTotal = ( externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
            externalFree = ( externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
        }
        else {
            internalTotal = ( (long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
            internalFree = ( (long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
            externalTotal = ( (long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
            externalFree = ( (long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
        }

        long total = internalTotal + externalTotal;
        long free = internalFree + externalFree;
        long used = total - free;

        return (int)(total);
    }

}
