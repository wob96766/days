package com.mindspree.days.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.mindspree.days.AppApplication;
import com.mindspree.days.data.DBHelper;
import com.mindspree.days.model.PhotosTableModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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


    public ArrayList getExtraFeatPhotoList(int ExtraFeat){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();
        Cursor c = db.rawQuery("SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE extra_feat = " + ExtraFeat +
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

    public ArrayList getWeightCoeffPhotoList(int WeightCoeff){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();
        Cursor c = db.rawQuery("SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE weight_coeff = " + WeightCoeff +
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

    public ArrayList getPSNRPhotoList(int PSNR){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String file_location_name = "";
        String update_date = "";
        ArrayList photoArrayList = new ArrayList();
        Cursor c = db.rawQuery("SELECT file_location, update_date " +
                " FROM photos " +
                " WHERE psnr_index = " + PSNR +
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


    public float getQualityRankWithPhotoURL(String file_location_name){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        float quality_score = 0;

        Cursor c = db.rawQuery(" SELECT quality_rank " +
                " FROM PHOTOS " +
                " WHERE file_location = '" + file_location_name + "';", null);

        if(c.moveToFirst()){
            do{
                quality_score = c.getFloat(0);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return quality_score;
    }


    public float getExtraFeatWithPhotoURL(String file_location_name){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        float Extra_Feat = 0;

        Cursor c = db.rawQuery(" SELECT extra_feat " +
                " FROM PHOTOS " +
                " WHERE file_location = '" + file_location_name + "';", null);

        if(c.moveToFirst()){
            do{
                Extra_Feat = c.getFloat(0);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return Extra_Feat;
    }

    public float getWeightCoeffWithPhotoURL(String file_location_name){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        float Weight_Coeff = 0;

        Cursor c = db.rawQuery(" SELECT weight_coeff " +
                " FROM PHOTOS " +
                " WHERE file_location = '" + file_location_name + "';", null);

        if(c.moveToFirst()){
            do{
                Weight_Coeff = c.getFloat(0);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return Weight_Coeff;
    }

    public float getBestPhotoFlagWithPhotoURL(String file_location_name){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        float BestPhotoFlag = 0;

        Cursor c = db.rawQuery(" SELECT is_best " +
                " FROM PHOTOS " +
                " WHERE file_location = '" + file_location_name + "';", null);

        if(c.moveToFirst()){
            do{
                BestPhotoFlag = c.getFloat(0);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return BestPhotoFlag;
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

    public void updatePSNRIndex(String file_location, int pSNR) {
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET psnr_index = " + pSNR +
                " WHERE file_location = '" + file_location + "';");

        db.close();
    }

    public void updateExtraFeatIndex(String file_location, int extraFeat) {
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET extra_feat = " + extraFeat +
                " WHERE file_location = '" + file_location + "';");

        db.close();
    }

    public void updateWeightCoeffIndex(String file_location, double WeightCoeff) {
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET weight_coeff = " + WeightCoeff +
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

    public void updateIsBestScore(String file_location, double final_score){
        DBHelper dbHelper = AppApplication.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE photos" +
                " SET is_best = " + final_score +
                " WHERE file_location = '" + file_location + "';");

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
