package com.practicum.practice_work_1_1.presentations

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.practice_work_1_1.*
import com.practicum.practice_work_1_1.databinding.FragmentServeBinding
import com.practicum.practice_work_1_1.exhibit.PhotoPagingAdapter
import com.practicum.practice_work_1_1.models.ServeViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("StaticFieldLeak")
lateinit var feedContext: Context
var searchRequest = ""

class ServeFragment : Fragment() {

    private val bundle = Bundle()
    private var _binding: FragmentServeBinding? = null
    private val binding get() = _binding!!
    private val photoAdapter =
        PhotoPagingAdapter { FeedPhoto -> onItemClick(FeedPhoto) }
    private val viewModel: ServeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val feedPhotoDao = (activity?.application as App).db
                return ServeViewModel(feedPhotoDao) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editor = sharedPreferences.edit()
        binding.feedRecycler.adapter = photoAdapter
        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedContext = requireContext()

        val uri = sharedPreferences.getString("PhotoId-Redirect", "")
        if (uri != "") {
            editor.putString("PhotoId-Redirect", "")
            editor.apply()
            bundle.putString("PhotoId", uri)
            findNavController().navigate(R.id.feedPhotoDetails, bundle)
        }

        binding.searchBtn.setOnClickListener {
            binding.inputText.isVisible = !binding.inputText.isVisible
            if (binding.inputText.isVisible) binding.inputText.text.clear()
        }

//        binding.searchBtn.setOnClickListener {
//            searchRequest = "${binding.inputText.text}"
//            startLoad()
//            view.hideKeyboard()
//        }

        binding.inputText.setOnKeyListener(View.OnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                searchRequest = "${binding.inputText.text}"
                startLoad()
                view.hideKeyboard()
                return@OnKeyListener true
            } else false
        })

        binding.inputText.setOnClickListener {
            binding.inputText.text.clear()
        }

        try {
            startLoad()
        } catch (e: Exception) {
            println("Error 4:$e")
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun startLoad() {
        try {
            viewModel.pagedPhoto.onEach {
                photoAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        } catch (e: Exception) {
            println("Error 1: $e")
        }
        try {
            if (searchRequest != "") {
                photoAdapter.refresh()
            }
        } catch (e: Exception) {
            println("Error 2: $e")
        }
    }

    private fun onItemClick(item: FeedPhoto) {
        bundle.putString("PhotoId", item.id)
        findNavController().navigate(R.id.feedPhotoDetails, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun customToast(message: String) {
    val toast = Toast.makeText(feedContext, message, Toast.LENGTH_LONG)
    val toastContainer = toast.view as LinearLayout
    val iconWarning = ImageView(feedContext)
    iconWarning.setImageResource(R.drawable.icon_warning)
    toastContainer.addView(iconWarning, 0)
    toast.show()
}