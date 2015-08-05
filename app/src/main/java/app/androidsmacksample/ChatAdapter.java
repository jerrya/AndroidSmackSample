package app.androidsmacksample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatItem>{

    private static class ViewHolder {
        TextView mChatSender;
        TextView mChatMessage;
    }

    public ChatAdapter(Context context, List<ChatItem> chatList) {
        super(context, 0, chatList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatItem chat = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.chat_item_layout, parent, false);
            viewHolder.mChatSender = (TextView) convertView.findViewById(R.id.list_sender);
            viewHolder.mChatMessage = (TextView) convertView.findViewById(R.id.list_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mChatSender.setText(chat.getSender());
        viewHolder.mChatMessage.setText(chat.getMessage());

        return convertView;
    }
}
