package com.example.nurhadis.SaveMe.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nurhadis.SaveMe.Model.User;
import com.example.nurhadis.SaveMe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button mRegister;
    private EditText textName;
    private EditText textEmail;
    private EditText textPassword;
    private EditText textPhone;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabaseRef;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        mRegister = (Button) findViewById(R.id.signUp);
        textName = (EditText) findViewById(R.id.fullName);
        textEmail = (EditText) findViewById(R.id.email);
        textPassword = (EditText) findViewById(R.id.password);

        mRegister.setOnClickListener(this);

        //Assign Instance
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    private void registerUser() {
        final String name, email, password, phone;

         name = textName.getText().toString().trim();
         email = textEmail.getText().toString().trim();
         password = textPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email",Toast.LENGTH_SHORT).show();
            //stopping the function execution further;
            return;
        }
        if (TextUtils.isEmpty(name)){
            //email is empty
            Toast.makeText(this, "Please enter Fullname",Toast.LENGTH_SHORT).show();
            //stopping the function execution further;
            return;
        }
        if (TextUtils.isEmpty(password)){
            //email is empty
            Toast.makeText(this, "Please enter password",Toast.LENGTH_SHORT).show();
            //stopping the function execution further;
            return;
        }


        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user is succesfull registered and logged in
                            //we will start the profile activity here
                            //and display toast
                            String key = firebaseAuth.getCurrentUser().getUid();

                            DatabaseReference mCurrent_db = mDatabaseRef.child(key);

                            User user = new User();

                            user.setEmailUser(email);
                            user.setFullname(name);
                            user.setPassUser(password);
                            user.setIncomes("-");
                            user.setSave("-");
                            user.setExpenses("-");

                            mCurrent_db.setValue(user);

                            Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity( new Intent(SignUp.this, MainActivity.class));

                        }
                        else {

                            Toast.makeText(SignUp.this, "Couldn't register, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

        @Override
    public void onClick(View view) {

            registerUser();
    }
}