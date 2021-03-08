/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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
 */

#include <jni.h>
#include <native_crash_create.h>

typedef struct {
    int nameSize;
    int descriptorSize;
    int type;
} noteSection;

int NativeCrashCreate_five() {
    noteSection *section = NULL;
    section->nameSize = 10;
    return 0;
}

int NativeCrashCreate_four() {
    return NativeCrashCreate_five();
}

int NativeCrashCreate_three() {
    return NativeCrashCreate_four();
}

int NativeCrashCreate_two() {
    return NativeCrashCreate_three();
}

int NativeCrashCreate_one() {
    return NativeCrashCreate_two();
}

extern "C" int Java_com_huawei_agc_quickstart_crash_MainActivity_nativeCrashCreate(JNIEnv *env, jobject thiz) {
    return NativeCrashCreate_one();
}