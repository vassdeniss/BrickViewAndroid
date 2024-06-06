package com.vassdeniss.brickview.ui.register;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vassdeniss.brickview.BottomNavigationHelper;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.databinding.FragmentRegisterBinding;
import com.vassdeniss.brickview.ui.LoggedInUserView;

public class RegisterFragment extends Fragment {
    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        final EditText usernameEditText = this.binding.username;
        final EditText emailEditText = this.binding.email;
        final EditText passwordEditText = this.binding.password;
        final EditText repeatPasswordEditText = this.binding.repeatPassword;
        final Button registerButton = this.binding.register;
        final ProgressBar loadingProgressBar = this.binding.loading;

        this.registerViewModel.getRegisterFormState().observe(this.getViewLifecycleOwner(), registerFormState -> {
            if (registerFormState == null) {
                return;
            }

            registerButton.setEnabled(registerFormState.isDataValid());

            if (registerFormState.getUsernameError() != null) {
                usernameEditText.setError(this.getString(registerFormState.getUsernameError()));
            }

            if (registerFormState.getEmailError() != null) {
                emailEditText.setError(this.getString(registerFormState.getEmailError()));
            }

            if (registerFormState.getPasswordError() != null) {
                passwordEditText.setError(this.getString(registerFormState.getPasswordError()));
            }

            if (registerFormState.getRepeatPasswordError() != null) {
                repeatPasswordEditText.setError(this.getString(registerFormState.getRepeatPasswordError()));
            }
        });

        this.registerViewModel.getResult().observe(getViewLifecycleOwner(), registerResult -> {
            if (registerResult == null) {
                return;
            }

            registerButton.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.GONE);
            if (registerResult.getError() != null) {
                this.showRegisterFailed(registerResult.getError());
            }

            if (registerResult.getSuccess() != null) {
                this.updateUiWithUser(registerResult.getSuccess());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(
                        usernameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        repeatPasswordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        repeatPasswordEditText.addTextChangedListener(afterTextChangedListener);
        registerButton.setOnClickListener(v -> {
            registerButton.setVisibility(View.INVISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
            registerViewModel.register(usernameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    repeatPasswordEditText.getText().toString(),
                    requireContext());
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + " " + model.getDisplayName();
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }

        findNavController(this).navigate(R.id.action_register_to_profile);
        BottomNavigationHelper.updateNav(requireActivity());
    }

    private void showRegisterFailed(String errorString) {
        if (this.getContext() != null && this.getContext().getApplicationContext() != null) {
            Toast.makeText(
                    this.getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
