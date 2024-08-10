package com.ar.meditation.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.ar.meditation.R
import com.ar.meditation.activity.MainActivity
import com.ar.meditation.databinding.FragmentSignupBinding
import com.ar.meditation.model.SignupModel
import com.ar.utils.Utilsprogressbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD

class Signup : Fragment() {
    lateinit var referrence:DatabaseReference
    lateinit var binding: FragmentSignupBinding
    lateinit var kProgressHUD:KProgressHUD


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSignupBinding.inflate(layoutInflater)
        kProgressHUD= Utilsprogressbar.showProgressDialog(requireActivity())
        referrence = FirebaseDatabase.getInstance().reference
        init_view()
        binding.signinSide.setOnClickListener {
            replaceFragment(R.id.authLoginFragment)
        }
        binding.signupBt.setOnClickListener {
            kProgressHUD.show()
            if (validation()){
                checkUserExist()
                //dataToFirebase()
            }
        }
        return binding.root

    }
    fun checkUserExist(){
        referrence.database.getReference("User").child(binding.cnicInput.text.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        kProgressHUD.dismiss()
                        Toast.makeText(requireContext(),"this email exist in database please login",Toast.LENGTH_SHORT).show()
                    } else {
                        dataToFirebase()
                    }
                }catch (e:Exception)
                {
                    dataToFirebase()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
                println("Error checking value: ${databaseError.message}")
            }
        })
    }

    private fun dataToFirebase() {
        val signupData=SignupModel(binding.nameInput.text.toString(),binding.ageInput.text.toString(),binding.cnicInput.text.toString(),binding.diseaseInput.text.toString(),binding.passInput.text.toString())
        referrence.child("User").child(binding.cnicInput.text.toString()).setValue(signupData).addOnCompleteListener {
            kProgressHUD.dismiss()
            if (it.isSuccessful)
            {
                Toast.makeText(requireContext(),"Successfully Register",Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireActivity(),MainActivity::class.java))
            }
            else
            {
                Toast.makeText(requireContext(),"something went wrong",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun validation():Boolean {

        if (binding.nameInput.text!!.length<3)
        {
            kProgressHUD.dismiss()
            binding.nameInputlayout.error="Enter Your Full Name"
            return false
        }
        if (binding.cnicInput.text!!.length<13)
        {
            kProgressHUD.dismiss()

            binding.cnicInputlayout.error="Enter Your Correct CNIC Number"
            return false
        }
        if (binding.passInput.text!!.length<6)
        {
            kProgressHUD.dismiss()

            binding.passInputlayout.error="Enter at least 6 digit password"
            return false
        }
        if (binding.passInput.text.isNullOrEmpty())
        {
            kProgressHUD.dismiss()

            binding.passInputlayout.error="Enter Your Age"
            return false
        }
        if (binding.diseaseInput.text.isNullOrEmpty())
        {
            kProgressHUD.dismiss()

            binding.diseaseInputlayout.error="Enter Your disease"
            return false
        }
        else{
            return true
        }

    }
    fun replaceFragment(fragment: Int) {
        Navigation.findNavController(requireActivity(), R.id.authHostFragment).navigate(fragment, null)
    }
    private fun init_view() {
        val next = "<font color='#303030'> Don't have an account? </font>"
        val second = "<font color='#793397'> Signin</font>"
        binding.signinSide.text = Html.fromHtml(next + second)
    }


}