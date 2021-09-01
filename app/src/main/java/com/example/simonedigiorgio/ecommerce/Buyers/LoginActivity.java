package com.example.simonedigiorgio.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simonedigiorgio.ecommerce.AESCrypt;
import com.example.simonedigiorgio.ecommerce.Admin.AdminHomeActivity;
import com.example.simonedigiorgio.ecommerce.Sellers.SellerProductCategoryActivity;
import com.example.simonedigiorgio.ecommerce.Model.Users;
import com.example.simonedigiorgio.ecommerce.Prevalent.Prevalent;
import com.example.simonedigiorgio.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

	private EditText InputNumber, InputPassword;
	private Button LoginButton;
	private ProgressDialog loadingBar;
	private TextView AdminLink, NotAdminLink, ForgetPasswordLink;


	private String parentDbName = "Users";
	private CheckBox chkBoxRememberMe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		LoginButton = findViewById(R.id.login_btn);
		InputPassword = findViewById(R.id.login_password_input);
		InputNumber = findViewById(R.id.login_phone_number_number);
		AdminLink = findViewById(R.id.admin_panel_link);
		NotAdminLink = findViewById(R.id.not_admin_panel_link);
		ForgetPasswordLink = findViewById(R.id.forget_password_link);
		loadingBar = new ProgressDialog(this);

		chkBoxRememberMe = findViewById(R.id.remember_me_chkb);

		Paper.init(this);

		LoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LoginUser();
			}
		});

		ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
				intent.putExtra("check", "login");
				startActivity(intent);
			}
		});

		AdminLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LoginButton.setText("Login Admin");
				AdminLink.setVisibility(View.INVISIBLE);
				NotAdminLink.setVisibility(View.VISIBLE);
				parentDbName = "Admins";
			}
		});

		NotAdminLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LoginButton.setText("Login");
				AdminLink.setVisibility(View.VISIBLE);
				NotAdminLink.setVisibility(View.INVISIBLE);
				parentDbName = "Users";
			}
		});

	}

	private void LoginUser()
	{
		String phone = InputNumber.getText().toString();
		String password = InputPassword.getText().toString();

		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
		}
		else if(TextUtils.isEmpty(password)){
			Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
		} else {
			loadingBar.setTitle("Login Account");
			loadingBar.setMessage("Please wait, while we are checking the credentials.");
			loadingBar.setCanceledOnTouchOutside(false);
			loadingBar.show();

			AllowAccessToAccount(phone, password);

		}
	}

	private void AllowAccessToAccount(final String phone, final String password){

		if(chkBoxRememberMe.isChecked()) {

			Paper.book().write(Prevalent.UserPhoneKey, phone);
			Paper.book().write(Prevalent.UserPassword, password);

			if (parentDbName.equals("Admins")){
				Paper.book().write(Prevalent.parentDbName, parentDbName);
			} else if (parentDbName.equals("Users")) {
				Paper.book().write(Prevalent.parentDbName, parentDbName);
			}
		}

		final DatabaseReference RootRef;
		RootRef = FirebaseDatabase.getInstance().getReference();

		RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if(dataSnapshot.child(parentDbName).child(phone).exists()) {
					Users usersdata = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

					if (parentDbName.equals("Admins")) {
						if (usersdata.getPhone().equals(phone)) {
							if (usersdata.getPassword().equals(password)) {
								Toast.makeText(LoginActivity.this, "Welcome Admin,you are logged in Successfully...", Toast.LENGTH_SHORT).show();
								loadingBar.dismiss();

								Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
								startActivity(intent);
							}
						}
					} else if (parentDbName.equals("Users")) {

						try {
							String decryptpass = AESCrypt.decrypt(usersdata.getPassword());

							if (usersdata.getPhone().equals(phone)) {
								if (password.equals(decryptpass)) {
									Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
									loadingBar.dismiss();

									Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
									Prevalent.currentOnlineUser = usersdata;
									Log.d("AAAAA", Prevalent.currentOnlineUser.getName());
									startActivity(intent);
								}
							} else {
								loadingBar.dismiss();
								Toast.makeText(LoginActivity.this, "Password is incorrent.", Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
					loadingBar.dismiss();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}
}
