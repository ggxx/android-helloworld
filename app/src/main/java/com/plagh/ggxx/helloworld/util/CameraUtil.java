package com.plagh.ggxx.helloworld.util;

import android.hardware.Camera;

/**
 * Created by ggxx on 6/21/2017.
 */

public final class CameraUtil {

    public static final int INVALID_CAMERA_ID = -1;

    /**
     * 获取前置摄像头id
     *
     * @return
     */
    public static int findFrontFacingCamera() {
        int cameraId = INVALID_CAMERA_ID;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                //Log.d("CameraUtil", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 获取后置摄像头id
     *
     * @return
     */
    public static int findBackFacingCamera() {
        int cameraId = INVALID_CAMERA_ID;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                //Log.d("CameraUtil", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public static boolean isCameraIdValid(int cameraId) {
        return cameraId != INVALID_CAMERA_ID;
    }


    static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }


    static public int myDecodeYUV420SP(byte[] yuv420sp, int width, int height,
                                       int mWidth, int mHeight) {
        // Y矩阵长度frameSize , V和U矩阵第一位即frameSize
        final int frameSize = width * height;
        // yp为Y在矩阵中的位置，yph为所需要点的高mHeight-1，ypw为所需要点的宽mWidth
        int yp, yph = mHeight - 1, ypw = mWidth;
        yp = width * yph + ypw;
        // uvp为
        // uv在数组中的位置，V和U矩阵第一位即frameSize，yph>>1取值范围（0，0，1，1，2，2...）yph从0开始，即UV数组为Y数组的1/2.
        int uvp = frameSize + (yph >> 1) * width, u = 0, v = 0;
        // 获取Y的数值
        int y = (0xff & ((int) yuv420sp[yp])) - 16;
        if (y < 0)
            y = 0;
        if ((ypw & 1) == 0) {
            v = (0xff & yuv420sp[uvp++]) - 128;
            u = (0xff & yuv420sp[uvp]) - 128;
        } else {
            u = (0xff & yuv420sp[uvp--]) - 128;
            v = (0xff & yuv420sp[uvp]) - 128;
        }

        int y1192 = 1192 * y;
        int r = (y1192 + 1634 * v);
        int g = (y1192 - 833 * v - 400 * u);
        int b = (y1192 + 2066 * u);

        if (r < 0)
            r = 0;
        else if (r > 262143)
            r = 262143;
        if (g < 0)
            g = 0;
        else if (g > 262143)
            g = 262143;
        if (b < 0)
            b = 0;
        else if (b > 262143)
            b = 262143;

        return (0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff));
    }
}