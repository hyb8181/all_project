/******************************************************************************
 * Copyright (C) 2011 NDK Project
 * File name: 	ndk_global.h
 * Description: 头文件
 * Author: 		汪军
 * Version: 	1.0
 * Data: 		2011年12月17日
 * History:		修改日期					修改者	修改内容
 * 			 	2011年12月17日13:52 		汪军		create
 ******************************************************************************/
#ifndef __NDK_GLOBAL_H__
#define __NDK_GLOBAL_H__
#include <string.h>
#include <jni.h>
#include <android/log.h>

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include "time.h"

#define LOG_TAG "ndk_global"
#define LOG(a)  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,a)
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__))
#endif
