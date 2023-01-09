package com.ls.gallery.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ls.gallery.R
import com.ls.gallery.adapter.GalleryListAdapter
import com.ls.gallery.databinding.FragmentGalleryPageLayoutBinding
import com.ls.gallery.viewmodel.GalleryViewModel
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private lateinit var root: FragmentGalleryPageLayoutBinding

    private val viewModel: GalleryViewModel by viewModels()

    private val galleryAdapter = GalleryListAdapter()

    private val requestPermission =
        registerForActivityResult(RequestPermission()) {
            if (it) {
                viewModel.galleryUseCase.fetchGallery(requireContext())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = FragmentGalleryPageLayoutBinding.inflate(inflater, container, false)
        return root.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        root.btnAll.setOnClickListener {
            viewModel.galleryUseCase.fetchGallery(requireContext())
        }

        root.rvGallery.apply {
            this.layoutManager = GridLayoutManager(requireContext(), 3)
            this.adapter = galleryAdapter
        }

        initViewModel()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.galleryUseCase.fetchGallery(requireContext())
        } else {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.galleryUseCase.galleryList.collect {
                galleryAdapter.setData(it)
            }
        }
    }
}