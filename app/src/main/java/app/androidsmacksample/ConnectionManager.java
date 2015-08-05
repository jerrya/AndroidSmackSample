package app.androidsmacksample;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class ConnectionManager extends Service {

    protected static final String TAG = "ConnectionManager";

    protected static final String SERVICE_NAME = "";
    protected static final String HOST_NAME = "";

    public static AbstractXMPPConnection mConnection;
    private XMPPTCPConnectionConfiguration mConnectionConfiguration;

    boolean startConnected = false;

    private OnLoggedIn mLoggedIn;
    public void onLoggedIn(OnLoggedIn mCallBack) {
        this.mLoggedIn = mCallBack;
    }

    private final IBinder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder {
        ConnectionManager mService() {
            return ConnectionManager.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return START_STICKY;
    }

    protected void onHandleIntent(Intent intent) {

        if(intent == null) {
            Log.e(TAG, "Stopped service");
            return;
        }

        int event = intent.getIntExtra("event", 1);
        switch(event) {
            // login
            case 0:
                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");

                if(username != null && password != null) {
                    startLogin(username, password);
                }
                break;
            default:
                disconnect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void startLogin(final String username, final String password) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                mConnectionConfiguration = XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(username, password)
                        .setServiceName(SERVICE_NAME)
                        .setHost(HOST_NAME)
                        .setPort(5222)
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .build();

                mConnection = new XMPPTCPConnection(mConnectionConfiguration);

                try {
                    mConnection.connect();
                    startConnected = true;
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }

                if(startConnected) {
                    connectionLogin(username, password);
                    Log.e(TAG, "Connected");
                } else {
                    Log.e(TAG, "Unable to connect");
                }

            }
        }).start();
    }

    boolean loggedIn = true;
    public void connectionLogin(final String username, final String password) {

        try {
            mConnection.login();
        } catch (Exception e) {
            loggedIn = false;
        }

        if(!loggedIn) {
            Log.e(TAG, "Unable to login");

            disconnect();
            loggedIn = true;
        } else {
            createChatListener();
            // interface callback
//            EventBus.getDefault().post(new MessageEvent(true));
            Log.e(TAG, "Logged in");
        }
    }

    private MyChatMessageListener mChatMessageListener;
    public void createChatListener() {
        if(mConnection != null) {
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            chatManager.setNormalIncluded(false);
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    if (!createdLocally) {
                        mChatMessageListener = new MyChatMessageListener();
                        chat.addMessageListener(mChatMessageListener);
                        Log.e(TAG, "ChatListener created");
                    }
                }
            });
        }
    }

    public void disconnect() {
        if(mConnection != null && mConnection.isConnected()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    mConnection.disconnect();
                    Log.e(TAG, "Connection disconnected");
                    return null;
                }
            }.execute();
        }
    }
}
