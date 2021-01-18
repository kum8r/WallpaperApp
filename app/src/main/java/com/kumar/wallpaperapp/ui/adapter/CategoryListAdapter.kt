package com.kumar.wallpaperapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kumar.wallpaperapp.R
import com.kumar.wallpaperapp.data.Category
import com.kumar.wallpaperapp.ui.activity.ImagesListActivity
import com.kumar.wallpaperapp.utils.RandomColors

class CategoryListAdapter(val activity: Activity, val isSubCategoryImages:Boolean = false)
    : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {
    var categoryList: List<Category> = emptyList()

    inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int) {
            val textView: TextView = itemView.findViewById(R.id.category_item)
            textView.text = categoryList[position].name
            textView.setBackgroundColor(RandomColors().color)

            textView.setOnClickListener {
                val intent = Intent(activity, ImagesListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("category", categoryList[position])
                intent.putExtra("isSubCategoryImageList", isSubCategoryImages)
                intent.putExtra("isCategoryList", false)
                intent.putExtra("isImageList", true)
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setCategory(category: List<Category>) {
        categoryList = category
        notifyDataSetChanged()
    }
}