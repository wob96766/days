/*
 * File: clusteringTest6_emxutil.h
 *
 * MATLAB Coder version            : 2.8
 * C/C++ source code generated on  : 09-Nov-2015 17:58:21
 */

#ifndef __CLUSTERINGTEST6_EMXUTIL_H__
#define __CLUSTERINGTEST6_EMXUTIL_H__

/* Include Files */
#include <math.h>
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rtwtypes.h"
#include "clusteringTest6_types.h"

/* Function Declarations */
#ifdef __cplusplus

extern "C" {

#endif

  extern void emxEnsureCapacity(emxArray__common *emxArray, int oldNumel, int
    elementSize);
  extern void emxFree_char_T(emxArray_char_T **pEmxArray);
  extern void emxInit_char_T(emxArray_char_T **pEmxArray, int numDimensions);

#ifdef __cplusplus

}
#endif
#endif

/*
 * File trailer for clusteringTest6_emxutil.h
 *
 * [EOF]
 */
