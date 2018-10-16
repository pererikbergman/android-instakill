package com.rakangsoftware.instakill.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.rakangsoftware.instakill.utils.IO
import com.rakangsoftware.instakill.utils.UI
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

class PictureRepository(context: Context) {

    private val applicationContext = context.applicationContext
    private val storage = FirebaseStorage.getInstance()

    fun save(picture: ByteArray, complete: (File) -> Unit = {}) {
        println("auth.currentUser: " + FirebaseAuth.getInstance().currentUser?.uid)
        IO.execute {
            val file = saveLocally(picture)
            addPicturePathToGallery(file)
            saveRemote(file) { downloadUri ->
                println("Url:$downloadUri")
            }
        }
    }

    private fun saveRemote(file: File, complete: (Uri) -> Unit = {}) {
        val imagesRef: StorageReference = storage.reference
            .child("images")
            .child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .child(file.name)

        val stream = FileInputStream(file)
        val uploadTask = imagesRef.putStream(stream)
        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation imagesRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                complete(task.result)
            } else {
                // Handle failures
                // ...
            }
        }
    }

    private fun saveLocally(picture: ByteArray): File {
        // pictures location: /storage/emulated/0/Pictures/
        val pictures = getExternalStoragePublicDirectory(DIRECTORY_PICTURES).absolutePath

        // Subfolder: Instakill
        val instakill = "Instakill"

        // Filename: IMG_20181013_152955.jpg
        val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "IMG_$date.jpg"

        // Full path: /storage/emulated/0/Pictures/instakill/IMG_20181013_152955.jpg
        val picturePath = "$pictures${File.separator}$instakill${File.separator}$filename"

        // /storage/emulated/0/Pictures/instakill/IMG_20181013_152955.jpg
        val file = File(picturePath)
            .also { it.parentFile.parentFile.mkdirs() }
            .also { it.parentFile.mkdirs() }
            .also { it.outputStream().write(picture) }
        return file
    }

    private fun addPicturePathToGallery(file: File) {
        applicationContext.sendBroadcast(
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = Uri.fromFile(file)
            }
        )
    }
}