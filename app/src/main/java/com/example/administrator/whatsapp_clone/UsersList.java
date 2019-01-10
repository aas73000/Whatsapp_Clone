package com.example.administrator.whatsapp_clone;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.rohanpeshkar.helper.HelperActivity;
import me.rohanpeshkar.helper.HelperUtils;

public class UsersList extends HelperActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayAdapter arrayAdapter;
    private ArrayList arrayList;

    @BindView(R.id.usersListView)
    ListView listView;
    @BindView(R.id.usersactivitySwipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

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
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        swipeRefreshLayout.setOnRefreshListener(this);
        getNamesOfAllUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(UsersList.this, "Logout successfully", FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS, true).show();
                    launch(SignInActivity.class);
                    finish();
                } else {
                    showToast("error logout" + e.getMessage());
                }
            }
        });
        return super.onOptionsItemSelected(item);
    }

    private void getNamesOfAllUsers() {
        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
        parseUserParseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseUserParseQuery.orderByDescending("username");
        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects != null && objects.size() > 0 && e == null) {
                    for (ParseUser user : objects) {
                        arrayList.add(user.getUsername());

                    }
                    listView.setAdapter(arrayAdapter);
                    HelperUtils.logInfo("arraylist:" + arrayList);
                } else {
                    showToast(e.getMessage() + "");
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if(swipeRefreshLayout.isRefreshing()){
            getUpdatedUserData();
        }
    }
    private void getUpdatedUserData(){
        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
        parseUserParseQuery.whereNotContainedIn("username",arrayList);
        parseUserParseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser());
        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects != null && objects.size()>0 && e == null){
                    for(ParseUser user:objects){
                        arrayList.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
