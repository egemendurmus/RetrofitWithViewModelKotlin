package mobilfabrikator.kotlindenemeleri.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import mobilfabrikator.kotlindenemeleri.R
import mobilfabrikator.kotlindenemeleri.model.ChannelProgramsResponse
import mobilfabrikator.kotlindenemeleri.ui.FragmentPagerHelperInterface
import mobilfabrikator.kotlindenemeleri.ui.adapter.DailyProgramListAdapter


class DailyProgramListFragments : Fragment(), FragmentPagerHelperInterface {
    private var adapter: DailyProgramListAdapter? = null
    private var listView: ListView? = null
    var root: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.daily_schedule_fragment, container, false)
        listView = root!!.findViewById(R.id.list)
        return root
    }


    override fun fragmentBecameVisible(getfilteredProgramList: ArrayList<ChannelProgramsResponse>, tabPosition: Int?) {

        try {
            adapter = DailyProgramListAdapter(this.getActivity()!!, getfilteredProgramList)
            listView!!.adapter = adapter
        } catch (ex: Exception) {

        }


    }


}