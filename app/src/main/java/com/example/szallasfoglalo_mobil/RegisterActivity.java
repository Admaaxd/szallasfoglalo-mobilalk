package com.example.szallasfoglalo_mobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 99;
    private static final String LOG_TAG = RegisterActivity.class.getName();

    EditText etRegName;
    EditText etRegEmail;
    EditText etRegPassword;
    EditText etRegConfirmPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);

        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        etRegName.setText(username);
        etRegPassword.setText(password);

        auth = FirebaseAuth.getInstance();
    }

    private void startBooking(){
        Intent intent = new Intent(this, AccomodationList.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void register_now(View view) {
        String userName = etRegName.getText().toString();
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String confirmPassword = etRegConfirmPassword.getText().toString();

        if (!password.equals(confirmPassword)) {
            Log.e(LOG_TAG, "The two passwords do not match!");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    startBooking();
                } else {
                    Log.d(LOG_TAG, "User wasn't created successfully:", task.getException());
                    Toast.makeText(RegisterActivity.this, "User wasn't created successfully:", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}