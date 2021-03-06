package com.twtstudio.retrox.news.explore.gallery

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide

import com.twtstudio.retrox.news.R
import com.twtstudio.retrox.news.api.PicProvider
import com.twtstudio.retrox.news.api.bean.GalleryPhotoBean
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by retrox on 10/04/2017.
 */
class GalleryActivity : AppCompatActivity() {

    val picApi = PicProvider().picApi
    val adapter = GalleryAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }

        val id = intent.getIntExtra("id",45)
        setContentView(R.layout.activity_explore_photos)
        val recyclerview = findViewById(R.id.recyclerview) as RecyclerView

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)
//        recyclerview.recycledViewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0,20) }

        picApi.getGalleryPhotos(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ adapter.refreshData(it) }, Throwable::printStackTrace)
    }


    class GalleryAdapter( val context: Context, val list: MutableList<GalleryPhotoBean> = ArrayList()) : RecyclerView.Adapter<GalleryAdapter.GalleryItemHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: GalleryItemHolder?, position: Int) {
            val data = list[position]
            holder?.apply {
                image.setImageResource(R.drawable.gallery_text)
//                image.setImageURI()
                Glide.with(context).load(data.imageUrl).into(image)
                image.setOnClickListener {

                    ARouter.getInstance().build("/photo/preview")
                            .withString("url",data.imageUrl)
                            .navigation()
                }
//                image.setGyroscopeObserver(observer)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GalleryItemHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_explore_gallery_item, parent, false)
            return GalleryItemHolder(view)
        }

        fun refreshData(list: List<GalleryPhotoBean>) {
            this.list.addAll(list)
            notifyDataSetChanged()
        }

        class GalleryItemHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            val image = itemView?.findViewById(R.id.panorama_imageview) as ImageView
            init {
//                image.setGyroscopeObserver(observer)
            }
        }
    }
}