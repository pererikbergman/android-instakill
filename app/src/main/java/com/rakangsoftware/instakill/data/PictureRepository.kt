package com.rakangsoftware.instakill.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import com.rakangsoftware.instakill.utils.IO
import com.rakangsoftware.instakill.utils.UI
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PictureRepository(context: Context) {

    private val applicationContext = context.applicationContext

    fun save(picture: ByteArray, complete: (File) -> Unit = {}) {
        IO.execute {
            // pictures location: /storage/emulated/0/Pictures/
            val pictures = getExternalStoragePublicDirectory(DIRECTORY_PICTURES).absolutePath

            // Subfolder: Instakill
            val instakill = "Instakill"

            // Filename: IMG_20181013_152955.jpg
            val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val filename = "IMG_$date.jpg"

            // Full path: /storage/emulated/0/Pictures/Instakill/IMG_20181013_152955.jpg
            val picturePath = "$pictures${File.separator}$instakill${File.separator}$filename"

            // /storage/emulated/0/Pictures/instakill/IMG_20181013_152955.jpg
            val file = File(picturePath)
                    .also { it.parentFile.parentFile.mkdirs() }
                    .also { it.parentFile.mkdirs() }
                    .also { it.outputStream().write(picture) }

            UI.execute {
                addPicturePathToGallery(file)
                complete(file)
            }
        }
    }

    private fun addPicturePathToGallery(file: File) {
        applicationContext.sendBroadcast(
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                    data = Uri.fromFile(file)
                }
        )
    }
}