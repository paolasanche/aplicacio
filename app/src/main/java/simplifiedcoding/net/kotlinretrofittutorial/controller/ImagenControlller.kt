package simplifiedcoding.net.kotlinretrofittutorial.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import java.io.File

object ImagenControlller {
    fun selectPhotoFromGallery(activity: Fragment, code: Int){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)

    }
    // permite guardar la imagen
    fun saveImage(context: Context, id: Long, uri: Uri){
        val file = File(context.filesDir, id.toString())
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(bytes)
    }
    // evita error en img remplazo
    fun getImageUri(context: Context, id: Long): Uri{
        val file = File(context.filesDir, id.toString())

        return if(file.exists()) Uri.fromFile(file)
        else Uri.parse("android.resource://com.hugobelman.basededatossqlite/drawable/placeholderimg")
    }
    // borrar imagenes
    fun deleteImage(context: Context, id: Long){
        val file = File(context.filesDir, id.toString())
        file.delete()
    }
}