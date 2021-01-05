package com.keyejxptwn.diffpatch

class BsPatchUtils {
    //伴生对象
    companion object {
        init {
            System.loadLibrary("bspatch_utlis")
        }
        //native方法 返回0表示合成成功
        external fun patch(oldApk: String, newApk: String, patchFile: String): Int
    }

}