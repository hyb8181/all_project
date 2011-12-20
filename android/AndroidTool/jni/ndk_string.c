/******************************************************************************
 * Copyright (C) 2011 NDK Project
 * File name: 	ndk_string.c
 * Description: 本文件主要完成string类的封装
 * Author: 		wj
 * Version: 	0.1
 * Data: 		完成日期
 * History:		修改日期			修改者	修改内容
 * 				2011年12月14日 	wj		base
 ******************************************************************************/
#include "ndk_global.h"
#include "ndk_string.h"
/******************************************************************************
 * Function: 		ndk_string_jump_match_test
 * Description: 	跳跃匹配人名测试用例
 * Calls: 			null
 * Called By:		Java_com_AndroidTool_HelloJni_stringFromJNI
 * Table Accessed:	null
 * Table Undated:	null
 * Input: 			null
 * Output:			null
 * Return:			null
 * Others:			null
 ******************************************************************************/
int ndk_string_jump_match_test(){
	char a[]="lch";
	char b[3][5]={{"li"},{"chua"},{"jian"}};
	char c[3][5];
	int i = 0;
	int j_index = 0 ;
	int k = 0;
	int j = j_index;
	int match_c = 0;

	int j_max = strlen(b[j_index]);
	int c_num = 0;
	for(; c_num < sizeof(c[3][5]); c_num++)
	{
		memset(c_num+c,0,sizeof(c));
	}

	for(j_index= 0; j_index < strlen(b[j_index]); j_index++)
	{
		j = j_index;
		for(;i < strlen(a) && k < strlen(b[j]);)
		{
            LOGI("ndk_string_jump_match_test: i=%d j=%d k = %d", i, j, k);
			if(a[i] != b[j][k])
			{
				j++;
				k=0;
			}
			else
			{
				c[j][k] = 1;
				match_c++;
				i++;k++;
			}

			if(match_c == strlen(a))
			{
				//return c[3][5];
				int i_c;
				int j_c;
				//printf("结果为:");
				for(i_c = 0; i_c < 3; i_c++)
				{
					for(j_c = 0; j_c < 5; j_c++)
					{
						//printf("%d",c[i_c][j_c]);
					}
				}
				//printf("\n");

				return 1;
			}
		}
		memset(&c[3][5],0,sizeof(c[3][5]));
		match_c = 0;
	}
	return 0;
}

//浮点数转为字符串
//memset(chtotaltimer, '\0',sizeof(chtotaltimer));
//totaltimer =(double)(end-start);
//sprintf(chtotaltimer, "%f",totaltimer);
//LOGI("C语言性能测试: shijian=%f", i, j, k);
