package com.example.wifiology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DataActivity extends AppCompatActivity implements View.OnClickListener{

    Button userButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        userButton = findViewById(R.id.userButton);
        userButton.setText(SharedPrefs.getString(SharedPrefs.username,getApplicationContext()));
        userButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.userButton:
                showUserDialog();
                break;
            default:
                break;
        }
    }

    void logOut(){
        SharedPrefs.putString(SharedPrefs.username,"",getApplicationContext());
        SharedPrefs.putString(SharedPrefs.password,"",getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    void showUserDialog(){
        PopupMenu popup = new PopupMenu(DataActivity.this, userButton);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.popupLogOut:
                        logOut();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        popup.show();
    }
}
