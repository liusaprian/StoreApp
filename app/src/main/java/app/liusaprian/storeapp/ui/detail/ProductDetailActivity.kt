package app.liusaprian.storeapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.ActivityProductDetailBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.cart.CartActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductDetailActivity : AppCompatActivity() {

    private val binding: ActivityProductDetailBinding by viewBinding()
    private lateinit var product : ProductResponseItem

    companion object {
        const val DETAIL_DATA = "detail"
    }

    private val addToCartBtnClickListener = View.OnClickListener {
        FirestoreOperation().addProductToCart(product)
        Toast.makeText(this@ProductDetailActivity, "Added to Cart", Toast.LENGTH_SHORT).show()
    }

    private val cartBtnClickListener = View.OnClickListener {
        startActivity(Intent(this, CartActivity::class.java))
    }

    private val addToFavoriteClickListener = View.OnClickListener {
        Firebase.firestore
            .collection(FirestoreOperation.FAVORITE_PRODUCTS)
            .document(product.id.toString())
            .get()
            .addOnSuccessListener {
                FirestoreOperation().setProductFavoriteState(product, !it.exists())
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        product = intent.getParcelableExtra<ProductResponseItem>(DETAIL_DATA) as ProductResponseItem
        initDetailData()
        initBottomSheet()
        initCartItemCount()

        Firebase.firestore.collection(FirestoreOperation.FAVORITE_PRODUCTS)
            .document(product.id.toString())
            .addSnapshotListener { value, _ ->
                if(value != null && value.exists())
                    binding.bottomSheet.favoriteFab.setImageResource(R.drawable.ic_baseline_favorite_24)
                else binding.bottomSheet.favoriteFab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

        with(binding) {
            detail.backBtn.setOnClickListener { finish() }
            addToCartBtn.setOnClickListener(addToCartBtnClickListener)
            detail.detailCart.setOnClickListener(cartBtnClickListener)
            detail.detailCartItemCount.setOnClickListener(cartBtnClickListener)
            bottomSheet.favoriteFab.setOnClickListener(addToFavoriteClickListener)
        }
    }

    private fun initDetailData() {
        Glide.with(this)
            .load(product.image)
            .into(binding.detail.productDetailImage)
        binding.bottomSheet.apply {
            productDetailName.text = product.title
            productDetailPrice.text = getString(R.string.price, product.price.toString())
            productDetailDesc.text = product.description
            productDetailCategory.text = product.category
        }
    }

    private fun initBottomSheet() {
        val sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                with(binding.bottomSheet) {
                    descText.alpha = slideOffset
                    productDetailDesc.alpha = slideOffset
                    productDetailName.maxLines = (slideOffset * 10 + 1).toInt()
                    productDetailName.ellipsize = if(slideOffset == 0f) TextUtils.TruncateAt.END else null
                }
            }
        })
    }

    private fun initCartItemCount() {
        Firebase.firestore.collection(FirestoreOperation.CART_PRODUCTS).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                binding.detail.detailCartItemCount.visibility = if(snapshot.size() == 0) View.INVISIBLE else View.VISIBLE
                binding.detail.detailCartItemCount.text = snapshot.size().toString()
            }
        }
    }
}