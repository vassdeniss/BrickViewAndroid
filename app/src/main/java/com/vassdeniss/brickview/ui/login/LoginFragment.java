package com.vassdeniss.brickview.ui.login;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vassdeniss.brickview.BottomNavigationHelper;
import com.vassdeniss.brickview.databinding.FragmentLoginBinding;

import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.ui.LoggedInUserView;

public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = FragmentLoginBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = this.binding.username;
        final EditText passwordEditText = this.binding.password;
        final Button loginButton = this.binding.login;
        final ProgressBar loadingProgressBar = this.binding.loading;

        this.loginViewModel.getLoginFormState().observe(this.getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null) {
                return;
            }

            loginButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(this.getString(loginFormState.getUsernameError()));
            }

            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(this.getString(loginFormState.getPasswordError()));
            }
        });

        this.loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            if (loginResult == null) {
                return;
            }

            loginButton.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                this.showLoginFailed(loginResult.getError());
            }

            if (loginResult.getSuccess() != null) {
                this.updateUiWithUser(loginResult.getSuccess());
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), requireContext());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loginButton.setVisibility(View.INVISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(), requireContext());
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + " " + model.getDisplayName();
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }

        findNavController(this).navigate(R.id.action_login_to_profile);
        BottomNavigationHelper.updateNav(getActivity());
    }

    private void showLoginFailed(String errorString) {
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
