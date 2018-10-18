package com.rakangsoftware.instakill.screen.camera


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraListener

import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.databinding.CameraFragmentBinding
import com.rakangsoftware.instakill.screen.dashboard.DashboardActivity
import com.rakangsoftware.instakill.screen.dashboard.NavigationViewModel

class CameraFragment : Fragment() {

    private lateinit var navigation: NavigationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel: CameraViewModel = ViewModelProviders.of(
            this, CameraViewModelFactory(container!!.context)
        ).get(CameraViewModel::class.java)
        val binding: CameraFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.camera_fragment, container, false)

        binding.camera.setLifecycleOwner(this.viewLifecycleOwner)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.takePicture.observe(this, Observer {
            if (it !== null && it) {
                binding.camera.capturePicture()
            }
        })

        viewModel.pictureSaved.observe(this, Observer { filename ->
            filename?.let {
                Snackbar.make(binding.root, "File $filename saved!", Snackbar.LENGTH_SHORT).show()
                navigation.launch("feed")
            }
        })

        binding.camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                viewModel.savePicture(jpeg)
            }
        })
        return binding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigation = ViewModelProviders.of(context as DashboardActivity).get(NavigationViewModel::class.java)

    }

    companion object {

        @JvmStatic
        fun newInstance() = CameraFragment()
    }
}
