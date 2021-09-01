package com.example.simonedigiorgio.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.simonedigiorgio.ecommerce.Buyers.HomeActivity;
import com.example.simonedigiorgio.ecommerce.Buyers.MainActivity;
import com.example.simonedigiorgio.ecommerce.R;
import com.example.simonedigiorgio.ecommerce.Sellers.SellerProductCategoryActivity;

public class AdminHomeActivity extends AppCompatActivity {

	private Button LogoutBtn, CheckOrderBtn, maintainProductsBtn, checkApproveProductsBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home);

		Paper.init(this);

		LogoutBtn = findViewById(R.id.admin_logout_btn);
		CheckOrderBtn = findViewById(R.id.check_orders_btn);
		maintainProductsBtn = findViewById(R.id.maintain_btn);
		checkApproveProductsBtn = findViewById(R.id.check_approve_products_btn);

		maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
				intent.putExtra("Admin", "Admin");
				startActivity(intent);
			}
		});

		LogoutBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Paper.book().destroy();
				Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});

		CheckOrderBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrderActivity.class);
				startActivity(intent);
			}
		});

		checkApproveProductsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewProductsActivity.class);
				startActivity(intent);
			}
		});
	}
}
