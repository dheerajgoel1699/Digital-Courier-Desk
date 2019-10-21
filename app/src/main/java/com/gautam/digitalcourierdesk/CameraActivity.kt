package com.gautam.digitalcourierdesk

import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.activity_camera.*
import org.jetbrains.anko.startActivity
import java.io.File

class CameraActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    while(true){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                123
            )
        } else {
            break
        }}
        textureView.post {
            startCamera()
        }
        manualButton.setOnClickListener {
            startActivity<ManualEntryActivity>()
        }

    }
    private fun startCamera() {
        // to capture image on button click
        val imageCaptureConfig=ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational(1,1))
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()
        val imageCapture=ImageCapture(imageCaptureConfig)
        imageButton.setOnClickListener{
            val file=File(externalMediaDirs.first(),"${System.currentTimeMillis()}.jpg")
//            imageCapture.takePicture(file,object : ImageCapture.OnImageSavedListener{
//                override fun onImageSaved(file: File) {
//                    Toast.makeText(this@CameraActivity,"Picture Captured at ${file.path}",Toast.LENGTH_LONG).show()
//                }
//
//                override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
//                    Toast.makeText(this@CameraActivity,"Error Capturing Picture",Toast.LENGTH_LONG).show()
//                }
//
//            })
            imageCapture.takePicture(object  : ImageCapture.OnImageCapturedListener() {
                override fun onCaptureSuccess(image: ImageProxy?, rotationDegrees: Int) {
                    super.onCaptureSuccess(image, rotationDegrees)
                }

                override fun onError(
                    useCaseError: ImageCapture.UseCaseError?,
                    message: String?,
                    cause: Throwable?
                ) {
                    super.onError(useCaseError, message, cause)
                }

            })
        }



        //To get the preview of the camera
        val previousConfig=PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1,1))
            setTargetResolution(Size(1080,1080))
            setLensFacing(CameraX.LensFacing.BACK)
        }.build()
        val preView=Preview(previousConfig)
        preView.setOnPreviewOutputUpdateListener {
            val parent=textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView,0)
            updatePreview()
            textureView.surfaceTexture=it.surfaceTexture
        }
        CameraX.bindToLifecycle(this,preView,imageCapture)
    }

    private fun updatePreview() {
        val matrix=android.graphics.Matrix()

        val centerX=textureView.width/2f
        val centerY=textureView.height/2f

        val rotaion=when(textureView.display.rotation){
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotaion.toFloat(),centerX,centerY)

        textureView.setTransform(matrix)
    }
}
