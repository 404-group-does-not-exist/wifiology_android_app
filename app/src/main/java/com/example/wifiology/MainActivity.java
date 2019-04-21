package com.example.wifiology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button login,register,loginSwitch,registerSwitch;
    EditText username,password,usernameReg,passwordReg,emailReg;
    TextView status, statusReg;
    ConstraintLayout loginLayout,registerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginSwitch = findViewById(R.id.loginSwitch);
        registerSwitch = findViewById(R.id.registerSwitch);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        loginSwitch.setOnClickListener(this);
        registerSwitch.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

        loginLayout = findViewById(R.id.loginLayout);
        registerLayout = findViewById(R.id.registerLayout);

        username = findViewById(R.id.usernameFieldText);
        password = findViewById(R.id.passwordFieldText);
        usernameReg = findViewById(R.id.usernameFieldTextReg);
        passwordReg = findViewById(R.id.passwordFieldTextReg);
        emailReg = findViewById(R.id.emailFieldTextReg);

        statusReg = findViewById(R.id.statusReg);
        status = findViewById(R.id.status);

        clearFields();
        setStatus("",false);

        AsyncHTTPClient.setup(this);
        attemptLogin();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.login:
                login();
                break;
            case R.id.register:
                registerUser();
                break;
            case R.id.loginSwitch:
                loginLayout.setVisibility(View.VISIBLE);
                registerLayout.setVisibility(View.GONE);
                clearFields();
                break;
            case R.id.registerSwitch:
                loginLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
                clearFields();
                break;
            default:
                break;
        }
    }

    void attemptLogin(){
        String username = SharedPrefs.getString(SharedPrefs.username,getApplicationContext());
        String password = SharedPrefs.getString(SharedPrefs.password,getApplicationContext());

        if (!username.equals("") && !password.equals("")){
            login(username,password);
        }else{
            loginLayout.setVisibility(View.VISIBLE);
        }
    }

    void clearFields(){
        username.setText("");
        password.setText("");
        usernameReg.setText("");
        passwordReg.setText("");
        emailReg.setText("");
    }

    void setStatus(String msg, boolean isGood){
        status.setText(msg);
        statusReg.setText(msg);
        int colG = getResources().getColor(android.R.color.holo_green_dark);
        int colR = getResources().getColor(android.R.color.holo_red_dark);
        if (isGood){
            status.setTextColor(colG);
            statusReg.setTextColor(colG);
        }else{
            status.setTextColor(colR);
            statusReg.setTextColor(colR);
        }
    }

    void registerUser(){
        statusReg.setText("");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("emailAddress",emailReg.getText().toString());
        params.put("userName",usernameReg.getText().toString());
        params.put("password",passwordReg.getText().toString());
        params.put("description","In app registration!");
        AsyncHTTPClient.postString("users", params,false, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setStatus("Registration Successful, Logging in...",true);
                login(usernameReg.getText().toString(),passwordReg.getText().toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setStatus("Error " + "" + ": " + error.toString(),false);
            }
        });
    }

    void login(){
        login(username.getText().toString(), password.getText().toString());
    }

    void login(final String username, final String password){
        status.setText("");
        final Context context = getApplicationContext();
        AsyncHTTPClient.auth(username, password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setStatus("Login Successful!",true);
                SharedPrefs.putString(SharedPrefs.username,username,context);
                SharedPrefs.putString(SharedPrefs.password,password,context);

                Intent intent = new Intent(context, DataActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setStatus("Error " + "" + ": " + error.toString(),false);

                if (loginLayout.getVisibility() == View.GONE && loginLayout.getVisibility() == View.GONE){
                    loginLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
