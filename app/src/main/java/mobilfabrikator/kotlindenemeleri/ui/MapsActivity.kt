package mobilfabrikator.kotlindenemeleri.ui

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_maps.*
import mobilfabrikator.kotlindenemeleri.R
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var mLat: Double = 0.0
    var mLng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mLat = getIntent().getDoubleExtra("mLat", 0.0)
        mLng = getIntent().getDoubleExtra("mLng", 0.0)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val currentLocation = LatLng(mLat, mLng)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,18.0f))

        mMap.setOnCameraChangeListener(object: GoogleMap.OnCameraChangeListener {
            override fun onCameraChange(arg0: CameraPosition) {
                Log.i("centerLat", arg0.target.latitude.toString());
                Log.i("centerLong", arg0.target.longitude.toString());
                
                getShowAdress(arg0.target.latitude,arg0.target.longitude)


            }
        })

    }

    private fun getShowAdress(latitude: Double, longitude: Double) {
        val geocoder: Geocoder
        val addresses:List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        addresses = geocoder.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        val address = addresses.get(0).getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        val mahalle = addresses.get(0).getSubLocality()
        val sehir = addresses.get(0).getAdminArea()
        val country = addresses.get(0).getCountryName()
        val postalCode = addresses.get(0).getPostalCode()
        val knownName = addresses.get(0).getFeatureName()
        val cadde = addresses.get(0).getThoroughfare()
        val ilce = addresses.get(0).getSubAdminArea()
        adresText.text = "adresim : "+ilce + "----"+mahalle+"---"+ cadde

      //  println("adresim  "+ilce + "----"+mahalle+"---"+ cadde)

    }
}
