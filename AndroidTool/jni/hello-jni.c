/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <math.h>
#define LOG_TAG "JNITAG"
#define LOG(a)  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,a)


/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:unresolved include <android/log.h>
 *
 *   apps/samples/hello-jni/project/src/com/example/HelloJni/HelloJni.java
 */

int test(int a, int b);
int ndk_string_jump_match_test();

Java_com_AndroidTool_HelloJni_stringFromJNI( JNIEnv* env, jobject thiz )
{
	LOG("JNI被调用");
	char *str ="hello str";
	int result;
	int i;
	result = test(3,4);
	LOG("=========================jumpmatch_start");
	for(i = 0; i < 90000; i++)
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


