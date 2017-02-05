/*
 * File: huffman_ac.c
 *
 * MATLAB Coder version            : 2.8
 * C/C++ source code generated on  : 09-Nov-2015 17:58:21
 */

/* Include Files */
#include "rt_nonfinite.h"
#include "clusteringTest6.h"
#include "huffman_ac.h"
#include "clusteringTest6_emxutil.h"
#include "dec2bin.h"

/* Type Definitions */
#ifndef struct_emxArray_char_T_1x17
#define struct_emxArray_char_T_1x17

struct emxArray_char_T_1x17
{
  char data[17];
  int size[2];
};

#endif                                 /*struct_emxArray_char_T_1x17*/

#ifndef typedef_emxArray_char_T_1x17
#define typedef_emxArray_char_T_1x17

typedef struct emxArray_char_T_1x17 emxArray_char_T_1x17;

#endif                                 /*typedef_emxArray_char_T_1x17*/

#ifndef struct_stfiAHqtGYJfiwc8OK5S2jH
#define struct_stfiAHqtGYJfiwc8OK5S2jH

struct stfiAHqtGYJfiwc8OK5S2jH
{
  emxArray_char_T_1x17 elem;
};

#endif                                 /*struct_stfiAHqtGYJfiwc8OK5S2jH*/

#ifndef typedef_struct_T
#define typedef_struct_T

typedef struct stfiAHqtGYJfiwc8OK5S2jH struct_T;

#endif                                 /*typedef_struct_T*/

/* Function Definitions */

/*
 * Arguments    : const double vector_zz[10]
 *                emxArray_char_T *value
 * Return Type  : void
 */
void huffman_ac(const double vector_zz[10], emxArray_char_T *value)
{
  emxArray_char_T *C;
  int mtmp;
  double empty;
  struct_T r0;
  struct_T r1;
  static const char a[2] = { '0', '1' };

  struct_T r2;
  static const char b_a[3] = { '1', '0', '0' };

  struct_T r3;
  static const char c_a[4] = { '1', '0', '1', '1' };

  struct_T r4;
  static const char d_a[5] = { '1', '1', '0', '1', '0' };

  struct_T r5;
  static const char e_a[7] = { '1', '1', '1', '1', '0', '0', '0' };

  struct_T r6;
  static const char f_a[8] = { '1', '1', '1', '1', '1', '0', '0', '0' };

  struct_T r7;
  static const char g_a[10] = { '1', '1', '1', '1', '1', '1', '0', '1', '1', '0'
  };

  struct_T r8;
  struct_T r9;
  static const char h_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '0', '0', '1', '0' };

  static const char i_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '0', '0', '1', '1' };

  struct_T r10;
  static const char j_a[4] = { '1', '1', '0', '0' };

  struct_T r11;
  static const char k_a[5] = { '1', '1', '0', '1', '1' };

  struct_T r12;
  static const char l_a[7] = { '1', '1', '1', '1', '0', '0', '1' };

  struct_T r13;
  static const char m_a[9] = { '1', '1', '1', '1', '1', '0', '1', '1', '0' };

  struct_T r14;
  static const char n_a[11] = { '1', '1', '1', '1', '1', '1', '1', '0', '1', '1',
    '0' };

  struct_T r15;
  struct_T r16;
  struct_T r17;
  struct_T r18;
  static const char o_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '0', '1', '0', '0' };

  static const char p_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '0', '1', '0', '1' };

  static const char q_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '0', '1', '1', '0' };

  static const char r_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '0', '1', '1', '1' };

  struct_T r19;
  static const char s_a[17] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '0', '1', '0', '0', '0' };

  struct_T r20;
  static const char t_a[5] = { '1', '1', '1', '0', '0' };

  struct_T r21;
  static const char u_a[8] = { '1', '1', '1', '1', '1', '0', '0', '1' };

  struct_T r22;
  static const char v_a[10] = { '1', '1', '1', '1', '1', '1', '0', '1', '1', '1'
  };

  struct_T r23;
  static const char w_a[12] = { '1', '1', '1', '1', '1', '1', '1', '1', '0', '1',
    '0', '0' };

  struct_T r24;
  struct_T r25;
  struct_T r26;
  struct_T r27;
  struct_T r28;
  struct_T r29;
  static const char x_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '1', '0', '0', '1' };

  static const char y_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0', '1', '0', '1', '0' };

  static const char ab_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '0', '1', '0', '1', '1' };

  static const char bb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '0', '1', '1', '0', '0' };

  static const char cb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '0', '1', '1', '0', '1' };

  static const char db_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '0', '1', '1', '1', '0' };

  struct_T r30;
  static const char eb_a[6] = { '1', '1', '1', '0', '1', '0' };

  struct_T r31;
  static const char fb_a[9] = { '1', '1', '1', '1', '1', '0', '1', '1', '1' };

  struct_T r32;
  static const char gb_a[12] = { '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '1', '0', '1' };

  struct_T r33;
  struct_T r34;
  struct_T r35;
  struct_T r36;
  struct_T r37;
  struct_T r38;
  struct_T r39;
  static const char hb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '0', '1', '1', '1', '1' };

  static const char ib_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '0', '0', '0' };

  static const char jb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '0', '0', '1' };

  static const char kb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '0', '1', '0' };

  static const char lb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '0', '1', '1' };

  static const char mb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '1', '0', '0' };

  static const char nb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '1', '0', '1' };

  struct_T r40;
  static const char ob_a[6] = { '1', '1', '1', '0', '1', '1' };

  struct_T r41;
  static const char pb_a[10] = { '1', '1', '1', '1', '1', '1', '1', '0', '0',
    '0' };

  struct_T r42;
  struct_T r43;
  struct_T r44;
  struct_T r45;
  struct_T r46;
  struct_T r47;
  struct_T r48;
  struct_T r49;
  static const char qb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '1', '1', '0' };

  static const char rb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '0', '1', '1', '1' };

  static const char sb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '0', '0', '0' };

  static const char tb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '0', '0', '1' };

  static const char ub_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '0', '1', '0' };

  static const char vb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '0', '1', '1' };

  static const char wb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '1', '0', '0' };

  static const char xb_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '1', '0', '1' };

  struct_T r50;
  static const char yb_a[7] = { '1', '1', '1', '1', '0', '1', '0' };

  struct_T r51;
  static const char ac_a[11] = { '1', '1', '1', '1', '1', '1', '1', '0', '1',
    '1', '1' };

  struct_T r52;
  struct_T r53;
  struct_T r54;
  struct_T r55;
  struct_T r56;
  struct_T r57;
  struct_T r58;
  struct_T r59;
  static const char bc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '1', '1', '0' };

  static const char cc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '1', '1', '1', '1', '1' };

  static const char dc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '0', '0', '0' };

  static const char ec_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '0', '0', '1' };

  static const char fc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '0', '1', '0' };

  static const char gc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '0', '1', '1' };

  static const char hc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '1', '0', '0' };

  static const char ic_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '1', '0', '1' };

  struct_T r60;
  static const char jc_a[7] = { '1', '1', '1', '1', '0', '1', '1' };

  struct_T r61;
  static const char kc_a[12] = { '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '1', '1', '0' };

  struct_T r62;
  struct_T r63;
  struct_T r64;
  struct_T r65;
  struct_T r66;
  struct_T r67;
  struct_T r68;
  struct_T r69;
  static const char lc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '1', '1', '0' };

  static const char mc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '0', '1', '1', '1' };

  static const char nc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '0', '0', '0' };

  static const char oc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '0', '0', '1' };

  static const char pc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '0', '1', '0' };

  static const char qc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '0', '1', '1' };

  static const char rc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '1', '0', '0' };

  static const char sc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '1', '0', '1' };

  struct_T r70;
  static const char tc_a[8] = { '1', '1', '1', '1', '1', '0', '1', '0' };

  struct_T r71;
  static const char uc_a[12] = { '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '1', '1', '1' };

  struct_T r72;
  struct_T r73;
  struct_T r74;
  struct_T r75;
  struct_T r76;
  struct_T r77;
  struct_T r78;
  struct_T r79;
  static const char vc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '1', '1', '0' };

  static const char wc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '0', '1', '1', '1', '1' };

  static const char xc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '0', '0', '0' };

  static const char yc_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '0', '0', '1' };

  static const char ad_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '0', '1', '0' };

  static const char bd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '0', '1', '1' };

  static const char cd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '1', '0', '0' };

  static const char dd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '1', '0', '1' };

  struct_T r80;
  static const char ed_a[9] = { '1', '1', '1', '1', '1', '1', '0', '0', '0' };

  struct_T r81;
  static const char fd_a[15] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '0', '0', '0', '0', '0' };

  struct_T r82;
  struct_T r83;
  struct_T r84;
  struct_T r85;
  struct_T r86;
  struct_T r87;
  struct_T r88;
  struct_T r89;
  static const char gd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '1', '1', '0' };

  static const char hd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '0', '1', '1', '1' };

  static const char id_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '0', '0', '0' };

  static const char jd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '0', '0', '1' };

  static const char kd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '0', '1', '0' };

  static const char ld_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '0', '1', '1' };

  static const char md_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '1', '0', '0' };

  static const char nd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '1', '0', '1' };

  struct_T r90;
  static const char od_a[9] = { '1', '1', '1', '1', '1', '1', '0', '0', '1' };

  struct_T r91;
  struct_T r92;
  struct_T r93;
  struct_T r94;
  struct_T r95;
  struct_T r96;
  struct_T r97;
  struct_T r98;
  struct_T r99;
  static const char pd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '1', '1', '0' };

  static const char qd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '0', '1', '1', '1', '1', '1', '1' };

  static const char rd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '0', '0', '0', '0' };

  static const char sd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '0', '0', '1', '0' };

  static const char td_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '0', '0', '1', '1' };

  static const char ud_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '0', '1', '0', '0' };

  static const char vd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '0', '1', '0', '1' };

  static const char wd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '0', '1', '1', '0' };

  struct_T r100;
  static const char xd_a[9] = { '1', '1', '1', '1', '1', '1', '0', '1', '0' };

  struct_T r101;
  struct_T r102;
  struct_T r103;
  struct_T r104;
  struct_T r105;
  struct_T r106;
  struct_T r107;
  struct_T r108;
  struct_T r109;
  static const char yd_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '0', '1', '1', '1' };

  static const char ae_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '0', '0', '0' };

  static const char be_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '0', '0', '1' };

  static const char ce_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '0', '1', '0' };

  static const char de_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '0', '1', '1' };

  static const char ee_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '1', '0', '0' };

  static const char fe_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '1', '0', '1' };

  static const char ge_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '1', '1', '0' };

  static const char he_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '0', '1', '1', '1', '1' };

  struct_T r110;
  static const char ie_a[10] = { '1', '1', '1', '1', '1', '1', '1', '0', '0',
    '1' };

  struct_T r111;
  struct_T r112;
  struct_T r113;
  struct_T r114;
  struct_T r115;
  struct_T r116;
  struct_T r117;
  struct_T r118;
  struct_T r119;
  static const char je_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '0', '0', '0' };

  static const char ke_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '0', '0', '1' };

  static const char le_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '0', '1', '0' };

  static const char me_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '0', '1', '1' };

  static const char ne_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '1', '0', '0' };

  static const char oe_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '1', '0', '1' };

  static const char pe_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '1', '1', '0' };

  static const char qe_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '0', '1', '1', '1' };

  static const char re_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '0', '0', '0' };

  struct_T r120;
  static const char se_a[10] = { '1', '1', '1', '1', '1', '1', '1', '0', '1',
    '0' };

  struct_T r121;
  struct_T r122;
  struct_T r123;
  struct_T r124;
  struct_T r125;
  struct_T r126;
  struct_T r127;
  struct_T r128;
  struct_T r129;
  static const char te_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '0', '0', '1' };

  static const char ue_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '0', '1', '0' };

  static const char ve_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '0', '1', '1' };

  static const char we_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '1', '0', '0' };

  static const char xe_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '1', '0', '1' };

  static const char ye_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '1', '1', '0' };

  static const char af_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '0', '1', '1', '1', '1', '1' };

  static const char bf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '0', '0', '0' };

  static const char cf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '0', '0', '1' };

  struct_T r130;
  static const char df_a[11] = { '1', '1', '1', '1', '1', '1', '1', '1', '0',
    '0', '0' };

  struct_T r131;
  struct_T r132;
  struct_T r133;
  struct_T r134;
  struct_T r135;
  struct_T r136;
  struct_T r137;
  struct_T r138;
  struct_T r139;
  struct_T r140;
  struct_T r141;
  struct_T r142;
  struct_T r143;
  struct_T r144;
  struct_T r145;
  struct_T r146;
  struct_T r147;
  struct_T r148;
  struct_T r149;
  struct_T r150;
  struct_T r151;
  struct_T r152;
  struct_T r153;
  struct_T r154;
  struct_T r155;
  struct_T r156;
  struct_T r157;
  struct_T r158;
  struct_T r159;
  static const char ef_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '0', '1', '0' };

  static const char ff_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '0', '1', '1' };

  static const char gf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '1', '0', '0' };

  static const char hf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '1', '0', '1' };

  static const char if_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '1', '1', '0' };

  static const char jf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '0', '1', '1', '1' };

  static const char kf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '0', '0', '0' };

  static const char lf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '0', '0', '1' };

  static const char mf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '0', '1', '0' };

  static const char nf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '0', '1', '1' };

  static const char of_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '1', '0', '0' };

  static const char pf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '1', '0', '1' };

  static const char qf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '1', '1', '0' };

  static const char rf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '0', '1', '1', '1', '1' };

  static const char sf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '0', '0', '0' };

  static const char tf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '0', '0', '1' };

  static const char uf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '0', '1', '0' };

  static const char vf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '0', '1', '1' };

  static const char wf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '1', '0', '0' };

  static const char xf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '1', '0', '1' };

  static const char yf_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '1', '1', '0' };

  static const char ag_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '0', '1', '1', '1' };

  static const char bg_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '1', '0', '0', '0' };

  static const char cg_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '1', '0', '0', '1' };

  static const char dg_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '1', '0', '1', '0' };

  static const char eg_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '1', '0', '1', '1' };

  static const char fg_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '1', '1', '0', '0' };

  static const char gg_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '1', '1', '0', '1' };

  static const char hg_a[16] = { '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '1', '1', '1', '1', '1', '1', '0' };

  struct_T ac_huffman1[160];
  int i;
  boolean_T exitg1;
  boolean_T guard1 = false;
  int tmp_size[2];
  char tmp_data[52];
  signed char varargin_1[2];
  int j;
  int loop_ub;
  char temp_data[69];
  int xs;
  int cs;
  int C1_size[2];
  char C1_data[52];
  char ac_huffman1_data[69];
  static const char cv0[4] = { '1', '0', '1', '0' };

  emxInit_char_T(&C, 2);
  mtmp = C->size[0] * C->size[1];
  C->size[0] = 1;
  C->size[1] = 0;
  emxEnsureCapacity((emxArray__common *)C, mtmp, (int)sizeof(char));
  empty = 0.0;

  /*   */
  /*  ac_huffman={ */
  /*     '00'               '01'               '100'              '1011'             '11010'            '1111000'          '11111000'         '1111110110'       '1111111110000010' '1111111110000011';... */
  /*     '1100'             '11011'            '1111001'          '111110110'        '11111110110'      '1111111110000100' '1111111110000101' '1111111110000110' '1111111110000111' '11111111100001000';... */
  /*     '11100'            '11111001'         '1111110111'       '111111110100'     '1111111110001001' '1111111110001010' '1111111110001011' '1111111110001100' '1111111110001101' '1111111110001110';... */
  /*     '111010'           '111110111'        '111111110101'     '1111111110001111' '1111111110010000' '1111111110010001' '1111111110010010' '1111111110010011' '1111111110010100' '1111111110010101';... */
  /*     '111011'           '1111111000'       '1111111110010110' '1111111110010111' '1111111110011000' '1111111110011001' '1111111110011010' '1111111110011011' '1111111110011100' '1111111110011101';... */
  /*     '1111010'          '11111110111'      '1111111110011110' '1111111110011111' '1111111110100000' '1111111110100001' '1111111110100010' '1111111110100011' '1111111110100100' '1111111110100101';... */
  /*     '1111011'          '111111110110'     '1111111110100110' '1111111110100111' '1111111110101000' '1111111110101001' '1111111110101010' '1111111110101011' '1111111110101100' '1111111110101101';... */
  /*     '11111010'         '111111110111'     '1111111110101110' '1111111110101111' '1111111110110000' '1111111110110001' '1111111110110010' '1111111110110011' '1111111110110100' '1111111110110101';... */
  /*     '111111000'        '111111111000000'  '1111111110110110' '1111111110110111' '1111111110111000' '1111111110111001' '1111111110111010' '1111111110111011' '1111111110111100' '1111111110111101';... */
  /*     '111111001'        '1111111110111110' '1111111110111111' '1111111111000000' '1111111111000000' '1111111111000010' '1111111111000011' '1111111111000100' '1111111111000101' '1111111111000110';... */
  /*     '111111010'        '1111111111000111' '1111111111001000' '1111111111001001' '1111111111001010' '1111111111001011' '1111111111001100' '1111111111001101' '1111111111001110' '1111111111001111';... */
  /*     '1111111001'       '1111111111010000' '1111111111010001' '1111111111010010' '1111111111010011' '1111111111010100' '1111111111010101' '1111111111010110' '1111111111010111' '1111111111011000';... */
  /*     '1111111010'       '1111111111011001' '1111111111011010' '1111111111011011' '1111111111011100' '1111111111011101' '1111111111011110' '1111111111011111' '1111111111100000' '1111111111100001';... */
  /*     '11111111000'      '1111111111100010' '1111111111100011' '1111111111100100' '1111111111100101' '1111111111100110' '1111111111100111' '1111111111101000' '1111111111101001' '1111111111101010';... */
  /*     '1111111111101011' '1111111111101100' '1111111111101101' '1111111111101110' '1111111111101111' '1111111111110000' '1111111111110001' '1111111111110010' '1111111111110011' '1111111111110100';... */
  /*     '1111111111110101' '1111111111110110' '1111111111110111' '1111111111111000' '1111111111111001' '1111111111111010' '1111111111111011' '1111111111111100' '1111111111111101' '1111111111111110' */
  /*  }; */
  r0.elem.size[0] = 1;
  r0.elem.size[1] = 2;
  r1.elem.size[0] = 1;
  r1.elem.size[1] = 2;
  for (mtmp = 0; mtmp < 2; mtmp++) {
    r0.elem.data[mtmp] = '0';
    r1.elem.data[mtmp] = a[mtmp];
  }

  r2.elem.size[0] = 1;
  r2.elem.size[1] = 3;
  for (mtmp = 0; mtmp < 3; mtmp++) {
    r2.elem.data[mtmp] = b_a[mtmp];
  }

  r3.elem.size[0] = 1;
  r3.elem.size[1] = 4;
  for (mtmp = 0; mtmp < 4; mtmp++) {
    r3.elem.data[mtmp] = c_a[mtmp];
  }

  r4.elem.size[0] = 1;
  r4.elem.size[1] = 5;
  for (mtmp = 0; mtmp < 5; mtmp++) {
    r4.elem.data[mtmp] = d_a[mtmp];
  }

  r5.elem.size[0] = 1;
  r5.elem.size[1] = 7;
  for (mtmp = 0; mtmp < 7; mtmp++) {
    r5.elem.data[mtmp] = e_a[mtmp];
  }

  r6.elem.size[0] = 1;
  r6.elem.size[1] = 8;
  for (mtmp = 0; mtmp < 8; mtmp++) {
    r6.elem.data[mtmp] = f_a[mtmp];
  }

  r7.elem.size[0] = 1;
  r7.elem.size[1] = 10;
  for (mtmp = 0; mtmp < 10; mtmp++) {
    r7.elem.data[mtmp] = g_a[mtmp];
  }

  r8.elem.size[0] = 1;
  r8.elem.size[1] = 16;
  r9.elem.size[0] = 1;
  r9.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r8.elem.data[mtmp] = h_a[mtmp];
    r9.elem.data[mtmp] = i_a[mtmp];
  }

  r10.elem.size[0] = 1;
  r10.elem.size[1] = 4;
  for (mtmp = 0; mtmp < 4; mtmp++) {
    r10.elem.data[mtmp] = j_a[mtmp];
  }

  r11.elem.size[0] = 1;
  r11.elem.size[1] = 5;
  for (mtmp = 0; mtmp < 5; mtmp++) {
    r11.elem.data[mtmp] = k_a[mtmp];
  }

  r12.elem.size[0] = 1;
  r12.elem.size[1] = 7;
  for (mtmp = 0; mtmp < 7; mtmp++) {
    r12.elem.data[mtmp] = l_a[mtmp];
  }

  r13.elem.size[0] = 1;
  r13.elem.size[1] = 9;
  for (mtmp = 0; mtmp < 9; mtmp++) {
    r13.elem.data[mtmp] = m_a[mtmp];
  }

  r14.elem.size[0] = 1;
  r14.elem.size[1] = 11;
  for (mtmp = 0; mtmp < 11; mtmp++) {
    r14.elem.data[mtmp] = n_a[mtmp];
  }

  r15.elem.size[0] = 1;
  r15.elem.size[1] = 16;
  r16.elem.size[0] = 1;
  r16.elem.size[1] = 16;
  r17.elem.size[0] = 1;
  r17.elem.size[1] = 16;
  r18.elem.size[0] = 1;
  r18.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r15.elem.data[mtmp] = o_a[mtmp];
    r16.elem.data[mtmp] = p_a[mtmp];
    r17.elem.data[mtmp] = q_a[mtmp];
    r18.elem.data[mtmp] = r_a[mtmp];
  }

  r19.elem.size[0] = 1;
  r19.elem.size[1] = 17;
  for (mtmp = 0; mtmp < 17; mtmp++) {
    r19.elem.data[mtmp] = s_a[mtmp];
  }

  r20.elem.size[0] = 1;
  r20.elem.size[1] = 5;
  for (mtmp = 0; mtmp < 5; mtmp++) {
    r20.elem.data[mtmp] = t_a[mtmp];
  }

  r21.elem.size[0] = 1;
  r21.elem.size[1] = 8;
  for (mtmp = 0; mtmp < 8; mtmp++) {
    r21.elem.data[mtmp] = u_a[mtmp];
  }

  r22.elem.size[0] = 1;
  r22.elem.size[1] = 10;
  for (mtmp = 0; mtmp < 10; mtmp++) {
    r22.elem.data[mtmp] = v_a[mtmp];
  }

  r23.elem.size[0] = 1;
  r23.elem.size[1] = 12;
  for (mtmp = 0; mtmp < 12; mtmp++) {
    r23.elem.data[mtmp] = w_a[mtmp];
  }

  r24.elem.size[0] = 1;
  r24.elem.size[1] = 16;
  r25.elem.size[0] = 1;
  r25.elem.size[1] = 16;
  r26.elem.size[0] = 1;
  r26.elem.size[1] = 16;
  r27.elem.size[0] = 1;
  r27.elem.size[1] = 16;
  r28.elem.size[0] = 1;
  r28.elem.size[1] = 16;
  r29.elem.size[0] = 1;
  r29.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r24.elem.data[mtmp] = x_a[mtmp];
    r25.elem.data[mtmp] = y_a[mtmp];
    r26.elem.data[mtmp] = ab_a[mtmp];
    r27.elem.data[mtmp] = bb_a[mtmp];
    r28.elem.data[mtmp] = cb_a[mtmp];
    r29.elem.data[mtmp] = db_a[mtmp];
  }

  r30.elem.size[0] = 1;
  r30.elem.size[1] = 6;
  for (mtmp = 0; mtmp < 6; mtmp++) {
    r30.elem.data[mtmp] = eb_a[mtmp];
  }

  r31.elem.size[0] = 1;
  r31.elem.size[1] = 9;
  for (mtmp = 0; mtmp < 9; mtmp++) {
    r31.elem.data[mtmp] = fb_a[mtmp];
  }

  r32.elem.size[0] = 1;
  r32.elem.size[1] = 12;
  for (mtmp = 0; mtmp < 12; mtmp++) {
    r32.elem.data[mtmp] = gb_a[mtmp];
  }

  r33.elem.size[0] = 1;
  r33.elem.size[1] = 16;
  r34.elem.size[0] = 1;
  r34.elem.size[1] = 16;
  r35.elem.size[0] = 1;
  r35.elem.size[1] = 16;
  r36.elem.size[0] = 1;
  r36.elem.size[1] = 16;
  r37.elem.size[0] = 1;
  r37.elem.size[1] = 16;
  r38.elem.size[0] = 1;
  r38.elem.size[1] = 16;
  r39.elem.size[0] = 1;
  r39.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r33.elem.data[mtmp] = hb_a[mtmp];
    r34.elem.data[mtmp] = ib_a[mtmp];
    r35.elem.data[mtmp] = jb_a[mtmp];
    r36.elem.data[mtmp] = kb_a[mtmp];
    r37.elem.data[mtmp] = lb_a[mtmp];
    r38.elem.data[mtmp] = mb_a[mtmp];
    r39.elem.data[mtmp] = nb_a[mtmp];
  }

  r40.elem.size[0] = 1;
  r40.elem.size[1] = 6;
  for (mtmp = 0; mtmp < 6; mtmp++) {
    r40.elem.data[mtmp] = ob_a[mtmp];
  }

  r41.elem.size[0] = 1;
  r41.elem.size[1] = 10;
  for (mtmp = 0; mtmp < 10; mtmp++) {
    r41.elem.data[mtmp] = pb_a[mtmp];
  }

  r42.elem.size[0] = 1;
  r42.elem.size[1] = 16;
  r43.elem.size[0] = 1;
  r43.elem.size[1] = 16;
  r44.elem.size[0] = 1;
  r44.elem.size[1] = 16;
  r45.elem.size[0] = 1;
  r45.elem.size[1] = 16;
  r46.elem.size[0] = 1;
  r46.elem.size[1] = 16;
  r47.elem.size[0] = 1;
  r47.elem.size[1] = 16;
  r48.elem.size[0] = 1;
  r48.elem.size[1] = 16;
  r49.elem.size[0] = 1;
  r49.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r42.elem.data[mtmp] = qb_a[mtmp];
    r43.elem.data[mtmp] = rb_a[mtmp];
    r44.elem.data[mtmp] = sb_a[mtmp];
    r45.elem.data[mtmp] = tb_a[mtmp];
    r46.elem.data[mtmp] = ub_a[mtmp];
    r47.elem.data[mtmp] = vb_a[mtmp];
    r48.elem.data[mtmp] = wb_a[mtmp];
    r49.elem.data[mtmp] = xb_a[mtmp];
  }

  r50.elem.size[0] = 1;
  r50.elem.size[1] = 7;
  for (mtmp = 0; mtmp < 7; mtmp++) {
    r50.elem.data[mtmp] = yb_a[mtmp];
  }

  r51.elem.size[0] = 1;
  r51.elem.size[1] = 11;
  for (mtmp = 0; mtmp < 11; mtmp++) {
    r51.elem.data[mtmp] = ac_a[mtmp];
  }

  r52.elem.size[0] = 1;
  r52.elem.size[1] = 16;
  r53.elem.size[0] = 1;
  r53.elem.size[1] = 16;
  r54.elem.size[0] = 1;
  r54.elem.size[1] = 16;
  r55.elem.size[0] = 1;
  r55.elem.size[1] = 16;
  r56.elem.size[0] = 1;
  r56.elem.size[1] = 16;
  r57.elem.size[0] = 1;
  r57.elem.size[1] = 16;
  r58.elem.size[0] = 1;
  r58.elem.size[1] = 16;
  r59.elem.size[0] = 1;
  r59.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r52.elem.data[mtmp] = bc_a[mtmp];
    r53.elem.data[mtmp] = cc_a[mtmp];
    r54.elem.data[mtmp] = dc_a[mtmp];
    r55.elem.data[mtmp] = ec_a[mtmp];
    r56.elem.data[mtmp] = fc_a[mtmp];
    r57.elem.data[mtmp] = gc_a[mtmp];
    r58.elem.data[mtmp] = hc_a[mtmp];
    r59.elem.data[mtmp] = ic_a[mtmp];
  }

  r60.elem.size[0] = 1;
  r60.elem.size[1] = 7;
  for (mtmp = 0; mtmp < 7; mtmp++) {
    r60.elem.data[mtmp] = jc_a[mtmp];
  }

  r61.elem.size[0] = 1;
  r61.elem.size[1] = 12;
  for (mtmp = 0; mtmp < 12; mtmp++) {
    r61.elem.data[mtmp] = kc_a[mtmp];
  }

  r62.elem.size[0] = 1;
  r62.elem.size[1] = 16;
  r63.elem.size[0] = 1;
  r63.elem.size[1] = 16;
  r64.elem.size[0] = 1;
  r64.elem.size[1] = 16;
  r65.elem.size[0] = 1;
  r65.elem.size[1] = 16;
  r66.elem.size[0] = 1;
  r66.elem.size[1] = 16;
  r67.elem.size[0] = 1;
  r67.elem.size[1] = 16;
  r68.elem.size[0] = 1;
  r68.elem.size[1] = 16;
  r69.elem.size[0] = 1;
  r69.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r62.elem.data[mtmp] = lc_a[mtmp];
    r63.elem.data[mtmp] = mc_a[mtmp];
    r64.elem.data[mtmp] = nc_a[mtmp];
    r65.elem.data[mtmp] = oc_a[mtmp];
    r66.elem.data[mtmp] = pc_a[mtmp];
    r67.elem.data[mtmp] = qc_a[mtmp];
    r68.elem.data[mtmp] = rc_a[mtmp];
    r69.elem.data[mtmp] = sc_a[mtmp];
  }

  r70.elem.size[0] = 1;
  r70.elem.size[1] = 8;
  for (mtmp = 0; mtmp < 8; mtmp++) {
    r70.elem.data[mtmp] = tc_a[mtmp];
  }

  r71.elem.size[0] = 1;
  r71.elem.size[1] = 12;
  for (mtmp = 0; mtmp < 12; mtmp++) {
    r71.elem.data[mtmp] = uc_a[mtmp];
  }

  r72.elem.size[0] = 1;
  r72.elem.size[1] = 16;
  r73.elem.size[0] = 1;
  r73.elem.size[1] = 16;
  r74.elem.size[0] = 1;
  r74.elem.size[1] = 16;
  r75.elem.size[0] = 1;
  r75.elem.size[1] = 16;
  r76.elem.size[0] = 1;
  r76.elem.size[1] = 16;
  r77.elem.size[0] = 1;
  r77.elem.size[1] = 16;
  r78.elem.size[0] = 1;
  r78.elem.size[1] = 16;
  r79.elem.size[0] = 1;
  r79.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r72.elem.data[mtmp] = vc_a[mtmp];
    r73.elem.data[mtmp] = wc_a[mtmp];
    r74.elem.data[mtmp] = xc_a[mtmp];
    r75.elem.data[mtmp] = yc_a[mtmp];
    r76.elem.data[mtmp] = ad_a[mtmp];
    r77.elem.data[mtmp] = bd_a[mtmp];
    r78.elem.data[mtmp] = cd_a[mtmp];
    r79.elem.data[mtmp] = dd_a[mtmp];
  }

  r80.elem.size[0] = 1;
  r80.elem.size[1] = 9;
  for (mtmp = 0; mtmp < 9; mtmp++) {
    r80.elem.data[mtmp] = ed_a[mtmp];
  }

  r81.elem.size[0] = 1;
  r81.elem.size[1] = 15;
  for (mtmp = 0; mtmp < 15; mtmp++) {
    r81.elem.data[mtmp] = fd_a[mtmp];
  }

  r82.elem.size[0] = 1;
  r82.elem.size[1] = 16;
  r83.elem.size[0] = 1;
  r83.elem.size[1] = 16;
  r84.elem.size[0] = 1;
  r84.elem.size[1] = 16;
  r85.elem.size[0] = 1;
  r85.elem.size[1] = 16;
  r86.elem.size[0] = 1;
  r86.elem.size[1] = 16;
  r87.elem.size[0] = 1;
  r87.elem.size[1] = 16;
  r88.elem.size[0] = 1;
  r88.elem.size[1] = 16;
  r89.elem.size[0] = 1;
  r89.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r82.elem.data[mtmp] = gd_a[mtmp];
    r83.elem.data[mtmp] = hd_a[mtmp];
    r84.elem.data[mtmp] = id_a[mtmp];
    r85.elem.data[mtmp] = jd_a[mtmp];
    r86.elem.data[mtmp] = kd_a[mtmp];
    r87.elem.data[mtmp] = ld_a[mtmp];
    r88.elem.data[mtmp] = md_a[mtmp];
    r89.elem.data[mtmp] = nd_a[mtmp];
  }

  r90.elem.size[0] = 1;
  r90.elem.size[1] = 9;
  for (mtmp = 0; mtmp < 9; mtmp++) {
    r90.elem.data[mtmp] = od_a[mtmp];
  }

  r91.elem.size[0] = 1;
  r91.elem.size[1] = 16;
  r92.elem.size[0] = 1;
  r92.elem.size[1] = 16;
  r93.elem.size[0] = 1;
  r93.elem.size[1] = 16;
  r94.elem.size[0] = 1;
  r94.elem.size[1] = 16;
  r95.elem.size[0] = 1;
  r95.elem.size[1] = 16;
  r96.elem.size[0] = 1;
  r96.elem.size[1] = 16;
  r97.elem.size[0] = 1;
  r97.elem.size[1] = 16;
  r98.elem.size[0] = 1;
  r98.elem.size[1] = 16;
  r99.elem.size[0] = 1;
  r99.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r91.elem.data[mtmp] = pd_a[mtmp];
    r92.elem.data[mtmp] = qd_a[mtmp];
    r93.elem.data[mtmp] = rd_a[mtmp];
    r94.elem.data[mtmp] = rd_a[mtmp];
    r95.elem.data[mtmp] = sd_a[mtmp];
    r96.elem.data[mtmp] = td_a[mtmp];
    r97.elem.data[mtmp] = ud_a[mtmp];
    r98.elem.data[mtmp] = vd_a[mtmp];
    r99.elem.data[mtmp] = wd_a[mtmp];
  }

  r100.elem.size[0] = 1;
  r100.elem.size[1] = 9;
  for (mtmp = 0; mtmp < 9; mtmp++) {
    r100.elem.data[mtmp] = xd_a[mtmp];
  }

  r101.elem.size[0] = 1;
  r101.elem.size[1] = 16;
  r102.elem.size[0] = 1;
  r102.elem.size[1] = 16;
  r103.elem.size[0] = 1;
  r103.elem.size[1] = 16;
  r104.elem.size[0] = 1;
  r104.elem.size[1] = 16;
  r105.elem.size[0] = 1;
  r105.elem.size[1] = 16;
  r106.elem.size[0] = 1;
  r106.elem.size[1] = 16;
  r107.elem.size[0] = 1;
  r107.elem.size[1] = 16;
  r108.elem.size[0] = 1;
  r108.elem.size[1] = 16;
  r109.elem.size[0] = 1;
  r109.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r101.elem.data[mtmp] = yd_a[mtmp];
    r102.elem.data[mtmp] = ae_a[mtmp];
    r103.elem.data[mtmp] = be_a[mtmp];
    r104.elem.data[mtmp] = ce_a[mtmp];
    r105.elem.data[mtmp] = de_a[mtmp];
    r106.elem.data[mtmp] = ee_a[mtmp];
    r107.elem.data[mtmp] = fe_a[mtmp];
    r108.elem.data[mtmp] = ge_a[mtmp];
    r109.elem.data[mtmp] = he_a[mtmp];
  }

  r110.elem.size[0] = 1;
  r110.elem.size[1] = 10;
  for (mtmp = 0; mtmp < 10; mtmp++) {
    r110.elem.data[mtmp] = ie_a[mtmp];
  }

  r111.elem.size[0] = 1;
  r111.elem.size[1] = 16;
  r112.elem.size[0] = 1;
  r112.elem.size[1] = 16;
  r113.elem.size[0] = 1;
  r113.elem.size[1] = 16;
  r114.elem.size[0] = 1;
  r114.elem.size[1] = 16;
  r115.elem.size[0] = 1;
  r115.elem.size[1] = 16;
  r116.elem.size[0] = 1;
  r116.elem.size[1] = 16;
  r117.elem.size[0] = 1;
  r117.elem.size[1] = 16;
  r118.elem.size[0] = 1;
  r118.elem.size[1] = 16;
  r119.elem.size[0] = 1;
  r119.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r111.elem.data[mtmp] = je_a[mtmp];
    r112.elem.data[mtmp] = ke_a[mtmp];
    r113.elem.data[mtmp] = le_a[mtmp];
    r114.elem.data[mtmp] = me_a[mtmp];
    r115.elem.data[mtmp] = ne_a[mtmp];
    r116.elem.data[mtmp] = oe_a[mtmp];
    r117.elem.data[mtmp] = pe_a[mtmp];
    r118.elem.data[mtmp] = qe_a[mtmp];
    r119.elem.data[mtmp] = re_a[mtmp];
  }

  r120.elem.size[0] = 1;
  r120.elem.size[1] = 10;
  for (mtmp = 0; mtmp < 10; mtmp++) {
    r120.elem.data[mtmp] = se_a[mtmp];
  }

  r121.elem.size[0] = 1;
  r121.elem.size[1] = 16;
  r122.elem.size[0] = 1;
  r122.elem.size[1] = 16;
  r123.elem.size[0] = 1;
  r123.elem.size[1] = 16;
  r124.elem.size[0] = 1;
  r124.elem.size[1] = 16;
  r125.elem.size[0] = 1;
  r125.elem.size[1] = 16;
  r126.elem.size[0] = 1;
  r126.elem.size[1] = 16;
  r127.elem.size[0] = 1;
  r127.elem.size[1] = 16;
  r128.elem.size[0] = 1;
  r128.elem.size[1] = 16;
  r129.elem.size[0] = 1;
  r129.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r121.elem.data[mtmp] = te_a[mtmp];
    r122.elem.data[mtmp] = ue_a[mtmp];
    r123.elem.data[mtmp] = ve_a[mtmp];
    r124.elem.data[mtmp] = we_a[mtmp];
    r125.elem.data[mtmp] = xe_a[mtmp];
    r126.elem.data[mtmp] = ye_a[mtmp];
    r127.elem.data[mtmp] = af_a[mtmp];
    r128.elem.data[mtmp] = bf_a[mtmp];
    r129.elem.data[mtmp] = cf_a[mtmp];
  }

  r130.elem.size[0] = 1;
  r130.elem.size[1] = 11;
  for (mtmp = 0; mtmp < 11; mtmp++) {
    r130.elem.data[mtmp] = df_a[mtmp];
  }

  r131.elem.size[0] = 1;
  r131.elem.size[1] = 16;
  r132.elem.size[0] = 1;
  r132.elem.size[1] = 16;
  r133.elem.size[0] = 1;
  r133.elem.size[1] = 16;
  r134.elem.size[0] = 1;
  r134.elem.size[1] = 16;
  r135.elem.size[0] = 1;
  r135.elem.size[1] = 16;
  r136.elem.size[0] = 1;
  r136.elem.size[1] = 16;
  r137.elem.size[0] = 1;
  r137.elem.size[1] = 16;
  r138.elem.size[0] = 1;
  r138.elem.size[1] = 16;
  r139.elem.size[0] = 1;
  r139.elem.size[1] = 16;
  r140.elem.size[0] = 1;
  r140.elem.size[1] = 16;
  r141.elem.size[0] = 1;
  r141.elem.size[1] = 16;
  r142.elem.size[0] = 1;
  r142.elem.size[1] = 16;
  r143.elem.size[0] = 1;
  r143.elem.size[1] = 16;
  r144.elem.size[0] = 1;
  r144.elem.size[1] = 16;
  r145.elem.size[0] = 1;
  r145.elem.size[1] = 16;
  r146.elem.size[0] = 1;
  r146.elem.size[1] = 16;
  r147.elem.size[0] = 1;
  r147.elem.size[1] = 16;
  r148.elem.size[0] = 1;
  r148.elem.size[1] = 16;
  r149.elem.size[0] = 1;
  r149.elem.size[1] = 16;
  r150.elem.size[0] = 1;
  r150.elem.size[1] = 16;
  r151.elem.size[0] = 1;
  r151.elem.size[1] = 16;
  r152.elem.size[0] = 1;
  r152.elem.size[1] = 16;
  r153.elem.size[0] = 1;
  r153.elem.size[1] = 16;
  r154.elem.size[0] = 1;
  r154.elem.size[1] = 16;
  r155.elem.size[0] = 1;
  r155.elem.size[1] = 16;
  r156.elem.size[0] = 1;
  r156.elem.size[1] = 16;
  r157.elem.size[0] = 1;
  r157.elem.size[1] = 16;
  r158.elem.size[0] = 1;
  r158.elem.size[1] = 16;
  r159.elem.size[0] = 1;
  r159.elem.size[1] = 16;
  for (mtmp = 0; mtmp < 16; mtmp++) {
    r131.elem.data[mtmp] = ef_a[mtmp];
    r132.elem.data[mtmp] = ff_a[mtmp];
    r133.elem.data[mtmp] = gf_a[mtmp];
    r134.elem.data[mtmp] = hf_a[mtmp];
    r135.elem.data[mtmp] = if_a[mtmp];
    r136.elem.data[mtmp] = jf_a[mtmp];
    r137.elem.data[mtmp] = kf_a[mtmp];
    r138.elem.data[mtmp] = lf_a[mtmp];
    r139.elem.data[mtmp] = mf_a[mtmp];
    r140.elem.data[mtmp] = nf_a[mtmp];
    r141.elem.data[mtmp] = of_a[mtmp];
    r142.elem.data[mtmp] = pf_a[mtmp];
    r143.elem.data[mtmp] = qf_a[mtmp];
    r144.elem.data[mtmp] = rf_a[mtmp];
    r145.elem.data[mtmp] = sf_a[mtmp];
    r146.elem.data[mtmp] = tf_a[mtmp];
    r147.elem.data[mtmp] = uf_a[mtmp];
    r148.elem.data[mtmp] = vf_a[mtmp];
    r149.elem.data[mtmp] = wf_a[mtmp];
    r150.elem.data[mtmp] = xf_a[mtmp];
    r151.elem.data[mtmp] = yf_a[mtmp];
    r152.elem.data[mtmp] = ag_a[mtmp];
    r153.elem.data[mtmp] = bg_a[mtmp];
    r154.elem.data[mtmp] = cg_a[mtmp];
    r155.elem.data[mtmp] = dg_a[mtmp];
    r156.elem.data[mtmp] = eg_a[mtmp];
    r157.elem.data[mtmp] = fg_a[mtmp];
    r158.elem.data[mtmp] = gg_a[mtmp];
    r159.elem.data[mtmp] = hg_a[mtmp];
  }

  ac_huffman1[0] = r0;
  ac_huffman1[1] = r1;
  ac_huffman1[2] = r2;
  ac_huffman1[3] = r3;
  ac_huffman1[4] = r4;
  ac_huffman1[5] = r5;
  ac_huffman1[6] = r6;
  ac_huffman1[7] = r7;
  ac_huffman1[8] = r8;
  ac_huffman1[9] = r9;
  ac_huffman1[10] = r10;
  ac_huffman1[11] = r11;
  ac_huffman1[12] = r12;
  ac_huffman1[13] = r13;
  ac_huffman1[14] = r14;
  ac_huffman1[15] = r15;
  ac_huffman1[16] = r16;
  ac_huffman1[17] = r17;
  ac_huffman1[18] = r18;
  ac_huffman1[19] = r19;
  ac_huffman1[20] = r20;
  ac_huffman1[21] = r21;
  ac_huffman1[22] = r22;
  ac_huffman1[23] = r23;
  ac_huffman1[24] = r24;
  ac_huffman1[25] = r25;
  ac_huffman1[26] = r26;
  ac_huffman1[27] = r27;
  ac_huffman1[28] = r28;
  ac_huffman1[29] = r29;
  ac_huffman1[30] = r30;
  ac_huffman1[31] = r31;
  ac_huffman1[32] = r32;
  ac_huffman1[33] = r33;
  ac_huffman1[34] = r34;
  ac_huffman1[35] = r35;
  ac_huffman1[36] = r36;
  ac_huffman1[37] = r37;
  ac_huffman1[38] = r38;
  ac_huffman1[39] = r39;
  ac_huffman1[40] = r40;
  ac_huffman1[41] = r41;
  ac_huffman1[42] = r42;
  ac_huffman1[43] = r43;
  ac_huffman1[44] = r44;
  ac_huffman1[45] = r45;
  ac_huffman1[46] = r46;
  ac_huffman1[47] = r47;
  ac_huffman1[48] = r48;
  ac_huffman1[49] = r49;
  ac_huffman1[50] = r50;
  ac_huffman1[51] = r51;
  ac_huffman1[52] = r52;
  ac_huffman1[53] = r53;
  ac_huffman1[54] = r54;
  ac_huffman1[55] = r55;
  ac_huffman1[56] = r56;
  ac_huffman1[57] = r57;
  ac_huffman1[58] = r58;
  ac_huffman1[59] = r59;
  ac_huffman1[60] = r60;
  ac_huffman1[61] = r61;
  ac_huffman1[62] = r62;
  ac_huffman1[63] = r63;
  ac_huffman1[64] = r64;
  ac_huffman1[65] = r65;
  ac_huffman1[66] = r66;
  ac_huffman1[67] = r67;
  ac_huffman1[68] = r68;
  ac_huffman1[69] = r69;
  ac_huffman1[70] = r70;
  ac_huffman1[71] = r71;
  ac_huffman1[72] = r72;
  ac_huffman1[73] = r73;
  ac_huffman1[74] = r74;
  ac_huffman1[75] = r75;
  ac_huffman1[76] = r76;
  ac_huffman1[77] = r77;
  ac_huffman1[78] = r78;
  ac_huffman1[79] = r79;
  ac_huffman1[80] = r80;
  ac_huffman1[81] = r81;
  ac_huffman1[82] = r82;
  ac_huffman1[83] = r83;
  ac_huffman1[84] = r84;
  ac_huffman1[85] = r85;
  ac_huffman1[86] = r86;
  ac_huffman1[87] = r87;
  ac_huffman1[88] = r88;
  ac_huffman1[89] = r89;
  ac_huffman1[90] = r90;
  ac_huffman1[91] = r91;
  ac_huffman1[92] = r92;
  ac_huffman1[93] = r93;
  ac_huffman1[94] = r94;
  ac_huffman1[95] = r95;
  ac_huffman1[96] = r96;
  ac_huffman1[97] = r97;
  ac_huffman1[98] = r98;
  ac_huffman1[99] = r99;
  ac_huffman1[100] = r100;
  ac_huffman1[101] = r101;
  ac_huffman1[102] = r102;
  ac_huffman1[103] = r103;
  ac_huffman1[104] = r104;
  ac_huffman1[105] = r105;
  ac_huffman1[106] = r106;
  ac_huffman1[107] = r107;
  ac_huffman1[108] = r108;
  ac_huffman1[109] = r109;
  ac_huffman1[110] = r110;
  ac_huffman1[111] = r111;
  ac_huffman1[112] = r112;
  ac_huffman1[113] = r113;
  ac_huffman1[114] = r114;
  ac_huffman1[115] = r115;
  ac_huffman1[116] = r116;
  ac_huffman1[117] = r117;
  ac_huffman1[118] = r118;
  ac_huffman1[119] = r119;
  ac_huffman1[120] = r120;
  ac_huffman1[121] = r121;
  ac_huffman1[122] = r122;
  ac_huffman1[123] = r123;
  ac_huffman1[124] = r124;
  ac_huffman1[125] = r125;
  ac_huffman1[126] = r126;
  ac_huffman1[127] = r127;
  ac_huffman1[128] = r128;
  ac_huffman1[129] = r129;
  ac_huffman1[130] = r130;
  ac_huffman1[131] = r131;
  ac_huffman1[132] = r132;
  ac_huffman1[133] = r133;
  ac_huffman1[134] = r134;
  ac_huffman1[135] = r135;
  ac_huffman1[136] = r136;
  ac_huffman1[137] = r137;
  ac_huffman1[138] = r138;
  ac_huffman1[139] = r139;
  ac_huffman1[140] = r140;
  ac_huffman1[141] = r141;
  ac_huffman1[142] = r142;
  ac_huffman1[143] = r143;
  ac_huffman1[144] = r144;
  ac_huffman1[145] = r145;
  ac_huffman1[146] = r146;
  ac_huffman1[147] = r147;
  ac_huffman1[148] = r148;
  ac_huffman1[149] = r149;
  ac_huffman1[150] = r150;
  ac_huffman1[151] = r151;
  ac_huffman1[152] = r152;
  ac_huffman1[153] = r153;
  ac_huffman1[154] = r154;
  ac_huffman1[155] = r155;
  ac_huffman1[156] = r156;
  ac_huffman1[157] = r157;
  ac_huffman1[158] = r158;
  ac_huffman1[159] = r159;
  i = 0;
  exitg1 = false;
  while ((!exitg1) && (i < 10)) {
    guard1 = false;
    if (vector_zz[i] >= 0.0) {
      if (vector_zz[i] == 0.0) {
        if (empty == 15.0) {
          exitg1 = true;
        } else {
          empty++;
          guard1 = true;
        }
      } else {
        /*  sub2ind(size(zeros(10,16)), max(size(dec2bin(abs(vector_zz(i))))),empty+1) */
        /* temp = [ac_huffman(empty+1, max(size(dec2bin(abs(vector_zz(i)))))) dec2bin(vector_zz(i))]; */
        dec2bin(fabs(vector_zz[i]), tmp_data, tmp_size);
        for (mtmp = 0; mtmp < 2; mtmp++) {
          varargin_1[mtmp] = (signed char)tmp_size[mtmp];
        }

        mtmp = 0;
        if (varargin_1[1] > 1) {
          mtmp = varargin_1[1] - 1;
        }

        j = mtmp + 10 * ((int)(empty + 1.0) - 1);
        dec2bin(vector_zz[i], tmp_data, tmp_size);
        loop_ub = ac_huffman1[j].elem.size[1];
        for (mtmp = 0; mtmp < loop_ub; mtmp++) {
          temp_data[mtmp] = ac_huffman1[j].elem.data[mtmp];
        }

        loop_ub = tmp_size[1];
        for (mtmp = 0; mtmp < loop_ub; mtmp++) {
          temp_data[mtmp + ac_huffman1[j].elem.size[1]] = tmp_data[tmp_size[0] *
            mtmp];
        }

        xs = C->size[1];
        cs = ac_huffman1[j].elem.size[1] + tmp_size[1];
        mtmp = C->size[0] * C->size[1];
        C->size[1] = (xs + ac_huffman1[j].elem.size[1]) + tmp_size[1];
        emxEnsureCapacity((emxArray__common *)C, mtmp, (int)sizeof(char));
        for (mtmp = 0; mtmp < cs; mtmp++) {
          C->data[xs + mtmp] = temp_data[mtmp];
        }

        empty = 0.0;
        guard1 = true;
      }
    } else {
      dec2bin(fabs(vector_zz[i]), C1_data, C1_size);
      for (mtmp = 0; mtmp < 2; mtmp++) {
        varargin_1[mtmp] = (signed char)C1_size[mtmp];
      }

      mtmp = 1;
      if (varargin_1[1] > 1) {
        mtmp = varargin_1[1];
      }

      for (j = 0; j < mtmp; j++) {
        if (C1_data[C1_size[0] * j] == '0') {
          C1_data[C1_size[0] * j] = '1';
        } else {
          C1_data[C1_size[0] * j] = '0';
        }
      }

      dec2bin(fabs(vector_zz[i]), tmp_data, tmp_size);
      for (mtmp = 0; mtmp < 2; mtmp++) {
        varargin_1[mtmp] = (signed char)tmp_size[mtmp];
      }

      mtmp = 0;
      if (varargin_1[1] > 1) {
        mtmp = varargin_1[1] - 1;
      }

      j = mtmp + 10 * ((int)(empty + 1.0) - 1);

      /*  temp = [ac_huffman(empty+1, max(size(dec2bin(abs(vector_zz(i)))))) C1]; */
      xs = C->size[1];
      cs = ac_huffman1[j].elem.size[1] + C1_size[1];
      mtmp = C->size[0] * C->size[1];
      C->size[1] = (xs + ac_huffman1[j].elem.size[1]) + C1_size[1];
      emxEnsureCapacity((emxArray__common *)C, mtmp, (int)sizeof(char));
      loop_ub = ac_huffman1[j].elem.size[1];
      for (mtmp = 0; mtmp < loop_ub; mtmp++) {
        ac_huffman1_data[mtmp] = ac_huffman1[j].elem.data[mtmp];
      }

      loop_ub = C1_size[1];
      for (mtmp = 0; mtmp < loop_ub; mtmp++) {
        ac_huffman1_data[mtmp + ac_huffman1[j].elem.size[1]] = C1_data[C1_size[0]
          * mtmp];
      }

      for (mtmp = 0; mtmp < cs; mtmp++) {
        C->data[xs + mtmp] = ac_huffman1_data[mtmp];
      }

      empty = 0.0;
      guard1 = true;
    }

    if (guard1) {
      i++;
    }
  }

  mtmp = value->size[0] * value->size[1];
  value->size[0] = 1;
  value->size[1] = C->size[1] + 4;
  emxEnsureCapacity((emxArray__common *)value, mtmp, (int)sizeof(char));
  loop_ub = C->size[1];
  for (mtmp = 0; mtmp < loop_ub; mtmp++) {
    value->data[value->size[0] * mtmp] = C->data[C->size[0] * mtmp];
  }

  for (mtmp = 0; mtmp < 4; mtmp++) {
    value->data[value->size[0] * (mtmp + C->size[1])] = cv0[mtmp];
  }

  emxFree_char_T(&C);
}

/*
 * File trailer for huffman_ac.c
 *
 * [EOF]
 */
