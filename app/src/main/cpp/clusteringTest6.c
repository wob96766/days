/*
 * File: clusteringTest6.c
 *
 * MATLAB Coder version            : 2.8
 * C/C++ source code generated on  : 09-Nov-2015 17:58:21
 */

/* Include Files */
#include "rt_nonfinite.h"
#include "clusteringTest6.h"
#include "dct.h"
#include "huffman_ac.h"
#include "clusteringTest6_emxutil.h"

/* Function Definitions */

/*
 * This part is for clustering picture and currently empty
 * Arguments    : const double image[600]
 *                double prev_feature[144]
 *                double isFirst
 * Return Type  : double
 */
double clusteringTest6(const double image[600], double prev_feature[144], double
  isFirst)
{
  double kdist;
  unsigned int feature[144];
  double zig_zag_ac[9072];
  int k;
  double image_aspect[9216];
  int i;
  int i0;
  int b_i;
  int j;
  int b_j;
  double b_image_aspect[64];
  double dv0[64];
  double T[64];
  int m;
  double b_T[64];
  static const unsigned char y[64] = { 16U, 12U, 14U, 14U, 18U, 24U, 49U, 72U,
    11U, 12U, 13U, 17U, 22U, 36U, 64U, 92U, 10U, 14U, 16U, 22U, 37U, 55U, 78U,
    95U, 16U, 19U, 24U, 29U, 56U, 64U, 87U, 98U, 24U, 26U, 40U, 51U, 68U, 81U,
    103U, 112U, 40U, 58U, 57U, 87U, 109U, 194U, 121U, 100U, 51U, 60U, 69U, 80U,
    103U, 113U, 120U, 103U, 61U, 55U, 56U, 62U, 77U, 92U, 101U, 99U };

  static const signed char iv0[63] = { 8, 1, 2, 9, 16, 24, 17, 10, 3, 4, 11, 18,
    25, 32, 40, 33, 26, 19, 12, 5, 6, 13, 20, 27, 34, 41, 48, 56, 49, 42, 35, 28,
    21, 14, 7, 15, 22, 29, 36, 43, 50, 57, 58, 51, 44, 37, 30, 23, 31, 38, 45,
    52, 59, 60, 53, 46, 39, 47, 54, 61, 62, 55, 63 };

  double b_zig_zag_ac[10];
  emxArray_char_T *r160;
  double b_y[144];
  double c_y;
  memset(&feature[0], 0, 144U * sizeof(unsigned int));
  memset(&zig_zag_ac[0], 0, 9072U * sizeof(double));
  k = -1;

  /* % Adjust Aspect Ratio */
  memset(&image_aspect[0], 0, 9216U * sizeof(double));
  for (i = 0; i < 30; i++) {
    memcpy(&image_aspect[26 + 72 * (49 + i)], &image[20 * i], 20U * sizeof
           (double));
  }

  for (i0 = 0; i0 < 9216; i0++) {
    image_aspect[i0] -= 128.0;
  }

  /* % Segmenting Image Blocks Of 8x8 */
  for (i = 0; i < 9; i++) {
    b_i = i << 3;
    for (j = 0; j < 16; j++) {
      b_j = j << 3;

      /* DCT2 2-D discrete cosine transform. */
      /*    B = DCT2(A) returns the discrete cosine transform of A. */
      /*    The matrix B is the same size as A and contains the */
      /*    discrete cosine transform coefficients. */
      /*  */
      /*    B = DCT2(A,[M N]) or B = DCT2(A,M,N) pads the matrix A with */
      /*    zeros to size M-by-N before transforming. If M or N is */
      /*    smaller than the corresponding dimension of A, DCT2 truncates */
      /*    A.  */
      /*  */
      /*    This transform can be inverted using IDCT2. */
      /*  */
      /*    Class Support */
      /*    ------------- */
      /*    A can be numeric or logical. The returned matrix B is of  */
      /*    class double. */
      /*  */
      /*    Example */
      /*    ------- */
      /*        RGB = imread('autumn.tif'); */
      /*        I = rgb2gray(RGB); */
      /*        J = dct2(I); */
      /*        imshow(log(abs(J)),[]), colormap(jet), colorbar */
      /*  */
      /*    The commands below set values less than magnitude 10 in the */
      /*    DCT matrix to zero, then reconstruct the image using the */
      /*    inverse DCT function IDCT2. */
      /*  */
      /*        J(abs(J)<10) = 0; */
      /*        K = idct2(J); */
      /*        figure, imshow(I) */
      /*        figure, imshow(K,[0 255]) */
      /*  */
      /*    See also FFT2, IDCT2, IFFT2. */
      /*      if(kdist>=th) */
      /*          cnt=cnt+1; */
      /*      end */
      /*  end */
      /*  clust = cnt; */
      /*    Copyright 1992-2005 The MathWorks, Inc. */
      /*    References:  */
      /*         1) A. K. Jain, "Fundamentals of Digital Image */
      /*            Processing", pp. 150-153. */
      /*         2) Wallace, "The JPEG Still Picture Compression Standard", */
      /*            Communications of the ACM, April 1991. */
      /*  Basic algorithm. */
      for (i0 = 0; i0 < 8; i0++) {
        memcpy(&b_image_aspect[i0 << 3], &image_aspect[b_i + 72 * (i0 + b_j)],
               sizeof(double) << 3);
      }

      dct(b_image_aspect, dv0);
      for (i0 = 0; i0 < 8; i0++) {
        for (m = 0; m < 8; m++) {
          T[m + (i0 << 3)] = dv0[i0 + (m << 3)];
        }
      }

      dct(T, dv0);
      k++;
      for (i0 = 0; i0 < 8; i0++) {
        for (m = 0; m < 8; m++) {
          b_T[m + (i0 << 3)] = dv0[i0 + (m << 3)];
          T[m + (i0 << 3)] = b_T[m + (i0 << 3)] / (double)y[m + (i0 << 3)];
        }
      }

      for (i0 = 0; i0 < 63; i0++) {
        zig_zag_ac[k + 144 * i0] = T[iv0[i0]];
      }
    }
  }

  /* % Huffman Compression and feature  */
  for (i0 = 0; i0 < 10; i0++) {
    b_zig_zag_ac[i0] = zig_zag_ac[144 * i0];
  }

  emxInit_char_T(&r160, 2);
  huffman_ac(b_zig_zag_ac, r160);
  feature[0] = (unsigned int)r160->size[1];
  for (m = 1; m - 1 < k; m++) {
    for (i0 = 0; i0 < 10; i0++) {
      b_zig_zag_ac[i0] = zig_zag_ac[m + 144 * i0];
    }

    huffman_ac(b_zig_zag_ac, r160);
    feature[m] = (unsigned int)r160->size[1];
  }

  emxFree_char_T(&r160);

  /* % Clustering */
  /*  if h > 0 */
  if (isFirst != 1.0) {
    for (i0 = 0; i0 < 144; i0++) {
      prev_feature[i0] = (double)feature[i0] - prev_feature[i0];
      b_y[i0] = prev_feature[i0] * prev_feature[i0];
    }

    c_y = b_y[0];
    for (k = 0; k < 143; k++) {
      c_y += b_y[k + 1];
    }

    kdist = c_y / 50.0;
  } else {
    kdist = 0.0;
  }

  for (i0 = 0; i0 < 144; i0++) {
    prev_feature[i0] = feature[i0];
  }

  return kdist;
}

/*
 * File trailer for clusteringTest6.c
 *
 * [EOF]
 */
