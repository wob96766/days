package com.mindspree.days.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mindspree.days.R;
import com.mindspree.days.engine.EngineDBInterface;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mindspree on 2016. 4. 3
 */

public class DnnModel {
     public String [] DNN_DB1 = {"Dutch oven","wok","caldron","frying pan","Crock Pot","plate","restaurant","groom","bakery","pasta"};
     public String [] DNN_DB2 = {"lakeside","seashore","lakefront"};
     public String [] DNN_DB3 = {"volcano","cliff","valley","mountainside","alp"};
     public String [] DNN_DB4 = {"amusement park","playground"};


    public String [] POI_DB1 = {"coffee", "Coffee","카페","커피", "Starbucks", "A TWOSOME PLACE"};
    public String [] POI_DB2 = {"restaurant","Restaurant","식당"};
    public String [] POI_DB3 = {"공원","PARK","Park","park"};
    public String [] POI_DB4 = {"극장","CINEMA","cinema","Cinema" ,"CGV"};
    public String [] POI_DB5 = {"Mall","mall","쇼핑몰","쇼핑","쇼핑센터","쇼핑 센터", "백화점","Department store", "department store"};
    public String [] POI_DB6 = {"월드","Amusement","놀이공원","놀이 공원"};


     public String [] FaceBasedPool_selfie_smile ={"I took some nice selfie with beautifule smile", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
     public String [] FaceBasedPool_selfie_nosmile ={"I took some selfie", "I took today's best selfie. Man, I think I know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
     public String [] FaceBasedPool_single_smile ={"I took some photo with beautifule smile", "I took today's best photo. Everyone smiles int the photo", "I had to take some photos. This is nice place and I was so happy here"};
     public String [] FaceBasedPool_single_nosmile ={"I took some photos", "I took this is today's best selfie. Man, I think I need to learn how to smile", "I had to take a couple of photos. This is pretty nice place"};
     public String [] FaceBasedPool_group_smile = {"I took some nice group photos with my frients. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
     public String [] FaceBasedPool_group_nosmile = {"I took some goupt photos", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of group photos"};

    public String [] FaceBasedPool_group_selfie ={"I took some nice group photos with my friends. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
    public String [] FaceBasedPool_group_noselfie ={"I took some goupt photos", "I took today's best selfie. Forgot to smile ~", "I tried to take a couple of group photos"};


    public String [] dailysummary_nobusy ={"It was not that busy. ","It was just one ordinary day. ", "It was not that busy today. Maybe I should make some appointment with my friend on Friday. "};
    public String [] dailysummary_lessbusy ={"It was a little bit busy day. ", "Well, I did some excercise today thanks to my busy schedule"};
    public String [] dailysummary_busy ={"It was a super busy day. ","I went to lots of places today. I am little bit tired.", "It was so busy today. I neeed to take some rest this weekend. "};

    // Only for today activity
    public String [] FaceBasedPool_selfie_hash ={"So Handsome","Looking good", "Me", "My life", "My day", "My style"};
    public String [] FaceBasedPool_singlePhoto_hash ={"Who is that nice guy in the picture ?" , "I see you~" , "What are you doing, bro ?", "Look at me man !"};
    public String [] FaceBasedPool_groupPhoto_hash ={"Best People !", "Big party", "Celebration"};
    public String [] FaceBasedPool_groudSelfiehash ={"Handsome guys", "Gangs", "Dudes", "Thugs", "Guys"};
    public String [] FaceBasedPool_smileSingle_hash ={"Beautifule Smile", "Smile", "Happiness"};
    public String [] FaceBasedPool_smileGroup_hash ={"Oh Happy Day ~", "Fun", ""};

    public Random generator = new Random();

    public boolean poiclassDetect(String poi_String, String [] Class_DB){

        boolean result=false;

        for(int i=0;i<Class_DB.length;i++){
            String temp = Class_DB[i];
            if(poi_String.contains(Class_DB[i])){
                result = true;
                break;
            }else {
                result = false;

            }

        }

        return result;

    }

    public String  getPOIstring(String poi_string, DnnModel dnnModel, double avg_PhotoCreateTime){

        // POI context based string generation
        //POI_DB1 : Coffe and tea
        //POI_DB2 : 식당, Restaurant
        //POI_DB3 : Park
        //POI_DB4 : Cinema
        //POI_DB5 : "shopping"
        //POI_DB6 : "놀이공원"

        String hash_string_POI="";



        if(poiclassDetect(poi_string,dnnModel.POI_DB1)){
            hash_string_POI =  String.format("In %s ",poi_string);
            hash_string_POI= hash_string_POI + String.format("%s. ","I had some coffee");
        } else if(poiclassDetect(poi_string,dnnModel.POI_DB2)){
            hash_string_POI =  String.format("In %s ",poi_string);

            if(avg_PhotoCreateTime>05 && avg_PhotoCreateTime< 10){
                //Breakfast
                hash_string_POI = hash_string_POI + String.format("%s ", "I had a breakfast. ");

            }else if(avg_PhotoCreateTime>=11 && avg_PhotoCreateTime< 14){
                //Lunch
                hash_string_POI = hash_string_POI +String.format("%s ", "I had a lunch. ");

            }else if(avg_PhotoCreateTime>=10 && avg_PhotoCreateTime< 11){
                //Lunch
                hash_string_POI =hash_string_POI + String.format("%s %s ", "I had a brunch. ");

            }else if(avg_PhotoCreateTime>=17 && avg_PhotoCreateTime< 19){
                //dinner
                hash_string_POI = hash_string_POI +String.format("%s %s ", "I had a dinner. ");
            }else if(avg_PhotoCreateTime>=19) {
                //Party
                hash_string_POI = hash_string_POI +String.format("%s ", "I had a party with my friends and colleagues");
            }

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB3)){
            hash_string_POI =  String.format("In %s ",poi_string);
            hash_string_POI= hash_string_POI + String.format("%s. ","I walked with my friends and took some rest");

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB4)){

            hash_string_POI =  String.format("In %s ",poi_string);
            hash_string_POI= hash_string_POI + String.format("%s. ","I watched movie with my friend");

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB5)){

            hash_string_POI =  String.format("In %s ",poi_string);
            hash_string_POI= hash_string_POI + String.format("%s. ","I did some shopping");

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB6)){

            hash_string_POI =  String.format("In %s ",poi_string);
            hash_string_POI= hash_string_POI + String.format("%s. ","I went to amusement park");

        }





        return hash_string_POI;
    }

    public String  getPOIhash(String poi_string, DnnModel dnnModel, double avg_PhotoCreateTime){

        // POI context based string generation
        //POI_DB1 : Coffe and tea
        //POI_DB2 : 식당, Restaurant
        //POI_DB3 : Park
        //POI_DB4 : Cinema
        //POI_DB5 : "shopping"
        //POI_DB6 : "놀이공원"

        String hash_string_POI="";



        if(poiclassDetect(poi_string,dnnModel.POI_DB1)){
            hash_string_POI=   String.format("%s ","#Coffee ");
            hash_string_POI = hash_string_POI + String.format("In %s ",poi_string);
        } else if(poiclassDetect(poi_string,dnnModel.POI_DB2)){

            if(avg_PhotoCreateTime>05 && avg_PhotoCreateTime< 10){
                //Breakfast
                hash_string_POI =  String.format("%s ", "#Breakfast ");

            }else if(avg_PhotoCreateTime>=11 && avg_PhotoCreateTime< 14){
                //Lunch
                hash_string_POI = String.format("%s ", "#Lunch ");

            }else if(avg_PhotoCreateTime>=10 && avg_PhotoCreateTime< 11){
                //Lunch
                hash_string_POI = String.format("%s ", "#Brunch ");

            }else if(avg_PhotoCreateTime>=17 && avg_PhotoCreateTime< 19){
                //dinner
                hash_string_POI = String.format("%s ", "#Dinner ");
            }else if(avg_PhotoCreateTime>=19) {
                //Party
                hash_string_POI = String.format("%s ", "#Dinner party");
            }
            hash_string_POI =  hash_string_POI + String.format("In %s ",poi_string);

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB3)){

            hash_string_POI=  String.format("%s ","#Walking");
            hash_string_POI = hash_string_POI +  String.format("In %s ",poi_string);

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB4)){


            hash_string_POI=String.format("%s ","#Movie");
            hash_string_POI = hash_string_POI +  String.format("In %s ",poi_string);

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB5)){


            hash_string_POI= String.format("%s ","#Shopping");
            hash_string_POI = hash_string_POI +  String.format("In %s ",poi_string);

        } else if(poiclassDetect(poi_string,dnnModel.POI_DB6)){


            hash_string_POI= String.format("%s ","#Amusement Park");
            hash_string_POI = hash_string_POI +  String.format("In %s ",poi_string);

        }





        return hash_string_POI;
    }

    public String hashFromFace(ArrayList PhotoList, int front_cam_width, int rear_cam_width)
    {
        String hash_string = "";
        int photoCount = PhotoList.size();
        float Num_Face=0;
        float Smile_Prob=0;
        int selfie_cnt=0;
        int singlePhoto_cnt=0;
        int groupSelfie_cnt=0;
        int groupPhoto_cnt=0;
        int smile_cnt=0;


        EngineDBInterface engineDBInterface = new EngineDBInterface();

        if(photoCount > 0) {
            // Extract the name of the representing photo
            //String PhotoString = getPhotoString();
            // Extract timeline data



            //photoCount=1; // for debugging
            if(photoCount>3)
                photoCount=3;

            for (int i = 0; i < photoCount; i++){
                String timelinePhotoFile = PhotoList.get(i).toString();

                int Im_width=0;
                int Im_height=0;



                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(timelinePhotoFile, options);
                int imgHeight = options.outHeight;
                int imgWidth = options.outWidth;

                int sample_size =1;
                if(imgHeight > 1000 && imgWidth>1000)
                    sample_size =32;
                else
                    sample_size =1;

                BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
                bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap_options.inSampleSize = sample_size;
                Bitmap bMap_temp = BitmapFactory.decodeFile(timelinePhotoFile, bitmap_options);
                Im_width = bMap_temp.getWidth() * sample_size ;

                Num_Face =  engineDBInterface.getExtraFeatWithPhotoURL(timelinePhotoFile);
                Smile_Prob =  engineDBInterface.getWeightCoeffWithPhotoURL(timelinePhotoFile);

                if( Num_Face == 1) {
                    if( Math.abs(front_cam_width - Im_width) < 500)
                        selfie_cnt++;
                    else if ( Math.abs(rear_cam_width - Im_width) < 500)
                        singlePhoto_cnt ++;
                }
                else if( Num_Face >1) {
                    if( Math.abs(front_cam_width - Im_width) < 500)
                        groupSelfie_cnt++;
                    else if ( Math.abs(rear_cam_width - Im_width) < 500)
                        groupPhoto_cnt++;

                }else {
                    //hash_string = hash_string + String.format("#%s ", "Oops");
                }

                if( Smile_Prob >= 0.6)
                    smile_cnt++;




            }

            int n =0;

            // Selfie check
            n = generator.nextInt(FaceBasedPool_selfie_hash.length);
            if(selfie_cnt > 0){
                hash_string = hash_string + String.format("#%d %s ", selfie_cnt, "Selfie");
                hash_string = hash_string + String.format("#%s ", FaceBasedPool_selfie_hash[n]);
            }

            // Group photo, single photo check
            n = generator.nextInt(FaceBasedPool_singlePhoto_hash.length);
            if(singlePhoto_cnt >0) {
                hash_string = hash_string + String.format("#%d %s ", singlePhoto_cnt, "Photos");
                hash_string = hash_string + String.format("#%s ", FaceBasedPool_singlePhoto_hash[n]);
            }

            n = generator.nextInt(FaceBasedPool_groupPhoto_hash.length);
            if(groupPhoto_cnt > 0){
                hash_string = hash_string + String.format("#%d %s", groupPhoto_cnt, "Group photos");
                hash_string = hash_string + String.format("#%s ", FaceBasedPool_groupPhoto_hash[n]);
            }

            n = generator.nextInt(FaceBasedPool_groudSelfiehash.length);
            if(groupSelfie_cnt >0) {
                hash_string = hash_string + String.format("#%d %s ", selfie_cnt, "Group Selfie");
                hash_string = hash_string + String.format("#%s ", FaceBasedPool_groudSelfiehash[n]);
            }

            // Smile detection

            if(smile_cnt ==1) {
                n = generator.nextInt(FaceBasedPool_smileSingle_hash.length);
                hash_string = hash_string + String.format("#%s ", FaceBasedPool_smileSingle_hash[n]);
            }else if(smile_cnt >1) {
                n = generator.nextInt(FaceBasedPool_smileGroup_hash.length);
                hash_string = hash_string + String.format("#%s ", FaceBasedPool_smileGroup_hash[n]);
            }


        } else {
//            hash_string = hash_string + String.format("#Nothing much ~");
        }


        return hash_string;
    }

}
