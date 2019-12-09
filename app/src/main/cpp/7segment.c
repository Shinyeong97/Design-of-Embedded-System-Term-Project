//
// Created by CAU on 2019-11-21.
//

#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <android/log.h>
/*
 * Class:     com_example__MainActivity
 * Method:    SSegmentWrite
 */
JNIEXPORT void JNICALL Java_com_example_PuyoPuzzle_MainActivity_SSegmentWrite
        (JNIEnv *jenv, jobject self, jint data){
    int fd;
    unsigned char bytevalues[4];
    unsigned char ret;

    if ((data < 0) || (data > 9999)) {
        printf("Invalid range!\n");
        return;
    }

    fd = open("/dev/7segment", O_RDWR);
    if (fd < 0) {
        __android_log_print(ANDROID_LOG_ERROR, "SSegmentWrite", "Device open error : /dev/7segment\n");
        return;
    }

    bytevalues[0] = data / 1000;
    data = data % 1000;
    bytevalues[1] = data / 100;
    data = data % 100;
    bytevalues[2] = data / 10;
    data = data % 10;
    bytevalues[3] = data;

    ret = write(fd, bytevalues, 4);
    if (ret < 0) {
        __android_log_print(ANDROID_LOG_ERROR, "SSegmentWrite", "Write Error!\n");
        return;
    }


    close(fd);

    return;
}
