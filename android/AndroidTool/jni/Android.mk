# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ndk_main
LOCAL_SRC_FILES := \
    ndk_main.c \
    ndk_string.c \
    
#gdb语句可去除
LOCAL_CFLAGS := -ggdb 
#增加LOG需要加以下语句
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog

LOCAL_CPPFLAGS:= \
#-I $(MY_ANDROID_SOURCE)/frameworks/base/core/jni/android/graphics \
#-I $(MY_ANDROID_SOURCE)/external/skia/include/core \
#-I $(MY_ANDROID_SOURCE)/external/skia/include/images \
#-I $(MY_ANDROID_SOURCE)/frameworks/base/include \
#-I $(MY_ANDROID_SOURCE)/system/core/include


include $(BUILD_SHARED_LIBRARY)
