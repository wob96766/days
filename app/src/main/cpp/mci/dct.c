/*
 * File: dct.c
 *
 * MATLAB Coder version            : 2.8
 * C/C++ source code generated on  : 09-Nov-2015 17:58:21
 */

/* Include Files */
#include "rt_nonfinite.h"
#include "clusteringTest6.h"
#include "dct.h"

/* Type Definitions */
#ifndef struct_dsp_DCT_0
#define struct_dsp_DCT_0

struct dsp_DCT_0
{
  int S0_isInitialized;
  int W0_IDXWKSPACE[8];
  double P0_ScaleFactor;
};

#endif                                 /*struct_dsp_DCT_0*/

#ifndef typedef_dsp_DCT_0
#define typedef_dsp_DCT_0

typedef struct dsp_DCT_0 dsp_DCT_0;

#endif                                 /*typedef_dsp_DCT_0*/

#ifndef typedef_dspcodegen_DCT
#define typedef_dspcodegen_DCT

typedef struct {
  int isInitialized;
  dsp_DCT_0 cSFunObject;
} dspcodegen_DCT;

#endif                                 /*typedef_dspcodegen_DCT*/

/* Function Declarations */
static void MWDSPCG_BitReverseData_YD(double y[], const int N, const int stride,
  const int startIdx);
static void MWDSPCG_Dct8_YD_Trig(double y[], const int elem[]);
static void MWDSPCG_IntroduceStride(int elem[], const int N, const int stride,
  int elemIdx);

/* Function Definitions */

/*
 * Arguments    : double y[]
 *                const int N
 *                const int stride
 *                const int startIdx
 * Return Type  : void
 */
static void MWDSPCG_BitReverseData_YD(double y[], const int N, const int stride,
  const int startIdx)
{
  int j;
  int iIdx;
  int i;
  int jIdx;
  double t;
  j = 0;
  iIdx = startIdx;
  for (i = 0; i < N - 1; i++) {
    /* swap bit-reversed pairs - do not double swap */
    if (i < j) {
      /* swap when i<j */
      jIdx = startIdx + j * stride;
      t = y[iIdx];
      y[iIdx] = y[jIdx];
      y[jIdx] = t;
    }

    jIdx = N >> 1;
    j ^= jIdx;
    while (!((j & jIdx) != 0)) {
      jIdx >>= 1;
      j ^= jIdx;
    }

    iIdx += stride;
  }
}

/*
 * Arguments    : double y[]
 *                const int elem[]
 * Return Type  : void
 */
static void MWDSPCG_Dct8_YD_Trig(double y[], const int elem[])
{
  int i;
  double acc;
  double t;

  /* Stage 1 */
  for (i = 0; i < 4; i++) {
    /* butterfly */
    acc = y[elem[i]];
    acc += y[elem[7 - i]];
    t = acc;
    acc = y[elem[i]];
    acc -= y[elem[7 - i]];
    y[elem[7 - i]] = acc;
    y[elem[i]] = t;
  }

  /* Stage 2 */
  for (i = 0; i < 2; i++) {
    /* butterfly */
    acc = y[elem[i]];
    acc += y[elem[3 - i]];
    t = acc;
    acc = y[elem[i]];
    acc -= y[elem[3 - i]];
    y[elem[3 - i]] = acc;
    y[elem[i]] = t;
  }

  /* cos(pi/4) */
  /* butterfly */
  acc = y[elem[6]];
  acc -= y[elem[5]];
  t = acc * 0.70710678118654757;
  acc = y[elem[6]];
  acc += y[elem[5]];
  y[elem[6]] = acc * 0.70710678118654757;
  y[elem[5]] = t;

  /* Stage 3 */
  /* butterfly */
  acc = y[elem[0]];
  acc += y[elem[1]];
  t = acc * 0.70710678118654757;
  acc = y[elem[0]];
  acc -= y[elem[1]];
  y[elem[1]] = acc * 0.70710678118654757;
  y[elem[0]] = t;

  /* cos(pi/8) */
  /* sin(pi/8) */
  /* butterfly */
  acc = y[elem[2]] * 0.38268343236508978;
  acc += y[elem[3]] * 0.92387953251128674;
  t = acc;
  acc = y[elem[3]] * 0.38268343236508978;
  acc += y[elem[2]] * -0.92387953251128674;
  y[elem[3]] = acc;
  y[elem[2]] = t;

  /* butterfly */
  acc = y[elem[4]];
  acc += y[elem[5]];
  t = acc;
  acc = y[elem[4]];
  acc -= y[elem[5]];
  y[elem[5]] = acc;
  y[elem[4]] = t;

  /* butterfly */
  acc = y[elem[7]];
  acc -= y[elem[6]];
  t = acc;
  acc = y[elem[7]];
  acc += y[elem[6]];
  y[elem[7]] = acc;
  y[elem[6]] = t;

  /* Stage 4 */
  /* cos(pi/16) */
  /* sin(pi/16) */
  /* butterfly */
  acc = y[elem[4]] * 0.19509032201612825;
  acc += y[elem[7]] * 0.98078528040323043;
  t = acc;
  acc = y[elem[7]] * 0.19509032201612825;
  acc += y[elem[4]] * -0.98078528040323043;
  y[elem[7]] = acc;
  y[elem[4]] = t;

  /* cos(3*pi/16) */
  /* sin(3*pi/16) */
  /* butterfly */
  acc = y[elem[5]] * 0.83146961230254524;
  acc += y[elem[6]] * 0.55557023301960218;
  t = acc;
  acc = y[elem[6]] * 0.83146961230254524;
  acc += y[elem[5]] * -0.55557023301960218;
  y[elem[6]] = acc;
  y[elem[5]] = t;
}

/*
 * Arguments    : int elem[]
 *                const int N
 *                const int stride
 *                int elemIdx
 * Return Type  : void
 */
static void MWDSPCG_IntroduceStride(int elem[], const int N, const int stride,
  int elemIdx)
{
  int i;
  for (i = 0; i < N; i++) {
    /* step by stride starting from elemIdx passed in */
    elem[i] = elemIdx;
    elemIdx += stride;
  }
}

/*
 * Arguments    : const double x_in[64]
 *                double y[64]
 * Return Type  : void
 */
void dct(const double x_in[64], double y[64])
{
  dspcodegen_DCT s;
  dspcodegen_DCT *obj;
  dsp_DCT_0 *b_obj;
  int idx;
  int highIdx;
  double b_y;
  obj = &s;
  s.isInitialized = 0;

  /* System object Constructor function: dsp.DCT */
  obj->cSFunObject.P0_ScaleFactor = 0.5;
  obj = &s;
  if (s.isInitialized != 1) {
    s.isInitialized = 1;
  }

  b_obj = &s.cSFunObject;

  /* System object Outputs function: dsp.DCT */
  memcpy(&y[0], &x_in[0], sizeof(double) << 6);
  idx = 0;
  for (highIdx = 0; highIdx < 8; highIdx++) {
    MWDSPCG_IntroduceStride((int *)&b_obj->W0_IDXWKSPACE[0U], 8, 1, idx);
    MWDSPCG_Dct8_YD_Trig((double *)&y[0U], (int *)&b_obj->W0_IDXWKSPACE[0U]);
    MWDSPCG_BitReverseData_YD((double *)&y[0U], 8, 1, idx);
    idx += 8;
  }

  for (idx = 0; idx < 64; idx++) {
    b_y = y[idx] * obj->cSFunObject.P0_ScaleFactor;
    y[idx] = b_y;
  }
}

/*
 * File trailer for dct.c
 *
 * [EOF]
 */
