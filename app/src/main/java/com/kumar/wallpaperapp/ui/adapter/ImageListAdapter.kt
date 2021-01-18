package com.kumar.wallpaperapp.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.kumar.wallpaperapp.R
import com.kumar.wallpaperapp.data.Wallpapers
import com.kumar.wallpaperapp.ui.activity.ImageViewerActivity
import com.kumar.wallpaperapp.ui.activity.MainActivity
import com.kumar.wallpaperapp.ui.fragment.HomeFragment
import com.kumar.wallpaperapp.utils.WallpaperType
import com.squareup.picasso.Picasso
import java.util.*

class ImageListAdapter(val context: Context, var onClickListener: OnClickListener?=null, val hasShowMore:Boolean = false)
    : Adapter<ImageListAdapter.ViewHolder>() {
    var wallpaperList:MutableList<Wallpapers> = mutableListOf()

    interface OnClickListener {
        fun onItemClicked()
    }

    inner  class  ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        fun bindItems(position: Int) {
            if (hasShowMore) {
                if (position == wallpaperList.size - 1) {
                    loadMoreBtn()
                } else {
                    imageView(position)
                }
            }
            else {
                imageView(position)
            }
        }

        private fun loadMoreBtn() {
            val btn = v.findViewById<Button>(R.id.load_more_btn)
            btn.setOnClickListener{
                onClickListener?.onItemClicked()
            }
        }

        private fun imageView(position:Int) {
            Log.e("TAG", "imageView: ${wallpaperList[position]}", )
            val imageItem = v.findViewById<ImageView>(R.id.hr_list_image_item)
            Picasso.get()
                .load(wallpaperList[position].url_image)
                .resize(300, 200)
                .onlyScaleDown()
                .into(imageItem)

            imageItem.setOnClickListener {
                val intent = Intent(context, ImageViewerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("wallpaper", wallpaperList[position])
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView:View = if (viewType == R.layout.horizontal_image_list_item) {
            LayoutInflater.from(parent.context).inflate(R.layout.horizontal_image_list_item, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.loadmore_btn,parent, false)
        }
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount(): Int {
        return wallpaperList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == wallpaperList.size - 1) {
            R.layout.loadmore_btn
        } else {
            R.layout.horizontal_image_list_item
        }
    }

    fun setWallpaper(wallpaperlist: List<Wallpapers>) {
        this.wallpaperList.clear()
        this.wallpaperList.addAll(wallpaperlist)
        notifyDataSetChanged()
    }

    fun getWallpaperListSize() : Int {
        return this.wallpaperList.size
    }

    fun addWallpapers(wallpaperlist: List<Wallpapers>) {
        this.wallpaperList.addAll(wallpaperlist)
        notifyDataSetChanged()
    }
}