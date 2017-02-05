package com.mindspree.days.model;

import com.mindspree.days.engine.EngineDBInterface;

import java.util.ArrayList;

/**
 * Created by vision51 on 2016. 12. 20..
 */

public class Cluster_input {
    EngineDBInterface engineDBInterface = null;
    public int numberOfAllPhotos ;

    public int task_flag =0;

    public double Cluster_cnt;
    public double Last_cluster;

    public ArrayList PrevfileArrayList ;
    public int size_PrevfileArrayList ;

    public PhotosTableModel PrevClusterLastfile;

    public double [] ClusTer ;
    public double [] kdist ;

    public double [] rkdist ;
    public double [] gkdist ;
    public double [] bkdist ;

    public double [] kdist_time ;
    public double [] edge_level_dist;

    public double [] LU_level_dist ;
    public double [] RU_level_dist ;
    public double [] LL_level_dist ;
    public double [] RL_level_dist ;
    public double [] Max_level_dist ;

    public double[] rb ;  // Feature Array
    public  double[] gb ;  // Feature Array
    public double[] bb ;  // Feature Array
    public double[] edge_level;

    public  double[] LUenergy_level;
    public double[] RUenergy_level;
    public double[] LLenergy_level;
    public double[] RLenergy_level;

    public void createVar(ArrayList fileArrayList)
    {
        engineDBInterface = new EngineDBInterface();
        numberOfAllPhotos = engineDBInterface.getNumberOfAllPhotos();

        Cluster_cnt= engineDBInterface.getNextClusterId();
        Last_cluster = engineDBInterface.getLastClusterId();

        PrevfileArrayList = null;
        size_PrevfileArrayList =0;

        ClusTer = new double[fileArrayList.size()];
        kdist = new double[fileArrayList.size()];
        rkdist = new double[fileArrayList.size()];
        gkdist = new double[fileArrayList.size()];
        bkdist = new double[fileArrayList.size()];

        kdist_time = new double[fileArrayList.size()];
        edge_level_dist = new double[fileArrayList.size()];

        LU_level_dist = new double[fileArrayList.size()];
        RU_level_dist = new double[fileArrayList.size()];
        LL_level_dist = new double[fileArrayList.size()];
        RL_level_dist = new double[fileArrayList.size()];
        Max_level_dist = new double[fileArrayList.size()];

        PrevClusterLastfile = new PhotosTableModel();
        rb = new double[144];  // Feature Array
        gb = new double[144];  // Feature Array
        bb = new double[144];  // Feature Array
        edge_level= new double[2];

        LUenergy_level= new double[2];
        RUenergy_level= new double[2];
        LLenergy_level= new double[2];
        RLenergy_level= new double[2];

    }
}
