从Android 4.1开始,Google Play 引入了应用程序的增量更新功能，App使用该升级方式，可节省约2/3的流 量。现在国内主流的应用市场也都支持应用的增量更新。

**增量更新**主要有两个过程，第一是后台将最新的apk（v2.0）与旧版本(v1.0)做对比，并**生成差分包**。第二步是移动端下载相对应的差分包，并在本地合并生成完整的最新apk，并引导用户安装。

#### 使用BSDiff

1. 官方说明:http://www.daemonology.net/bsdiff/
2. 下载官方源码包**bsdiff.c** 和**bspatch.c**以及**makefile**

   - **bsdiff.c** (比较两个文件的二进制数据，生成差分包)
   - **bspatch.c** (合并旧的文件 与差分包，生成新的文件)
   - **makefile** (用于`Linux/Mac`编译可执行文件,需提前安装bzip2源码包依赖)
3. `bsdiff oldFile.xxx  newFile.xxx patch`
4. `bspatch oldFile.xxx newFile.xxx patch`
5. `patch`只是一个二进制差异文件
6. 在`Linux`中,一切对象皆是文件

#### Android中使用

- 导入源文件到CPP目录

- 在CMakeLists.txt中声明使用

  ```cmake
  cmake_minimum_required(VERSION 3.4.1)
  #导入这个C/CPP文件夹中所有C/CPP源文件
  aux_source_directory(bzip2 SOURCE)
  
  add_library(
          bspatch_utlis
          SHARED
          native-lib.cpp
          #导入单个C/CPP文件
          bspatch.c
          ${SOURCE})
  include_directories(bzip2)
  
  
  find_library(
          log-lib
          log)
  
  target_link_libraries(
          bspatch_utlis
          ${log-lib})
  ```

  

- 在`native-lib.cpp`中声明JNI方法(可以了通过android studio自动生成)

  ```cpp
  #include <jni.h>
  #include <string>
  //当前是CPP,必须兼容C
  extern "C" {
  extern int executePatch(int argc, char *argv[]);
  }
  
  extern "C"
  JNIEXPORT jint JNICALL
  Java_com_keyejxptwn_diffpatch_BsPatchUtils_00024Companion_patch(JNIEnv *env, jobject thiz,
                                                                  jstring old_apk,
                                                                  jstring new_apk,
                                                                  jstring patch_file) {
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
  ```

  

- 在`MainActivity.java`中使用

  ```kotlin
   //增量更新点击
      fun update(view: View) {
          val newFile = File(getExternalFilesDir("apk"), "app.apk")
          val patchFile = File(getExternalFilesDir("apk"), "patch.apk")
          val result: Int = BsPatchUtils.patch(
              applicationInfo.sourceDir, newFile.getAbsolutePath(), patchFile.getAbsolutePath()
          )
          if (result == 0) install(newFile)
  
      }
  ```

  
