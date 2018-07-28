package mobilfabrikator.kotlindenemeleri.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import mobilfabrikator.kotlindenemeleri.api.ProgramListInterface
import mobilfabrikator.kotlindenemeleri.helper.pDialog
import mobilfabrikator.kotlindenemeleri.model.ChannelResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChannelViewModel : ViewModel() {
    //this is the data that we will fetch asynchronously
    private var channelList: MutableLiveData<ChannelResponse>? = null

    //we will call this method to get the data
    //if the list is null
    //we will load it asynchronously from server in this method
    //finally we will return the list
    val channelLists: LiveData<ChannelResponse>
        get() {
            if (channelList == null) {
                channelList = MutableLiveData()
                loadCategories()
            }
            return channelList !!
        }


    //This method is using Retrofit to get the JSON data from URL
    private fun loadCategories() {
        val apiNew = ProgramListInterface.create()
        val call = apiNew.getCategoryDetails()
        call.enqueue(object : Callback<ChannelResponse> {
            override fun onResponse(call: Call<ChannelResponse>, response: Response<ChannelResponse>) {
                //finally we are setting the list to our MutableLiveData
                channelList!!.setValue(response.body())
            }
            override fun onFailure(call: Call<ChannelResponse>, t: Throwable) {
                pDialog.dismiss()
                println("sonuç "+t)


            }
        })


    }
    
}

