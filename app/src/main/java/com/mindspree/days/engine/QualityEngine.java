package com.mindspree.days.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;


public class QualityEngine {

    public static void runQualityEngine() {


    }


    /**
     * @param path
     * @param sampleSize 1 = 100%, 2 = 50%(1/2), 4 = 25%(1/4), ...
     * @return
     */
    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }

        return null;
    }

    public static Bitmap getResizedBitmap(Bitmap srcBitmap, int newWidth, int newHeight) {
        try {
            return Bitmap.createScaledBitmap(srcBitmap, newWidth, newHeight, true);
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     * @param bytes
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes) {
        try {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
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
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap bitmapFromArray(int[][] pixels2d){
        int width = pixels2d.length;
        int height = pixels2d[0].length;
        int[] pixels = new int[width * height];
        int pixelsIndex = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                pixels[pixelsIndex] = pixels2d[i][j];
                pixelsIndex ++;
            }
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    public static double[][] edgeDetection_sobel(Bitmap bmap)
    {

        // double [][]Gx=new double[3][3];
        // double [][]Gy=new double[3][3];
        double [][]Gx={{-1,0,1},{-2,0,2},{-1,0,1}};
        double [][]Gy={{-1,-2,-1},{0,0,0},{1,2,1}};


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


        for (int x = 0; x < imgH3; x++)  // Width
        {
            for (int y = 0; y < imgW3; y++)  //Height
            {
                int colour = bmap.getPixel(y, x);
                garray[x][y] =  (double)(0.2989 * (float) Color.red(colour)) +  (0.5870 * (float) Color.green(colour)) +  (0.114 * (float) Color.blue(colour));

            }
        }

        for (int x = 1; x < imgH3-1; x++)  // Width
        {
            for (int y = 1; y < imgW3-1; y++)  //Height
            {

                Gx_out[x][y] = garray[x-1][y-1] * Gx[0][0] + garray[x][y-1] * Gx[1][0] + garray[x+1][y-1] * Gx[2][0]
                        + garray[x-1][y] * Gx[0][1] + garray[x][y] * Gx[1][1] + garray[x+1][y] * Gx[2][1]
                        + garray[x-1][y+1] * Gx[0][2] + garray[x][y+1] * Gx[1][2] + garray[x+1][y+1] * Gx[2][2];

                Gy_out[x][y] = garray[x-1][y-1] * Gy[0][0] + garray[x][y-1] * Gy[1][0] + garray[x+1][y-1] * Gy[2][0]
                        + garray[x-1][y] * Gy[0][1] + garray[x][y] * Gy[1][1] + garray[x+1][y] * Gy[2][1]
                        + garray[x-1][y+1] * Gy[0][2] + garray[x][y+1] * Gy[1][2] + garray[x+1][y+1] * Gy[2][2];

                G_out[x][y] =  Math.sqrt( Math.pow(Gx_out[x][y],2) +  Math.pow(Gy_out[x][y],2) )/255;

            }
        }


        for (int x = 1; x < imgH3-1; x++)  // Width
        {
            for (int y = 1; y < imgW3-1; y++)  //Height
            {
                if (G_out[x][y]>=0.8)
                    G_out[x][y] = 1;
                else
                    G_out[x][y] = 0;
            }
        }


        return G_out;
    }

}
