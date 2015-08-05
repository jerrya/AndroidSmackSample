package app.androidsmacksample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends Fragment {

    private EditText mUsernameText, mPasswordText;
    private Button mLoginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_screen_layout, container, false);

        mUsernameText = (EditText) view.findViewById(R.id.usernameEditText);
        mPasswordText = (EditText) view.findViewById(R.id.passwordEditText);

        mLoginButton = (Button) view.findViewById(R.id.loginButton);

        return view;
    }
}
