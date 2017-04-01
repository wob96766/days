package com.mindspree.days.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.mindspree.days.model.Cluster_input;
import com.mindspree.days.ui.MainActivity.ExecuteEngines;
import com.mindspree.days.ui.MainActivity.ExecuteEnginesAgain;
import com.mindspree.days.model.PhotosTableModel;

// This is for AI


public class ClusterEngine {
    /** Called when the activity is first created. */

    public static int runClusterEngine(ExecuteEngines task, ExecuteEnginesAgain againTask, EngineDBInterface engineDBInterface, ArrayList fileArrayList, Cluster_input cluster_input, double c, ClusterEngine myM, int i, FaceDetector fdetector) {



        // Imporatant clstering threshold parameters
        double Cluster_th = 21; // 42
        double Blurry_th =55;
        double time_feat_gain =1;

// For loop

        /////////////////////////////////////////////////////////
            /*                Image file reading                   */
        /////////////////////////////////////////////////////////
        PhotosTableModel fileParrayList= (PhotosTableModel)fileArrayList.get(i);
        String fname = (String) fileParrayList.getFile_location();


        double[][] garray_row =null;
        double[][] garray_row_prev =null;

        garray_row = impreprocess_for_cluster(fname, fdetector);

        cluster_input.edge_level[0] = garray_row[0][0];
        cluster_input.LUenergy_level[0] = garray_row[0][5];
        cluster_input.RUenergy_level[0] = garray_row[0][6];
        cluster_input.LLenergy_level[0] = garray_row[0][7];
        cluster_input.RLenergy_level[0] = garray_row[0][8];

        /////////////////////////////////////////////////////////////////
            /*          Clustering JNI insertion for Haejoing              */
        /////////////////////////////////////////////////////////////////
        //garray[x][y] =  (0.2989 * (float) Color.red(colour)) + (0.5870 * (float) Color.green(colour)) + (0.114 * (float) Color.blue(colour));


        if(i==0 ){
            if(cluster_input.Last_cluster ==0) // It means this is the first time when clustering function runs
            {
                c=1;
                cluster_input.rkdist[i] = 0.2989 * myM.clusteringTest6(garray_row[1], cluster_input.rb, c) ; // invoking the native method
                cluster_input.gkdist[i] = 0.5870 * myM.clusteringTest6(garray_row[2], cluster_input.gb, c) ; // invoking the native method
                cluster_input.bkdist[i] = 0.114 * myM.clusteringTest6(garray_row[3], cluster_input.bb, c) ; // invoking the native method
                cluster_input.kdist[i]=Math.max(Math.max(cluster_input.rkdist[i], cluster_input.gkdist[i]), cluster_input.bkdist[i]);


                if (cluster_input.kdist[i] >= Cluster_th)
                    cluster_input.Cluster_cnt = cluster_input.Cluster_cnt + 1;

                cluster_input.ClusTer[i] = cluster_input.Cluster_cnt;
            }
            else  // It means some fies are clustered
            {
                c=0;
                if(cluster_input.Cluster_cnt==1)   // Still first cluster
                    cluster_input.PrevfileArrayList = engineDBInterface.getPhotoListForClusterId((int) cluster_input.Cluster_cnt);
                else                 // Some cluster exists
                    cluster_input.PrevfileArrayList = engineDBInterface.getPhotoListForClusterId((int) cluster_input.Cluster_cnt - 1);

                cluster_input.size_PrevfileArrayList=cluster_input.PrevfileArrayList.size();
                cluster_input.PrevClusterLastfile=(PhotosTableModel)cluster_input.PrevfileArrayList.get(cluster_input.size_PrevfileArrayList-1);

                String Prevfname =(String)cluster_input.PrevClusterLastfile.getFile_location();
                garray_row_prev = impreprocess_for_cluster(Prevfname, fdetector);

                double temp =0;
                // temp is not used but b will be used.So do not delete this line.
                temp = myM.clusteringTest6(garray_row_prev[1], cluster_input.rb, 1);  // In the first loop, if the previous cluster is not the fist cluster clusteringTest6 should calculate the feature vector. T
                temp = myM.clusteringTest6(garray_row_prev[2], cluster_input.gb, 1);  // In the first loop, if the previous cluster is not the fist cluster clusteringTest6 should calculate the feature vector. T
                temp = myM.clusteringTest6(garray_row_prev[3], cluster_input.bb, 1);  // In the first loop, if the previous cluster is not the fist cluster clusteringTest6 should calculate the feature vector. T

                cluster_input.rkdist[i] = 0.2989 * myM.clusteringTest6(garray_row[1], cluster_input.rb, c) ; // invoking the native method
                cluster_input.gkdist[i] = 0.5870 * myM.clusteringTest6(garray_row[2], cluster_input.gb, c) ; // invoking the native method
                cluster_input.bkdist[i] = 0.114 * myM.clusteringTest6(garray_row[3], cluster_input.bb, c) ; // invoking the native method

                cluster_input.kdist_time[i]= time_feature_euclidean_dist(fname, Prevfname);
                cluster_input.kdist[i]=Math.max(Math.max(cluster_input.rkdist[i], cluster_input.gkdist[i]), cluster_input.bkdist[i]);

                cluster_input.edge_level_dist[i]=Math.abs(cluster_input.edge_level[0] - cluster_input.edge_level[1]);

                cluster_input.LU_level_dist[i]=Math.abs(cluster_input.LUenergy_level[0] - cluster_input.LUenergy_level[1]);
                cluster_input.RU_level_dist[i]=Math.abs(cluster_input.RUenergy_level[0] - cluster_input.RUenergy_level[1]);
                cluster_input.LL_level_dist[i]=Math.abs(cluster_input.LLenergy_level[0] - cluster_input.LLenergy_level[1]);
                cluster_input.RL_level_dist[i]=Math.abs(cluster_input.RLenergy_level[0] - cluster_input.RLenergy_level[1]);

                cluster_input.kdist[i]=cluster_input.kdist[i] + cluster_input.edge_level_dist[i]*2 + (cluster_input.LU_level_dist[i] + cluster_input.RU_level_dist[i] + cluster_input.LL_level_dist[i] + cluster_input.RL_level_dist[i])*2 +cluster_input.kdist_time[i]* time_feat_gain;

                if (cluster_input.kdist[i] < Cluster_th)
                    cluster_input.Cluster_cnt=cluster_input.Cluster_cnt-1;
            }
        }
        else {
            c=0;

            // Read previous file name
            PhotosTableModel fileParrayList_prev= (PhotosTableModel)fileArrayList.get(i - 1);
            String Prevfname = (String) fileParrayList_prev.getFile_location();
            cluster_input.kdist_time[i]= time_feature_euclidean_dist(fname, Prevfname) ;
            cluster_input.rkdist[i] = 0.2989 * myM.clusteringTest6(garray_row[1], cluster_input.rb, c) ; // invoking the native method
            cluster_input.gkdist[i] = 0.5870 * myM.clusteringTest6(garray_row[2], cluster_input.gb, c) ; // invoking the native method
            cluster_input.bkdist[i] = 0.114 * myM.clusteringTest6(garray_row[3], cluster_input.bb, c) ; // invoking the native method


            if(cluster_input.kdist_time[i]>=1 && cluster_input.kdist_time[i] <15) {
                cluster_input.kdist[i] = Math.max(Math.max(cluster_input.rkdist[i], cluster_input.gkdist[i]), cluster_input.bkdist[i]);
                cluster_input.edge_level_dist[i] = Math.abs(cluster_input.edge_level[0] - cluster_input.edge_level[1]);

                cluster_input.LU_level_dist[i]=Math.abs(cluster_input.LUenergy_level[0] - cluster_input.LUenergy_level[1]);
                cluster_input.RU_level_dist[i]=Math.abs(cluster_input.RUenergy_level[0] - cluster_input.RUenergy_level[1]);
                cluster_input.LL_level_dist[i]=Math.abs(cluster_input.LLenergy_level[0] - cluster_input.LLenergy_level[1]);
                cluster_input.RL_level_dist[i]=Math.abs(cluster_input.RLenergy_level[0] - cluster_input.RLenergy_level[1]);

                cluster_input.Max_level_dist[i] = cluster_input.LU_level_dist[i] + cluster_input.RU_level_dist[i] + cluster_input.LL_level_dist[i] + cluster_input.RL_level_dist[i];

                cluster_input.kdist[i]=cluster_input.kdist[i] + cluster_input.edge_level_dist[i]*2 + (cluster_input.LU_level_dist[i] + cluster_input.RU_level_dist[i] +cluster_input. LL_level_dist[i] + cluster_input.RL_level_dist[i])*2 +cluster_input.kdist_time[i]* time_feat_gain;
            }
            else if (cluster_input.kdist_time[i]<1) {
                cluster_input.kdist[i]=Cluster_th-10;
            }else
                cluster_input.kdist[i]=Cluster_th+10;


            if (cluster_input.kdist[i] >= Cluster_th) {
                cluster_input.Cluster_cnt = cluster_input.Cluster_cnt + 1;
            }

            cluster_input.ClusTer[i] =cluster_input.Cluster_cnt;
        }

        System.out.printf("#Cluster score# : %s, %f%n",fname, cluster_input.kdist[i]);
        System.out.printf("#Cluster cnt# : %s, %d%n",fname,  (int)cluster_input.Cluster_cnt);

        /////////////////////////////////////////////////////////////////
            /*                          DB update                          */
        /////////////////////////////////////////////////////////////////

        engineDBInterface.updateClusterID(fname, (int) cluster_input.Cluster_cnt);



        // Updated progress bar status
        if (task != null) task.updatedProgress();
        if (againTask !=null) againTask.updatedProgress();

        cluster_input.edge_level[1]=cluster_input.edge_level[0];

        cluster_input.LUenergy_level[1] = cluster_input.LUenergy_level[0];
        cluster_input.RUenergy_level[1] = cluster_input.RUenergy_level[0];
        cluster_input.LLenergy_level[1] = cluster_input.LLenergy_level[0];
        cluster_input.RLenergy_level[1] = cluster_input.RLenergy_level[0];


        System.out.printf("#####");
        return (int)cluster_input.Cluster_cnt;
    }

    public static double time_feature_euclidean_dist(String fname, String fname_prev)
    {

        double[] feat_time=timeFeatureExtract(fname);
        double[] feat_prev_time=timeFeatureExtract(fname_prev);

        double feat_time_sum =0;
        double feat_gain[] = {3600 ,3600, 3600 ,3600, 100, 1};


        for (int m=1;m<4;m++) {
            feat_time_sum = feat_time_sum + Math.abs(feat_gain[m] * (feat_time[m] - feat_prev_time[m]));
        }

        double time_present = feat_time[4] * 60 + feat_time[5];
        double time_past = feat_prev_time[4] * 60 + feat_prev_time[5];

        double time_delta = Math.abs(time_present-time_past);
        double time_delta_sec = Math.abs(time_delta) % 60;
        double time_delta_min = (time_delta - time_delta_sec)/60;

        feat_time_sum= feat_time_sum + feat_gain[4] * time_delta_min + feat_gain[5] * time_delta_sec;



        return feat_time_sum ;

    }

    public static double[] timeFeatureExtract(String fname)
    {
        ExifInterface exif = null;
//        try {
//            exif = new ExifInterface(fname);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            exif = new ExifInterface(fname);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String exif_DATETIME = exif.getAttribute(ExifInterface.TAG_DATETIME);

        double exif_year = 0;
        double exif_month = 0;
        double exif_date = 0;
        double exif_hour = 0;
        double exif_min = 0;
        double exif_sec = 0;



        if (exif_DATETIME != null) {

            try {
                exif_year = Double.parseDouble(exif_DATETIME.substring(0, 4));
                exif_month = Double.parseDouble(exif_DATETIME.substring(5, 7));
                exif_date = Double.parseDouble(exif_DATETIME.substring(8, 10));
                exif_hour = Double.parseDouble(exif_DATETIME.substring(11, 13));
                exif_min = Double.parseDouble(exif_DATETIME.substring(14, 16));
                exif_sec = Double.parseDouble(exif_DATETIME.substring(17, 19));
            } catch (Exception e) {
                exif_year = 1997;
                exif_month = 11;
                exif_date = 11;
                exif_hour = 11;
                exif_min = 11;
                exif_sec = 11;
            }


        } else {
            exif_year = 1997;
            exif_month = 11;
            exif_date = 11;
            exif_hour = 11;
            exif_min = 11;
            exif_sec = 11;
        }

        double[] time_feature =new double[6];

        time_feature[0]=exif_year;
        time_feature[1]=exif_month;
        time_feature[2]=exif_date;
        time_feature[3]=exif_hour;
        time_feature[4]=exif_min;
        time_feature[5]=exif_sec;

        return time_feature;

    }




    /**
     *
     * @param path
     * @param sampleSize 1 = 100%, 2 = 50%(1/2), 4 = 25%(1/4), ...
     * @return
     */
    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize)
    {
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(path, options);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    public static Bitmap getResizedBitmap(Bitmap srcBitmap, int newWidth, int newHeight)
    {
        try
        {
            return Bitmap.createScaledBitmap(srcBitmap, newWidth, newHeight, true);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes)
    {
        try
        {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double[][] edgeDetection_sobel(Bitmap bmap)
    {


        double [][]Gx={{0,1,0},{1,-4,1},{0,1,0}};

        /////////////////////////////////////////////////////////
        /*               Gray scale image acquisition          */
        /////////////////////////////////////////////////////////
        double[][] garray, Gx_out, Gy_out, G_out;

        int imgW3 = bmap.getWidth();  // Height
        int imgH3 = bmap.getHeight();  // Width
        garray = new double[imgH3][imgW3];
        Gx_out = new double[imgH3][imgW3];
        Gy_out = new double[imgH3][imgW3];
        G_out = new double[imgH3][imgW3];


        double LU_energy=0;
        double RU_energy=0;
        double LL_energy=0;
        double RL_energy=0;

        double G_out_edge_level=0;

        for (int x = 0 +imgH3/4; x < imgH3-imgH3/4 ; x++)  // Width
        {
            for (int y = 0 +imgW3/4; y < imgW3-imgW3/4 ; y++)  //Height
            {
                int colour = bmap.getPixel(y, x);

                garray[x][y] =  (double)((float) Color.red(colour)) +  ( (float) Color.green(colour)) +  ((float) Color.blue(colour));

                if(x < imgH3/2 && y<= imgW3/2)
                    LU_energy = LU_energy+ garray[x][y];
                if(x >= imgH3/2 && y<= imgW3/2)
                    RU_energy = RU_energy+ garray[x][y];
                if(x < imgH3/2 && y> imgW3/2)
                    LL_energy = LL_energy+ garray[x][y];
                if(x >= imgH3/2 && y> imgW3/2)
                    RL_energy = RL_energy+ garray[x][y];


                if(x>=3+imgH3/4 && y >=1+imgH3/4 && y <=imgW3-2-imgH3/4) {

                    Gx_out[x-2][y] = garray[x - 1-2][y - 1] * Gx[0][0] + garray[x-2][y - 1] * Gx[1][0] + garray[x + 1-2][y - 1] * Gx[2][0]
                            + garray[x - 1-2][y] * Gx[0][1] + garray[x-2][y] * Gx[1][1] + garray[x + 1-2][y] * Gx[2][1]
                            + garray[x - 1-2][y + 1] * Gx[0][2] + garray[x-2][y + 1] * Gx[1][2] + garray[x + 1-2][y + 1] * Gx[2][2];


                    G_out[x-2][y] = Gx_out[x-2][y];

                }
                else
                    G_out[x][y]=0;

            }
        }

        G_out[0][0]=G_out_edge_level;
        G_out[0][5]= LU_energy / ( LU_energy+RU_energy+LL_energy+RL_energy+1);
        G_out[0][6]= RU_energy / ( LU_energy+RU_energy+LL_energy+RL_energy+1);
        G_out[0][7]= LL_energy / ( LU_energy+RU_energy+LL_energy+RL_energy+1);
        G_out[0][8]= RL_energy/ (LU_energy + RU_energy + LL_energy + RL_energy + 1);



        return G_out;
    }




    public static double[][] impreprocess_for_cluster(String fname, FaceDetector fdetector) {

        EngineDBInterface engineDBInterface = new EngineDBInterface();
        double Blurry_th = 55;
        //Bitmap bMap = getBitmapFromLocalPath(fname, 4); // 4 means 1/4 downsampling for faster processing

        /////////////////////////////////////////////////////////
                /*          Load image to bitmap                        */
        /////////////////////////////////////////////////////////
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fname, options);

        int imgHeight = options.outHeight;

        int sample_size =4;

        if(imgHeight <=1000)
            sample_size =4 ;
        else if(imgHeight >1000 && imgHeight<2000)
            sample_size =6 ;
        else if(imgHeight >=2000)
            sample_size =8 ;



        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap_options.inSampleSize = sample_size;
        Bitmap bMap_temp = BitmapFactory.decodeFile(fname, bitmap_options);

        //Bitmap bMap = BitmapFactory.decodeFile(fname, bitmap_options);
        Bitmap bMap = Bitmap.createBitmap(bMap_temp, bMap_temp.getWidth() / 6, bMap_temp.getHeight() / 6, bMap_temp.getWidth() / 6 * 5, bMap_temp.getHeight() / 6*5);

        /////////////////////////////////////////////////////////
        /*          Get image orientation and rerotation       */
        /////////////////////////////////////////////////////////
        // This is not necesary when face detection is turned off
        int orientation =1;

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(fname);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

        } catch (Exception e) {
            //e.printStackTrace();

            orientation = ExifInterface.ORIENTATION_NORMAL;
        }
        Bitmap bmRotated = rotateBitmap(bMap, orientation);

        ///////////////////////////////////////////////////////////////
        /*             Edge detection for quality score              */
        ///////////////////////////////////////////////////////////////

        int imgW = bmRotated.getWidth();  // Height
        int imgH = bmRotated.getHeight();  // Width

        double[][] sobel_out = new double[imgH][imgW];
        sobel_out = edgeDetection_sobel(bmRotated);

        double sum_sobel_out = 0;
        double var_sobel_out = 0;

        // OpenCV standard blurrying detection method.
        // http://www.pyimagesearch.com/2015/09/07/blur-detection-with-opencv/
        for (int x = imgH / 4; x < imgH - 1 - imgH / 4; x++)  // Width
            for (int y = imgW / 4; y < imgW - 1 - imgW / 4; y++)  //Height
                sum_sobel_out = sum_sobel_out + sobel_out[x][y];

        sum_sobel_out = sum_sobel_out / (imgH * imgW / 4);  // 10 is multiplied to make balance between this score and face detection score

        for (int x = imgH / 4; x < imgH - 1 - imgH / 4; x++)  // Width
            for (int y = imgW / 4; y < imgW - 1 - imgW / 4; y++)  //Height
                var_sobel_out = var_sobel_out + (sobel_out[x][y] - sum_sobel_out) * (sobel_out[x][y] - sum_sobel_out);

        var_sobel_out = (var_sobel_out / (imgH * imgW / 4) )/10000;


        /////////////////////////////////////////////////////////
        /*          FAce detection                             */
        /////////////////////////////////////////////////////////
        Frame frame = new Frame.Builder().setBitmap(bmRotated).build();
        SparseArray<Face> faces = fdetector.detect(frame);

        double avg_face_weight =0;

        double avg_faceSmile_weight =0;
        double avg_faceEyeOpen_weight =0;

        double avg_dist_from_center = 0;
        double max_dist = Math.sqrt((imgW - imgW / 2.0) * (imgW - imgW / 2.0) + (imgH - imgH / 2.0) * (imgH - imgH / 2.0));


        int Size_Face=faces.size();

        if(Size_Face>0)
        {
            for (int i = 0; i < Size_Face; ++i) {
                Face face = faces.valueAt(i);

                float face_weight1= face.getIsLeftEyeOpenProbability();
                float face_weight2 = face.getIsLeftEyeOpenProbability();
                float face_weight3= face.getIsSmilingProbability();

                float x= face.getPosition().x;
                float y = face.getPosition().y;

                double dist_from_center = Math.sqrt((x - imgW / 2.0) * (x - imgW / 2.0) + (y - imgH / 2.0) * (y - imgH / 2.0));


                avg_face_weight = avg_face_weight +face_weight1+ face_weight2+face_weight3 ;

                avg_faceSmile_weight = avg_faceSmile_weight +face_weight3 ;
                avg_faceEyeOpen_weight = avg_faceSmile_weight +face_weight1 + face_weight2 ;


                avg_dist_from_center = avg_dist_from_center + dist_from_center;

            }
            avg_faceSmile_weight=avg_faceSmile_weight/Size_Face;   //This is average smile probability
        }
        else{
            avg_faceSmile_weight =0;
            avg_face_weight =0;
            avg_faceEyeOpen_weight=0;
            avg_dist_from_center = Math.sqrt((imgW - imgW / 2.0) * (imgW - imgW / 2.0) + (imgH - imgH / 2.0) * (imgH - imgH / 2.0));
        }




        double face_dist_score =0;

        if (faces.size() == 0) {
            face_dist_score = 0.001 ;
            avg_face_weight =0;
        } else {
            face_dist_score = 1 - (avg_dist_from_center / (double) faces.size()) / max_dist ;  //Normalized and averaged face loaction with respec to the image center
            avg_face_weight =avg_face_weight/(double) faces.size();

            if(face_dist_score <0 )
                face_dist_score=0;
        }



        Log.d("Avg_dist_from_center", ":: " + String.valueOf(avg_dist_from_center));

        //
        //double score_quality = var_sobel_out;
        double score_quality = face_dist_score + avg_face_weight * 5 + var_sobel_out + faces.size() * (30*30) / (Math.sqrt((imgW - imgW / 2.0) * (imgW - imgW / 2.0) + (imgH - imgH / 2.0) * (imgH - imgH / 2.0))) ;


        System.out.printf("#face_dist_score# : %s, %f%n", fname, face_dist_score);
        System.out.printf("#avg_face_weight# : %s, %f%n", fname, avg_face_weight);
        System.out.printf("#var_sobel_out# : %s, %f%n", fname, var_sobel_out);


        System.out.printf("#score_quality score# : %s, %f%n", fname, score_quality);


        /////////////////////////////////////////////////////////
        /*                Image rescaling                      */
        /////////////////////////////////////////////////////////


        int imgW2, imgH2;

        if (imgH > imgW) {
            imgW2 = 20;   //20 for image clustering , 72 for image quality assessment
            imgH2 = 30;  //30 for image clustering , 128 for iamge quality assessment
        } // Portrait
        else {
            imgW2 = 30;  //30
            imgH2 = 20;   //20
        }//Landscape

        Bitmap bMap2 = getResizedBitmap(bmRotated, imgW2, imgH2);

        /////////////////////////////////////////////////////////
        /*               Gray scale image acquisition          */
        /////////////////////////////////////////////////////////
        //double[][] garray;
        int imgW3 = bMap2.getWidth();  // Height
        int imgH3 = bMap2.getHeight();  // Width
        //garray = new double[imgH3][imgW3];
        double[][] garray_row = new double[4][imgH3 * imgW3];
        double[][] garray_patch = new double[imgH3 /5][imgW3 /5];
        double[][] garray_xy = new double[imgH3][imgW3];
        double diff = 0;

        garray_row[0][0] = sobel_out[0][0];
        garray_row[0][1] = sobel_out[0][1];
        garray_row[0][2] = sobel_out[0][2];
        garray_row[0][3] = sobel_out[0][3];
        garray_row[0][4] = sobel_out[0][4];

        garray_row[0][5] = sobel_out[0][5] * 20;
        garray_row[0][6] = sobel_out[0][6] * 20;
        garray_row[0][7] = sobel_out[0][7] * 20;
        garray_row[0][8] = sobel_out[0][8] * 20;

        for (int x = imgH3 / 5 *2; x < imgH3 / 5*3; x++)  // Width
        {
            for (int y = imgW3 /5 *2; y < imgW3 /5 *3; y++)  //Height
            {
                int colour = bMap2.getPixel(y, x);

                garray_patch[x-imgH3 / 5 *2][y-imgW3 / 5 *2] = (float) Color.red(colour) + (float) Color.green(colour) + (float) Color.blue(colour);
            }
        }


        for (int x = 0; x < imgH3; x++)  // Width
        {
            for (int y = 0; y < imgW3; y++)  //Height
            {
                int colour = bMap2.getPixel(y, x);

                //  Reading in row direction means {1 4 2 5 3 6} , Double format.
                garray_row[1][y + x * imgW3] = (float) Color.red(colour);
                garray_row[2][y + x * imgW3] = (float) Color.green(colour);
                garray_row[3][y + x * imgW3] = (float) Color.blue(colour);
                garray_xy[x][y] = (float) Color.red(colour) + (float) Color.green(colour) + (float) Color.blue(colour);

            }
        }

        for (int m = 0; m < 5; m++) {
            for (int n = 0; n < 5; n++)  //Height
            {
                for (int x = 0; x < imgH3 / 5; x++) {
                    for (int y = 0; y < imgW3 / 5; y++)  //Height
                    {

                        diff = diff + Math.abs(garray_patch[x][y] - garray_xy[x + imgH3 / 5 * m][y + imgW3 / 5 * n]) / (255 * 2);

                    }
                }

                diff = diff*1;

            }
        }


        //////////////////////////////////////////////////////////////////
        /*                          DB update for score                 */
        //////////////////////////////////////////////////////////////////
        // Total quality score update
        engineDBInterface.updateQualityScore(fname, score_quality);
        // Face Area
        engineDBInterface.updateExtraFeatIndex(fname, Size_Face);


        // Smile probability : Temporarily using updateWeithtCoeffIndex
        engineDBInterface.updateWeightCoeffIndex(fname, avg_faceSmile_weight);


        // Burry index score update
        if (var_sobel_out < Blurry_th || diff <100) {
            engineDBInterface.updateCBlurryIndex(fname, 1);  // Image is blurred
        }


        return garray_row;
    }
    public native float[] DnnEngineJNI(String[] jargv);
    public native String DnnEngineClassJNI(String[] jargv);
    public native static void clusteringTest6Initialize();
    public native static double clusteringTest6(double[] a, double[] b, double c);
    static {
        System.loadLibrary("DNN-jni");
        System.loadLibrary("interface_jni");
    }

}
