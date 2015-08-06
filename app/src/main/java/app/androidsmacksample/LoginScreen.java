package app.androidsmacksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.greenrobot.event.EventBus;

/*
* LoginScreen is the main screen which a user will see on startup.
* It contains the following:
*   - Username edit text
*   - Password edit text
*   - Login button
* If the connection and login is successful, 'ChatScreen' is called
* */
public class LoginScreen extends Fragment {

    protected static final String TAG = "LoginScreen";

    private EditText mUsernameText, mPasswordText;
    private Button mLoginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_screen_layout, container, false);

        mUsernameText = (EditText) view.findViewById(R.id.usernameEditText);
        mPasswordText = (EditText) view.findViewById(R.id.passwordEditText);

        mLoginButton = (Button) view.findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We send the login details to our login method.
                // You should also be checking for length and valid input here.
                login(mUsernameText.getText().toString(), mPasswordText.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Called in Android UI's main thread
    // If the login is successful, we initiate the transition to ChatScreen
    public void onEventMainThread(LoggedInEvent event) {
        if(event.isSuccessful()) {
            Log.e(TAG, "Successful login");

            ChatScreen chatScreen = new ChatScreen();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, chatScreen);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Log.e(TAG, "Unable to enter");
        }
    }

    // We start the service via startService and pass the username and password along
    public void login(String username, String password) {
        Intent mServiceIntent = new Intent(getActivity(), ConnectionManager.class);
        mServiceIntent.putExtra("event", 0);
        mServiceIntent.putExtra("username", username);
        mServiceIntent.putExtra("password", password);
        getActivity().startService(mServiceIntent);
    }
}
