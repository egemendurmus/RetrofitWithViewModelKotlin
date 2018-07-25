package mobilfabrikator.kotlindenemeleri.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import mobilfabrikator.kotlindenemeleri.R
import mobilfabrikator.kotlindenemeleri.model.secondModel.MovieAppModel

class MovieAdapter(activity: Activity, private val mKisiListesi: Array<MovieAppModel>?) : BaseAdapter() {

    private val mInflater: LayoutInflater

    init {
        mInflater = activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return mKisiListesi!!.size
    }

    override fun getItem(position: Int): MovieAppModel {
        return mKisiListesi!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val satirView: View

        satirView = mInflater.inflate(R.layout.adapter_movie, null)
        val textView = satirView.findViewById<View>(R.id.textView) as TextView
        val imageView = satirView.findViewById<View>(R.id.imageView) as ImageView


        val kisi = mKisiListesi!![position]
        textView.text = kisi.name
        Picasso.get()
                .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")
                .into(imageView);



        return satirView
    }
}