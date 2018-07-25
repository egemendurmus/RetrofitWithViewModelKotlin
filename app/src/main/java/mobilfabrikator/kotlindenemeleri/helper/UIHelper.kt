package mobilfabrikator.kotlindenemeleri.helper

import android.app.ProgressDialog
import android.content.Context

lateinit var pDialog: ProgressDialog
fun DisplayProgressDialog(ctx : Context) {

    pDialog = ProgressDialog(ctx)
    pDialog!!.setMessage("Loading..")
    pDialog!!.setCancelable(false)
    pDialog!!.isIndeterminate = false
    pDialog!!.show()
}