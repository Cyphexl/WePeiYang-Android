package com.twt.service.home.user

import android.os.Bundle
import android.view.View
import com.example.news2.NewsTwoFragment
import com.twt.service.home.tools.ToolsFragment
import io.multimoon.colorful.CAppCompatActivity
import org.jetbrains.anko.frameLayout

class FragmentActivity : CAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = frameLayout {
            id = View.generateViewId()
        }
        val fragmentName = intent.getStringExtra("frag")
        val fragment = when (fragmentName) {
            "User" -> UserFragment()
            "Tool" -> ToolsFragment()
            "News" -> NewsTwoFragment()
            else -> UserFragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(rootView.id, fragment)
                .commit()
    }
}