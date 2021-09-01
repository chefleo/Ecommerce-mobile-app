package com.example.simonedigiorgio.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simonedigiorgio.ecommerce.Interface.ItemClickListener;
import com.example.simonedigiorgio.ecommerce.Model.Products;
import com.example.simonedigiorgio.ecommerce.R;
import com.example.simonedigiorgio.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProductsActivity extends AppCompatActivity {

	private RecyclerView recyclerView;
	RecyclerView.LayoutManager layoutManager;
	private DatabaseReference unverifiedProductRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_check_new_products);

		unverifiedProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

		recyclerView = findViewById(R.id.admin_products_checklist);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
	}

	@Override
	protected void onStart() {
		super.onStart();

		FirebaseRecyclerOptions<Products> options =
			new FirebaseRecyclerOptions.Builder<Products>()
			.setQuery(unverifiedProductRef.orderByChild("productState").equalTo("Not Approved"), Products.class)
				.build();

		FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
			new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
				@Override
				protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

					holder.txtProductName.setText(model.getPname());
					holder.txtProductDescription.setText(model.getDescription());
					holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
					Picasso.get().load(model.getImage()).into(holder.imageView);

					holder.itemView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							final String productID = model.getPid();

							CharSequence options[] = new CharSequence[]
								{
									"Yes",
									"No"
								};

							AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewProductsActivity.this);
							builder.setTitle("Do you want to Approved this Product. Are you Sure?");
							builder.setItems(options, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int position ) {
									if (position == 0){
										ChangeProductState(productID);
									}
									if (position == 1) {

									}
								}
							});
							builder.show();
						}
					});

				}

				@NonNull
				@Override
				public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
					View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
					ProductViewHolder holder = new ProductViewHolder(view);
					return holder;
				}
			};

		recyclerView.setAdapter(adapter);
		adapter.startListening();
	}

	private void ChangeProductState(String productID) {
		unverifiedProductRef.child(productID)
			.child("productState")
			.setValue("Approved")
			.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				Toast.makeText(AdminCheckNewProductsActivity.this,"That item has been approved, and it is now available for sale from the seller.", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
