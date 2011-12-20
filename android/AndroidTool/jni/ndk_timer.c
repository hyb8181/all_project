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
#include "ndk_timer.h"
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
double now_ms(void)
{
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec*1000. + tv.tv_usec/1000.;
}
/******************************************************************************
 * Function: 		now_colcktime
 * Description: 	获取当前秒数
 * Calls: 			null
 * Called By:		null
 * Input: 			null
 * Output:			null
 * Return:			timer
 * Others:			null
 ******************************************************************************/
double now_colcktime(void){
	clock_t time;
	time = clock();
	return (double)time;
}

