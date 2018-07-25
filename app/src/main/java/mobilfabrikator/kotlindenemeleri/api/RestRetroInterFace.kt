package mobilfabrikator.kotlindenemeleri.api

import mobilfabrikator.kotlindenemeleri.model.secondModel.MovieAppModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

public interface RestRetroInterFace {

    @GET("/json/glide.json")
    abstract fun getJsonValues(): Call<Array<MovieAppModel>>


    companion object Factory {
        val BASE_URL = "http://api.androidhive.info"
        fun create(): RestRetroInterFace {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(RestRetroInterFace::class.java)
        }
    }
}