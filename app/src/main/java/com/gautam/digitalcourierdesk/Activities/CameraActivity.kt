package com.gautam.digitalcourierdesk


import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import com.gautam.digitalcourierdesk.data_classes.entities
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.gson.Gson
import com.paralleldots.paralleldots.App
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.loading_layout.*
import org.jetbrains.anko.*
import java.io.File


class CameraActivity : AppCompatActivity(), LifecycleOwner {
    val pd = App(Utils.API_KEY)
    lateinit var alert:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        alert= Dialog(this)
        alert.setContentView(R.layout.loading_layout)
        alert.create()
        alert.setCancelable(false)
    while(true){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                123)
        } else {
            break
        }}
        textureView.post {
            startCamera()
        }
        manualButton.setOnClickListener {
            startActivity<ManualEntryActivity>("name" to "",
                "sender" to "",
                "track" to ""
                )
        }}
    private fun startCamera() {
        // to capture image on button click
        val imageCaptureConfig=ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational(1,1))
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()
        val imageCapture=ImageCapture(imageCaptureConfig)
        imageButton.setOnClickListener{
            it.isEnabled=false
            alert.show()
            alert.dots.showAndPlay()
            val file=File(externalMediaDirs.first(),"${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file,object : ImageCapture.OnImageSavedListener{
                override fun onImageSaved(file: File) {
                    var image: FirebaseVisionImage=FirebaseVisionImage.fromFilePath(this@CameraActivity, Uri.fromFile(file))
                    textRec(image)
                    file.delete()
                }
                override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                    Toast.makeText(this@CameraActivity,"Error Capturing Picture",Toast.LENGTH_LONG).show()
                }})}
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
    private fun textRec(image: FirebaseVisionImage) {
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val result = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                imageButton.isEnabled=true
                //Show progress dialog while sending email
                val text=firebaseVisionText.text
                Log.i("workk",text)
                if (text.isNullOrBlank() || text==""){
                    toast("No Text Found!")
                    alert.hide()
                    alert.dots.hideAndStop()}
                else{
                    doAsync {
                        try {
                            val result=pd.ner(firebaseVisionText.text)
                            Log.i("workk", result)
                            runOnUiThread {
                            alert.hide()
                            }
                            doneRec(result)
                    }
                        catch (e: Exception){
                            Log.i("workk",e.toString())
                            runOnUiThread {
                            alert.hide()}
                    }
                }}

            }
            .addOnFailureListener { e ->
                toast("Error $e")
            }}

    private fun doneRec(result: String?) {
        val gson=Gson()
        val entities=gson.fromJson(result,entities::class.java)
        val result=entities.entities
        var name=""
        var from=""
        for (entity in result){
            if (entity.category=="name"){
                name=entity.name
                break}
        }
        for (entity in result){
            if (entity.category=="group"){
                from=entity.name
                break}
        }
        runOnUiThread {
            startActivity<ManualEntryActivity>(
                "name" to name,
                "sender" to from
            )
        }

    }

    private fun updatePreview() {
        val matrix=Matrix()
        val centerX=textureView.width/2f
        val centerY=textureView.height/2f
        val rotaion=when(textureView.display.rotation){
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return}
        matrix.postRotate(-rotaion.toFloat(),centerX,centerY)
        textureView.setTransform(matrix)
    }}