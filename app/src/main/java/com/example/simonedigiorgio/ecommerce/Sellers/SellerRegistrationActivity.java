package com.example.simonedigiorgio.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simonedigiorgio.ecommerce.Buyers.MainActivity;
import com.example.simonedigiorgio.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

	private Button sellerLoginBegin, registerButton;
	private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
	private ProgressDialog loadingBar;

	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_registration);

		mAuth = FirebaseAuth.getInstance();
		loadingBar = new ProgressDialog(this);

		sellerLoginBegin = findViewById(R.id.seller_already_have_account_btn);
		registerButton = findViewById(R.id.seller_register_btn);
		nameInput = findViewById(R.id.seller_name);
		phoneInput = findViewById(R.id.seller_phone);
		emailInput = findViewById(R.id.seller_email);
		passwordInput = findViewById(R.id.seller_password);
		addressInput = findViewById(R.id.seller_address);

		sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
				startActivity(intent);
			}
		});

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("Entra: ", "Entra");
				registerSeller();
			}
		});
	}

	private void registerSeller() {

		final String name = nameInput.getText().toString();
		final String phone = phoneInput.getText().toString();
		final String email = emailInput.getText().toString();
		String password = passwordInput.getText().toString();
		final String address = addressInput.getText().toString();


		if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("")) {

			if(password.length() >= 6) {

				loadingBar.setTitle("Create Seller Account");
				loadingBar.setMessage("Please wait, while we are checking the credentials.");
				loadingBar.setCanceledOnTouchOutside(false);
				loadingBar.show();

				Log.e("Entra: ", "Entra2");

				mAuth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {

							if(task.isSuccessful()){
								final DatabaseReference rootRef;
								rootRef = FirebaseDatabase.getInstance().getReference();

								Log.e("Entra: ", "Entra3");

								String sid = mAuth.getCurrentUser().getUid();

								HashMap<String, Object> sellerMap = new HashMap<>();
								sellerMap.put("sid", sid);
								sellerMap.put("phone", phone);
								sellerMap.put("email", email);
								sellerMap.put("address", address);
								sellerMap.put("name", name);

								Log.e("Entra: ", "Entra4");

								rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {

											loadingBar.dismiss();
											Toast.makeText(SellerRegistrationActivity.this, "you are Registered Successfully.", Toast.LENGTH_SHORT).show();

											Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
											startActivity(intent);
											finish();
										}
									});
								}
							}
						});
					} else {
						Toast.makeText(this, "The password must be at least 6 characters", Toast.LENGTH_SHORT).show();
						}
				} else {
					Toast.makeText(this, "Please complete the registration form.", Toast.LENGTH_SHORT).show();
				}
	}
}
