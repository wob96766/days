cmake_minimum_required(VERSION 3.4.1)

add_library(DNN-jni SHARED
            DNN-jni.c darknet.c activation_layer.c activations.c art.c avgpool_layer.c batchnorm_layer.c blas.c box.c captcha.c cifar.c classifier.c coco.c col2im.c compare.c connected_layer.c convolutional_layer.c cost_layer.c crnn_layer.c crop_layer.c cuda.c data.c demo.c deconvolutional_layer.c detection_layer.c detector.c dice.c dropout_layer.c gemm.c go.c gru_layer.c im2col.c image.c layer.c list.c local_layer.c matrix.c maxpool_layer.c network.c nightmare.c normalization_layer.c option_list.c parser.c rnn_vid.c rnn.c rnn_layer.c region_layer.c reorg_layer.c route_layer.c shortcut_layer.c softmax_layer.c super.c swag.c tag.c tree.c utils.c voxel.c writing.c yolo.c)

add_library(interface_jni SHARED
            interface_jni.c clusteringTest6.c clusteringTest6_emxutil.c clusteringTest6_initialize.c clusteringTest6_terminate.c dct.c dec2bin.c huffman_ac.c rt_nonfinite.c rtGetInf.c rtGetNaN.c)

# Include libraries needed for DNN-jni lib
target_link_libraries(DNN-jni
                      android
                      log)

# Include libraries needed for hello-jni lib
target_link_libraries(interface_jni
                      android
                      log)