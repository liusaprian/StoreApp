package app.liusaprian.storeapp.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductResponseItem(
	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("price")
	val price: Double,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("category")
	val category: String,

	var quantity: Int
) : Parcelable {
	constructor() : this(
		"",
		0.0,
		"",
		0,
		"",
		"",
		0
	)
}
