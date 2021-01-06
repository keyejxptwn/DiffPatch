package com.keyejxptwn.diffpatch

object BsPatchUtils {
    init {
        System.loadLibrary("bspatch_utlis")
    }
    external fun patch(oldApk: String, newApk: String, patchFile: String): Int


}