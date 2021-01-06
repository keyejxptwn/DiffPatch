#include <jni.h>
#include <string>
//当前是CPP,必须兼容C
extern "C" {
extern int executePatch(int argc, char *argv[]);
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_keyejxptwn_diffpatch_BsPatchUtils_patch(JNIEnv *env, jobject thiz, jstring old_apk,
                                                 jstring new_apk, jstring patch_file) {
    //其实就是执行Linux命令行 patch old_apk new_apk patch
    int args = 4;//表示命令行个数 patch old_apk new_apk patch总共4个
    char *argv[args];//参数
    argv[0] = "patch";
    //转换字符串到C能识别的chars
    argv[1] = const_cast<char *>(env->GetStringUTFChars(old_apk, 0));
    argv[2] = const_cast<char *>(env->GetStringUTFChars(new_apk, 0));
    argv[3] = const_cast<char *>(env->GetStringUTFChars(patch_file, 0));
    //执行patch方法.点击进入C/CPP代码
    int result = executePatch(args, argv);
    //释放chars
    env->ReleaseStringUTFChars(old_apk, argv[1]);
    env->ReleaseStringUTFChars(new_apk, argv[2]);
    env->ReleaseStringUTFChars(patch_file, argv[3]);
    return result;

}