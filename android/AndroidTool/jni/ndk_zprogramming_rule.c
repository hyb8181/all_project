/******************************************************************************
 * Copyright (C) 2011 NDK Project
 * File name: 	文件名
 * Description: 用于详细说明此程序文件完成的主要功能,与其他模块或函数的接口,输出
 * 				值.取值范围.含义及参数间的控制,顺序,独立或依赖等关系
 * Author: 		作者
 * Version: 	版本
 * Data: 		完成日期
 * History: 	修改历史记录列表,每条修改记录应包括修改日期,修改者及修改内容简述.
 ******************************************************************************/
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <math.h>
#define LOG_TAG "JNITAG" 
#define LOG(a)  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,a)


int test(int a, int b);
int ndk_string_jump_match_test();
/******************************************************************************
 * Function: 		函数名称
 * Description: 	函数功能,性能等的描述
 * Calls: 			被本函数调用的函数清单
 * Called By:		调用本函数的函数清单
 * Input: 			输入参数说明,包括每个参数的作用,取值说明及参数间的关系.
 * Output:			对输出参数的说明
 * Return:			函数返回值的说明
 * Others:			其它说明
 ******************************************************************************/
Java_com_AndroidTool_HelloJni_stringFromJNI( JNIEnv* env, jobject thiz )
{
	LOG("JNI被调用1243567");
	char *str ="hello str";
	int result;
	int i;
	result = test(3,4);
	LOG("=========================jumpmatch_start");
	for(i = 0; i < 90001; i++)
	{
		ndk_string_jump_match_test();
	}
	LOG("==========================jumpmatch_end");

//	return (*env)->NewStringUTF(env, "2011年11月29日2011年11月29日14:57:50!");
	return (*env)->NewStringUTF(env, str);
}
int test(int a, int b){
	return a+b;
}

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
	printf("ndk_string_jump_match_test入口");
	for(; c_num < sizeof(c[3][5]); c_num++)
	{
		memset(c_num+c,0,sizeof(c));
	}

	for(j_index= 0; j_index < strlen(b[j_index]); j_index++)
	{
		j = j_index;

		for(;i < strlen(a) && k < strlen(b[j]);)
		{

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
				printf("结果为:");
				for(i_c = 0; i_c < 3; i_c++)
				{
					for(j_c = 0; j_c < 5; j_c++)
					{
						printf("%d",c[i_c][j_c]);
					}
				}
				printf("\n");

				return 1;
			}
		}
		memset(&c[3][5],0,sizeof(c[3][5]));
		match_c = 0;
		//j_max = strlen(b[j_index]);
	}
	return 0;
}
//获取本地时间
//time_t rawtime;
//struct tm * timeinfo;
//
//time (&rawtime);
//timeinfo = localtime (&rawtime);


