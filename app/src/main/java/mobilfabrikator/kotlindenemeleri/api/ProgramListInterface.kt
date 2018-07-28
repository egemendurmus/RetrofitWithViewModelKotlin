package mobilfabrikator.kotlindenemeleri.api

import mobilfabrikator.kotlindenemeleri.model.ChannelResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

public interface ProgramListInterface {
    @GET("mobil/programlist.asp")
    abstract fun getCategoryDetails(): Call<ChannelResponse>

    companion object Factory {
        val BASE_URL = "http://www.yirmidort.tv/"
        fun create(): ProgramListInterface {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(ProgramListInterface::class.java)
        }
    }
}