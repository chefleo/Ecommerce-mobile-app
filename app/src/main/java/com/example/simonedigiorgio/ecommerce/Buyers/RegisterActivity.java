package com.example.simonedigiorgio.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simonedigiorgio.ecommerce.AESCrypt;
import com.example.simonedigiorgio.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

	private Button CreateButtonAccount;
	private EditText InputName, InputPhoneNumber, InputPassword;
	private ProgressDialog loadingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		CreateButtonAccount = findViewById(R.id.register_btn);
		InputName = findViewById(R.id.register_username);
		InputPhoneNumber = findViewById(R.id.register_phone_number);
		InputPassword = findViewById(R.id.register_password_input);
		loadingBar = new ProgressDialog(this);

		CreateButtonAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CreateAccount();
			}
		});
	}

	private void CreateAccount(){
		String name = InputName.getText().toString();
		String phone = InputPhoneNumber.getText().toString();
		String password = InputPassword.getText().toString();

		if(TextUtils.isEmpty(name)){
			Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
		}
		else if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
		}
		else if(TextUtils.isEmpty(password)){
			Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
		}
		else {
			loadingBar.setTitle("Create Account");
			loadingBar.setMessage("Please wait, while we are checking the credentials.");
			loadingBar.setCanceledOnTouchOutside(false);
			loadingBar.show();

			ValidatephoneNumber(name, phone, password);
		}
	}

	private void ValidatephoneNumber(final String name, final String phone, final String password){

		final DatabaseReference RootRef;
		RootRef = FirebaseDatabase.getInstance().getReference();

//		String encryptpass = encrypt(password);

		try {
			final String encryptpass = AESCrypt.encrypt(password);

			RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					if(!(dataSnapshot.child("Users").child(phone).exists())) {
						HashMap<String, Object> userdataMap = new HashMap<>();
						userdataMap.put("phone", phone);
						userdataMap.put("password", encryptpass);
						userdataMap.put("name", name);

						RootRef.child("Users").child(phone).updateChildren(userdataMap)
							.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {

									if(task.isSuccessful()) {
										Toast.makeText(RegisterActivity.this, "Congratulation, your account has been created.", Toast.LENGTH_SHORT).show();
										loadingBar.dismiss();

										Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
										startActivity(intent);
									}
									else {
										loadingBar.dismiss();
										Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
									}
								}
							});


					} else {
						Toast.makeText(RegisterActivity.this, "This " +  phone + " already exist",Toast.LENGTH_SHORT).show();
						loadingBar.dismiss();
						Toast.makeText(RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(RegisterActivity.this, MainActivity.class );
						startActivity(intent);
					}

				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private String encrypt(String password) throws Exception{
//		SecretKey key = generateKey(password);
//		Cipher c = Cipher.getInstance("AES");
//		c.init(Cipher.ENCRYPT_MODE, key);
//		byte[] encVal = c.doFinal();
//	}
//
//	private SecretKey generateKey(String password) throws Exception {
//		final MessageDigest digest = MessageDigest.getInstance("SHA-256");
//		byte[] bytes = password.getBytes("UTF-8");
//		digest.update(bytes, 0, bytes.length);
//		byte[] key = digest.digest();
//		SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
//		return secretKeySpec;
//	}
}
