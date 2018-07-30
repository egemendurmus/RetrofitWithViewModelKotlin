package mobilfabrikator.kotlindenemeleri.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import mobilfabrikator.kotlindenemeleri.R
import mobilfabrikator.kotlindenemeleri.helper.FetchAddressIntentService
import mobilfabrikator.kotlindenemeleri.utils.MapAppUtils


class DraggableMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mContext: Context
    internal lateinit var mLocationMarkerText: TextView
    private var mCenterLatLong: LatLng? = null


    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private var mResultReceiver: AddressResultReceiver? = null
    /**
     * The formatted location address.
     */
    protected var mAddressOutput: String? = null
    protected var mAreaOutput: String? = null
    protected var mCityOutput: String? = null
    protected var mStateOutput: String? = null
    internal lateinit var mLocationAddress: EditText
    internal lateinit var mLocationText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.draggable_maps)
        mContext = this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?

        mLocationMarkerText = findViewById<View>(R.id.locationMarkertext) as TextView
        mLocationAddress = findViewById<View>(R.id.Address) as EditText
        mLocationText = findViewById<View>(R.id.Locality) as TextView



        mLocationText.setOnClickListener { openAutocompleteActivity() }
        mapFragment!!.getMapAsync(this)
        mResultReceiver = AddressResultReceiver(Handler())

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!MapAppUtils.isLocationEnabled(mContext)) {
                // notify user
                val dialog = AlertDialog.Builder(mContext)
                dialog.setMessage("Location not enabled!")
                dialog.setPositiveButton("Open location settings") { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                dialog.setNegativeButton("Cancel") { paramDialogInterface, paramInt ->
                    // TODO Auto-generated method stub
                }
                dialog.show()
            }
            buildGoogleApiClient()
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show()
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "OnMapReady")
        mMap = googleMap

        mMap!!.setOnCameraChangeListener { cameraPosition ->
            Log.d("Camera postion change" + "", cameraPosition.toString() + "")
            mCenterLatLong = cameraPosition.target


            mMap!!.clear()

            try {

                val mLocation = Location("")
                mLocation.latitude = mCenterLatLong!!.latitude
                mLocation.longitude = mCenterLatLong!!.longitude

                startIntentService(mLocation)
                mLocationMarkerText.text = "Lat : " + mCenterLatLong!!.latitude + "," + "Long : " + mCenterLatLong!!.longitude

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        //        mMap.setMyLocationEnabled(true);
        //        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //
        //        // Add a marker in Sydney and move the camera
        //        LatLng sydney = new LatLng(-34, 151);
        //        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            changeMap(mLastLocation)
            Log.d(TAG, "ON connected")

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        try {
            val mLocationRequest = LocationRequest()
            mLocationRequest.interval = 10000
            mLocationRequest.fastestInterval = 5000
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onConnectionSuspended(i: Int) {
        Log.i(TAG, "Connection suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location?) {
        try {
            if (location != null)
                changeMap(location)
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }


    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    override fun onStart() {
        super.onStart()
        try {
            mGoogleApiClient!!.connect()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStop() {
        super.onStop()
        try {

        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    private fun checkPlayServices(): Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                //finish();
            }
            return false
        }
        return true
    }

    private fun changeMap(location: Location) {

        Log.d(TAG, "Reaching map" + mMap!!)


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap!!.uiSettings.isZoomControlsEnabled = false
            val latLong: LatLng


            latLong = LatLng(location.latitude, location.longitude)

            val cameraPosition = CameraPosition.Builder()
                    .target(latLong).zoom(10f).tilt(10f).build()

            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
            mMap!!.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition))

            mLocationMarkerText.text = "Lat : " + location.latitude + "," + "Long : " + location.longitude
            startIntentService(location)


        } else {
            Toast.makeText(applicationContext,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show()
        }

    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(MapAppUtils.LocationConstants.RESULT_DATA_KEY)

            mAreaOutput = resultData.getString(MapAppUtils.LocationConstants.LOCATION_DATA_AREA)

            mCityOutput = resultData.getString(MapAppUtils.LocationConstants.LOCATION_DATA_CITY)
            mStateOutput = resultData.getString(MapAppUtils.LocationConstants.LOCATION_DATA_STREET)

            displayAddressOutput()

            // Show a toast message if an address was found.
            if (resultCode == MapAppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    /**
     * Updates the address in the UI.
     */
    protected fun displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
            // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput)
            //mLocationText.setText(mAreaOutput);
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected fun startIntentService(mLocation: Location) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        val intent = Intent(this, FetchAddressIntentService::class.java)

        // Pass the result receiver as an extra to the service.
        intent.putExtra(MapAppUtils.LocationConstants.RECEIVER, mResultReceiver)

        // Pass the location data as an extra to the service.
        intent.putExtra(MapAppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation)

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent)
    }


    private fun openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.connectionStatusCode,
                    0 /* requestCode */).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the user's selected place from the Intent.
                val place = PlaceAutocomplete.getPlace(mContext, data)

                // TODO call location based filter


                val latLong: LatLng


                latLong = place.latLng

                //mLocationText.setText(place.getName() + "");

                val cameraPosition = CameraPosition.Builder()
                        .target(latLong).zoom(10f).tilt(10f).build()

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap!!.isMyLocationEnabled = true
                mMap!!.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition))


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            val status = PlaceAutocomplete.getStatus(mContext, data)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }

    companion object {
        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private val TAG = "MAP LOCATION"
        private val REQUEST_CODE_AUTOCOMPLETE = 1
    }


}