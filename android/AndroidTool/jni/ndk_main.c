/******************************************************************************
 * Copyright (C) 2011 NDK Project
 * File name: 	ndk_main.c
 * Description: ndk主函数
 * Author: 		汪军
 * Version: 	1.0
 * Data: 		2011年12月15日
 * History:		修改日期					修改者	修改内容
 * 			 	2011年12月15日11:35:29 	汪军		create
 ******************************************************************************/
#include "ndk_global.h"
#include "ndk_main.h"
/******************************************************************************
 * Function: 		Java_com_AndroidTool_NdkMainActivity_ndkStringFromJNI
 * Description: 	HelloJni调用此JNI接口函数
 * Calls: 			ndk_string_jump_match_test		跳跃匹配测试
 * 					ndk_analysisPerformance			ndk性能测试
 * Called By:		Java_com_AndroidTool_HelloJni_stringFromJNI
 * Input: 			null
 * Output:			null
 * Return:			null
 * Others:			log打印消耗时间
 ******************************************************************************/
jstring
Java_com_AndroidTool_NdkMainActivity_ndkStringFromJNI( JNIEnv* env, jobject thiz )
{
	LOG("调用JNI字符串传入接口");
	char *str ="JNI中传入字符串";

	ndk_string_jump_match_test();//跳跃匹配

	return (*env)->NewStringUTF(env, str);
}
/******************************************************************************
 * Function: 		Java_com_AndroidTool_NdkMainActivity_ndkAnalysisPerformance
 * Description: 	JNI性能测试接口
 * Calls: 			ndk_analysisPerformance		ndk性能测试
 * Called By:		null
 * Input: 			null
 * Output:			null
 * Return:			null
 * Others:			log打印消耗时间
 ******************************************************************************/
jstring
Java_com_AndroidTool_NdkMainActivity_ndkAnalysisPerformance( JNIEnv* env, jobject thiz )
{
	LOG("调JNI性能测试接口");
	char *str ="JNI中传入字符串";

	ndk_analysisPerformance();//ndk性能测试

	return (*env)->NewStringUTF(env, str);
}
/******************************************************************************
 * Function: 		ndk_analysisPerformance
 * Description: 	对比分析C与JAVA的性能
 * Calls: 			被本函数调用的函数清单
 * Called By:		Java_com_AndroidTool_HelloJni_stringFromJNI
 * Input: 			null
 * Output:			null
 * Return:			null
 * Others:			log打印消耗时间
 ******************************************************************************/
void ndk_analysisPerformance(void){

	clock_t start,end;
	int j,k;
	start=clock();
	for(j = 0; j < 1000; j++)
	{
		for(k = 0; k < 10000; k++)
		{
		}
	}
	end=clock();

	//浮点数转为字符串
    LOGI("C语言性能测试: 时间=%f", (double)(end-start));

}

/******************************************************************************
 * Function: 		now_ms
 * Description: 	获取当前秒数
 * Calls: 			null
 * Called By:		null
 * Input: 			null
 * Output:			null
 * Return:			timer
 * Others:			null
 ******************************************************************************/
static double now_ms(void)
{
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec*1000. + tv.tv_usec/1000.;
}

