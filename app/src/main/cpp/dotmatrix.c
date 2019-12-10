//
// Created by CAU on 2019-11-30.
//

/*
 * Class:     com_example_PuyoPuzzle_MainActivity
 * Method:    DotWrite
 * DATA 1~9: 숫자 출력
 * DATA -1 : WIN    // 우승 시
 * DATA -2 : LOSE   // 패배 시
 * DATA -3 : SCORE  // 득점 시
 * DATA -4 : BEAT   // 상대 플레이어 패배 시
 * DATA -5 : FIRE   // 그냥...
 */
#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include <fcntl.h>
#include <bits/ioctl.h>
#include <sys/ioctl.h>
#include <unistd.h>

#define DOTM_MAGIC          0xBC
#define DOTM_SET_ALL        _IOW(DOTM_MAGIC, 0, int)
#define DOTM_SET_CLEAR      _IOW(DOTM_MAGIC, 1, int)
#define DOTM_WIN            _IOW(DOTM_MAGIC, 2, int)
#define DOTM_LOSE           _IOW(DOTM_MAGIC, 3, int)
#define DOTM_SCORE          _IOW(DOTM_MAGIC, 4, int)
#define DOTM_BEAT           _IOW(DOTM_MAGIC, 5, int)
#define DOTM_FIRE           _IOW(DOTM_MAGIC, 6, int)

JNIEXPORT void JNICALL
Java_com_example_PuyoPuzzle_MainActivity_DotWrite (JNIEnv *jenv, jobject self, jint data){
    int fd, i;

    if ((data < -5) || (data > 9)) {
        //__android_log_print(ANDROID_LOG_ERROR, "DotWrite", "Out of range! \n");
        return;
    }

    fd = open("/dev/dotmatrix", O_WRONLY);

    if (fd != -1) {
        // data가 한자리 자연수값이면 해당 자연수 출력
        if ((data >= 0) && (data <= 9)) {
            write(fd, &data, sizeof(data));
            usleep(1000 * 200);
            ioctl(fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SET_CLEAR));
        }

            // data == -1 이면 win 문구 출력
        else if (data == -1) {
            ioctl(fd, DOTM_WIN, NULL, _IOC_SIZE(DOTM_WIN));
            ioctl(fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SET_CLEAR));
        }

            // data == -2 이면 lose 문구 출력
        else if (data == -2) {
            ioctl(fd, DOTM_LOSE, NULL, _IOC_SIZE(DOTM_LOSE));
            ioctl(fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SET_CLEAR));
        }

            // data == -3 이면 득점 문구 출력
        else if (data == -3) {
            ioctl(fd, DOTM_SCORE, NULL, _IOC_SIZE(DOTM_SCORE));
            ioctl(fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SET_CLEAR));
        }

            // data == -4 이면 상대 플레이어 패배 문구 출력
        else if (data == -4) {
            ioctl(fd, DOTM_BEAT, NULL, _IOC_SIZE(DOTM_BEAT));
            ioctl(fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SET_CLEAR));
        }

            // data == -5 이면 불모양 출력
        else if (data == -5) {
            ioctl(fd, DOTM_FIRE, NULL, _IOC_SIZE(DOTM_FIRE));
            ioctl(fd, DOTM_FIRE, NULL, _IOC_SIZE(DOTM_FIRE));
            ioctl(fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SET_CLEAR));
        }

    }
    else {
        //__android_log_print(ANDROID_LOG_ERROR, "DotWrite", "error opening device : /dev/dotmatrix\n");
        return;
    }

    close(fd);

    return;
}