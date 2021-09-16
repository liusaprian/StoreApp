package app.liusaprian.storeapp.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.SearchItemBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.detail.ProductDetailActivity
import com.bumptech.glide.Glide

class SearchItemAdapter(private val productList: List<ProductResponseItem>) : RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductResponseItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.color.placeholder)
                    .into(searchProductImage)
                searchProductTitle.text = product.title
                searchProductCategory.text = product.category
                searchProductPrice.text = root.resources.getString(R.string.price, product.price.toString())
                searchAddFab.setOnClickListener {
                    FirestoreOperation().addProductToCart(product)
                    Toast.makeText(itemView.context, "Added to Cart", Toast.LENGTH_SHORT).show()
                }
                itemView.setOnClickListener {
                    val toDetailActivity = Intent(itemView.context, ProductDetailActivity::class.java)
                    toDetailActivity.putExtra(ProductDetailActivity.DETAIL_DATA, product)
                    itemView.context.startActivity(toDetailActivity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int  = productList.size
}