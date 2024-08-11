package com.ar.meditation.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ar.meditation.DataBase.SignupDatabase
import com.ar.meditation.R
import com.ar.meditation.activity.MainActivity
import com.ar.meditation.databinding.FragmentLoginBinding
import com.ar.meditation.model.SignupModel
import com.ar.meditation.repository.SignupRepository
import com.ar.meditation.viewmodel.SignupViewModel
import com.ar.meditation.viewmodel.SignupViewModelFactory
import com.ar.utils.Utilsprogressbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        kProgressHUD = Utilsprogressbar.showProgressDialog(requireActivity())

        val dao = SignupDatabase.getDatabase(requireContext()).signupDao()
        val repository = SignupRepository(dao)
        signupViewModel = ViewModelProvider(requireActivity(), SignupViewModelFactory(repository)).get(SignupViewModel::class.java)

        init_view()
        binding.lognBt.setOnClickListener {
            kProgressHUD.show()
            if (validation()) {
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

    private fun checkUserExist() {
        val cnic = binding.cnicsignInput.text.toString()
        val pass = binding.passsignInput.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val user = signupViewModel.getSignupByCnicAndPass(cnic, pass)
            withContext(Dispatchers.Main) {
                kProgressHUD.dismiss()
                if (user != null) {
                    requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                } else {
                    Toast.makeText(
                        requireContext(), "Invalid CNIC or password. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validation(): Boolean {
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
        return true
    }

    private fun init_view() {
        val next = "<font color='#303030'> Do you have an account? </font>"
        val second = "<font color='#793397'> Signup</font>"
        binding.loginSide.text = Html.fromHtml(next + second)
    }
}