#include <jni.h>
#include <string>
#include "android/log.h"

#define LOGI(M) __android_log_print(android_LogPriority::ANDROID_LOG_INFO ,"TTTTT",M)

//void LOGIm(const char *M , va_list...va_list1) {
//    __android_log_print(android_LogPriority::ANDROID_LOG_INFO ,"TTTTT",M,va_list1);
//}

extern "C"
int test1(int a){
    return a+1;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ex_cyy_demo4_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {

    //B BX BL   BX+BL=BLX
    //直接跳转
    //根据 Rm 最后一位判断 跳转后转为 Arm指令模式或Thumb 指令模式
    //跳转前保存当前位置到 lr 寄存器，以跳转后 mov pc,lr 返回


    //？_ACLE

#ifdef __i386
#warning "__i386 SUPPORT ! TTTTTTTTTTTTTTTTTTTTTTT"
#endif

#ifdef __x86_64__
#warning "__x86_64__ SUPPORT ! TTTTTTTTTTTTTTTTTTTTTTT"
#endif



#ifdef __ARM_ACLE
#warning "__ARM_ACLE enabled. TTTTTTTTTTTTTT"
#endif

#ifdef __arm__
#warning "__arm__ SUPPORT ! TTTTTTTTTTTTTTTTTTTTTTT"
#endif

#ifdef __aarch64__
#warning "__aarch64__ SUPPORT ! TTTTTTTTTTTTTTTTTTTTTTT"
#endif


#ifdef __ARM_ACLE
    LOGI("[ARM]");
    typedef int (*F1)(int);
    F1 foo = test1;
    int mr0 = 4;

#ifdef __arm__
    //R0-R7  R8-R12  R13   R14   R15
//       r -irq  r sp  r lr  r pc
    LOGI("[ARM32]");

    __asm__ __volatile__ (
        //"stmfd sp!,{%0}\n"
        "mov r1,%0\n"
        "ldr r0,=0x0\n"
        "blx %1\n"
        "mov %0,r0\n"
        //"ldmfd sp!,{%0}\n"
        //"ldr %0,=0x3\n"
        :"+r"(mr0)
        :"r"(foo)
        :"memory","r0","r1"
    );

    LOGI("aaaaa");
    __android_log_print(android_LogPriority::ANDROID_LOG_INFO ,"TTTTT","r0 = %d", mr0);
#endif

#ifdef __aarch64__
    //R0-R7  R8-R12  R13   R14   R15
//           r -irq  r sp  r lr  r pc
// B BL  给立即数 （地址） 跳转 ， BL保存PC+4 到 lr
// BR BLR 给寄存器，寻址后跳转  ， BLR保存PC+4 到 lr
//64位没有 BX
    LOGI("[ARM64]");

    __asm__ __volatile__ (
        //"stmfd sp!,{%0}\n"
        "mov x0,%0\n"
        //"mov x1,%0\n"
        "blr %1\n"
        "mov %0,x0\n"
        //"ldmfd sp!,{%0}\n"
        //"ldr %0,=0x3\n"
        :"+r"(mr0)
        :"r"(foo)
        :"memory","x0","x1"
    );

    LOGI("BBBBB");
    __android_log_print(android_LogPriority::ANDROID_LOG_INFO ,"TTTTT","x0 = %d", mr0);
#endif
#else
    LOGI("[NOT ARM]");
#endif

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
