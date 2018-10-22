package com.rakangsoftware.instakill.screen.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rakangsoftware.instakill.R

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.maps_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            println("Map loaded!!!")

            val latLng = LatLng(-34.0, 151.0)
            it.addMarker(
                MarkerOptions().position(latLng).title("hej hej")
            )

            it.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}