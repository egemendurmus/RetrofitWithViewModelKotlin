package mobilfabrikator.kotlindenemeleri.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import mobilfabrikator.kotlindenemeleri.R
import mobilfabrikator.kotlindenemeleri.helper.pDialog
import mobilfabrikator.kotlindenemeleri.model.GlobalVariables
import mobilfabrikator.kotlindenemeleri.viewmodel.CategoryViewModel


class MainActivity : AppCompatActivity() {
    val gVar = GlobalVariables()
    val mps = MapsActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnClick.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TabActivity::class.java)
            startActivity(intent)

        })

        btnOpenActivity.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)

        })
        maps_button.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("mLat", 40.978005)
            intent.putExtra("mLng", 28.857541)
            startActivity(intent)
        })


        Picasso.get().load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg").into(imageView2);


    }
    private fun callViewModel() {

        val model = ViewModelProviders.of(this).get<CategoryViewModel>(CategoryViewModel::class.java!!)
        model.categories.observe(this, Observer { heroList ->
            if (pDialog != null && pDialog!!.isShowing()) {
                pDialog.dismiss()
            }
            txtDisplay.text = heroList!!.categories!![1].title

        })

    }


}



