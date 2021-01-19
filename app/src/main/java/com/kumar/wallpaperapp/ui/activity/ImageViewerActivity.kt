package com.kumar.wallpaperapp.ui.activity

import android.Manifest.permission
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.databinding.ActivityImageViewerBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class ImageViewerActivity : AppCompatActivity() {
    private  var binding: ActivityImageViewerBinding? = null
    private lateinit var imageUrl:String
    private lateinit var imageName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val wallpaper:Wallpapers = intent.getSerializableExtra("wallpaper") as Wallpapers
        val imageItem: ImageView? = binding?.imageViewer
        imageUrl = wallpaper.url_image
        imageName = wallpaper.id
        var isImageLoaded = false

        Picasso.get()
            .load(wallpaper.url_image)
            .fit()
            .centerCrop()
            .into(imageItem, object:  Callback.EmptyCallback() {
                override fun onSuccess() {
                    super.onSuccess()
                    isImageLoaded = true
                }

                override fun onError(e: Exception?) {
                    super.onError(e)
                    isImageLoaded = false
                    Toast.makeText(this@ImageViewerActivity, "Image Cannot load, try again later", Toast.LENGTH_LONG).show()
                }
            })

        binding?.imageSizeText?.text = wallpaper.width + "x"  + wallpaper.height

        binding?.imageDownloadBtn?.setOnClickListener{
            if (isImageLoaded) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission(permission.WRITE_EXTERNAL_STORAGE)) {
                        downloadFile(imageName, imageUrl)
                    } else {
                        requestPermissions(arrayOf(permission.WRITE_EXTERNAL_STORAGE), 1001)
                    }
                } else {
                    downloadFile(imageName, imageUrl)
                }
            }
            else {
                Toast.makeText(this@ImageViewerActivity, "Image Not Loaded", Toast.LENGTH_LONG).show()
            }
        }



        binding?.imageSetWallpaper?.setOnClickListener{
            if (isImageLoaded) {
                setWallpaper()
            }
            else {
                Toast.makeText(this@ImageViewerActivity, "Image Not Loaded", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        val result = checkSelfPermission(applicationContext, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun downloadFile(fileName:String, fileUrl:String) {
        val downloadURI = Uri.parse(fileUrl)
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        try {
            val request = DownloadManager.Request(downloadURI)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(fileName)
                .setDescription("Downloading " + fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setMimeType(getMimeType(downloadURI))

            manager.enqueue(request)
            Toast.makeText(this, "Download Started", Toast.LENGTH_LONG).show()
        }
        catch(e: Exception) {

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadFile(imageName, imageUrl)
            }
        }
    }

    private fun getMimeType(uri:Uri) : String? {
        val resolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri))
    }

    fun setWallpaper() {
        binding?.imageViewer?.invalidate()
        val bitmap = binding?.imageViewer?.drawable?.toBitmap()

        val path: String = Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Wallpaper",
            null
        )

        val uri:Uri? = Uri.parse(path)
        val intent = Intent(Intent.ACTION_ATTACH_DATA)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setDataAndType(uri, "image/jpeg")
        intent.putExtra("mimeType","image/jpeg")
        this.startActivity(Intent.createChooser(intent, "Set As:"))


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }
}