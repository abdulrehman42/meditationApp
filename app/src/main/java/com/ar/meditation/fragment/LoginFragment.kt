package com.ar.meditation.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ar.meditation.R
import com.ar.meditation.activity.MainActivity
import com.ar.meditation.databinding.FragmentLoginBinding
import com.ar.meditation.model.Response
import com.ar.meditation.model.SignupModel
import com.ar.utils.Utilsprogressbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD

class LoginFragment : Fragment() {
    lateinit var referrence: DatabaseReference
    lateinit var kProgressHUD: KProgressHUD

    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        init_view()
        kProgressHUD= Utilsprogressbar.showProgressDialog(requireActivity())
        binding.lognBt.setOnClickListener {
            kProgressHUD.show()

            if (validation()){
               checkUserExist()
           }
        }
        binding.loginSide.setOnClickListener {
            Navigation.findNavController(
                requireActivity(),
                R.id.authHostFragment
            ).navigate(R.id.authsignupFragment, null)

        }
        return binding.root
    }
    fun checkUserExist(){
        val cnic=binding.cnicsignInput.text.toString()
        referrence = FirebaseDatabase.getInstance().getReference("User").child(cnic)

        referrence.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        val user: SignupModel? = dataSnapshot.getValue(SignupModel::class.java)
                        if (binding.passsignInput.text.toString() == user?.pass) {
                            kProgressHUD.dismiss()
                            requireActivity().startActivity(
                                Intent(requireActivity(), MainActivity::class.java)
                            )
                        } else {
                            kProgressHUD.dismiss()
                            Toast.makeText(
                                requireContext(), "Invalid CNIC or password. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        kProgressHUD.dismiss()
                        Toast.makeText(
                            requireContext(),
                            "This CNIC does not exist in the database. Please register now.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    kProgressHUD.dismiss()
                    Toast.makeText(
                        requireContext(), "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e:Exception)
                {
                    Toast.makeText(
                        requireContext(), "${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
                println("Error checking value: ${databaseError.message}")
            }
        })
    }

    private fun validation() :Boolean{


        if (binding.cnicsignInput.text!!.length < 13) {
            kProgressHUD.dismiss()

            binding.cnicsignInputlayout.error = "Enter Your Correct CNIC Number"
            return false
        }
        if (binding.passsignInput.text!!.length < 6) {
            kProgressHUD.dismiss()
            binding.passsignInputlayout.error = "Enter at least 6 digit password"
            return false

        }
        else{
            return true

        }
    }

    private fun init_view() {
        val next = "<font color='#303030'> Do you have an account? </font>"
        val second = "<font color='#793397'> Signup</font>"
        binding.loginSide.text = Html.fromHtml(next + second)
    }
}