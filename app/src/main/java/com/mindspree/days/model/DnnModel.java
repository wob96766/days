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


     public String [] FaceBasedPool_selfie_smile ={"I took some nice selfie with beautifule smile", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
     public String [] FaceBasedPool_selfie_nosmile ={"I took some selfie.", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of selfies. This is nice place and I was happy here"};
     public String [] FaceBasedPool_single_smile ={"I took some photo with beautifule smile", "I took today's best photo. Everyone smiles int the photo", "I had to take some photos. This is nice place and I was so happy here"};
     public String [] FaceBasedPool_single_nosmile ={"I took some photos", "I took this is today's best selfie. Man, I think I need to learn how to smile", "I had to take a couple of photos. This is pretty nice place"};
     public String [] FaceBasedPool_group_smile = {"I took some nice group photos with my frients. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
     public String [] FaceBasedPool_group_nosmile = {"I took some goupt photos", "I took today's best selfie. Man, I think I know know how to smile", "I had to take a couple of group photos"};

    public String [] FaceBasedPool_group_selfie ={"I took some nice group photos with my frients. It was so fun", "I took today's best selfie. Believe or not I know know how to smile", "I had to take a smiling group photos cause this is nice place"};
    public String [] FaceBasedPool_group_noselfie ={"I took some goupt photos", "I took today's best selfie. Forgot to smile ~", "I tried to take a couple of group photos"};


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
