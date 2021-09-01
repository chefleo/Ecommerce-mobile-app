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
import android.widget.TextView;
import android.widget.Toast;

import com.example.simonedigiorgio.ecommerce.AESCrypt;
import com.example.simonedigiorgio.ecommerce.Admin.AdminHomeActivity;
import com.example.simonedigiorgio.ecommerce.Model.Users;
import com.example.simonedigiorgio.ecommerce.Prevalent.Prevalent;
import com.example.simonedigiorgio.ecommerce.R;
import com.example.simonedigiorgio.ecommerce.Sellers.SellerHomeActivity;
import com.example.simonedigiorgio.ecommerce.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

	private Button joinNowButton, loginButton;
	private ProgressDialog loadingBar;
	private TextView sellerBegin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		joinNowButton = findViewById(R.id.main_join_now_btn);
		loginButton = findViewById(R.id.main_login_btn);
		sellerBegin = findViewById(R.id.seller_begin);
		loadingBar = new ProgressDialog(this);


		Paper.init(this);

		sellerBegin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
				startActivity(intent);
			}
		});

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		joinNowButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
		String UserPassword = Paper.book().read(Prevalent.UserPassword);
		String parentDbName = Paper.book().read(Prevalent.parentDbName);

		if (UserPhoneKey != "" && UserPassword != "") {
			if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPassword)) {
				AllowAccess(UserPhoneKey, UserPassword, parentDbName);

				loadingBar.setTitle("Already Logged In");
				loadingBar.setMessage("Please wait..");
				loadingBar.setCanceledOnTouchOutside(false);
				loadingBar.show();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

		if(firebaseUser != null) {
			Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			finish();
		}
	}

	private void AllowAccess(final String phone, final String password, final String parentDbName) {

		final DatabaseReference RootRef;
		RootRef = FirebaseDatabase.getInstance().getReference();

		RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if(dataSnapshot.child(parentDbName).child(phone).exists()){

					Users usersdata = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

					if (parentDbName.equals("Admins")){

						if(usersdata.getPhone().equals(phone)){
							if(usersdata.getPassword().equals(password)){
								Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
								loadingBar.dismiss();

								Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
								Prevalent.currentOnlineUser = usersdata;
								startActivity(intent);
							}
							else {
								loadingBar.dismiss();
								Toast.makeText(MainActivity.this, "Password is incorrent.", Toast.LENGTH_SHORT).show();
							}
						}

					} else if (parentDbName.equals("Users")) {
						try {
							String decryptpass = AESCrypt.decrypt(usersdata.getPassword());

							if (usersdata.getPhone().equals(phone)) {
								if (password.equals(decryptpass)) {
									Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
									loadingBar.dismiss();

									Intent intent = new Intent(MainActivity.this, HomeActivity.class);
									Prevalent.currentOnlineUser = usersdata;
									Log.d("AAAAA", Prevalent.currentOnlineUser.getName());
									startActivity(intent);
								}
							} else {
								loadingBar.dismiss();
								Toast.makeText(MainActivity.this, "Password is incorrent.", Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				} else {
					Toast.makeText(MainActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
					loadingBar.dismiss();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

}
