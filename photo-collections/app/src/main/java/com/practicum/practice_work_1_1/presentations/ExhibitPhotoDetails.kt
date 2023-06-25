package com.practicum.practice_work_1_1.presentations

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.practicum.practice_work_1_1.MainActivity
import com.practicum.practice_work_1_1.R
import com.practicum.practice_work_1_1.data.repositories.Repository
import com.practicum.practice_work_1_1.data.db.PhotoDetails
import com.practicum.practice_work_1_1.databinding.FragmentExhibitPhotoDetailsBinding


import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class ExhibitPhotoDetails : Fragment() {

    private var _binding: FragmentExhibitPhotoDetailsBinding? = null
    private val binding get() = _binding!!
    private var likeByUser by Delegates.notNull<Boolean>()
    private lateinit var repository: PhotoDetails
    private lateinit var likePhoto: PhotoDetails
    private lateinit var unlikePhoto: PhotoDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExhibitPhotoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoId = arguments?.getString("PhotoId").toString()
        val address = mutableListOf<String>()
        val exif = mutableListOf<String>()
        val tags = mutableListOf<String>()
        val exifMade = getString(R.string.photo_details_exif_made)
        val exifModel = getString(R.string.photo_details_exif_model)
        val exifExposure = getString(R.string.photo_details_exif_exposure)
        val exifAperture = getString(R.string.photo_details_exif_aperture)
        val exifFocalLength = getString(R.string.photo_details_exif_focal_length)
        val exifISO = getString(R.string.photo_details_camera_iso)

        viewLifecycleOwner.lifecycleScope.launch {
            repository = Repository().getDetailsPhoto(photoId!!)

            if (repository.exif.make != null) exif.add(exifMade + repository.exif.make)
            if (repository.exif.model != null) exif.add(exifModel + repository.exif.model)
            if (repository.exif.exposure_time != null) exif.add(exifExposure + repository.exif.exposure_time)
            if (repository.exif.aperture != null) exif.add(exifAperture + repository.exif.aperture)
            if (repository.exif.focal_length != null) exif.add(exifFocalLength + repository.exif.focal_length)
            if (repository.exif.iso != null) exif.add(exifISO + repository.exif.iso)
            repository.tags.onEach {
                tags.add("#${it.title}")
            }
            address.add(repository.location.city)
            address.add(repository.location.country)
            if (address.size == 2) {
                val lat = repository.location.position.latitude
                val lng = repository.location.position.longitude
                val mTitle = getString(R.string.photo_details_location_label)
                val map = "geo:0,0?q=${lat},${lng}(${mTitle})"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                intent.setPackage("com.google.android.apps.maps")
                binding.locationName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                binding.locationName.setOnClickListener {
                    startActivity(intent)
                }
            }
            binding.downloadButton.setOnClickListener {
                checkPermissions()
            }
            binding.progress.isVisible = true
            binding.mainPhoto.load(repository.urls.full) {
                target {
                    binding.progress.isVisible = false
                    binding.barrier.isVisible = true
                    if (address.size == 0) {
                        binding.iconLocation.isVisible = false
                        binding.locationName.isVisible = false
                    }
                    binding.mainPhoto.setImageDrawable(it)
                }
            }
            binding.authorAvatar.load(repository.user.profile_image.small) {
                transformations(CircleCropTransformation())
            }
            likeByUser = repository.liked_by_user
            checkLike()
            binding.indexLike.setOnClickListener {
                likeUnlike(photoId)
            }

            if (repository.liked_by_user) binding.indexLike.setColorFilter(Color.YELLOW)
            binding.authorName.text = repository.user.name
            binding.authorNickname.text = "@${repository.user.username}"
            binding.sumLikes.text = repository.likes.toString()
            binding.sumOfDownloads.text = "(${repository.downloads})"
            binding.locationName.text = address.joinToString(", ")
            binding.exif.text = exif.joinToString("\n")
            binding.hashTag.text = tags.joinToString(" ")
            binding.authorInfo.text = repository.user.bio
        }

        binding.sharedButton.setOnClickListener {
            val sharedLink = repository.links.html
            val sharedText = getString(R.string.photo_details_shared_link)
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharedLink)
            sendIntent.type = "text/plain"
            startActivity(Intent.createChooser(sendIntent, sharedText))
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }
    }

    private fun likeUnlike(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (likeByUser) {
                unlikePhoto = Repository().unsetLike(id)
                binding.sumLikes.text = unlikePhoto.likes.toString()
                likeByUser = false
                checkLike()
                println("DISLIKE")
            } else {
                likePhoto = Repository().setLike(id)
                binding.sumLikes.text = likePhoto.likes.toString()
                likeByUser = true
                checkLike()
                println("LIKE")
            }
        }
    }

    private fun checkLike() {
        if (likeByUser) binding.indexLike.setColorFilter(Color.YELLOW)
        else binding.indexLike.setColorFilter(Color.WHITE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startDownload(url: String, name: String) {
        (activity as MainActivity).startDownload(url, name)
    }

    companion object {
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    private fun checkPermissions() {
        val isAllGranted = REQUEST_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) {
            val url = repository.links.download
            val name = repository.id
            startDownload(url!!, name)
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.isNotEmpty() && map.values.all { it }) {
                val granted = getString(R.string.photo_details_permission_granted)
                Toast.makeText(requireContext(), granted, Toast.LENGTH_LONG).show()
            } else customToast(getString(R.string.photo_details_permission_not_granted))
        }
}