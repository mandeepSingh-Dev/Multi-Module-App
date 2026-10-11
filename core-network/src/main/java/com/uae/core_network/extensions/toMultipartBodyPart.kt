package com.uae.core_network.extensions

import com.uae.core_network.domain.model.MediaDetails
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun Uri.toMultipartBodyPart(
    context: Context,
    partName: String = "files"
): MultipartBody.Part? {

    val inputStream = context.contentResolver.openInputStream(this)

    val bytes = inputStream?.readBytes()

    val requestBody = bytes?.toRequestBody(
        context.contentResolver.getType(this)?.toMediaTypeOrNull() ?: "image/*".toMediaType()
    ) ?: return null

    val fileName = DocumentFile.fromSingleUri(context, this)?.name ?: "image.jpg"



    return MultipartBody.Part.createFormData(
        partName,
        fileName,
        requestBody
    )
}


fun Uri.getMediaDetails(context: Context): MediaDetails {
    val inputStream = context.contentResolver.openInputStream(this)

    val bytes = inputStream?.readBytes()

    val fileName = DocumentFile.fromSingleUri(context, this)?.name ?: "image.jpg"
    val size = bytes?.size
    val mimeType = getMimeType(context)
    return MediaDetails(
        fileName = fileName,
        size = size,
        mimeType = mimeType
    )
}
fun Uri.getMimeType(context: Context): String? {
    return context.contentResolver.getType(this)
}

