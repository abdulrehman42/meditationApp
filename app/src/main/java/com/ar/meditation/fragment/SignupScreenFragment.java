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
import com.ar.meditation.databinding.FragmentSignupBinding;
import com.ar.meditation.model.SignupModels;
import com.ar.meditation.repository.SignupRepository;
import com.ar.meditation.viewmodel.SignupViewModel;
import com.ar.meditation.viewmodel.SignupViewModelFactory;
import com.ar.utils.UtilsProgressBar;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

public class SignupScreenFragment extends Fragment {
    private FragmentSignupBinding binding;
    private KProgressHUD kProgressHUD;
    private SignupViewModel signupViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        kProgressHUD = UtilsProgressBar.showProgressDialog(requireActivity());

        SignupDao dao = SignupDatabase.getDatabase(requireContext()).signupDao();
        SignupRepository repository = new SignupRepository(dao);
        signupViewModel = new ViewModelProvider(requireActivity(), new SignupViewModelFactory(repository))
                .get(SignupViewModel.class);

        initView();
        binding.signinSide.setOnClickListener(v -> replaceFragment(R.id.authLoginFragment));
        binding.signupBt.setOnClickListener(v -> {
            kProgressHUD.show();
            if (validation()) {
                checkUserExist();
            }
        });
        return binding.getRoot();
    }

    private void checkUserExist() {
        String cnic = binding.cnicInput.getText().toString();

        // Observe LiveData on the main thread
        signupViewModel.getSignupByCnic(cnic).observe(getViewLifecycleOwner(), new Observer<List<SignupModels>>() {
            @Override
            public void onChanged(@Nullable List<SignupModels> userList) {
                // Dismiss the progress HUD on the main thread
                kProgressHUD.dismiss();

                // Perform background tasks on a separate thread if needed
                new Thread(() -> {
                    boolean exists = userList != null && userList.stream().anyMatch(user -> user.getCnic().equals(cnic));

                    // Update UI based on the results on the main thread
                    requireActivity().runOnUiThread(() -> {
                        if (exists) {
                            Toast.makeText(requireContext(), "This CNIC already exists in the database, please login", Toast.LENGTH_SHORT).show();
                        } else {
                            dataToDatabase();
                        }
                        // Remove the observer after processing
                        signupViewModel.getSignupByCnic(cnic).removeObservers(getViewLifecycleOwner());
                    });
                }).start();
            }
        });
    }

    private void dataToDatabase() {
        SignupModels signupData = new SignupModels(
                0, // id will be auto-generated
                binding.nameInput.getText().toString(),
                binding.ageInput.getText().toString(),
                binding.cnicInput.getText().toString(),
                binding.diseaseInput.getText().toString(),
                binding.passInput.getText().toString()
        );

        new Thread(() -> {
            signupViewModel.insert(signupData);
            requireActivity().runOnUiThread(() -> {
                kProgressHUD.dismiss();
                Toast.makeText(requireContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireActivity(), MainActivity.class));
                requireActivity().finish();
            });
        }).start();
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
        if (binding.diseaseInput.getText().toString().isEmpty()) {
            kProgressHUD.dismiss();
            binding.diseaseInputlayout.setError("Enter Your Disease");
            return false;
        }
        return true;
    }

    private void replaceFragment(int fragmentId) {
        Navigation.findNavController(requireActivity(), R.id.authHostFragment).navigate(fragmentId, null);
    }

    private void initView() {
        String next = "<font color='#303030'> Don't have an account? </font>";
        String second = "<font color='#793397'> Signin</font>";
        binding.signinSide.setText(Html.fromHtml(next + second));
    }
}
