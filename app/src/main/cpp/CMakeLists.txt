
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