/*
 * File: dec2bin.c
 *
 * MATLAB Coder version            : 2.8
 * C/C++ source code generated on  : 09-Nov-2015 17:58:21
 */

/* Include Files */
#include "rt_nonfinite.h"
#include "clusteringTest6.h"
#include "dec2bin.h"

/* Function Definitions */

/*
 * Arguments    : double d
 *                char s_data[]
 *                int s_size[2]
 * Return Type  : void
 */
void dec2bin(double d, char s_data[], int s_size[2])
{
  int j;
  double di;
  boolean_T exitg2;
  double olddi;
  int firstcol;
  boolean_T exitg1;
  boolean_T p;
  int loop_ub;
  char b_s_data[52];
  s_size[0] = 1;
  s_size[1] = 52;
  for (j = 0; j < 52; j++) {
    s_data[j] = '0';
  }

  di = d;
  j = 52;
  exitg2 = false;
  while ((!exitg2) && (j > 0)) {
    olddi = di;
    di = floor(di / 2.0);
    if (2.0 * di < olddi) {
      s_data[j - 1] = '1';
    }

    if (di > 0.0) {
      j--;
    } else {
      exitg2 = true;
    }
  }

  firstcol = 52;
  j = 1;
  exitg1 = false;
  while ((!exitg1) && (j <= 51)) {
    p = false;
    if (s_data[j - 1] != '0') {
      p = true;
    }

    if (p) {
      firstcol = j;
      exitg1 = true;
    } else {
      j++;
    }
  }

  if (firstcol > 1) {
    for (j = firstcol; j < 53; j++) {
      s_data[j - firstcol] = s_data[j - 1];
    }

    loop_ub = 53 - firstcol;
    for (j = 0; j < loop_ub; j++) {
      b_s_data[j] = s_data[j];
    }

    s_size[0] = 1;
    s_size[1] = 53 - firstcol;
    loop_ub = 53 - firstcol;
    for (j = 0; j < loop_ub; j++) {
      s_data[j] = b_s_data[j];
    }
  }
}

/*
 * File trailer for dec2bin.c
 *
 * [EOF]
 */
