package com.practicum.practice_work_1_1.presentations

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.practicum.practice_work_1_1.*
import com.practicum.practice_work_1_1.profile.ProfileAdapter
import com.practicum.practice_work_1_1.profile.AccountViewModel
import com.practicum.practice_work_1_1.models.AuthViewModel
import com.practicum.practice_work_1_1.data.db.LoadPhotoResponse
import com.practicum.practice_work_1_1.data.db.UserInfoResponse
import com.practicum.practice_work_1_1.data.repositories.Repository
import com.practicum.practice_work_1_1.databinding.FragmentAccountBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val bundle = Bundle()
    private val accountPhotoAdapter =
        ProfileAdapter { LoadPhotoResponse -> onItemClick(LoadPhotoResponse) }
    private val viewModel: AccountViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AccountViewModel() as T
            }
        }
    }
    private val viewModelExit: AuthViewModel by viewModels()
    private val logoutResponse = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModelExit.webLogoutComplete()
        } else {
            viewModelExit.webLogoutComplete()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recyclerProfile.adapter = accountPhotoAdapter
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var repo: UserInfoResponse
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                repo = Repository().getMyProfile()
                binding.locationText.text = repo.location
                if (repo.location != null) {
                    val map = "geo:0,0?q=${repo.location}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                    intent.setPackage("com.google.android.apps.maps")
                    binding.locationText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    binding.locationText.setOnClickListener {
                        startActivity(intent)
                    }
                } else binding.location.isVisible = false

                binding.profileBio.text = repo.bio ?: ""
                binding.profileTotalDownloads.text = repo.downloads.toString()
                binding.totalLikes.text = repo.total_likes.toString()
                binding.totalCollections.text = repo.total_collections.toString()
                binding.mailText.text = repo.email
                binding.profileFullName.text = repo.name
                binding.profileNickname.text = "@${repo.username}"
                binding.profileFollowers.text = resources.getQuantityString(
                    R.plurals.profile_followers,
                    repo.followers_count,
                    repo.followers_count
                )
                repo.following_count
                binding.profileFollowing.text = resources.getQuantityString(
                    R.plurals.profile_following,
                    repo.following_count,
                    repo.following_count
                )
                binding.profileTotalPhotos.text = resources.getQuantityString(
                    R.plurals.collection_photo_sum,
                    repo.total_photos,
                    repo.total_photos
                )
                binding.profileAvatar.load(repo.profile_image.medium) {
                    target {
                        binding.progress.isVisible = false
                        binding.accountInterface.isVisible = true
                        binding.profileAvatar.setImageDrawable(it)
                    }
                    transformations(CircleCropTransformation())
                }
                viewModel.id = repo.username
                viewModel.pagedPhoto.onEach {
                    accountPhotoAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            } catch (e: Exception) {
                println("Error:$e")
            }
        }
        binding.exitButton.setOnClickListener {
            binding.exitWindow.isVisible = true
        }
        binding.exitNo.setOnClickListener {
            binding.exitWindow.isVisible = false
        }
        binding.exitYes.setOnClickListener {
            viewModelExit.logout()
        }

        viewModelExit.logoutPageFlow.launchAndCollectIn(viewLifecycleOwner) {
            logoutResponse.launch(it)
        }

        viewModelExit.logoutCompletedFlow.launchAndCollectIn(viewLifecycleOwner) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.finish()
            editor = sharedPreferences.edit()
            editor.putString("TOKEN", "")
            editor.apply()
            startActivity(intent)
        }
    }

    private fun onItemClick(item: LoadPhotoResponse) {
        bundle.putString("PhotoId", item.id)
        findNavController().navigate(R.id.feedPhotoDetails, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}