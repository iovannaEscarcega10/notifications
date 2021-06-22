package com.example.notificaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    private EditText txt_name,  txt_email, txt_password;
    private Button btn_signIn;
    private String name = "", email = "", password = "";

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_sign_in);
        txt_name = (EditText) findViewById(R.id.name);
        txt_email = (EditText) findViewById(R.id.email);
        txt_password = (EditText) findViewById(R.id.password);

        btn_signIn = findViewById(R.id.btn_sign_in);
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txt_name.getText().toString();
                email = txt_email.getText().toString();
                password = txt_password.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    if (password.length() >= 6){
                        registerUser();
                    }
                    else {
                        Toast.makeText(SignIn.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignIn.this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = firebaseAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);
                    databaseReference.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Intent intent = new Intent(SignIn.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(SignIn.this, "No se pudo crear el perfil", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignIn.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}