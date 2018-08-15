package com.twtstudio.service.tjwhm.exam.list

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView


class ListActivity : AppCompatActivity() {

    private lateinit var rvList: RecyclerView
    private lateinit var etSearch: EditText

    companion object {
        const val LESSON_TYPE = "lesson_type"
        const val PARTY = "2"
        const val POLICY = "1"
        const val ONLINE = "3"
        const val MORE = "more"
    }

    private var statusBarView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.exam_activity_list)

        rvList = findViewById(R.id.rv_list)
        etSearch = findViewById(R.id.et_search_list)
        etSearch.visibility = View.GONE
        findViewById<ImageView>(R.id.iv_list_back).setOnClickListener { onBackPressed() }
        val type = intent.getStringExtra(LESSON_TYPE)
        if (type == ONLINE) etSearch.visibility = View.VISIBLE

        rvList.layoutManager = LinearLayoutManager(this@ListActivity)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty() && s.toString() != "") {
                    findClass(s.toString()) {
                        when (it) {
                            is RefreshState.Failure -> Toasty.error(this@ListActivity, "网络错误", Toast.LENGTH_SHORT).show()
                            is RefreshState.Success -> {
                                // 这个 warning 有问题，这个 List 是有可能为空的
                                if (it.message.date == null) {
                                    Toasty.info(this@ListActivity, "搜索结果为空！", Toast.LENGTH_SHORT).show()
                                    rvList.withItems { }
                                } else when (it.message.date.size) {
                                    0 -> Toasty.info(this@ListActivity, "搜索结果为空！", Toast.LENGTH_SHORT).show()
                                    else -> rvList.withItems {
                                        for (i in 0 until it.message.date.size) {
                                            lessonItem(this@ListActivity, it.message.date[i])
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    getList(type) {
                        when (it) {
                            is RefreshState.Failure -> Toasty.error(this@ListActivity, "网络错误", Toast.LENGTH_SHORT).show()
                            is RefreshState.Success ->
                                rvList.withItems {
                                    for (i in 0 until it.message.date.size) {
                                        lessonItem(this@ListActivity, it.message.date[i])
                                    }
                                }
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }
        getList(type) {
            when (it) {
                is RefreshState.Failure -> Toasty.error(this@ListActivity, "网络错误", Toast.LENGTH_SHORT).show()
                is RefreshState.Success ->
                    rvList.withItems {
                        for (i in 0 until it.message.date.size) {
                            lessonItem(this@ListActivity, it.message.date[i])
                        }
                    }

            }
        }
    }


    private fun initStatusBar() {
        if (statusBarView == null) {
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        statusBarView?.setBackgroundResource(R.drawable.exam_statusbar_gradient)
    }

}