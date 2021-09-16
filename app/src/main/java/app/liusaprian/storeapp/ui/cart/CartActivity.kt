package app.liusaprian.storeapp.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.ActivityCartBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.cart.adapter.CartAdapter
import app.liusaprian.storeapp.ui.checkout.CheckoutActivity
import app.liusaprian.storeapp.ui.main.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartActivity : AppCompatActivity() {

    private val binding: ActivityCartBinding by viewBinding()
    private val cartProductList = ArrayList<ProductResponseItem>()
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.emptyCartBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.cartBackBtn.setOnClickListener { finish() }

        Firebase.firestore.collection(FirestoreOperation.CART_PRODUCTS).addSnapshotListener { snapshot, _ ->
            if(snapshot != null && snapshot.size() > 0) {
                cartProductList.clear()
                cartProductList.addAll(snapshot.toObjects(ProductResponseItem::class.java))
                initCartRecyclerView(cartProductList)

                binding.cartItemSize.text = getString(R.string.item_qty, snapshot.size())

                total = 0.0
                snapshot.forEach {
                    val product = it.toObject(ProductResponseItem::class.java)
                    total += product.price * product.quantity
                }
                val totalString = String.format("%.2f", total)
                binding.cartProductSubtotalPrice.text = getString(R.string.price, totalString)

                showEmptyCart(false)
            } else showEmptyCart(true)
        }

        binding.cartCheckoutBtn.setOnClickListener {
            if(cartProductList.isEmpty()) Toast.makeText(this, "Cart is Empty", Toast.LENGTH_SHORT).show()
            else startActivity(Intent(this, CheckoutActivity::class.java).apply {
                putParcelableArrayListExtra(CheckoutActivity.CHECKOUT_PRODUCTS, cartProductList)
                putExtra(CheckoutActivity.SUBTOTAL, total)
            })
        }
    }

    private fun initCartRecyclerView(cartProductList: ArrayList<ProductResponseItem>) {
        with(binding) {
            cartProductRv.adapter = CartAdapter(cartProductList, supportFragmentManager)
            cartProductRv.layoutManager = LinearLayoutManager(this@CartActivity)
        }
    }

    private fun showEmptyCart(show: Boolean) {
        binding.emptyCartLayout.visibility = if(show) View.VISIBLE else View.GONE
        binding.cartLayout.visibility = if(!show) View.VISIBLE else View.GONE
    }
}