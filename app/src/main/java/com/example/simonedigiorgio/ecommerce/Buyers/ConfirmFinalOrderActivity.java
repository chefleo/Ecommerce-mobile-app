package com.example.simonedigiorgio.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simonedigiorgio.ecommerce.Prevalent.Prevalent;
import com.example.simonedigiorgio.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

	private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
	private Button confirmOrderBtn;

	private String totalAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_final_order);

		totalAmount = getIntent().getStringExtra("Total Price");
		Toast.makeText(this,"Total Price = $ " + totalAmount, Toast.LENGTH_SHORT).show();

		confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);
		nameEditText = findViewById(R.id.shippment_name);
		phoneEditText = findViewById(R.id.shippment_phone_number);
		addressEditText = findViewById(R.id.shippment_address);
		cityEditText = findViewById(R.id.shippment_city);

		Log.e("Entra", "Entra");
		Log.d("Entra", "Entra");

		confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("Entra", "Entra");
				Check();
			}
		});
	}

	private void Check() {
		if(TextUtils.isEmpty(nameEditText.getText().toString())){
			Toast.makeText(this,"Please provide your full name",Toast.LENGTH_SHORT).show();
		}
		else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
			Toast.makeText(this,"Please provide your phone number",Toast.LENGTH_SHORT).show();
		}
		else if(TextUtils.isEmpty(addressEditText.getText().toString())){
			Toast.makeText(this,"Please provide your address",Toast.LENGTH_SHORT).show();
		}
		else if(TextUtils.isEmpty(cityEditText.getText().toString())){
			Toast.makeText(this,"Please provide city name",Toast.LENGTH_SHORT).show();
		}
		else {
			Log.e("Entra", "Entra2");
			ConfirmOrder();
		}
	}

	private void ConfirmOrder() {

		Log.e("Entra", "Entra2");

		final String saveCurrentDate, saveCurrentTime;

		Calendar callForDate = Calendar.getInstance();
		SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
		saveCurrentDate = currentDate.format(callForDate.getTime());

		SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
		saveCurrentTime = currentDate.format(callForDate.getTime());

		final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
			.child("Orders")
			.child(Prevalent.currentOnlineUser.getPhone());

		Log.e("Entra", "Entra3");

		HashMap<String, Object> orderMap = new HashMap<>();
		orderMap.put("totalAmount", totalAmount);
		orderMap.put("name", nameEditText.getText().toString());
		orderMap.put("phone", phoneEditText.getText().toString());
		orderMap.put("address", addressEditText.getText().toString());
		orderMap.put("city", cityEditText.getText().toString());
		orderMap.put("date", saveCurrentDate);
		orderMap.put("time", saveCurrentTime);
		orderMap.put("state", "not shipped");

		Log.e("Entra", "Entra4");

		ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if(task.isSuccessful()) {
					FirebaseDatabase.getInstance().getReference()
						.child("Cart List")
						.child("User View")
						.child(Prevalent.currentOnlineUser.getPhone())
						.removeValue()
						.addOnCompleteListener(new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
								if(task.isSuccessful()) {
									Toast.makeText(ConfirmFinalOrderActivity.this, "your final order has been placed successfully.", Toast.LENGTH_SHORT).show();

									Log.e("Entra", "Entra5");
									Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Non puoi tornare indietro
									startActivity(intent);
									finish();
								}
							}
						});
				}

			}
		});

	}
}
