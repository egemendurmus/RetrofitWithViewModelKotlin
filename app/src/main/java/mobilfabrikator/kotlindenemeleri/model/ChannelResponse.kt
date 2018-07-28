package mobilfabrikator.kotlindenemeleri.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChannelResponse {


    @SerializedName("errorCode")
    @Expose
    var errorCode: Int? = null
    @SerializedName("errorMessage")
    @Expose
    var errorMessage: Any? = null
    @SerializedName("data")
    @Expose
    var data: List<ChannelProgramsResponse>? = null
}