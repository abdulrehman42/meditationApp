package com.ar.meditation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ar.meditation.DataBase.SignupDao;
import com.ar.meditation.DataBase.SignupDatabase;
import com.ar.meditation.R;
import com.ar.meditation.activity.MainActivity;
import com.ar.meditation.databinding.FragmentLoginBinding;
import com.ar.meditation.model.SignupModels;
import com.ar.meditation.repository.SignupRepository;
import com.ar.meditation.viewmodel.SignupViewModel;
import com.ar.meditation.viewmodel.SignupViewModelFactory;
import com.ar.utils.UtilsProgressBar;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

public class LoginFragmentScreen extends Fragment {
    private FragmentLoginBinding binding;
    private KProgressHUD kProgressHUD;
    private SignupViewModel signupViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        kProgressHUD = UtilsProgressBar.showProgressDialog(requireActivity());

        SignupDao dao = SignupDatabase.getDatabase(requireContext()).signupDao();
        SignupRepository repository = new SignupRepository(dao);
        signupViewModel = new ViewModelProvider(requireActivity(), new SignupViewModelFactory(repository))
                .get(SignupViewModel.class);

        initView();
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
        String pass = binding.passsignInput.getText().toString();

        // Observe LiveData on the main thread
        signupViewModel.getSignupByCnicAndPass(cnic, pass).observe(getViewLifecycleOwner(), new Observer<List<SignupModels>>() {
            @Override
            public void onChanged(@Nullable List<SignupModels> userList) {
                // Perform background tasks on a background thread
                new Thread(() -> {
                    // Dismiss the progress HUD on the main thread
                    requireActivity().runOnUiThread(() -> kProgressHUD.dismiss());

                    boolean exists = userList != null && userList.stream().anyMatch(user -> user.getCnic().equals(cnic));

                    // Update UI based on the results on the main thread
                    requireActivity().runOnUiThread(() -> {
                        if (exists) {
                            startActivity(new Intent(requireActivity(), MainActivity.class));
                        } else {
                            Toast.makeText(requireContext(), "Invalid CNIC or Password. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Remove the observer after processing
                    requireActivity().runOnUiThread(() -> signupViewModel.getSignupByCnicAndPass(cnic, pass).removeObservers(getViewLifecycleOwner()));
                }).start();
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
        String next = "<font color='#303030'> Do you have an account? </font>";
        String second = "<font color='#793397'> Signup</font>";
        binding.loginSide.setText(Html.fromHtml(next + second));
    }
}
