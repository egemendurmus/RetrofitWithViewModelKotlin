package mobilfabrikator.kotlindenemeleri.model.secondModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieAppModel {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("url")
    @Expose
    private val url: Urls? = null
    @SerializedName("timestamp")
    @Expose
    var timestamp: String? = null

    inner class Urls {

        @SerializedName("small")
        @Expose
         val small: String? = null
        @SerializedName("medium")
        @Expose
         val medium: String? = null
        @SerializedName("large")
        @Expose
         val large: String? = null
    }

}