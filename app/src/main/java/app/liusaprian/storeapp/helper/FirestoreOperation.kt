package app.liusaprian.storeapp.helper

import app.liusaprian.storeapp.response.ProductResponseItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreOperation {
    private val db = Firebase.firestore

    companion object {
        const val CART_PRODUCTS = "cart_products"
        const val FAVORITE_PRODUCTS = "favorite_products"
    }

    fun addProductToCart(product: ProductResponseItem) {
        db.collection(CART_PRODUCTS).document(product.id.toString()).get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val productData = it.toObject(ProductResponseItem::class.java)!!
                    db.collection(CART_PRODUCTS).document(productData.id.toString())
                        .update("quantity", productData.quantity+1)
                }
                else {
                    product.quantity = 1
                    db.collection(CART_PRODUCTS).document(product.id.toString()).set(product)
                }
            }
    }

    fun setProductFavoriteState(product: ProductResponseItem, fav: Boolean) {
        if(fav) db.collection(FAVORITE_PRODUCTS).document(product.id.toString()).set(product)
        else db.collection(FAVORITE_PRODUCTS).document(product.id.toString()).delete()
    }

}