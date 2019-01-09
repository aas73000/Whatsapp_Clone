package com.example.administrator.whatsapp_clone;

import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rohanpeshkar.helper.HelperActivity;

public class UsersList extends HelperActivity {
    private ArrayAdapter arrayAdapter;
    private ArrayList arrayList;

    @BindView(R.id.usersListView)ListView listView;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_users_list;
    }

    @Override
    protected boolean isToolbarPresent() {
        return false;
    }

    @Override
    protected void create() {
        ButterKnife.bind(this);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        getNamesOfAllUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getNamesOfAllUsers(){
        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
        parseUserParseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseUserParseQuery.orderByDescending("username");
        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects != null && objects.size()>0 && e == null){
                    for(ParseUser user:objects){
                        arrayList.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                }else{
                    showToast(e.getMessage()+"");
                }
            }
        });
    }
}
