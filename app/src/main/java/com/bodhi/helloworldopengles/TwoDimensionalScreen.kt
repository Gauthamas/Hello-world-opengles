package com.bodhi.helloworldopengles

import android.content.Context
import android.opengl.GLES31
import com.bodhi.helloworldopengles.RawResourceReader.readTextFileFromRawResource
import com.bodhi.helloworldopengles.TextureHelper.loadTexture
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10


class TwoDimensionalScreen(private val mActivityContext: Context)  {


    private val mVertexBuffer: FloatBuffer
    private val mTextureBuffer: FloatBuffer
    private val mProgram: Int
    private var mPositionHandle = 0
    private var mTextureCoordinateHandle = 0
    private var mTextureDataHandle = 0
    private var mTextureUniformHandle = 0
    private var mIsPortraitShaderHandle = 0
    private var mIsPortraitShader = 0

    private var mViewWidthShader = 0.0f
    private var mViewWidthShaderHandle = 0

    private var mViewHeightShader = 0.0f
    private var mViewHeightShaderHandle = 0

    private var mAspectRatioShader = 512.0f/256.0f
    private var mAspectRatioShaderHandle = 0

    private val mVertexCount = sTriangleCoords.size / COORDS_PER_VERTEX
    private val mVertexStride = COORDS_PER_VERTEX * 4

    private val mVertexShaderCode: String? = readTextFileFromRawResource(
            mActivityContext,
            R.raw.vertex_shader
        )
    private val mFragmentShaderCode: String? = readTextFileFromRawResource(
            mActivityContext,
            R.raw.fragment_shader
        )


    fun draw(gl: GL10, width: Int, height: Int, isPortrait: Boolean) {

        GLES31.glUseProgram(mProgram)
        GLES31.glViewport(0, 0, width, height)

        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0)


        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "a_Position")
        mTextureCoordinateHandle = GLES31.glGetAttribLocation(mProgram, "a_TexCoordinate")
        mTextureUniformHandle = GLES31.glGetUniformLocation(mProgram, "u_Texture")
        mIsPortraitShaderHandle = GLES31.glGetUniformLocation(mProgram, "u_IsPortrait")
        mViewHeightShaderHandle = GLES31.glGetUniformLocation(mProgram, "u_ViewHeight")
        mViewWidthShaderHandle = GLES31.glGetUniformLocation(mProgram, "u_ViewWidth")
        mAspectRatioShaderHandle = GLES31.glGetUniformLocation(mProgram, "u_AspectRatio")

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0)


        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mTextureDataHandle)

        GLES31.glUniform1i(mTextureUniformHandle, 0)

        if(isPortrait){
            mIsPortraitShader = 1
        } else {
            mIsPortraitShader = 0
        }

        GLES31.glUniform1i(mIsPortraitShaderHandle, mIsPortraitShader)

        GLES31.glUniform1f(mViewWidthShaderHandle, width.toFloat())

        GLES31.glUniform1f(mViewHeightShaderHandle, height.toFloat())

        GLES31.glUniform1f(mAspectRatioShaderHandle, mAspectRatioShader)

        GLES31.glEnableVertexAttribArray(mTextureCoordinateHandle)

        GLES31.glVertexAttribPointer(
            mTextureCoordinateHandle, 2, GLES31.GL_FLOAT, false,
            0, mTextureBuffer
        )

        GLES31.glEnableVertexAttribArray(mPositionHandle)

        GLES31.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES31.GL_FLOAT, false,
            mVertexStride, mVertexBuffer
        )

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, mVertexCount)

        GLES31.glDisableVertexAttribArray(mPositionHandle)
        GLES31.glDisableVertexAttribArray(mTextureCoordinateHandle)


    }

    companion object {
        const val COORDS_PER_VERTEX = 3
        var sTriangleCoords = floatArrayOf(
            -1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f
        )
        var sTextureCoords = floatArrayOf(
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f

        )

    }

    private fun loadShader(type: Int, shaderCode: String?): Int {

        val shader = GLES31.glCreateShader(type)

        GLES31.glShaderSource(shader, shaderCode)
        GLES31.glCompileShader(shader)
        return shader
    }

    init {

        mVertexBuffer =
            ByteBuffer.allocateDirect(sTriangleCoords.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mVertexBuffer.put(sTriangleCoords).position(0)

        mTextureBuffer =
            ByteBuffer.allocateDirect(sTextureCoords.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTextureBuffer.put(sTextureCoords).position(0)


        val vertexShader: Int = loadShader(
            GLES31.GL_VERTEX_SHADER,
            mVertexShaderCode
        )
        val fragmentShader: Int = loadShader(
            GLES31.GL_FRAGMENT_SHADER,
            mFragmentShaderCode
        )

        mProgram = GLES31.glCreateProgram()

        GLES31.glAttachShader(mProgram, vertexShader)

        GLES31.glAttachShader(mProgram, fragmentShader)

        GLES31.glBindAttribLocation(mProgram, 0, "a_Position")
        GLES31.glBindAttribLocation(mProgram, 1, "a_TexCoordinate")

        GLES31.glLinkProgram(mProgram)

        mTextureDataHandle = loadTexture(mActivityContext, R.drawable.helloworld)

    }
}