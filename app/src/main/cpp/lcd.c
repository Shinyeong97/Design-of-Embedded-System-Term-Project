//
// Created by CAU on 2019-11-21.
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
/*
 * Class:     com_example_test_MainActivity
 * Method:    LcdWrite
 */

#define LCD_MAGIC               0xBD
#define LCD_SET_CURSOR_POS     _IOW(LCD_MAGIC, 0, int)

JNIEXPORT jint JNICALL Java_com_example_test_MainActivity_LcdWrite
        (JNIEnv *jenv, jobject self, jstring first, jstring second){
    int fd, pos;


    /* Write Only */
    fd = open("/dev/lcd", O_WRONLY);
    if (fd < 0) {
        //__android_log_print(ANDROID_LOG_ERROR, "LcdWrite", "Device open error : /dev/lcd\n");
        return -1;
    }

    const char *firstString = (*jenv)->GetStringUTFChars(jenv, first, 0);
    const char *secondString = (*jenv)->GetStringUTFChars(jenv, second, 0);

    pos = 0;
    ioctl(fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
    write(fd, firstString, strlen(firstString));

    pos = 16;
    ioctl(fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
    write(fd, secondString, strlen(secondString));

    (*jenv)->ReleaseStringUTFChars(jenv, first, firstString);
    (*jenv)->ReleaseStringUTFChars(jenv, second, secondString);

    close(fd);

    return 0;
}
