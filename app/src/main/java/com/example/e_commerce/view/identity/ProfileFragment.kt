package com.example.e_commerce.view.identity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val IMAGE_PICKER = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.callUserProfile()
        observers()

        binding.profileImageview.setOnClickListener(){
            showImagePicker()
        }

    }

    fun showImagePicker(){

        Matisse.from(this).choose(MimeType.ofImage()).capture(true).captureStrategy(CaptureStrategy(true,"com.example.e_commerce"))
            .forResult(IMAGE_PICKER)
    }
    fun observers(){
        profileViewModel.profileLiveData.observe(viewLifecycleOwner,{
            binding.profileProgressBar.animate().alpha(0f)
            binding.emailTextview.text = it.email
            binding.fullnameTextview.text = it.fullName
            Picasso.get().load("http://18.196.156.64/Images/${it.image}").into(binding.profileImageview)

        })
        profileViewModel.profileErrorsLiveData.observe(viewLifecycleOwner,{
            binding.profileProgressBar.animate().alpha(0f)
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        })
    }

}

