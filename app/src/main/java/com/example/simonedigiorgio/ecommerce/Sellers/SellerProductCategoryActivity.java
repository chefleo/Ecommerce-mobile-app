package com.example.simonedigiorgio.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.simonedigiorgio.ecommerce.R;

public class SellerProductCategoryActivity extends AppCompatActivity {

	private ImageView tShirts, sportsTShirts, femaleDresses, sweathers;
	private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
	private ImageView headPhonesHandFree, Laptop, watches, mobilePhones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_product_category);



		tShirts = findViewById(R.id.t_shirts);
		sportsTShirts = findViewById(R.id.sports_t_shirts);
		femaleDresses = findViewById(R.id.female_dresses);
		sweathers = findViewById(R.id.sweathers);

		glasses = findViewById(R.id.glasses);
		hatsCaps = findViewById(R.id.hats_caps);
		walletsBagsPurses = findViewById(R.id.purses_bags_wallet);
		shoes = findViewById(R.id.shoes);

		headPhonesHandFree = findViewById(R.id.headphones_handfree);
		Laptop = findViewById(R.id.laptop_pc);
		watches = findViewById(R.id.watches);
		mobilePhones = findViewById(R.id.mobilephones);


		tShirts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "tShirt");
				startActivity(intent);
			}
		});

		sportsTShirts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "SportTShirts");
				startActivity(intent);
			}
		});

		femaleDresses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Female Dresses");
				startActivity(intent);
			}
		});

		sweathers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Sweathers");
				startActivity(intent);
			}
		});

		glasses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Glasses");
				startActivity(intent);
			}
		});

		hatsCaps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Hats Caps");
				startActivity(intent);
			}
		});

		walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Wallet Bags Purses");
				startActivity(intent);
			}
		});

		shoes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Shoes");
				startActivity(intent);
			}
		});

		headPhonesHandFree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "HeadPhones HandFree");
				startActivity(intent);
			}
		});

		Laptop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Laptop");
				startActivity(intent);
			}
		});

		watches.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Watches");
				startActivity(intent);
			}
		});

		mobilePhones.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
				intent.putExtra("category", "Mobile Phones");
				startActivity(intent);
			}
		});

	}
}
