package com.ar.meditation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;

import com.ar.meditation.R;
import com.ar.meditation.activity.MainActivity;
import com.ar.meditation.databinding.FragmentSignupBinding;
import com.ar.meditation.model.SignupModels;
import com.ar.utils.UtilsProgressBars;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;


public class SignupScreen extends Fragment {
    private DatabaseReference reference;
    private FragmentSignupBinding binding;
    private KProgressHUD kProgressHUD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        kProgressHUD = UtilsProgressBars.showProgressDialog(requireActivity());
        reference = FirebaseDatabase.getInstance().getReference();
        initView();

        binding.signinSide.setOnClickListener(v -> replaceFragment(R.id.authLoginFragment));

        binding.signupBt.setOnClickListener(v -> {
            kProgressHUD.show();
            if (validation()) {
                checkUserExist();
                // dataToFirebase();
            }
        });

        return binding.getRoot();
    }

    private void checkUserExist() {
        reference.child("User").child(binding.cnicInput.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                kProgressHUD.dismiss();
                                Toast.makeText(requireContext(), "This email exists in the database. Please login.", Toast.LENGTH_SHORT).show();
                            } else {
                                dataToFirebase();
                            }
                        } catch (Exception e) {
                            dataToFirebase();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        System.out.println("Error checking value: " + databaseError.getMessage());
                    }
                });
    }

    private void dataToFirebase() {
        SignupModels signupData = new SignupModels(
                binding.nameInput.getText().toString(),
                binding.ageInput.getText().toString(),
                binding.cnicInput.getText().toString(),
                binding.diseaseInput.getText().toString(),
                binding.passInput.getText().toString()
        );
        reference.child("User").child(binding.cnicInput.getText().toString())
                .setValue(signupData)
                .addOnCompleteListener(task -> {
                    kProgressHUD.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                        requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validation() {
        if (binding.nameInput.getText().length() < 3) {
            kProgressHUD.dismiss();
            binding.nameInputlayout.setError("Enter Your Full Name");
            return false;
        }
        if (binding.cnicInput.getText().length() < 13) {
            kProgressHUD.dismiss();
            binding.cnicInputlayout.setError("Enter Your Correct CNIC Number");
            return false;
        }
        if (binding.passInput.getText().length() < 6) {
            kProgressHUD.dismiss();
            binding.passInputlayout.setError("Enter at least 6 digit password");
            return false;
        }
        if (binding.passInput.getText().toString().isEmpty()) {
            kProgressHUD.dismiss();
            binding.passInputlayout.setError("Enter Your Age");
            return false;
        }
        if (binding.diseaseInput.getText().toString().isEmpty()) {
            kProgressHUD.dismiss();
            binding.diseaseInputlayout.setError("Enter Your Disease");
            return false;
        }
        return true;
    }

    private void replaceFragment(int fragment) {
        Navigation.findNavController(requireActivity(), R.id.authHostFragment).navigate(fragment);
    }

    private void initView() {
        String next = "<font color='#303030'>Don't have an account? </font>";
        String second = "<font color='#793397'>Signin</font>";
        binding.signinSide.setText(Html.fromHtml(next + second));
    }

}
