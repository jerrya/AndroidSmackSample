package app.androidsmacksample;

import android.util.Log;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

public class MyChatMessageListener implements ChatMessageListener {

    protected static final String TAG = "MyChatMessageListener";

    @Override
    public void processMessage(Chat chat, Message message) {
        final String mChatSender = message.getFrom();
        final String mChatMessage = message.getBody();

        Log.e(TAG, mChatSender + ": " + mChatMessage);

        MainActivity.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatScreen.instance().updateChatList(mChatSender, mChatMessage);
            }
        });
    }
}
