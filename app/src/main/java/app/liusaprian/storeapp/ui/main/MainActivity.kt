package app.liusaprian.storeapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.data.StoreRepository
import app.liusaprian.storeapp.databinding.ActivityMainBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.ui.main.adapter.ProductAdapter
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.cart.CartActivity
import app.liusaprian.storeapp.ui.favorite.FavoriteActivity
import app.liusaprian.storeapp.ui.search.SearchActivity
import app.liusaprian.storeapp.ui.main.adapter.RecommendedAdapter
import app.liusaprian.storeapp.ui.profile.ProfileActivity
import app.liusaprian.storeapp.viewmodel.MainViewModel
import app.liusaprian.storeapp.viewmodel.ViewModelFactory
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private val productList = ArrayList<ProductResponseItem>()

    private val cartBtnClickListener = View.OnClickListener {
        startActivity(Intent(this, CartActivity::class.java))
    }

    private val searchBtnClickListener = View.OnClickListener {
        startActivity(Intent(this, SearchActivity::class.java).apply {
            putParcelableArrayListExtra(SearchActivity.PRODUCTS, productList)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory(StoreRepository())
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        mainViewModel.getCategories()
        mainViewModel.categories.observe(this, {
            for(category in it) {
                val chip = layoutInflater.inflate(R.layout.chip_layout, binding.categoryChips, false) as Chip
                chip.text = category
                binding.categoryChips.addView(chip)
            }
        })

        getProductsByCategory("")
        mainViewModel.getRecommendedProducts()

        with(binding) {
            rvProducts.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            rvRecommended.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            categoryChips.setOnCheckedChangeListener { group, checkedId ->
                getProductsByCategory((group.getChildAt(checkedId-1) as Chip).text.toString())
            }
        }

        mainViewModel.products.observe(this, productObserver)
        mainViewModel.recommendedProducts.observe(this, recommendedObserver)

        Firebase.firestore.collection(FirestoreOperation.CART_PRODUCTS).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                binding.cartItemCount.visibility = if(snapshot.size() == 0) View.INVISIBLE else View.VISIBLE
                binding.cartItemCount.text = snapshot.size().toString()
            }
        }

        binding.favorite.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
        binding.profileImage.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
        binding.cart.setOnClickListener(cartBtnClickListener)
        binding.cartItemCount.setOnClickListener(cartBtnClickListener)
        binding.searchBtn.setOnClickListener(searchBtnClickListener)
    }

    private fun getProductsByCategory(category: String) {
        mainViewModel.getProductsByCategory(category)
    }

    private val productObserver = Observer<List<ProductResponseItem>> {
        if(it.isNotEmpty()) {
            binding.rvProducts.adapter = ProductAdapter(it)
            productList.addAll(it)
        }
    }

    private val recommendedObserver = Observer<List<ProductResponseItem>> {
        if(it.isNotEmpty()) {
            binding.rvRecommended.adapter = RecommendedAdapter(it)
        }
    }

}