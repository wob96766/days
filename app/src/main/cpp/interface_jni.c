/* Deploying MATLAB code to Deploying MATLAB code to lightweight platforms
%% Copyright 2013 - 2013 The MathWorks, Inc. */

/* #include <stdio.h> */

#include "interface_jni.h" /* autogenerated JNI header */

#include "clusteringTest6_initialize.h"

#include "clusteringTest6.h"


/*
 * Class:     example
 * Method:    clusteringTest6
 * Signature: ([D[DD)D
 */

void Java_com_mindspree_days_engine_ClusterEngine_clusteringTest6Initialize( JNIEnv* env,
                                                          jobject thiz )
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
#elif defined(__mips64)
    #define ABI "mips64"
#elif defined(__mips__)
    #define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
    #define ABI "unknown"
#endif

    clusteringTest6_initialize();

}



jdouble Java_com_mindspree_days_engine_ClusterEngine_clusteringTest6
  (JNIEnv *env, jclass cls, jdoubleArray a, jdoubleArray b, jdouble c)
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
#elif defined(__mips64)
    #define ABI "mips64"
#elif defined(__mips__)
    #define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
    #define ABI "unknown"
#endif


    jdouble kdist;
    /* copying JAVA array to C array */
    jdouble* ac = (*env)->GetDoubleArrayElements(env,a,0);
    jdouble* bc = (*env)->GetDoubleArrayElements(env,b,0);

    kdist = clusteringTest6(ac, bc,c);
        
    /* deleting temporary C array */
    (*env)->ReleaseDoubleArrayElements(env, a, ac, 0);
    (*env)->ReleaseDoubleArrayElements(env, b, bc, 0);
    return kdist;
}


