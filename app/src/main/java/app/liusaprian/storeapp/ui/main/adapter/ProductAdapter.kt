package app.liusaprian.storeapp.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.ProductItemBinding
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.detail.ProductDetailActivity
import com.bumptech.glide.Glide

class ProductAdapter(private val productList: List<ProductResponseItem>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductResponseItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.color.placeholder)
                    .into(productImage)
                productTitle.text = product.title
                productPrice.text = root.resources.getString(R.string.price, product.price.toString())
                itemView.setOnClickListener {
                    val toDetailActivity = Intent(itemView.context, ProductDetailActivity::class.java)
                    toDetailActivity.putExtra(ProductDetailActivity.DETAIL_DATA, product)
                    itemView.context.startActivity(toDetailActivity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int  = productList.size
}