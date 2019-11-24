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
 * Method:    PbuttonRead
 */
JNIEXPORT jint JNICALL Java_com_example_test_MainActivity_PbuttonRead
        (JNIEnv *jenv, jobject self){
    int fd;
    unsigned char wordvalue;
    unsigned char ret;

    fd = open("/dev/pbutton", O_RDONLY);
    if (fd < 0) {
        //__android_log_print(ANDROID_LOG_ERROR, "PbuttonRead", "Device open error : /dev/pushbutton\n");
        return -1;
    }

    ret = read(fd, &wordvalue, 1);
    if (ret < 0) {
        //__android_log_print(ANDROID_LOG_ERROR, "PbuttonRead", "Read Error!\n");
        return -1;
    }

    close(fd);

    return wordvalue;
}
