package com.playlab.testingapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.playlab.testingapp.R
import com.playlab.testingapp.databinding.FragmentAddShoppingItemBinding
import com.playlab.testingapp.other.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItemFragment @Inject constructor(
    val glide: RequestManager,
    var viewModel: ShoppingViewModel? = null
): Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var binding: FragmentAddShoppingItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddShoppingItemBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObservers()

        binding.btnAddShoppingItem.setOnClickListener{
            viewModel?.insertShoppingItem(
                binding.etShoppingItemName.text.toString(),
                binding.etShoppingItemAmount.text.toString(),
                binding.etShoppingItemPrice.text.toString(),
            )
        }

        binding.ivShoppingImage.setOnClickListener{
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel?.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subscribeToObservers() {
        viewModel?.curImageUrl?.observe(viewLifecycleOwner, Observer {
            glide.load(it).into(binding.ivShoppingImage)
        })

        viewModel?.insertShoppingItemStatus?.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { result ->
                when(result.status){
                    Status.SUCCESS -> {
                        Snackbar.make(
                            binding.root,
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "An unknown error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        /*NO-OP*/
                    }
                }
            }
        })
    }
}