package mobilfabrikator.kotlindenemeleri.ui

import mobilfabrikator.kotlindenemeleri.model.ChannelProgramsResponse

interface FragmentPagerHelperInterface {
    fun fragmentBecameVisible(getfilteredProgramList: ArrayList<ChannelProgramsResponse>, tabPosition: Int?)
}
