/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>

#include <time.h>
#include <stdlib.h>
#include <stdio.h>

#include "parser.h"
#include "utils.h"
#include "cuda.h"
#include "blas.h"
#include "connected_layer.h"

#include "data.h"

#include <errno.h>
#include <android/asset_manager.h>


extern float * main(int argc, char **argv);

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   hello-jni/app/src/main/java/com/example/hellojni/HelloJni.java
 */


float max_array(float a[], int num_elements)
{
    int i;
    float max=-32000.0;
    for (i=0; i<num_elements; i++)
    {
        if (a[i]>max)
        {
            max=a[i];
        }
    }
    return(max);

}


JNIEXPORT jfloatArray JNICALL Java_com_mindspree_days_engine_ClusterEngine_DnnEngineJNI( JNIEnv* env, jobject thiz , jobjectArray jargv)
{
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
    #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
    #define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    jfloatArray result;
    result = (*env) -> NewFloatArray(env, 6272);
    if (result == NULL) {
        return NULL; /* out of memory error thrown */
    }



    // Get the number of args
    jsize ArgCount = (*env)->GetArrayLength(env, jargv);

    // malloc the array of char* to be passed to the legacy main
    char ** argv = malloc(sizeof(char*)*(ArgCount+1)); // +1 for fake program name at index 0
    argv[ 0 ] = "MyProgramName";

    int i;
    for ( i = 0; i < ArgCount; ++i ) {

        jstring string = (jstring)((*env)->GetObjectArrayElement(env, jargv, i));
        const char *cstring = (*env)->GetStringUTFChars(env, string, 0);
        argv[ i + 1 ] = strdup( cstring );
        (*env)->ReleaseStringUTFChars(env, string, cstring );
        (*env)->DeleteLocalRef(env, string );
    }




    float * feat_return = main(ArgCount+1, argv);



    // cleanup
    for( i = 0; i < ArgCount; ++i ) free( argv[ i + 1 ] );
    free( argv );


//    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");


    (*env)->SetFloatArrayRegion(env, result, 0, 6272, feat_return);
    return result;
}



JNIEXPORT jstring JNICALL Java_com_mindspree_days_engine_ClusterEngine_DnnEngineClassJNI( JNIEnv* env, jobject thiz , jobjectArray jargv)
{
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
    #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
    #define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    jfloatArray result;
    result = (*env) -> NewFloatArray(env, 1000);
    if (result == NULL) {
        return NULL; /* out of memory error thrown */
    }



    // Get the number of args
    jsize ArgCount = (*env)->GetArrayLength(env, jargv);

    // malloc the array of char* to be passed to the legacy main
    char ** argv = malloc(sizeof(char*)*(ArgCount+1)); // +1 for fake program name at index 0
    argv[ 0 ] = "MyProgramName";

    int i;
    for ( i = 0; i < ArgCount; ++i ) {

        jstring string = (jstring)((*env)->GetObjectArrayElement(env, jargv, i));
        const char *cstring = (*env)->GetStringUTFChars(env, string, 0);
        argv[ i + 1 ] = strdup( cstring );
        (*env)->ReleaseStringUTFChars(env, string, cstring );
        (*env)->DeleteLocalRef(env, string );
    }




    float * feat_return = main(ArgCount+1, argv);


    int n = 1000; //Size of array feat_return
    float max_val= max_array(feat_return, n);
    int index=0;

    for(i=0;i<1000;i++){
        if(feat_return[i]==max_val)
            index=i;
    }
    // Add classfication logic here

    // Get labels
    char *name_list = "/storage/emulated/0/Download/data/imagenet.shortnames.list";
    char **names = get_labels(name_list);

    jstring test2 = names[index];

    // cleanup
    for( i = 0; i < ArgCount; ++i ) free( argv[ i + 1 ] );
    free( argv );


    return (*env)->NewStringUTF(env, test2);

}




JNIEXPORT jstring JNICALL Java_com_mindspree_days_model_DatelineModel_DnnEngineClassJNI( JNIEnv* env, jobject thiz , jobjectArray jargv)
{
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
    #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
    #define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    jfloatArray result;
    result = (*env) -> NewFloatArray(env, 1000);
    if (result == NULL) {
        return NULL; /* out of memory error thrown */
    }



    // Get the number of args
    jsize ArgCount = (*env)->GetArrayLength(env, jargv);

    // malloc the array of char* to be passed to the legacy main
    char ** argv = malloc(sizeof(char*)*(ArgCount+1)); // +1 for fake program name at index 0
    argv[ 0 ] = "MyProgramName";

    int i;
    for ( i = 0; i < ArgCount; ++i ) {

        jstring string = (jstring)((*env)->GetObjectArrayElement(env, jargv, i));
        const char *cstring = (*env)->GetStringUTFChars(env, string, 0);
        argv[ i + 1 ] = strdup( cstring );
        (*env)->ReleaseStringUTFChars(env, string, cstring );
        (*env)->DeleteLocalRef(env, string );
    }




    float * feat_return = main(ArgCount+1, argv);


    int n = 1000; //Size of array feat_return
    float max_val= max_array(feat_return, n);
    int index=0;

    for(i=0;i<1000;i++){
        if(feat_return[i]==max_val)
            index=i;
    }

    // Add classfication logic here

    // Get labels
    char *name_list = "/storage/emulated/0/Download/data/imagenet.shortnames.list";
    char **names = get_labels(name_list);

    jstring test2 = names[index];

    // cleanup
    for( i = 0; i < ArgCount; ++i ) free( argv[ i + 1 ] );
    free( argv );


    return (*env)->NewStringUTF(env, test2);

}

JNIEXPORT jstring JNICALL Java_com_mindspree_days_model_DnnModel_DnnEngineClassJNI( JNIEnv* env, jobject thiz , jobjectArray jargv)
{
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
    #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
    #define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    jfloatArray result;
    result = (*env) -> NewFloatArray(env, 1000);
    if (result == NULL) {
        return NULL; /* out of memory error thrown */
    }



    // Get the number of args
    jsize ArgCount = (*env)->GetArrayLength(env, jargv);

    // malloc the array of char* to be passed to the legacy main
    char ** argv = malloc(sizeof(char*)*(ArgCount+1)); // +1 for fake program name at index 0
    argv[ 0 ] = "MyProgramName";

    int i;
    for ( i = 0; i < ArgCount; ++i ) {

        jstring string = (jstring)((*env)->GetObjectArrayElement(env, jargv, i));
        const char *cstring = (*env)->GetStringUTFChars(env, string, 0);
        argv[ i + 1 ] = strdup( cstring );
        (*env)->ReleaseStringUTFChars(env, string, cstring );
        (*env)->DeleteLocalRef(env, string );
    }




    float * feat_return = main(ArgCount+1, argv);


    int n = 1000; //Size of array feat_return
    float max_val= max_array(feat_return, n);
    int index=0;

    for(i=0;i<1000;i++){
        if(feat_return[i]==max_val)
            index=i;
    }

    // Add classfication logic here

    // Get labels
    char *name_list = "/storage/emulated/0/Download/data/imagenet.shortnames.list";
    char **names = get_labels(name_list);

    jstring test2 = names[index];

    // cleanup
    for( i = 0; i < ArgCount; ++i ) free( argv[ i + 1 ] );
    free( argv );


    return (*env)->NewStringUTF(env, test2);

}
