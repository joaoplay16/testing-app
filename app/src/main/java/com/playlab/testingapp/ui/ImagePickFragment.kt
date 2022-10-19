package com.playlab.testingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.playlab.testingapp.R
import com.playlab.testingapp.adapter.ImageAdapter
import com.playlab.testingapp.databinding.FragmentImagePickBinding
import com.playlab.testingapp.other.Constants.GRID_SPAN_COUNT
import com.playlab.testingapp.other.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter,
) : Fragment(R.layout.fragment_image_pick) {

    lateinit var viewModel: ShoppingViewModel

    lateinit var binding: FragmentImagePickBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePickBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        searchForImage()
        setupObservers()
        setupRecyclerView()

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
        }

        binding.etSearch.setOnEditorActionListener{ view, actionId, event ->
            return@setOnEditorActionListener when (actionId){
                EditorInfo.IME_ACTION_DONE -> {
                    searchForImage(view.text.toString())
                    true
                } else -> false
            }
        }
    }

    private fun searchForImage(query: String = "fruit"){
        viewModel.searchForImage(query)

    }

    private fun setupObservers(){
        viewModel.images.observe(viewLifecycleOwner, Observer {
            val imageResource = it.getContentIfNotHandled()

            if(imageResource?.status == Status.SUCCESS){
                val imageResponse = imageResource.data
                val images = imageResponse?.hits?.map { imageResult ->
                    imageResult.largeImageURL
                }

                imageAdapter.images = images!!
            }
        })
    }

    private fun setupRecyclerView(){
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }
}