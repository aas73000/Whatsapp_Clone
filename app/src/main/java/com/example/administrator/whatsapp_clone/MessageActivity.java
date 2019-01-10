package com.example.administrator.whatsapp_clone;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rohanpeshkar.helper.HelperActivity;
import me.rohanpeshkar.helper.HelperUtils;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class MessageActivity extends HelperActivity implements View.OnClickListener {

    @BindView(R.id.messageListView)
    ListView listView;
    @BindView(R.id.messageactivityMeassage)
    ExtendedEditText message;
    @BindView(R.id.messageactivitySend)
    Button sendButton;

    private ArrayList arrayList;
    private ArrayAdapter arrayAdapter;
    private String sender,receiver;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean isToolbarPresent() {
        return false;
    }

    @Override
    protected void create() {
        sender = ParseUser.getCurrentUser().getUsername();
        receiver = UsersList.getReceiverName();
        setTitle(receiver);
        ButterKnife.bind(this);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        retrieveSendingAndReceiverMessages();
        listView.setAdapter(arrayAdapter);
        sendButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        arrayList.add(ParseUser.getCurrentUser().getUsername()+":"+message.getText().toString());
        ParseObject parseObject = new ParseObject("Message");
        try {
            parseObject.put("wa_sender",sender);
        } catch (Exception e) {
            HelperUtils.logInfo("Sender"+e.getMessage());
        }
        try {
            parseObject.put("message",message.getText().toString());
        } catch (Exception e) {
            HelperUtils.logInfo("Message"+e.getMessage());
        }
        try {
            parseObject.put("wa_receiver",receiver);
        } catch (Exception e) {
            HelperUtils.logInfo("receiver"+e.getMessage());
        }
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    FancyToast.makeText(MessageActivity.this,"Message send",FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,true).show();
                    arrayAdapter.notifyDataSetChanged();
                }else{
                    showToast("error:"+e.getMessage());
                }

            }
        });
    }

    private void retrieveSendingAndReceiverMessages() {
        ParseQuery<ParseObject> senderMessages = ParseQuery.getQuery("Message");
        ParseQuery<ParseObject> receiverMessages = ParseQuery.getQuery("Message");
        senderMessages.whereEqualTo("wa_sender",sender).whereEqualTo("wa_receiver",receiver);
        receiverMessages.whereEqualTo("wa_sender",receiver).whereEqualTo("wa_receiver",sender);
        ArrayList<ParseQuery<ParseObject>> parseQueries = new ArrayList<>();
        parseQueries.add(senderMessages);
        parseQueries.add(receiverMessages);
        ParseQuery<ParseObject> finalMessages = ParseQuery.or(parseQueries);
        finalMessages.orderByAscending("createdAt");
        finalMessages.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects != null && objects.size()>0 && e == null){
                    for(ParseObject parseObject1:objects){
                        String waMessage = parseObject1.get("message")+"";
                        if(parseObject1.get("wa_sender").toString() == sender){
                            waMessage = sender+":"+waMessage;
                        }else {
                            waMessage = receiver+":"+waMessage;
                        }
                        arrayList.add(waMessage);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

}
