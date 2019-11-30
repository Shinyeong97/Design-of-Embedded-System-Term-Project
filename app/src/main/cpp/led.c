//
// Created by CAU on 2019-11-30.
//

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <android/log.h>

JNIEXPORT jint JNICALL
Java_com_example_PuyoPuzzle_MainActivity_LedWrite (JNIEnv *jenv, jobject self, jint data){
    int fd;
    unsigned char ret;
    int b1 = 165;
    int b2 = 90;
    int zero = 0;

    if ((data < -1) || (data > 0xff)) {
//        __android_log_print(ANDROID_LOG_ERROR, "LedWrite", "Out of range! \n");
        return -1;
    }

    fd = open("/dev/led", O_RDWR);
    if (fd < 0) {
//        __android_log_print(ANDROID_LOG_ERROR, "LedWrite", "Device open error : /dev/led\n");
        return -1;
    }

    if (data == -1) {
        ret = write(fd, &b1, 1);
        usleep(1000 * 200);
        ret = write(fd, &b2, 1);
        usleep(1000 * 200);
        ret = write(fd, &b1, 1);
        usleep(1000 * 200);
        ret = write(fd, &b2, 1);
        usleep(1000 * 200);
        ret = write(fd, &zero, 1);
    }
    else {
        ret = write(fd, &data, 1);
        usleep(1000 * 400);
        ret = write(fd, &zero, 1);
    }

    close(fd);

    return 0;
}