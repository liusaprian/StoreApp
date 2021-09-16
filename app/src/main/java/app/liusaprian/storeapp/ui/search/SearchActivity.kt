package app.liusaprian.storeapp.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.ActivitySearchBinding
import app.liusaprian.storeapp.response.ProductResponseItem

class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by viewBinding()
    private val productList = ArrayList<ProductResponseItem>()

    companion object {
        const val PRODUCTS = "products"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        intent.getParcelableArrayListExtra<ProductResponseItem>(PRODUCTS)?.let {
            productList.addAll(it)
        }

        initRecyclerView(productList)
        binding.searchBackBtn.setOnClickListener { onBackPressed() }
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence, p1: Int, p2: Int, p3: Int) {
                searchProduct(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun searchProduct(query: String) {
        val searchMatchProduct = ArrayList<ProductResponseItem>()
        searchMatchProduct.addAll(
            productList.filter { it.title.lowercase().contains(query.lowercase()) }
        )
        if(searchMatchProduct.isEmpty()) Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show()
        else initRecyclerView(searchMatchProduct)
    }

    private fun initRecyclerView(productList: ArrayList<ProductResponseItem>) {
        binding.searchProductRv.adapter = SearchItemAdapter(productList)
        binding.searchProductRv.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }
}