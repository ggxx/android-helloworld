package com.plagh.ggxx.helloworld;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ggxx on 6/23/2017.
 */

public class MyGLSurfaceView extends GLSurfaceView
        implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "MyGLSurfaceView";
    Context mContext;
    SurfaceTexture mSurface;
    int mTextureID = -1;
    CameraDrawer mDirectDrawer;
    Camera camera;
    EffectContext mEffectContext;
    Effect mEffect;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        // 设置OpenGl ES的版本为2.0
        setEGLContextClientVersion(2);
        // 设置与当前GLSurfaceView绑定的Renderer
        setRenderer(this);
        // 设置渲染的模式
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onSurfaceCreated...");
        mTextureID = createTextureID();
        mSurface = new SurfaceTexture(mTextureID);
        mSurface.setOnFrameAvailableListener(this);
        mDirectDrawer = new CameraDrawer(mTextureID);
        camera = Camera.open();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onSurfaceChanged...");

        // ---
        int[] mTexNames = new int[1];
        GLES20.glGenTextures(1, mTexNames, 0);
        // ---

        GLES20.glViewport(0, 0, width, height);
        try {
            camera.setPreviewTexture(mSurface);
            camera.startPreview();
        } catch (IOException ioe) {
            Log.i(TAG, ioe.getMessage());
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onDrawFrame...");
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage();
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);


        if (mEffectContext == null) {
            mEffectContext = EffectContext.createWithCurrentGlContext();
        }
        if (mEffect != null) {
            mEffect.release();
        }

        if (EffectFactory.isEffectSupported(EffectFactory.EFFECT_BRIGHTNESS)) {

            EffectFactory factory = mEffectContext.getFactory();
            mEffect = factory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
            mEffect.setParameter("brightness", 2f);
            //mEffect.apply(0, 0, 1, 1);
        }

        mDirectDrawer.draw(mtx);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        camera.stopPreview();
    }

    private int createTextureID() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public SurfaceTexture _getSurfaceTexture() {
        return mSurface;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onFrameAvailable...");
        this.requestRender();
    }
}
