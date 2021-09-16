package app.liusaprian.storeapp.ui.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import app.liusaprian.storeapp.databinding.ActivityCheckoutBinding
import app.liusaprian.storeapp.ui.checkout.ordersummary.OrderSummaryFragment

class CheckoutActivity : AppCompatActivity() {

    private val binding: ActivityCheckoutBinding by viewBinding()

    companion object {
        const val CHECKOUT_PRODUCTS = "checkout_products"
        const val SUBTOTAL = "subtotal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val orderSummaryFragment = OrderSummaryFragment()
        orderSummaryFragment.arguments = Bundle().apply {
            putParcelableArrayList(CHECKOUT_PRODUCTS, intent.getParcelableArrayListExtra(CHECKOUT_PRODUCTS))
            putDouble(SUBTOTAL, intent.getDoubleExtra(SUBTOTAL, 0.0))
        }

        supportFragmentManager.beginTransaction()
            .add(binding.checkoutFrameContainer.id, orderSummaryFragment)
            .commit()
    }
}