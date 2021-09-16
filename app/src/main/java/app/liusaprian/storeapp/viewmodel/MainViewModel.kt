package app.liusaprian.storeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.liusaprian.storeapp.data.StoreRepository
import app.liusaprian.storeapp.response.ProductResponseItem
import kotlinx.coroutines.launch

class MainViewModel (private val repository: StoreRepository) : ViewModel() {

    private val _products = MutableLiveData<List<ProductResponseItem>>()
    val products: LiveData<List<ProductResponseItem>> = _products

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _recommendedProducts = MutableLiveData<List<ProductResponseItem>>()
    val recommendedProducts: LiveData<List<ProductResponseItem>> = _recommendedProducts

    fun getProductsByCategory(category: String) = viewModelScope.launch {
        _products.postValue(repository.getProducts(category))
    }

    fun getRecommendedProducts() = viewModelScope.launch {
        _recommendedProducts.postValue(repository.getProducts(""))
    }

    fun getCategories() = viewModelScope.launch {
        _categories.postValue(repository.getCategories())
    }
}