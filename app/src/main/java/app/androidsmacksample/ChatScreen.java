package app.androidsmacksample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends ListFragment {

    protected static final String TAG = "ChatScreen";

    private ChatAdapter mChatAdaper;
    private List<ChatItem> mChatList = new ArrayList<>();
    private boolean messageSent = false;

    private EditText mMessageInput, mToUsername;
    private Button mSendButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_screen_layout, container, false);

        mMessageInput = (EditText) view.findViewById(R.id.yourMessage);
        mToUsername = (EditText) view.findViewById(R.id.toUsername);

        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mToUsername.getText().toString(), mMessageInput.getText().toString());
            }
        });

        return view;
    }

    private static ChatScreen inst;
    public static ChatScreen instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
        mChatAdaper = new ChatAdapter(getActivity(), mChatList);
        getListView().setAdapter(mChatAdaper);
    }

    public void updateChatList(String sender, String message) {
        mChatList.add(new ChatItem(sender, message));
        mChatAdaper.notifyDataSetChanged();
    }

    public void sendMessage(String address, String chat_message) {
        updateChatList(ConnectionManager.mConnection.getUser(), chat_message);

        ChatManager chatManager = ChatManager.getInstanceFor(ConnectionManager.mConnection);

        final Chat newChat = chatManager.createChat(address);

        final Message message = new Message();
        message.setBody(chat_message);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    newChat.sendMessage(message);
                    messageSent = true;
                } catch (SmackException.NotConnectedException e) {
                    Log.e(TAG, "Unable to send chat: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(messageSent) {
                    Toast.makeText(getActivity(), "Sent!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Unable to send", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}
