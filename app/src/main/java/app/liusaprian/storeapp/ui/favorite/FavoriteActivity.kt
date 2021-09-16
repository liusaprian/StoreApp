package app.liusaprian.storeapp.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import app.liusaprian.storeapp.databinding.ActivityFavoriteBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.response.ProductResponseItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoriteActivity : AppCompatActivity() {

    private val binding: ActivityFavoriteBinding by viewBinding()
    private val favoriteList = ArrayList<ProductResponseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.favoriteBackBtn.setOnClickListener { finish() }
        Firebase.firestore.collection(FirestoreOperation.FAVORITE_PRODUCTS)
            .addSnapshotListener { snapshot, _ ->
                if(snapshot != null && snapshot.size() > 0) {
                    favoriteList.clear()
                    favoriteList.addAll(snapshot.toObjects(ProductResponseItem::class.java))
                    initRecyclerView(favoriteList)
                    showEmptyFavoritePage(false)
                } else showEmptyFavoritePage(true)
            }
    }

    private fun initRecyclerView(favoriteList: ArrayList<ProductResponseItem>) {
        binding.favoriteProductsRv.adapter = FavoriteAdapter(favoriteList)
        binding.favoriteProductsRv.layoutManager = LinearLayoutManager(this)
    }

    private fun showEmptyFavoritePage(show: Boolean) {
        binding.emptyFavoriteText.visibility = if(show) View.VISIBLE else View.GONE
        binding.favoriteProductsRv.visibility = if(!show) View.VISIBLE else View.GONE
    }
}