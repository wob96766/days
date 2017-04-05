package com.mindspree.days.model;

import com.mindspree.days.R;

/**
 * Created by mindspree on 2016. 12. 30..
 */

public class DnnModel {
     public String [] DNN_DB1 = {"Dutch oven","wok","caldron","frying pan","Crock Pot","plate","restaurant","groom","bakery","pasta"};
     public String [] DNN_DB2 = {"lakeside","seashore","lakefront"};
     public String [] DNN_DB3 = {"volcano","cliff","valley","mountainside","alp"};
     public String [] DNN_DB4 = {"amusement park","playground"};


    public String [] POI_DB1 = {"coffee", "Coffee","카페","커피", "Starbucks", "A TWOSOME PLACE"};
    public String [] POI_DB2 = {"restaurant","Restaurant","식당"};
    public String [] POI_DB3 = {"공원","PARK","Park","park"};
    public String [] POI_DB4 = {"극장","CINEMA","cinema","Cinema"};
    public String [] POI_DB5 = {"Mall","mall","쇼핑몰","쇼핑","쇼핑센터","쇼핑 센터", "백화점","Department store", "department store"};
    public String [] POI_DB6 = {"월드","Amusement","놀이공원","놀이 공원"};


     public String [] FaceBasedPool_selfie_smile ={"I took some nice selfie with beautifule smile", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
     public String [] FaceBasedPool_selfie_nosmile ={"I took some selfie", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
     public String [] FaceBasedPool_single_smile ={"I took some photo with beautifule smile", "I took today's best photo. Everyone smiles int the photo", "I had to take some photos. This is nice place and I was so happy here"};
     public String [] FaceBasedPool_single_nosmile ={"I took some photos", "I took this is today's best selfie. Man, I think I need to learn how to smile", "I had to take a couple of photos. This is pretty nice place"};
     public String [] FaceBasedPool_group_smile = {"I took some nice group photos with my frients. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
     public String [] FaceBasedPool_group_nosmile = {"I took some goupt photos", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of group photos"};

    public String [] FaceBasedPool_group_selfie ={"I took some nice group photos with my friends. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
    public String [] FaceBasedPool_group_noselfie ={"I took some goupt photos", "I took today's best selfie. Forgot to smile ~", "I tried to take a couple of group photos"};


    public String [] dailysummary_nobusy ={"It was not that busy. ","It was just one ordinary day. ", "It was not that busy today. Maybe I should make some appointment with my friend on Friday. "};
    public String [] dailysummary_lessbusy ={"It was a little bit busy day. ", "Well, I did some excercise today thanks to my busy schedule"};
    public String [] dailysummary_busy ={"It was a super busy day. ","I went to lots of places today. I am little bit tired.", "It was so busy today. I neeed to take some rest this weekend. "};

}
//
//
//// Selfie check
//                    if (selfie_cnt > 0) {
//                            // Smile detection
//                            hash_string = hash_string + String.format("In %s ",poi_string);
//
//                            if (smile_cnt > 0) {
//                            hash_string = hash_string + String.format("%s %s ", "I took some nice selfie with beautifule smile", connection);
//                            } else {
//                            hash_string = hash_string + String.format("%s %s ", "I took some selfie", connection);
//                            }
//                            }
//
//                            // Group photo, single photo check
//                            if (singlePhoto_cnt > 0) {
//
//                            // Smile detection
//                            hash_string = hash_string + String.format("In %s ",poi_string);
//
//                            if (smile_cnt > 0) {
//                            hash_string = hash_string + String.format("%s %s ", "I took some nice photo of my buddy with big smile", connection);
//                            } else {
//                            hash_string = hash_string + String.format("%s %s ", "I took some nice photo of my buddy", connection);
//
//                            }
//                            }
//
//                            if (groupPhoto_cnt > 0) {
//
//                            // Smile detection
//                            hash_string = hash_string + String.format("In %s ",poi_string);
//
//                            if (smile_cnt > 0) {
//                            hash_string = hash_string + String.format("%s %s ", "I took some nice group photos. What a beautiful smile !", connection);
//                            } else {
//                            hash_string = hash_string + String.format("%s %s ", "I took some nice group photos.", connection);
//
//                            }
//
//                            }
//
//                            if (groupSelfie_cnt > 0) {
//                            // Smile detection
//                            hash_string = hash_string + String.format("In %s ",poi_string);
//
//                            if (smile_cnt > 0) {
//                            hash_string = hash_string + String.format("%s %s ", "I took some nice selfie with my buddies. Everybody happy", connection);
//                            } else {
//                            hash_string = hash_string + String.format("%s %s ", "I took some nice selfie with my buddies", connection);
//                            }
//
//                            }
