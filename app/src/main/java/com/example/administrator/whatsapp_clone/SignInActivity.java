package com.example.administrator.whatsapp_clone;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.tripl3dev.prettystates.StateExecuterKt;
import com.tripl3dev.prettystates.StatesConfigFactory;
import com.tripl3dev.prettystates.StatesConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rohanpeshkar.helper.HelperActivity;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class SignInActivity extends HelperActivity implements View.OnClickListener{
    @BindView(R.id.signinactivityUserName)ExtendedEditText username;
    @BindView(R.id.signinactivityPassword)ExtendedEditText password;
    @BindView(R.id.signinactivitySignInButton)Button signin;
    @BindView(R.id.signinactivitySignUpButton)Button signup;


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected boolean isToolbarPresent() {
        return false;
    }

    @Override
    protected void create() {
        ButterKnife.bind(this);
        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signinactivitySignInButton:
                StatesConfigFactory.Companion.intialize().initDefaultViews();
                StatesConfigFactory.Companion.intialize().addStateView(1,R.layout.activity_sign_in);
                StateExecuterKt.setState(v,StatesConstants.LOADING_STATE);
                checkingLogCreditinals(v);
                break;
            case R.id.signinactivitySignUpButton:
                launch(SignUpActivity.class);
                finish();
                break;
        }

    }

    private void checkingLogCreditinals(View v) {
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null && e== null){
                    launch(UsersList.class);
                    FancyToast.makeText(SignInActivity.this,username.getText()+" Login successsfully",FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,true).show();
                }else{
                    showToast("error:"+e.getMessage());
                }
                StateExecuterKt.setState(v,StatesConstants.NORMAL_STATE);
            }
        });
    }
}
