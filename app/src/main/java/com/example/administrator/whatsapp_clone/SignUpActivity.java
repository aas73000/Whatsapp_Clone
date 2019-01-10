package com.example.administrator.whatsapp_clone;


import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.tripl3dev.prettystates.StateExecuterKt;
import com.tripl3dev.prettystates.StatesConfigFactory;
import com.tripl3dev.prettystates.StatesConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rohanpeshkar.helper.HelperActivity;
import me.rohanpeshkar.helper.HelperUtils;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

//public class ParseErrorHandler{
//    public static void handleParseError(ParseException e){
//        if(e.getCode() == ParseException.INVALID_SESSION_TOKEN){
//            handleInvalidSessionToken();
//        }
//    }
//    private static void handleInvalidSessionToken(){
//        startActivityForResult(new Parse)
//    }
//}


public class SignUpActivity extends HelperActivity implements View.OnClickListener {

    @BindView(R.id.signupEmail)
    ExtendedEditText email;
    @BindView(R.id.signupUserName)
    ExtendedEditText userName;
    @BindView(R.id.signupPassword)
    ExtendedEditText password;
    @BindView(R.id.signupactivitySignUpButton)
    Button signup;
    @BindView(R.id.signupactivitySiginButton)
    Button signin;


    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected boolean isToolbarPresent() {
        return false;
    }

    @Override
    protected void create() {
        ButterKnife.bind(this);
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        HelperUtils.logInfo(signin.getText() + " email123");
// Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();


    }

    @Override
    public void onClick(View v) {
        HelperUtils.logInfo("listiner working");
        switch (v.getId()) {
            case R.id.signupactivitySignUpButton:
                StatesConfigFactory.Companion.intialize().initDefaultViews();
                StatesConfigFactory.Companion.intialize().addStateView(1, R.layout.activity_sign_up);
                StateExecuterKt.setState(v, StatesConstants.LOADING_STATE);
                singningUpTheUserToServer(v);
                HelperUtils.logInfo("inside case");
                break;
            case R.id.signupactivitySiginButton:
                launch(SignInActivity.class);
                finish();
                break;
        }
    }

    private void singningUpTheUserToServer(View v) {
        ParseUser parseUser = new ParseUser();
        parseUser.setEmail(email.getText().toString());
        parseUser.setUsername(userName.getText().toString());
        parseUser.setPassword(password.getText().toString());
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    launch(UsersList.class);
                    FancyToast.makeText(SignUpActivity.this, "Sign up successfully ", FancyToast.LENGTH_LONG
                            , FancyToast.SUCCESS, true).show();
                } else {
                    showToast("error signing" + e.getMessage());
                }
                StateExecuterKt.setState(v, StatesConstants.NORMAL_STATE);
            }
        });
    }

}
