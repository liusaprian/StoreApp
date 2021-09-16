package app.liusaprian.storeapp.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.FavoriteItemBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.detail.ProductDetailActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoriteAdapter(private val productList: List<ProductResponseItem>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductResponseItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.color.placeholder)
                    .into(favoriteImage)
                favoriteTitle.text = product.title
                favoriteDesc.text = product.description
                favoritePrice.text = root.resources.getString(R.string.price, product.price.toString())
                favoriteBtn.setOnClickListener {
                    Firebase.firestore
                        .collection(FirestoreOperation.FAVORITE_PRODUCTS)
                        .document(product.id.toString()).delete()
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
        val view = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int  = productList.size
}