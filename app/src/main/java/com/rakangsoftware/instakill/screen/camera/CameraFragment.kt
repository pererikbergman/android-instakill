package com.rakangsoftware.instakill.screen.camera


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.rakangsoftware.instakill.utils.showSnackbar

class CameraFragment : Fragment() {

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
                showSnackbar(binding.root, "File $filename saved!")
            }
        })

        binding.camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                viewModel.savePicture(jpeg)
            }
        })
        return binding.root
    }


    companion object {

        @JvmStatic
        fun newInstance() = CameraFragment()
    }
}
