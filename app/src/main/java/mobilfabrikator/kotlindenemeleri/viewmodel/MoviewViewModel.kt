package mobilfabrikator.kotlindenemeleri.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import mobilfabrikator.kotlindenemeleri.api.RestRetroInterFace
import mobilfabrikator.kotlindenemeleri.model.secondModel.MovieAppModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviewViewModel : ViewModel() {

    //this is the data that we will fetch asynchronously
    private var movieList: MutableLiveData<Array<MovieAppModel>>? = null

    //we will call this method to get the data
    //if the list is null
    //we will load it asynchronously from server in this method
    //finally we will return the list
    val heroes: LiveData<Array<MovieAppModel>>
        get() {
            if (movieList == null) {
                movieList = MutableLiveData()
                loadMovies()
            }
            return movieList !!
        }


    //This method is using Retrofit to get the JSON data from URL
    private fun loadMovies() {
        val apiNew = RestRetroInterFace.create()
        val call = apiNew.getJsonValues()
        call.enqueue(object : Callback<Array<MovieAppModel>> {
            override fun onResponse(call: Call<Array<MovieAppModel>>, response: Response<Array<MovieAppModel>>) {
                //finally we are setting the list to our MutableLiveData
                movieList!!.setValue(response.body())
            }
            override fun onFailure(call: Call<Array<MovieAppModel>>, t: Throwable) {

            }
        })
    }
}