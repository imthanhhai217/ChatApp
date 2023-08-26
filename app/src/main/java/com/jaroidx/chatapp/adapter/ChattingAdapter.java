package com.jaroidx.chatapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaroidx.chatapp.R;
import com.jaroidx.chatapp.model.ChatMessage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String mCurrentUid;
    private ArrayList<ChatMessage> mListChatMessages;

    public static final int MESSAGE_SELF = 0;
    public static final int MESSAGE_FRIEND = 1;
    public static final int MESSAGE_LOG = 2;
    public static final int MESSAGE_TYPING = 3;

    public ChattingAdapter(ArrayList<ChatMessage> mListChatMessages, String currentUid) {
        this.mListChatMessages = mListChatMessages;
        this.mCurrentUid = currentUid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_SELF) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chat_self, parent, false);
            return new SelfMessageViewHolder(view);
        } else if (viewType == MESSAGE_FRIEND) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chat_message_friend, parent, false);
            return new FriendMessageViewHolder(view);
        } else if (viewType == MESSAGE_LOG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chat_log, parent, false);
            return new LogMessageViewHolder(view);
        } else if (viewType == MESSAGE_TYPING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chat_typing, parent, false);
            return new TypingMessageViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mListChatMessages.get(position).getMessageType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = mListChatMessages.get(position);
        if (getItemViewType(position) == MESSAGE_SELF) {
            SelfMessageViewHolder selfMessageViewHolder = (SelfMessageViewHolder) holder;
            selfMessageViewHolder.tvMessage.setText(chatMessage.getMessage());
        } else if (getItemViewType(position) == MESSAGE_FRIEND) {
            FriendMessageViewHolder friendMessageViewHolder = (FriendMessageViewHolder) holder;
            friendMessageViewHolder.tvMessage.setText(chatMessage.getMessage());
        } else if (getItemViewType(position) == MESSAGE_LOG) {
            LogMessageViewHolder logMessageViewHolder = (LogMessageViewHolder) holder;
            logMessageViewHolder.tvMessage.setText(chatMessage.getMessage());
        } else if (getItemViewType(position) == MESSAGE_TYPING) {
            TypingMessageViewHolder typingMessageViewHolder = (TypingMessageViewHolder) holder;
            typingMessageViewHolder.tvMessage.setText(chatMessage.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mListChatMessages.size();
    }

    public void removeItem(String userId) {
        for (int i = mListChatMessages.size() - 1; i >= 0; i--) {
            ChatMessage message = mListChatMessages.get(i);
            if (message.getMessageType() == ChattingAdapter.MESSAGE_TYPING && message.getUid().equals(userId)) {
                mListChatMessages.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public class SelfMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llMessageRow)
        RelativeLayout llMessageRow;
        @BindView(R.id.llMessage)
        LinearLayout llMessage;
        @BindView(R.id.tvMessage)
        TextView tvMessage;

        public SelfMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FriendMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llMessageRow)
        RelativeLayout llMessageRow;
        @BindView(R.id.llMessage)
        LinearLayout llMessage;
        @BindView(R.id.tvMessage)
        TextView tvMessage;

        public FriendMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public class LogMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMessage)
        TextView tvMessage;

        public LogMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public class TypingMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMessage)
        TextView tvMessage;

        public TypingMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
