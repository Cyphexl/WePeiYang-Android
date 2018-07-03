package com.twtstudio.service.dishesreviews.home.view

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.LazyFragment
import com.twtstudio.service.dishesreviews.home.model.HomeDataViewModel
import com.twtstudio.service.dishesreviews.home.view.adapters.HomePagerAdapter
import org.jetbrains.anko.coroutines.experimental.asReference


class HomeFragment : LazyFragment() {
    private lateinit var homeAdapter: HomePagerAdapter
    private lateinit var homeDataViewModel: HomeDataViewModel
    private lateinit var refreshLayout: SwipeRefreshLayout
    override fun getResId(): Int {
        return R.layout.dishes_reviews_fragment_home
    }

    override fun onRealViewLoaded(view: View) {
        refreshLayout = view.findViewById(R.id.refresh_home)
        homeDataViewModel = ViewModelProviders.of(this).get(HomeDataViewModel::class.java)
        view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            homeAdapter = HomePagerAdapter(listOf(HomePagerAdapter.BANNER,
                    HomePagerAdapter.DINNING_HALL,
                    HomePagerAdapter.AD,
                    HomePagerAdapter.REVIEWS
            ), context, this@HomeFragment)
            adapter = homeAdapter
        }
        refresh()
        refreshLayout.apply {
            setColorSchemeResources(R.color.dishColorPrimary)
            setOnRefreshListener {
                refresh()
            }
        }
    }

    private fun refresh() {
        homeDataViewModel.homeBeanLiveData.apply {
            refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE, callback = simpleCallback())
            bindNonNull(this@HomeFragment) {
                homeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun simpleCallback(success: String? = "加载成功", error: String? = "发生错误", refreshing: String? = "加载中"): suspend (RefreshState<*>) -> Unit =
            with(this.asReference()) {
                {
                    when (it) {
                        is RefreshState.Success -> if (success != null) refreshLayout.isRefreshing = false
                        is RefreshState.Failure -> if (error != null) es.dmoral.toasty.Toasty.error(context!!, "$error ${it.throwable.message}！${it.javaClass.name}").show()
                    }
                }
            }

}
