package com.kumar.wallpaperapp.ui.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.databinding.ActivityImageViewerBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.OutputStream

class ImageViewerActivity : AppCompatActivity() {
    private  var binding: ActivityImageViewerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val wallpaper:Wallpapers = intent.getSerializableExtra("wallpaper") as Wallpapers

        val imageItem: ImageView? = binding?.imageViewer

          Picasso.get()
            .load(wallpaper.url_image)
              .fit()
              .centerCrop()
            .into(imageItem)

        binding?.imageSizeText?.text = wallpaper.width + "x"  + wallpaper.height

        binding?.imageDownloadBtn?.setOnClickListener{

//
//                    val target = object : Target() {
//                        override fun
//                    }
            val uri = getUri(this)
//            saveImage(this, uri, )
        }

    }

    fun getUri(context: Context):Uri? {
        val path = Environment.DIRECTORY_PICTURES
        val mimeType = "image/*"
        val resolver = context.contentResolver
        val contentValues = ContentValues()
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path)
        val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var uri:Uri? = null
        uri = resolver.insert(contentUri, contentValues)
        return uri
    }

    fun saveImage(context:Context, uri:Uri?, bitmap:Bitmap) {
        Toast.makeText(context, "Image Downloaded Started", Toast.LENGTH_LONG).show()
        GlobalScope.launch {

            var stream:OutputStream? = null
            val resolver = context.contentResolver

            try {
                if (uri == null) {
                    Log.e("error", "saveImage: Error", )
                    return@launch
                }

                stream = resolver.openOutputStream(uri)
                if (stream == null) {
                    Log.e("error", "saveImage: error stream", )
                    return@launch
                }
                val saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                if (!saved) {
                    Log.e("error", "saveImage: Failed tosave", )
                }
            }
            catch (e: IOException) {
                if (uri != null) {
                        resolver.delete(uri, null, null)
                    }
            }finally {
                if (stream != null) {
                    stream.close()
                }
            }
        }
        Toast.makeText(context, "Image Downloaded Finished", Toast.LENGTH_LONG).show()
    }

    fun setWallpaper(context: Context) {
        val uri:Uri? = getUri(context)
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