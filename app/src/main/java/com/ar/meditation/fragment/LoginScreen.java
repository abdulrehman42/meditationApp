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
import com.ar.meditation.databinding.FragmentLoginBinding;
import com.ar.meditation.model.SignupModels;
import com.ar.utils.UtilsProgressBars;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

public class LoginScreen extends Fragment {
    private DatabaseReference reference;
    private KProgressHUD kProgressHUD;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        initView();
        kProgressHUD = UtilsProgressBars.showProgressDialog(requireActivity());

        binding.lognBt.setOnClickListener(v -> {
            kProgressHUD.show();
            if (validation()) {
                checkUserExist();
            }
        });

        binding.loginSide.setOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.authHostFragment)
                        .navigate(R.id.authsignupFragment, null)
        );

        return binding.getRoot();
    }

    private void checkUserExist() {
        String cnic = binding.cnicsignInput.getText().toString();
        reference = FirebaseDatabase.getInstance().getReference("User").child(cnic);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        SignupModels user = dataSnapshot.getValue(SignupModels.class);
                        if (binding.passsignInput.getText().toString().equals(user.getPass())) {
                            kProgressHUD.dismiss();
                            requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
                        } else {
                            kProgressHUD.dismiss();
                            Toast.makeText(requireContext(), "Invalid CNIC or password. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        kProgressHUD.dismiss();
                        Toast.makeText(requireContext(), "This CNIC does not exist in the database. Please register now.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    kProgressHUD.dismiss();
                    Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                System.out.println("Error checking value: " + databaseError.getMessage());
            }
        });
    }

    private boolean validation() {
        if (binding.cnicsignInput.getText().length() < 13) {
            kProgressHUD.dismiss();
            binding.cnicsignInputlayout.setError("Enter Your Correct CNIC Number");
            return false;
        }

        if (binding.passsignInput.getText().length() < 6) {
            kProgressHUD.dismiss();
            binding.passsignInputlayout.setError("Enter at least 6 digit password");
            return false;
        }

        return true;
    }

    private void initView() {
        String next = "<font color='#303030'>Do you have an account? </font>";
        String second = "<font color='#793397'>Signup</font>";
        binding.loginSide.setText(Html.fromHtml(next + second));
    }
}