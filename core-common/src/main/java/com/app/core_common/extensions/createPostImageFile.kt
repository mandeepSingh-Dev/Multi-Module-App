package com.app.core_common.extensions

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import com.app.core_common.utils.Constants
import java.io.File


fun createPostImageFile(dir: File, fileName : String): File? {
    try {
        val folder = File(dir, Constants.FileFolders.POST_IMAGES).createFolder()
        return File(folder, fileName)
    }catch (e:Exception){
        return null
    }
}

fun File.createFolder(): File? {
    return try{
        val folder = File(this.parentFile, this.name)
        if(!folder.exists()){
            folder.mkdir()
        }
        folder
    }catch (e:Exception){
        null
    }
}

fun String?.isUrl() : Boolean?{
    if(isNullOrEmpty()) return null
    return Patterns.WEB_URL.matcher(this).matches()
}

fun Context.showToast(message : String?, length : Int = Toast.LENGTH_LONG){
    if(message.isNullOrEmpty()) return
    Toast.makeText(this,message, length).show()
}

