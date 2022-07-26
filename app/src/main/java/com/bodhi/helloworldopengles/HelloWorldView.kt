package com.bodhi.helloworldopengles

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class HelloWorldView : GLSurfaceView {
    private var mActivityContext: Context? = null;
    constructor(context: Context?) : super(context) {
        init()
        mActivityContext = context
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
        mActivityContext = context
    }

    private fun init() {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        setRenderer(HelloWorldRenderer(context))
    }
}