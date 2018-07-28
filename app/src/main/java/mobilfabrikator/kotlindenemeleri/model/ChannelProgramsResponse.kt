package mobilfabrikator.kotlindenemeleri.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class ChannelProgramsResponse {
    @SerializedName("day")
    @Expose
    var day: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("shortContent")
    @Expose
    var shortContent: String? = null
    @SerializedName("startTime")
    @Expose
    public  var startTime: String? = null
    @SerializedName("endTime")
    @Expose
    var endTime: String? = null
}