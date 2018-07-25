package mobilfabrikator.kotlindenemeleri.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_second.*
import mobilfabrikator.kotlindenemeleri.R
import mobilfabrikator.kotlindenemeleri.helper.DisplayProgressDialog
import mobilfabrikator.kotlindenemeleri.helper.pDialog
import mobilfabrikator.kotlindenemeleri.ui.adapter.MovieAdapter
import mobilfabrikator.kotlindenemeleri.viewmodel.MoviewViewModel

class SecondActivity : AppCompatActivity() {

    internal lateinit var listView: ListView
    internal lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        listView = findViewById(R.id.list)
        DisplayProgressDialog(this)
        callViewModel()

    }
    private fun callViewModel() {

        val model = ViewModelProviders.of(this).get<MoviewViewModel>(MoviewViewModel::class.java!!)
        model.heroes.observe(this, Observer { heroList ->
            //  println(heroList!!.categories!![0].title)
              if (pDialog != null && pDialog!!.isShowing()) {
                  pDialog.dismiss()
              }
            button.text = heroList!![0].name
            adapter = MovieAdapter(this@SecondActivity, heroList)
            listView.adapter = adapter


        })

    }

}
