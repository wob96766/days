/*
 * File: clusteringTest6_types.h
 *
 * MATLAB Coder version            : 2.8
 * C/C++ source code generated on  : 09-Nov-2015 17:58:21
 */

#ifndef __CLUSTERINGTEST6_TYPES_H__
#define __CLUSTERINGTEST6_TYPES_H__

/* Include Files */
#include "rtwtypes.h"

/* Type Definitions */
#ifndef struct_emxArray__common
#define struct_emxArray__common

struct emxArray__common
{
  void *data;
  int *size;
  int allocatedSize;
  int numDimensions;
  boolean_T canFreeData;
};

#endif                                 /*struct_emxArray__common*/

#ifndef typedef_emxArray__common
#define typedef_emxArray__common

typedef struct emxArray__common emxArray__common;

#endif                                 /*typedef_emxArray__common*/

#ifndef struct_emxArray_char_T
#define struct_emxArray_char_T

struct emxArray_char_T
{
  char *data;
  int *size;
  int allocatedSize;
  int numDimensions;
  boolean_T canFreeData;
};

#endif                                 /*struct_emxArray_char_T*/

#ifndef typedef_emxArray_char_T
#define typedef_emxArray_char_T

typedef struct emxArray_char_T emxArray_char_T;

#endif                                 /*typedef_emxArray_char_T*/
#endif

/*
 * File trailer for clusteringTest6_types.h
 *
 * [EOF]
 */
