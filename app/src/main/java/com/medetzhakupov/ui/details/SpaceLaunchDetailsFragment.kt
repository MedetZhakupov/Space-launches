package com.medetzhakupov.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.medetzhakupov.R
import com.medetzhakupov.data.model.SpaceLaunch
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.space_launch_details_fragment.coordinates
import kotlinx.android.synthetic.main.space_launch_details_fragment.description
import kotlinx.android.synthetic.main.space_launch_details_fragment.image
import kotlinx.android.synthetic.main.space_launch_details_fragment.title


private const val EXTRA_SPACE_LAUNCH = "EXTRA_SPACE_LAUNCH"

class SpaceLaunchDetailsFragment : Fragment() {

    fun withArguments(spaceLaunch: SpaceLaunch) = apply {
        arguments = bundleOf(EXTRA_SPACE_LAUNCH to spaceLaunch)
    }

    private val spaceLaunch: SpaceLaunch
        get() = requireNotNull(requireArguments().getParcelable(EXTRA_SPACE_LAUNCH))


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.space_launch_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load(spaceLaunch.pad.location.map_image).into(image)
        title.text = spaceLaunch.pad.name
        coordinates.apply {
            text = " ${spaceLaunch.pad.latitude}  ${spaceLaunch.pad.latitude}"
            setOnClickListener { startGoogleMaps(spaceLaunch.pad.latitude, spaceLaunch.pad.longitude) }
        }
        description.text = spaceLaunch.mission?.description
    }

    private fun startGoogleMaps(latitude: String, longitude: String) {
        val gmmIntentUri: Uri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}