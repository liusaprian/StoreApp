package app.liusaprian.storeapp.ui.cart.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.CartItemBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.cart.dialog.RemoveItemDialogFragment
import app.liusaprian.storeapp.ui.detail.ProductDetailActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartAdapter(private val productList: List<ProductResponseItem>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position], fragmentManager)
    }

    override fun getItemCount(): Int  = productList.size

    class ViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductResponseItem, fragmentManager: FragmentManager) {
            val document = Firebase.firestore.collection(FirestoreOperation.CART_PRODUCTS).document(product.id.toString())
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.color.placeholder)
                    .into(cartProductImage)
                cartProductTitle.text = product.title
                cartProductPrice.text = root.resources.getString(R.string.price, product.price.toString())
                cartProductQuantity.text = product.quantity.toString()
                cartAddBtn.setOnClickListener {
                    document.update("quantity", product.quantity+1)
                }
                cartReduceBtn.setOnClickListener {
                    if(product.quantity > 1) document.update("quantity", product.quantity-1)
                    else {
                        val removeItemDialogFragment = RemoveItemDialogFragment.newInstance(product)
                        removeItemDialogFragment.show(fragmentManager, RemoveItemDialogFragment::class.java.simpleName)
                    }

                }
                itemView.setOnClickListener {
                    val toDetailActivity = Intent(itemView.context, ProductDetailActivity::class.java)
                    toDetailActivity.putExtra(ProductDetailActivity.DETAIL_DATA, product)
                    itemView.context.startActivity(toDetailActivity)
                }
            }
        }
    }
}