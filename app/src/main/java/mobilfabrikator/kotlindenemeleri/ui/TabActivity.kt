package mobilfabrikator.kotlindenemeleri.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import mobilfabrikator.kotlindenemeleri.R
import mobilfabrikator.kotlindenemeleri.helper.DisplayProgressDialog
import mobilfabrikator.kotlindenemeleri.helper.pDialog
import mobilfabrikator.kotlindenemeleri.model.ChannelProgramsResponse
import mobilfabrikator.kotlindenemeleri.model.ChannelResponse
import mobilfabrikator.kotlindenemeleri.model.GlobalVariables
import mobilfabrikator.kotlindenemeleri.ui.adapter.TabsPagerAdapter
import mobilfabrikator.kotlindenemeleri.viewmodel.ChannelViewModel


class TabActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    val pst = GlobalVariables()
    var channelList = ChannelResponse().data
    val mAdapter = TabsPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        callProgramList()
        DisplayProgressDialog(this)

        viewPager = findViewById<View>(R.id.viewpager) as ViewPager?
        setupViewPager(viewPager!!)
        tabLayout = findViewById<View>(R.id.tabs) as TabLayout?
        tabLayout!!.setupWithViewPager(viewPager)
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                pst!!.getTabPosition = position
                getPagerInterface(mAdapter, position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun callProgramList() {
        val model = ViewModelProviders.of(this).get<ChannelViewModel>(ChannelViewModel::class.java!!)
        model.channelLists.observe(this, Observer { heroList ->
            if (pDialog != null && pDialog!!.isShowing()) {
                pDialog.dismiss()
            }
            channelList = heroList!!.data
            pst.getProgramList = channelList
            // dpf.channelList = heroList!!.data
            if (channelList != null) {
                loadProgramList(0)
                getPagerInterface(mAdapter, 0)
            }

        })

    }

    private fun loadProgramList(position: Int) {
        pst.getfilteredProgramList.clear()
        for (i in 0 until pst.getProgramList!!.size) { //değerleri sırası ile k değişkenine atadık
            if (pst.getProgramList!!.get(i).day!!.toInt() == position) {
                pst.getfilteredProgramList.add(element = pst.getProgramList!!.get(i))

            }
        }
    }

    //todo kullanmıyorum ancak kotlinde static variable'ı göstermek için silmyiorum
    companion object {
        var positions: Int = 0
        var responseList: List<ChannelProgramsResponse> = ArrayList<ChannelProgramsResponse>()
    }

    private fun getPagerInterface(mAdapter: TabsPagerAdapter, position: Int) {
        val fragment = mAdapter.instantiateItem(viewPager!!, position) as FragmentPagerHelperInterface
        if (fragment != null) {
            loadProgramList(position)
            responseList = pst.getfilteredProgramList
            positions = position
            fragment.fragmentBecameVisible(pst.getfilteredProgramList, pst.getTabPosition)

        }
    }

    private fun setupViewPager(viewPager: ViewPager) {

        mAdapter.notifyDataSetChanged()
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.adapter = mAdapter
    }

}