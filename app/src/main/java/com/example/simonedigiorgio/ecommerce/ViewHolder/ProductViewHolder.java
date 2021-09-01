package com.example.simonedigiorgio.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simonedigiorgio.ecommerce.Interface.ItemClickListener;
import com.example.simonedigiorgio.ecommerce.R;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

	public TextView txtProductName, txtProductDescription, txtProductPrice;
	public ImageView imageView;
	public ItemClickListener listener;

	public ProductViewHolder(View itemView) {
		super(itemView);

		imageView = itemView.findViewById(R.id.product_image);
		txtProductName = itemView.findViewById(R.id.product_name);
		txtProductDescription = itemView.findViewById(R.id.product_description);
		txtProductPrice = itemView.findViewById(R.id.product_price);
	}

	public void setItemClickListener(ItemClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View view) {
		listener.onClick(view, getAdapterPosition(), false);
	}
}
