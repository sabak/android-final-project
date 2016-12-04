package com.saba.bogchat.view;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.saba.bogchat.App;
import com.saba.bogchat.R;
import com.saba.bogchat.db.LocalDbHelper;
import com.saba.bogchat.db.manager.ContactManager;
import com.saba.bogchat.db.manager.MessageManager;
import com.saba.bogchat.model.Contact;
import com.saba.bogchat.model.Message;
import com.saba.bogchat.model.storage.ContactStorage;
import com.saba.bogchat.model.storage.MessageStorage;
import com.saba.bogchat.transport.ChatEventListener;
import com.saba.bogchat.view.fragment.ContactsFragment;
import com.saba.bogchat.view.fragment.RecentChatsFragment;
import com.saba.bogchat.view.listview.adapter.ChatCursorAdapter;

import java.util.Date;

public class ChatActivity extends AppCompatActivity implements ChatEventListener {
    private LocalDbHelper mHelper;
    private MessageStorage mMessageStorage;
    private ChatCursorAdapter mAdapter;
    private ListView mListView;
    private EditText mMessageText;
    private Contact mMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* Getting chat member's id */
        Intent intent = getIntent();
        String idFromContacts = intent.getStringExtra(ContactsFragment.USER_ID);
        String idFromConversations = intent.getStringExtra(RecentChatsFragment.USER_ID);
        final String userId = idFromContacts == null ? idFromConversations : idFromContacts;

        /* Starting listening to incoming messages */
        App.getInstance().getChatTransport().addChatEventListener(this);

        /* Caching UI elements */
        mListView = (ListView) findViewById(R.id.list_view_chat);
        mMessageText = (EditText) findViewById(R.id.edit_text_msg);
        Button sendButton = (Button) findViewById(R.id.button_send);

        /* Creating storage helpers */
        mHelper = new LocalDbHelper(this);
        ContactStorage contactStorage = new ContactManager(mHelper);
        mMessageStorage = new MessageManager(mHelper);
        mMember = contactStorage.getContact(userId);
        mMessageStorage.markAsRead(userId);

        /* Creating and setting cursor adapter */
        Cursor cursor = mHelper.getChat(userId);
        mAdapter = new ChatCursorAdapter(this, cursor, mMember);
        mListView.setAdapter(mAdapter);

        /* Setting listeners on edittext and buttons */
        mMessageText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String message = mMessageText.getText().toString();
                    sendMessage(message);
                    return true;
                }
                return false;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String message = mMessageText.getText().toString();
                sendMessage(message);
            }
        });

        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //to scroll the list view to bottom on data change
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(mMember.getDisplayName());
    }

    private void sendMessage(String message) {
        if (message.isEmpty())
            return;
        Message msg = new Message(mMember.getUserId(), message,
                new Date(System.currentTimeMillis()), false, false);
        mMessageText.getText().clear();
        mMessageStorage.storeMessage(msg);
        signalList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().getChatTransport().removeChatEventListener(this);
    }

    @Override
    public void onIncomingMsg(Message m) {
        mMessageStorage.markAsRead(m.getMemberId());
        signalList();
    }

    @Override
    public void onOutgoingMsg(Message m) {
        signalList();
    }

    @Override
    public void onStatusChanged(long contactId, boolean isOnline) {
    }

    public void signalList() {
        Cursor newCursor = mHelper.getChat(mMember.getUserId());
        mAdapter.changeCursor(newCursor);
    }

}
