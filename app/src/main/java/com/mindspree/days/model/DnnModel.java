package com.mindspree.days.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;

import com.mindspree.days.R;
import com.mindspree.days.engine.ClusterEngine;
import com.mindspree.days.engine.EngineDBInterface;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import static com.mindspree.days.lib.AppUtils.arraylistTostringarray_nooverlap;
import static com.mindspree.days.lib.AppUtils.arraylistsize_nooverlap;

/**
 * Created by mindspree on 2016. 4. 3
 */

public class DnnModel {

    AppConfig appConfig = new AppConfig();

    public DnnModel dnnModel;

    public ArrayList DNN_result;
    public String hashString_DNN="";
    public ClusterEngine clusterEngine;

    public int language_mode = appConfig.LANGUAGE_MODE;

    public ArrayList POILIST_key;
    public ArrayList POICAT_content;

    public String [] DNN_DB1 = {"Dutch oven","wok","caldron","frying pan","Crock Pot","plate","restaurant","groom","bakery","pasta"};
    public String [] DNN_DB2 = {"lakeside","seashore","lakefront"};
    public String [] DNN_DB3 = {"volcano","cliff","valley","mountainside","alp"};
    public String [] DNN_DB4 = {"amusement park","playground"};


    public String [] POI_DB1 = {"coffee", "Coffee","카페","커피", "Starbucks", "A TWOSOME PLACE", "Cafe", "cafe"};
    public String [] POI_DB2 = {"restaurant","Restaurant","식당"};
    public String [] POI_DB3 = {"공원","PARK","Park","park"};
    public String [] POI_DB4 = {"극장","CINEMA","cinema","Cinema" ,"CGV"};
    public String [] POI_DB5 = {"Mall","mall","쇼핑몰","쇼핑","쇼핑센터","쇼핑 센터", "백화점","Department store", "department store", "shopping", "Shopping", "Store", "store"};
    public String [] POI_DB6 = {"월드","Amusement","놀이공원","놀이 공원"};
    public String [] POI_DB7 = {"mart","Mart","마트","market","Market","마켓"};

    public String [] FaceBasedPool_selfie_smile ;
    public String [] FaceBasedPool_selfie_nosmile;
    public String [] FaceBasedPool_single_smile ;
    public String [] FaceBasedPool_single_nosmile ;
    public String [] FaceBasedPool_group_smile ;
    public String [] FaceBasedPool_group_nosmile ;

    public String [] FaceBasedPool_group_selfie ;
    public String [] FaceBasedPool_group_noselfie ;


    public String [] dailysummary_nobusy ;
    public String [] dailysummary_lessbusy ;
    public String [] dailysummary_busy ;

    // Only for today activity
    public String [] FaceBasedPool_selfie_hash ;
    public String [] FaceBasedPool_singlePhoto_hash ;
    public String [] FaceBasedPool_groupPhoto_hash ;
    public String [] FaceBasedPool_groudSelfiehash ;
    public String [] FaceBasedPool_smileSingle_hash;
    public String [] FaceBasedPool_smileGroup_hash ;


    // English mode
    public String [] FaceBasedPool_selfie_smile_en ={"I took some nice selfie with beautifule smile", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
    public String [] FaceBasedPool_selfie_nosmile_en ={"I took some selfie", "I took today's best selfie. Man, I think I know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
    public String [] FaceBasedPool_single_smile_en ={"I took some photo with beautifule smile", "I took today's best photo. Everyone smiles int the photo", "I had to take some photos. This is nice place and I was so happy here"};
    public String [] FaceBasedPool_single_nosmile_en ={"I took some photos", "I took this is today's best selfie. Man, I think I need to learn how to smile", "I had to take a couple of photos. This is pretty nice place"};
    public String [] FaceBasedPool_group_smile_en = {"I took some nice group photos with my frients. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
    public String [] FaceBasedPool_group_nosmile_en = {"I took some goupt photos", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of group photos"};

    public String [] FaceBasedPool_group_selfie_en ={"I took some nice group photos with my friends. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
    public String [] FaceBasedPool_group_noselfie_en ={"I took some goupt photos", "I took today's best selfie. Forgot to smile ~", "I tried to take a couple of group photos"};


    public String [] dailysummary_nobusy_en ={"It was not that busy. ","It was just one ordinary day. ", "It was not that busy today. Maybe I should make some appointment with my friend on Friday. "};
    public String [] dailysummary_lessbusy_en ={"It was a little bit busy day. ", "Well, I did some excercise today thanks to my busy schedule"};
    public String [] dailysummary_busy_en ={"It was a super busy day. ","I went to lots of places today. I am little bit tired.", "It was so busy today. I neeed to take some rest this weekend. "};

    // Only for today activity
    public String [] FaceBasedPool_selfie_hash_en ={"So Handsome","Looking good", "Me", "My life", "My day", "My style"};
    public String [] FaceBasedPool_singlePhoto_hash_en ={"Who is that nice guy in the picture ?" , "I see you~" , "What are you doing, bro ?", "Look at me man !"};
    public String [] FaceBasedPool_groupPhoto_hash_en ={"Best People !", "Big party", "Celebration"};
    public String [] FaceBasedPool_groudSelfiehash_en ={"Handsome guys", "Gangs", "Dudes", "Thugs", "Guys"};
    public String [] FaceBasedPool_smileSingle_hash_en ={"Beautifule Smile", "Smile", "Happiness"};
    public String [] FaceBasedPool_smileGroup_hash_en ={"Oh Happy Day ~", "Fun"};



    // Korea mode
    public String [] FaceBasedPool_selfie_smile_kr ={"간만에 웃는 사진도 찍었다", "오늘 찍은 최고의 사진인 듯. 크~ 간만에 잘 나온 미소 사진", "오늘 셀피 몇장 찍었다. 좋은 곳에서 셀피 찰칵"};
    public String [] FaceBasedPool_selfie_nosmile_kr ={"간만에 셀피도 찍었다", "오늘 찍은 최고의 사진인듯. ㅎㅎ 간만에 잘나왔는데 다음엔 좀 더 표정관리를 해야겠군", "여기 온 기념으로 오늘 셀피 몇장 찍었다"};
    public String [] FaceBasedPool_single_smile_kr ={"나이스 스마일 샷 ~ 오늘도 즐거운 하루", "오늘의 베스트 샷. 다들 스마일 ~", " ㅎㅎ "};
    public String [] FaceBasedPool_single_nosmile_kr ={"무표정 시크한 셀피도 남겼다", "간만에 진지한 사진도 한 컷 남겼다", "근엄한 사진도 한 컷 찍었다"};
    public String [] FaceBasedPool_group_smile_kr = {"친구 동료들과 즐거운 사진도 찍었다", "오늘의 베스트 그룹 샷..(다들 표정이 좋네)", "오늘의 베스트 그룹 샷..(다들 표정 좋군)", "오늘도 다들 즐거운 하루를 보냈다"};
    public String [] FaceBasedPool_group_nosmile_kr = {"단체 사진을 찍었다", "오늘의 베스트 그룹 샷.", "단체 사진도 몇장 찍었다"};

    public String [] FaceBasedPool_group_selfie_kr ={"친구들과 사진도 찍고 즐거운 하루였다", "오늘의 베스트 샷. 나름 사진빨 잘 받는 듯.", "간만에 괜찮은 곳에서 그룹 사진도 몇장 찍었다"};
    public String [] FaceBasedPool_group_noselfie_kr ={"간만에 사진은 필수...", "간만에 찍은 사진인데 표정이 영 ㅎㅎ", "그룹 사진 몇장 찍어봤다"};


    public String [] dailysummary_nobusy_kr ={"어쨌든 오늘은 대략 한가한 하루였다. ","어쨌든 오늘은 대략 평범한 하루였다. ", "어쨌든 오늘은 전반적으로 무료한 하루였다. 주말에는 약속 좀 만들어 볼까. "};
    public String [] dailysummary_lessbusy_kr ={"약간 바쁜 하루였음. ", "바쁜 스케쥴 덕분에 살짝 뺑이친 하루였다. ㅜㅜ "};
    public String [] dailysummary_busy_kr ={"아 겁나 바쁜 하루였음. ","여기저기 다니느라 겁나 지치고 힘들었다.", "아 오늘은 너무 바빴네. 주말에는 푹 쉬어야겠다 "};
    public String [] dailysummary_nopoi_kr ={"오늘은 별 특별한 일이 없었다. 조금은 지루한 하루 였다. 내일은 어디라도 가야지. ","오늘은 하루 종일 집에만 있었다. 간만에 푹 쉬긴 했네", "결국 하루 종일 방콕...주말엔 어디든 나가봐야겠다. "};

    // Only for today activity
    public String [] FaceBasedPool_selfie_hash_kr ={"잘생김","최고", "나", "My life", "나의 하루", "My style"};
    public String [] FaceBasedPool_singlePhoto_hash_kr ={"저 잘생긴 친구는 누구?" , "I see you~" , "나이스", "날 보세요"};
    public String [] FaceBasedPool_groupPhoto_hash_kr ={"Best People !", "그룹 모임", "기념"};
    public String [] FaceBasedPool_groudSelfiehash_kr ={"멋진 사람들", "Gangs", "Dudes", "Thugs", "Guys"};
    public String [] FaceBasedPool_smileSingle_hash_kr ={"아름다운 미소~", "Smile", "행복"};
    public String [] FaceBasedPool_smileGroup_hash_kr ={"멋진 하루 ~", "즐거움"};





    DnnModel()
    {
        if(language_mode==1)
        {
            FaceBasedPool_selfie_smile = FaceBasedPool_selfie_smile_en ;
            FaceBasedPool_selfie_nosmile = FaceBasedPool_selfie_nosmile_en;
            FaceBasedPool_single_smile = FaceBasedPool_single_smile_en;
            FaceBasedPool_single_nosmile = FaceBasedPool_single_nosmile_en;
            FaceBasedPool_group_smile=FaceBasedPool_group_smile_en ;
            FaceBasedPool_group_nosmile=FaceBasedPool_group_nosmile_en ;

            FaceBasedPool_group_selfie =FaceBasedPool_group_selfie_en;
            FaceBasedPool_group_noselfie =FaceBasedPool_group_noselfie_en;


            dailysummary_nobusy = dailysummary_nobusy_en;
            dailysummary_lessbusy =dailysummary_lessbusy_en;
            dailysummary_busy = dailysummary_busy_en;

            // Only for today activity
            FaceBasedPool_selfie_hash = FaceBasedPool_selfie_hash_en;
            FaceBasedPool_singlePhoto_hash = FaceBasedPool_singlePhoto_hash_en;
            FaceBasedPool_groupPhoto_hash = FaceBasedPool_groupPhoto_hash_en;
            FaceBasedPool_groudSelfiehash = FaceBasedPool_groudSelfiehash_en;
            FaceBasedPool_smileSingle_hash = FaceBasedPool_smileSingle_hash_en;
            FaceBasedPool_smileGroup_hash = FaceBasedPool_smileGroup_hash_en;

        }else if(language_mode==2){

            FaceBasedPool_selfie_smile = FaceBasedPool_selfie_smile_kr ;
            FaceBasedPool_selfie_nosmile = FaceBasedPool_selfie_nosmile_kr;
            FaceBasedPool_single_smile = FaceBasedPool_single_smile_kr;
            FaceBasedPool_single_nosmile = FaceBasedPool_single_nosmile_kr;
            FaceBasedPool_group_smile=FaceBasedPool_group_smile_kr ;
            FaceBasedPool_group_nosmile=FaceBasedPool_group_nosmile_kr ;

            FaceBasedPool_group_selfie =FaceBasedPool_group_selfie_kr;
            FaceBasedPool_group_noselfie =FaceBasedPool_group_noselfie_kr;


            dailysummary_nobusy = dailysummary_nobusy_kr;
            dailysummary_lessbusy =dailysummary_lessbusy_kr;
            dailysummary_busy = dailysummary_busy_kr;

            // Only for today activity
            FaceBasedPool_selfie_hash = FaceBasedPool_selfie_hash_kr;
            FaceBasedPool_singlePhoto_hash = FaceBasedPool_singlePhoto_hash_kr;
            FaceBasedPool_groupPhoto_hash = FaceBasedPool_groupPhoto_hash_kr;
            FaceBasedPool_groudSelfiehash = FaceBasedPool_groudSelfiehash_kr;
            FaceBasedPool_smileSingle_hash = FaceBasedPool_smileSingle_hash_kr;
            FaceBasedPool_smileGroup_hash = FaceBasedPool_smileGroup_hash_kr;

        }

    }


    public void POILIST_key_set(ArrayList poi_list) {
        POILIST_key = poi_list;
    }

    public void POICAT_val_set(ArrayList poicat_list){
        POICAT_content = poicat_list;
    }

    public String poicatLUT(String poi_String){

        int index =0;
        String poicat_val ="";


        int num_loop =0;
        if(POICAT_content.size()>=POILIST_key.size())
            num_loop =POILIST_key.size();
        else
            num_loop =POICAT_content.size();

        if( (POILIST_key !=null) && (POILIST_key.size() >0) && (poi_String !=null))
        {
            for(int k=0;k< num_loop;k++)
            {

                    if(POILIST_key.get(k).toString().equals(poi_String)) {

                        if( (POICAT_content !=null) && (POICAT_content.size()>0) && (POICAT_content.get(k) !=null))
                            poicat_val = POICAT_content.get(k).toString();  // Where crash happended

                    }




            }
        }else{
            return poicat_val;
        }



        return poicat_val;

    }

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

    public String  getPOIstring(String poi_string, ArrayList DNN_result_in, DnnModel dnnModel, double avg_PhotoCreateTime, int weekend_days){

        // POI context based string generation
        //POI_DB1 : Coffe and tea
        //POI_DB2 : 식당, Restaurant
        //POI_DB3 : Park
        //POI_DB4 : Cinema
        //POI_DB5 : "shopping"
        //POI_DB6 : "놀이공원"

        String hash_string_POI="";
        DNN_result=DNN_result_in;

        if(language_mode==1) // Englinsh
        {
            if(poiclassDetect(poi_string,dnnModel.POI_DB1)){
                hash_string_POI =  String.format("%s 에서 ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","커피를 한잔 마셨다. ");
                DNN_result.add(String.format("#%s", "커피 한잔"));
            } else if(poiclassDetect(poi_string,dnnModel.POI_DB2)){
                hash_string_POI =  String.format("%s 에서 ",poi_string);

                if(avg_PhotoCreateTime>05 && avg_PhotoCreateTime< 10){
                    //Breakfast
                    hash_string_POI = hash_string_POI + String.format("%s ", "아침을 먹었다. ");
                    DNN_result.add(String.format("#%s", "아침 식사"));
                }else if(avg_PhotoCreateTime>=11 && avg_PhotoCreateTime< 14){
                    //Lunch
                    hash_string_POI = hash_string_POI +String.format("%s ", "점심을 먹었다. ");
                    DNN_result.add(String.format("#%s", "점심 식사"));
                }else if(avg_PhotoCreateTime>=10 && avg_PhotoCreateTime< 11){
                    //Lunch
                    hash_string_POI =hash_string_POI + String.format("%s ", "브런치를 먹었다. ");
                    DNN_result.add(String.format("#%s", "브런치"));
                }else if(avg_PhotoCreateTime>=17 && avg_PhotoCreateTime< 19){
                    //dinner
                    hash_string_POI = hash_string_POI +String.format("%s ", "저녁을 먹었다. ");
                    DNN_result.add(String.format("#%s", "저녁 식사"));
                }else if(avg_PhotoCreateTime>=19) {
                    //Party
                    hash_string_POI = hash_string_POI +String.format("%s ", "친구 동료들과 저녁 회식 모임을 했다. ");
                    DNN_result.add(String.format("#%s", "회식"));
                }

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB3)){
                hash_string_POI =  String.format("%s 에서 ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","산책도 하면서 쉬었다. ");
                DNN_result.add(String.format("#%s", "산책"));
            } else if(poiclassDetect(poi_string,dnnModel.POI_DB4)){

                hash_string_POI =  String.format("%s 에서 ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","영화를 관람했다 ");
                DNN_result.add(String.format("#%s", "영화 관람"));
            } else if(poiclassDetect(poi_string,dnnModel.POI_DB5)){

                hash_string_POI =  String.format("%s 에서 ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","쇼핑을 했다 ");
                DNN_result.add(String.format("#%s", "쇼핑"));
            } else if(poiclassDetect(poi_string,dnnModel.POI_DB6)){

                hash_string_POI =  String.format("%s ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","공원에 놀러 갔다 ");
                DNN_result.add(String.format("#%s", "놀이 공원"));
            }
        }
        else if(language_mode==2)  // Korean
        {
            if (poiclassDetect(poi_string, dnnModel.POI_DB1)) {
                hash_string_POI = String.format("%s 에서 ", poi_string);
                hash_string_POI = hash_string_POI + String.format("%s. ", "커피를 마셨다");
                DNN_result.add(String.format("#%s", "커피"));
            } else if (poiclassDetect(poi_string, dnnModel.POI_DB2)) {
                hash_string_POI = String.format("%s 에서 ", poi_string);

                if (avg_PhotoCreateTime > 05 && avg_PhotoCreateTime < 10) {
                    //Breakfast
                    hash_string_POI = hash_string_POI + String.format("%s ", "아침을 먹었다. ");
                    DNN_result.add(String.format("#%s", "아침 식사"));
                } else if (avg_PhotoCreateTime >= 11 && avg_PhotoCreateTime < 14) {
                    //Lunch
                    hash_string_POI = hash_string_POI + String.format("%s ", "점심을 먹었다. ");
                    DNN_result.add(String.format("#%s", "점심 식사"));
                } else if (avg_PhotoCreateTime >= 10 && avg_PhotoCreateTime < 11) {
                    //Lunch
                    hash_string_POI = hash_string_POI + String.format("%s %s ", "브런치를 먹었다. ");
                    DNN_result.add(String.format("#%s", "브런치"));
                } else if (avg_PhotoCreateTime >= 17 && avg_PhotoCreateTime < 19) {
                    //dinner
                    hash_string_POI = hash_string_POI + String.format("%s %s ", "저녁을 먹었다. ");
                    DNN_result.add(String.format("#%s", "저녁 식사"));
                } else if (avg_PhotoCreateTime >= 19) {
                    //Party
                    if (weekend_days == 0)  // Week days
                    {
                        hash_string_POI = hash_string_POI + String.format("%s ", "동료들과 저녁 모임을 했다.");
                        DNN_result.add(String.format("#%s", "저녁 회식"));
                    }else   // Weekend
                    {
                        hash_string_POI = hash_string_POI + String.format("%s ", "주말 저녁 데이트/외출을 즐겼다 .");
                        DNN_result.add(String.format("#%s", "주말 저녁 외식, 데이트"));
                    }
                }

            } else if (poiclassDetect(poi_string, dnnModel.POI_DB3)) {
                if (weekend_days == 1)  // Weekend
                {
                    hash_string_POI = hash_string_POI + String.format("%s %s %s", "주말", poi_string, " 에서 산책을 했다.");
                    DNN_result.add(String.format("#%s", "주말 공원 산책"));
                }

            } else if (poiclassDetect(poi_string, dnnModel.POI_DB4)) {

                hash_string_POI = String.format("%s에  ", poi_string);
                if (weekend_days == 1)  // Weekend
                {

                    hash_string_POI = hash_string_POI + String.format("%s. ", "영화 관람하러 극장에 왔다.(신나는 주말 극장 데이트) ");
                    DNN_result.add(String.format("#%s", "주말 극장 나들이"));


                }else{
                    if (avg_PhotoCreateTime > 19 && avg_PhotoCreateTime < 24){
                        hash_string_POI = hash_string_POI + String.format("%s. ", "간만에 영화보러 왔다.(간만 주중 영화) ");
                        DNN_result.add(String.format("#%s", "주중 극장 나들이"));
                    }

                }

            }else if (poiclassDetect(poi_string, dnnModel.POI_DB5)) {

                hash_string_POI = String.format("%s에서 ", poi_string);
                hash_string_POI = hash_string_POI + String.format("%s. ", "쇼핑을 했다");
                DNN_result.add(String.format("#%s", "쇼핑"));

            } else if (poiclassDetect(poi_string, dnnModel.POI_DB6)) {

                hash_string_POI = String.format("%s ", poi_string);
                hash_string_POI = hash_string_POI + String.format("%s. ", "놀이 공원에 갔다");
                DNN_result.add(String.format("#%s", "놀이 공원"));
            }
            else if (poiclassDetect(poi_string, dnnModel.POI_DB7)) {

                hash_string_POI = String.format("%s에서 ", poi_string);
                hash_string_POI = hash_string_POI + String.format("%s. ", "장을 봤다");
                DNN_result.add(String.format("#%s", "장보기"));
            }
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

        if (language_mode == 1) // Englinsh
        {
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


        }
        else if(language_mode ==2){
            if(poiclassDetect(poi_string,dnnModel.POI_DB1)){
                hash_string_POI=   String.format("%s %s","#커피", "#Coffee");
            } else if(poiclassDetect(poi_string,dnnModel.POI_DB2)){

                if(avg_PhotoCreateTime>05 && avg_PhotoCreateTime< 10){
                    //Breakfast
                    hash_string_POI =  String.format("%s ", "#아침 ");

                }else if(avg_PhotoCreateTime>=11 && avg_PhotoCreateTime< 14){
                    //Lunch
                    hash_string_POI = String.format("%s ", "#점심 ");

                }else if(avg_PhotoCreateTime>=10 && avg_PhotoCreateTime< 11){
                    //Lunch
                    hash_string_POI = String.format("%s ", "#브런치 ");

                }else if(avg_PhotoCreateTime>=17 && avg_PhotoCreateTime< 19){
                    //dinner
                    hash_string_POI = String.format("%s ", "#저녁 ");
                }else if(avg_PhotoCreateTime>=19) {
                    //Party
                    hash_string_POI = String.format("%s ", "#회식");
                }

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB3)){

                hash_string_POI=  String.format("%s ","#산책");

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB4)){


                hash_string_POI=String.format("%s ","#영화");

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB5)){


                hash_string_POI= String.format("%s ","#쇼핑");

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB6)){


                hash_string_POI= String.format("%s ","#놀이 공원");

            }


        }




        return hash_string_POI;
    }

    // This function is for Today hash generation
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
            if (photoCount > 3)
                photoCount = 3;

            for (int i = 0; i < photoCount; i++) {
                String timelinePhotoFile = PhotoList.get(i).toString();

                int Im_width = 0;
                int Im_height = 0;


                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(timelinePhotoFile, options);
                int imgHeight = options.outHeight;
                int imgWidth = options.outWidth;

                int sample_size = 1;
                if (imgHeight > 1000 && imgWidth > 1000)
                    sample_size = 16;
                else
                    sample_size = 1;

                BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
                bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap_options.inSampleSize = sample_size;
                Bitmap bMap_temp = BitmapFactory.decodeFile(timelinePhotoFile, bitmap_options);
                Im_width = bMap_temp.getWidth() * sample_size;

                Num_Face = engineDBInterface.getExtraFeatWithPhotoURL(timelinePhotoFile);
                Smile_Prob = engineDBInterface.getWeightCoeffWithPhotoURL(timelinePhotoFile);

                if (Num_Face == 1) {
                    if (Math.abs(front_cam_width - Im_width) < 500)
                        selfie_cnt++;
                    else if (Math.abs(rear_cam_width - Im_width) < 500)
                        singlePhoto_cnt++;
                } else if (Num_Face > 1) {
                    if (Math.abs(front_cam_width - Im_width) < 500)
                        groupSelfie_cnt++;
                    else if (Math.abs(rear_cam_width - Im_width) < 500)
                        groupPhoto_cnt++;

                } else {
                    //hash_string = hash_string + String.format("#%s ", "Oops");
                }

                if (Smile_Prob >= 0.6)
                    smile_cnt++;


            }

            int n = 0;

            if (language_mode == 1) // Englinsh
            {
                // Selfie check
                n = generator.nextInt(FaceBasedPool_selfie_hash.length);
                if (selfie_cnt > 0) {
                    hash_string = hash_string + String.format("#%d %s ", selfie_cnt, "Selfie");
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_selfie_hash[n]);
                }

                // Group photo, single photo check
                n = generator.nextInt(FaceBasedPool_singlePhoto_hash.length);
                if (singlePhoto_cnt > 0) {
                    hash_string = hash_string + String.format("#%d %s ", singlePhoto_cnt, "Photos");
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_singlePhoto_hash[n]);
                }

                n = generator.nextInt(FaceBasedPool_groupPhoto_hash.length);
                if (groupPhoto_cnt > 0) {
                    hash_string = hash_string + String.format("#%d %s", groupPhoto_cnt, "Group photos");
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_groupPhoto_hash[n]);
                }

                n = generator.nextInt(FaceBasedPool_groudSelfiehash.length);
                if (groupSelfie_cnt > 0) {
                    hash_string = hash_string + String.format("#%d %s ", selfie_cnt, "Group Selfie");
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_groudSelfiehash[n]);
                }

                // Smile detection

                if (smile_cnt == 1) {
                    n = generator.nextInt(FaceBasedPool_smileSingle_hash.length);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_smileSingle_hash[n]);
                } else if (smile_cnt > 1) {
                    n = generator.nextInt(FaceBasedPool_smileGroup_hash.length);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_smileGroup_hash[n]);
                }
            } else if(language_mode == 2) // Korean
            {

                // Selfie check
                n = generator.nextInt(FaceBasedPool_selfie_hash.length);
                if (selfie_cnt > 0) {
                    hash_string = hash_string + String.format("#%s %d장", "셀피" , selfie_cnt);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_selfie_hash[n]);
                }

                // Group photo, single photo check
                n = generator.nextInt(FaceBasedPool_singlePhoto_hash.length);
                if (singlePhoto_cnt > 0) {
                    hash_string = hash_string + String.format("#%s %d장", "사진" , selfie_cnt);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_singlePhoto_hash[n]);
                }

                n = generator.nextInt(FaceBasedPool_groupPhoto_hash.length);
                if (groupPhoto_cnt > 0) {
                    hash_string = hash_string + String.format("#%s %d장", "그룹 사진" , selfie_cnt);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_groupPhoto_hash[n]);
                }

                n = generator.nextInt(FaceBasedPool_groudSelfiehash.length);
                if (groupSelfie_cnt > 0) {
                    hash_string = hash_string + String.format("#%s %d장", "그룹 셀피" , selfie_cnt);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_groudSelfiehash[n]);
                }

                // Smile detection

                if (smile_cnt == 1) {
                    n = generator.nextInt(FaceBasedPool_smileSingle_hash.length);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_smileSingle_hash[n]);
                } else if (smile_cnt > 1) {
                    n = generator.nextInt(FaceBasedPool_smileGroup_hash.length);
                    hash_string = hash_string + String.format("#%s ", FaceBasedPool_smileGroup_hash[n]);
                }
            }




        } else {
//            hash_string = hash_string + String.format("#Nothing much ~");
        }

        System.gc();
        return hash_string;
    }


    public String POIbasedSentence(Integer [] uniqKeysArray,ArrayList DNN_result_in, String [] poiList_nooverlap, ArrayList poiList, String hash_string){

            DNN_result=DNN_result_in;
            String poicat = "";



            for(int j=0;j<poiList_nooverlap.length;j++) {

                poicat=poicatLUT(poiList_nooverlap[j]);
                if(poicat.equals(""))
                    DNN_result.add(String.format("#%s", poiList_nooverlap[j]));
                else
                    DNN_result.add(String.format("#%s, %s", poiList_nooverlap[j], poicat));
            }

            if(poiList_nooverlap.length==1){
                if(poiList_nooverlap[0].contains(AppUtils.getAppText(R.string.text_location_home))) {
                    hash_string = hash_string + String.format("%s . ", "오늘은 아무데도 가지 않고 하루종일 집에서 있었다");
                }else{
                    hash_string = hash_string + String.format("%s %s %s. ", "오늘은 주로 ", poiList_nooverlap[0], " 에만 있었다");
                }

            } else if(poiList_nooverlap.length==2){

                if(poiList_nooverlap[0].contains(AppUtils.getAppText(R.string.text_location_home)) && poiList_nooverlap[1].contains(AppUtils.getAppText(R.string.text_location_home))) {


                }else if(poiList_nooverlap[0].contains(AppUtils.getAppText(R.string.text_location_home)) || !poiList_nooverlap[1].contains(AppUtils.getAppText(R.string.text_location_home))){


                    hash_string = hash_string + String.format("오늘 나는 %s에 갔다. ", poiList_nooverlap[1]);


                }else if(!poiList_nooverlap[0].contains(AppUtils.getAppText(R.string.text_location_home)) || poiList_nooverlap[1].contains(AppUtils.getAppText(R.string.text_location_home))){
                    hash_string = hash_string + String.format("오늘 나는 %s에 갔다. ", poiList_nooverlap[0]);

                }else{
                    for (int k=0;k<2;k++){

                        if(k==0)
                            hash_string = hash_string + String.format("오늘 %s 에도 가고 ", poiList_nooverlap[k]);
                        else{
                            if(poiList.size() > k)
                                hash_string = hash_string + String.format("%s 에도 갔다. ", poiList_nooverlap[k]);
                        }

                    }
                }



            }else if(poiList_nooverlap.length>2) {

                // this part describes which places I went to
                hash_string = hash_string + String.format("%s.", "오늘은 여기 저기 돌아다녔다 ");

                for (int l = 0; l < poiList_nooverlap.length; l++) {

                    if (l == 0) {

                        if (poiList_nooverlap[l].contains(AppUtils.getAppText(R.string.text_location_home))) {

                        } else
                            hash_string = hash_string + String.format("%s", poiList_nooverlap[l]);

                    } else if (l < poiList_nooverlap.length - 1) {
                        hash_string = hash_string + String.format(", %s ", poiList_nooverlap[l]);
                    } else if (l == poiList_nooverlap.length - 1) {
                        hash_string = hash_string + String.format("그리고 %s에도 들렸다. ", poiList_nooverlap[l]);
                    }
                }
            }



                // this part describes where I took photos
                if(uniqKeysArray!=null)
                {

                    hash_string = hash_string + String.format("\n");

                    if(uniqKeysArray.length==1){
                        int index_key= uniqKeysArray[0];
                        if(index_key >= poiList.size())
                            index_key = poiList.size()-1;

                        if(uniqKeysArray[0].toString().contains(AppUtils.getAppText(R.string.text_location_home))){
                            hash_string = hash_string + String.format("집에서 사진도 몇 장 찍었다");
                        }else{
                            hash_string = hash_string + String.format(" %s 에서 사진도 몇 장 찍었다.", poiList.get(index_key).toString());
                        }
                    }else if (uniqKeysArray.length>=2) {

                        // Build unique "poi with photo" list
                        ArrayList<String> poiListwithPhoto = new ArrayList<>();
                        for (int l = 0; l < uniqKeysArray.length; l++) {
                            int index_key = uniqKeysArray[l];
                            if (index_key >= poiList.size())
                                index_key = poiList.size() - 1;

                            if (poiList.get(index_key).toString() != null)
                                poiListwithPhoto.add(poiList.get(index_key).toString());
                        }



                            int array_size = arraylistsize_nooverlap(poiListwithPhoto);
                            String[] poiListwithPhoto_nooverlap = new String[array_size];
                            poiListwithPhoto_nooverlap = arraylistTostringarray_nooverlap(poiListwithPhoto);

                            if(poiListwithPhoto_nooverlap.length==1){
                                if(poiListwithPhoto_nooverlap[0].contains(AppUtils.getAppText(R.string.text_location_home)))
                                    hash_string = hash_string + String.format("집에서 사진도 몇 장 찍었다");
                                else
                                    hash_string = hash_string + String.format(" %s 에서 사진도 몇 장 찍었다.", poiListwithPhoto_nooverlap[0]);

                            }else if(poiListwithPhoto_nooverlap.length==2){

                                for (int l = 0; l < poiListwithPhoto_nooverlap.length; l++) {
                                    if (l == 0) {
                                        if (uniqKeysArray[l].toString().contains(AppUtils.getAppText(R.string.text_location_home)))
                                            hash_string = hash_string + String.format("집 ");
                                        else
                                            hash_string = hash_string + String.format("%s ", poiListwithPhoto_nooverlap[l]);

                                    }else
                                        hash_string = hash_string + String.format("그리고 %s 에서는 사진도 몇 장 남겼다. ", poiListwithPhoto_nooverlap[l]);

                                }

                            }else if(poiListwithPhoto_nooverlap.length>2){

                                for (int l = 0; l < poiListwithPhoto_nooverlap.length; l++) {
                                    if (l == 0) {
                                        if (uniqKeysArray[l].toString().contains(AppUtils.getAppText(R.string.text_location_home)))
                                            hash_string = hash_string + String.format("집 ");
                                        else
                                            hash_string = hash_string + String.format("%s ", poiListwithPhoto_nooverlap[l]);

                                    }else if (l < uniqKeysArray.length - 1)
                                        hash_string = hash_string + String.format(", %s ", poiListwithPhoto_nooverlap[l]);
                                    else if (l == uniqKeysArray.length - 1)
                                        hash_string = hash_string + String.format("그리고 %s 에서는 사진도 몇 장 남겼다. ", poiListwithPhoto_nooverlap[l]);
                                }

                            }


                    }

                }




        return hash_string;

    }



    public String SentenceFromPhoto_korean(ClusterEngine clusterEngine, int offset,int size,ArrayList DNN_result_in, String poi_string, ArrayList PhotoList,  int front_cam_width, int rear_cam_width, String [] DNN_path, int weekend_days)
    {
        String hash_string = "";
        ArrayList<String> hashList = new ArrayList<String>();
        String hash_string_DNN= "";
        String hash_string_POI= "";
        DnnModel dnnModel = new DnnModel();
        Random generator = new Random();
        double avg_PhotoCreateTime =0;

        EngineDBInterface engineDBInterface = new EngineDBInterface();

        // POI context based string generation
        //POI_DB1 : Coffe and tea
        //POI_DB2 : 식당, Restaurant
        //POI_DB3 : Park
        //POI_DB4 : Cinema
        //POI_DB5 : "shopping"
        //POI_DB6 : "놀이공원"
        for (int i = offset; i < offset + size; i++){
            String timelinePhotoFile = PhotoList.get(i).toString();
            double temp[] = clusterEngine.timeFeatureExtract(timelinePhotoFile);
            avg_PhotoCreateTime=avg_PhotoCreateTime+temp[3];
        }
        avg_PhotoCreateTime=avg_PhotoCreateTime/size;

        // POI context based sentence
        boolean POI_DB1_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB1);
        boolean POI_DB2_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB2);
        boolean POI_DB3_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB3);
        boolean POI_DB4_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB4);
        boolean POI_DB5_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB5);
        boolean POI_DB6_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB6);

        // POI category & name based sentence gen
        hash_string_POI = dnnModel.getPOIstring(poi_string, DNN_result_in, dnnModel, avg_PhotoCreateTime, weekend_days);
        hash_string=hash_string+hash_string_POI;

        int sentence_cnt =0;

        for (int i = offset; i < offset + size; i++){
            String timelinePhotoFile = PhotoList.get(i).toString();

            int Im_width=0;
            int Im_height=0;
            float Num_Face=0;
            float Smile_Prob=0;
            int selfie_cnt=0;
            int singlePhoto_cnt=0;
            int groupSelfie_cnt=0;
            int groupPhoto_cnt=0;
            int smile_cnt=0;
            int n = 0;

            // Face detectioin : Face number. Eye close. Smile probability
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(timelinePhotoFile, options);
            int imgHeight = options.outHeight;
            int imgWidth = options.outWidth;
            int sample_size =1;

            if(imgHeight <=300)
                sample_size =1 ;
            else if(imgHeight <=1000)
                sample_size =4 ;
            else if(imgHeight >1000 && imgHeight<2000)
                sample_size =6 ;
            else if(imgHeight >=2000)
                sample_size =8 ;

            BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
            bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap_options.inSampleSize = sample_size;
            Bitmap bMap_temp = BitmapFactory.decodeFile(timelinePhotoFile, bitmap_options);

            if(bMap_temp==null)
                return hash_string;

            Num_Face =  engineDBInterface.getExtraFeatWithPhotoURL(timelinePhotoFile);
            Smile_Prob =  engineDBInterface.getWeightCoeffWithPhotoURL(timelinePhotoFile);

            // Selfie, solo , group
            if( Num_Face == 1) {
                if( Math.abs(front_cam_width - imgWidth) < 500)
                    selfie_cnt++;
                else if ( Math.abs(rear_cam_width - imgWidth) < 500)
                    singlePhoto_cnt ++;
            }
            else if( Num_Face >1) {

                if( Math.abs(front_cam_width - imgWidth) < 500)
                    groupSelfie_cnt++;
                else if ( Math.abs(rear_cam_width - imgWidth) < 500)
                    groupPhoto_cnt++;

            }

            if( Smile_Prob >= 0.6)
                smile_cnt++;


            // Selfie check
            if (selfie_cnt > 0) {
                // Smile detection
                n = generator.nextInt(dnnModel.FaceBasedPool_selfie_smile.length);
                if (smile_cnt > 0) {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_selfie_smile[n]));
                    DNN_result.add(String.format("#%s", "스마일 셀피"));
                } else {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_selfie_nosmile[n]));
                    DNN_result.add(String.format("#%s", "셀피"));
                }
            }

            // Group photo, single photo check
            if (singlePhoto_cnt > 0) {
                // Smile detection
                n = generator.nextInt(dnnModel.FaceBasedPool_single_smile.length);
                if (smile_cnt > 0) {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_single_smile[n]));
                    DNN_result.add(String.format("#%s", "스마일 인물 사진"));
                } else {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_single_nosmile[n]));
                    DNN_result.add(String.format("#%s", "인물 사진"));
                }
            }

            if (groupPhoto_cnt > 0) {
                // Smile detection
                n = generator.nextInt(dnnModel.FaceBasedPool_group_smile.length);
                if (smile_cnt > 0) {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_group_smile[n]));
                } else {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_group_nosmile[n]));
                }
                DNN_result.add(String.format("#%s", "단체사진"));
            }

            if (groupSelfie_cnt > 0) {
                // Smile detection
                n = generator.nextInt(dnnModel.FaceBasedPool_group_selfie.length);
                if (smile_cnt > 0) {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_group_selfie[n]));
                    DNN_result.add(String.format("#%s", "단체 스마일 셀피"));
                } else {
                    hashList.add(String.format("%s", dnnModel.FaceBasedPool_group_noselfie[n]));
                    DNN_result.add(String.format("#%s", "단체 셀피"));
                }
            }

            // This is to interpret non humand photos such as food and landscape
            // Do nothing at this point
            //4. Deep learning engine
            // It detects food, mountain, cliff, river, sea, seashore only
            File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/days_resample_images"); //
            String days_moment_resample_image ;
            days_moment_resample_image=resampleandsave_single(i, myDir, timelinePhotoFile);

            // Run neural network
            // This is for classification
            String DNN_test_path_resample =days_moment_resample_image;
            String[] jargv =new String[7];
            jargv[0] ="classifier_Class";
            jargv[1] ="predictCustom";  // This is for classification
            jargv[2] =DNN_path[0];
            jargv[3] =DNN_path[1];
            jargv[4] =DNN_path[2];
            jargv[5] =DNN_test_path_resample;
            jargv[6] = outDir+"/";

            String class_predict = null;
            Boolean foodClass =false;
            Boolean WaterClass =false;
            Boolean MounatainClass=false ;
            Boolean PlayClass =false;

            if((!POI_DB2_DETECT || !POI_DB6_DETECT || selfie_cnt==0 || groupPhoto_cnt==0 || groupSelfie_cnt==0 ||singlePhoto_cnt==0) && (i == offset)) {
                class_predict = DnnEngineClassJNI(jargv);
                foodClass = classDetect(class_predict, dnnModel.DNN_DB1);
                WaterClass = classDetect(class_predict, dnnModel.DNN_DB2);
                MounatainClass = classDetect(class_predict, dnnModel.DNN_DB3);
                PlayClass = classDetect(class_predict, dnnModel.DNN_DB4);
                System.gc();
            }


            double [] temp_time = clusterEngine.timeFeatureExtract(timelinePhotoFile);
            double pic_time = temp_time[3];

            if(foodClass && pic_time!=1997 && !POI_DB2_DETECT)
            {

                if(pic_time>05 && pic_time< 10){
                    //Breakfast
                    hash_string_DNN = String.format("%s에서 %s ", poi_string, "아침을 먹었다. " );
                    class_predict =String.format("#%s #%s","아침", poi_string);
                }else if(pic_time>=11 && pic_time< 14){
                    //Lunch
                    hash_string_DNN = String.format("%s에서 %s ", poi_string, "점심을 먹었다. " );
                    class_predict =String.format("#%s #%s","점심", poi_string);
                }else if(pic_time>=17 && pic_time< 19){
                    //dinner
                    hash_string_DNN = String.format("%s에서 %s ", poi_string, "저녁을 먹었다. " );
                    class_predict =String.format("#%s #%s","저녁", poi_string);
                }else if(pic_time>=19) {
                    //Party
                    hash_string_DNN = String.format("%s에서 %s ", poi_string, "저녁 회식을 했다.");
                    class_predict =String.format("#%s #%s","회식", poi_string);
                }

                DNN_result.add(class_predict);

            }else if(WaterClass){
                hash_string_DNN = "오늘 야외에서 물놀이를 했다. ";
                class_predict =String.format("#%s #%s","물놀이", poi_string);
                DNN_result.add(class_predict);
            }else if(MounatainClass){
                hash_string_DNN = "오늘 산에서 즐거운 시간을 보냈다. ";
                class_predict =String.format("#%s #%s","hiking", poi_string);
                DNN_result.add(class_predict);
            }else if(PlayClass && !POI_DB6_DETECT){
                hash_string_DNN = "놀이 공원에 가서 신나게 놀았다.";
                class_predict =String.format("#%s #%s","놀이 공원", poi_string);
                DNN_result.add(class_predict);
            }

            sentence_cnt++;

        }

        // Insert hashList overllapping removal
        String connection="";
        int hashList_size= hashList.size();
        if(hashList_size>0){
            // Get the unique hash result,  remove overlapping
            int array_size = arraylistsize_nooverlap(hashList);
            String [] hashList_nooverlap = new String[array_size];
            hashList_nooverlap= arraylistTostringarray_nooverlap(hashList);
            int hashList_nooverlap_size= hashList.size();

            for(int m=0;m<hashList_nooverlap_size;m++){

                if(hashList_nooverlap_size==1)
                    connection=". ";
                else if(hashList_nooverlap_size==2 && m==0)
                    connection=" 그리고";
                else if(hashList_nooverlap_size==2 && m==1)
                    connection=". ";
                else if(hashList_nooverlap_size>2 && m<size-2)
                    connection=", ";
                else if(hashList_nooverlap_size>2 && m==size-2)
                    connection=" 그리고";
                else if(hashList_nooverlap_size>2 && m==size-1)
                    connection=". ";
                else
                    connection="";

                hash_string= hash_string + hashList_nooverlap[m] + connection;
            }
        }


        hash_string = hash_string + hash_string_DNN;
        return hash_string;
    }



    public String resampleandsave_single(int index, File myDir, String DNN_test_path_input) {

        String days_moment_resample_image=null;


        File files = new File(DNN_test_path_input);
        if (files.exists()) {
            Bitmap bm = AppUtils.downsampleImageFile(DNN_test_path_input, 122, 149);


            if(myDir.exists() && myDir.isDirectory()) {
                // do nothing
            }else{
                myDir.mkdirs();
            }

            String fname = "Days_Moment_" + index + ".days";
            File file = new File(myDir, fname);
            days_moment_resample_image=file.toString();

            if (file.exists())
                file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return days_moment_resample_image;
    }

    public boolean classDetect(String class_predict, String [] Class_DB){

        boolean result=false;

        for(int i=0;i<Class_DB.length;i++){
            String temp = Class_DB[i];
            if(class_predict.equals(Class_DB[i])){
                result = true;
                break;
            }else {
                result = false;

            }

        }

        return result;

    }





    public native String DnnEngineClassJNI(String[] jargv);
    static {
        System.loadLibrary("DNN-jni");
    }

}
