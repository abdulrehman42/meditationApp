package com.ar.meditation.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ar.meditation.DataBase.SignupDatabase
import com.ar.meditation.R
import com.ar.meditation.activity.MainActivity
import com.ar.meditation.databinding.FragmentSignupBinding
import com.ar.meditation.model.SignupModel
import com.ar.meditation.repository.SignupRepository
import com.ar.meditation.viewmodel.SignupViewModel
import com.ar.meditation.viewmodel.SignupViewModelFactory
import com.ar.utils.Utilsprogressbar
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Signup : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        kProgressHUD = Utilsprogressbar.showProgressDialog(requireActivity())

        val dao = SignupDatabase.getDatabase(requireContext()).signupDao()
        val repository = SignupRepository(dao)
        signupViewModel = ViewModelProvider(requireActivity(), SignupViewModelFactory(repository)).get(
            SignupViewModel::class.java)

        init_view()
        binding.signinSide.setOnClickListener {
            replaceFragment(R.id.authLoginFragment)
        }
        binding.signupBt.setOnClickListener {
            kProgressHUD.show()
            if (validation()) {
                checkUserExist()
            }
        }
        return binding.root
    }

    private fun checkUserExist() {
        val cnic = binding.cnicInput.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val user = signupViewModel.getSignupByCnic(cnic)
            withContext(Dispatchers.Main) {
                kProgressHUD.dismiss()
                if (user != null) {
                    Toast.makeText(requireContext(), "This CNIC already exists in the database, please login", Toast.LENGTH_SHORT).show()
                } else {
                    dataToDatabase()
                }
            }
        }
    }

    private fun dataToDatabase() {
        val signupData = SignupModel(
            cnic = binding.cnicInput.text.toString(),
            name = binding.nameInput.text.toString(),
            age = binding.ageInput.text.toString(),
            diseases = binding.diseaseInput.text.toString(),
            pass = binding.passInput.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            signupViewModel.insert(signupData)
            withContext(Dispatchers.Main) {
                kProgressHUD.dismiss()
                Toast.makeText(requireContext(), "Successfully Registered", Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
        }
    }

    private fun validation(): Boolean {
        if (binding.nameInput.text!!.length < 3) {
            kProgressHUD.dismiss()
            binding.nameInputlayout.error = "Enter Your Full Name"
            return false
        }
        if (binding.cnicInput.text!!.length < 13) {
            kProgressHUD.dismiss()
            binding.cnicInputlayout.error = "Enter Your Correct CNIC Number"
            return false
        }
        if (binding.passInput.text!!.length < 6) {
            kProgressHUD.dismiss()
            binding.passInputlayout.error = "Enter at least 6 digit password"
            return false
        }
        if (binding.diseaseInput.text.isNullOrEmpty()) {
            kProgressHUD.dismiss()
            binding.diseaseInputlayout.error = "Enter Your Disease"
            return false
        }
        return true
    }

    private fun replaceFragment(fragment: Int) {
        Navigation.findNavController(requireActivity(), R.id.authHostFragment).navigate(fragment, null)
    }

    private fun init_view() {
        val next = "<font color='#303030'> Don't have an account? </font>"
        val second = "<font color='#793397'> Signin</font>"
        binding.signinSide.text = Html.fromHtml(next + second)
    }
}