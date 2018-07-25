package mobilfabrikator.kotlindenemeleri.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import mobilfabrikator.kotlindenemeleri.api.ApiInterface
import mobilfabrikator.kotlindenemeleri.helper.pDialog
import mobilfabrikator.kotlindenemeleri.model.CategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class CategoryViewModel : ViewModel() {

    //this is the data that we will fetch asynchronously
    private var categoryList: MutableLiveData<CategoryResponse>? = null

    //we will call this method to get the data
    //if the list is null
    //we will load it asynchronously from server in this method
    //finally we will return the list
    val heroes: LiveData<CategoryResponse>
        get() {
            if (categoryList == null) {
                categoryList = MutableLiveData()
                loadCategories()
            }
            return categoryList !!
        }


    //This method is using Retrofit to get the JSON data from URL
    private fun loadCategories() {
        val api = ApiInterface.create()
        val call = api.getCategoryDetails()
        call.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                //finally we are setting the list to our MutableLiveData
                categoryList!!.setValue(response.body())
            }
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                pDialog.dismiss()

            }
        })
    }
}