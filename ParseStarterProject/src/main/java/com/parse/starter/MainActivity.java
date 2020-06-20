
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {
  EditText usernameTxt, passwordTxt;
  Button signBtn;
  TextView loginTxt;
  int a = 0;
  ImageView logoIV;
  LinearLayout backLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram");

    usernameTxt = (EditText) findViewById(R.id.username);
    passwordTxt = (EditText) findViewById(R.id.password);
    signBtn = (Button) findViewById(R.id.signBtn);
    loginTxt = (TextView) findViewById(R.id.textView);
    logoIV = (ImageView) findViewById(R.id.imageView);
    backLayout = (LinearLayout) findViewById(R.id.layout);

    logoIV.setOnClickListener(this);
    backLayout.setOnClickListener(this);
    passwordTxt.setOnKeyListener(this);

    if (ParseUser.getCurrentUser() != null) {
      Intent intent = new Intent(MainActivity.this,UserListActivity.class);
      startActivity(intent);
    }


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  public void btnClicked(View view) {
    if (signBtn.getText().equals("Sign Up")) {
      signUp();
      usernameTxt.setText("");
      passwordTxt.setText("");
    } else {
      login();
      usernameTxt.setText("");
      passwordTxt.setText("");
    }
  }

  public void switchBtns(View view) {
    if (a == 0) {
      signBtn.setText("Login");
      loginTxt.setText("Or sign Up");
      a = 1;
    } else {
      signBtn.setText("Sign Up");
      loginTxt.setText("Or Login");
      a = 0;
    }
  }
  public void signUp() {
    String username = usernameTxt.getText().toString();
    String password = passwordTxt.getText().toString();
    if (username.equals("") || password.equals("")) {
      Toast.makeText(this, "A username and a password are required", Toast.LENGTH_SHORT).show();
    } else {
      ParseUser user = new ParseUser();
      user.setUsername(username);
      user.setPassword(password);
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if (e == null) {
            Intent intent = new Intent(MainActivity.this,UserListActivity.class);
            startActivity(intent);
          } else {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  public void login() {
    String username = usernameTxt.getText().toString();
    String password = passwordTxt.getText().toString();
    if (username.equals("") || password.equals("")) {
      Toast.makeText(this, "A username and a password are required", Toast.LENGTH_SHORT).show();
    } else {
      ParseUser.logInInBackground(username, password, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if (user != null) {
            Intent intent = new Intent(MainActivity.this,UserListActivity.class);
            startActivity(intent);
          } else {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
        btnClicked(view);
    }

    return false;
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.imageView || view.getId() == R.id.layout) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }
}