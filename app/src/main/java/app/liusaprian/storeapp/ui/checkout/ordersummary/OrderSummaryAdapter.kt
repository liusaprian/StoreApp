package app.liusaprian.storeapp.ui.checkout.ordersummary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.OrderSummaryItemBinding
import app.liusaprian.storeapp.response.ProductResponseItem
import com.bumptech.glide.Glide

class OrderSummaryAdapter(private val productList: List<ProductResponseItem>) : RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: OrderSummaryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductResponseItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.color.placeholder)
                    .into(orderImage)
                orderTitle.text = product.title
                orderCategory.text = product.category
                orderPrice.text = root.resources.getString(R.string.price, product.price.toString())
                orderQuantity.text = root.resources.getString(R.string.order_quantity, product.quantity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = OrderSummaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int  = productList.size
}