package app.liusaprian.storeapp.ui.cart.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.RemoveItemDialogBinding
import app.liusaprian.storeapp.helper.FirestoreOperation
import app.liusaprian.storeapp.response.ProductResponseItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RemoveItemDialogFragment : DialogFragment() {

    private val binding: RemoveItemDialogBinding by viewBinding()
    private lateinit var product: ProductResponseItem

    companion object {
        private const val PRODUCT = "product"

        fun newInstance(product: ProductResponseItem) : RemoveItemDialogFragment =
            RemoveItemDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT, product)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getParcelable(PRODUCT)!!
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.remove_item_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogTitle.text = getString(R.string.remove_dialog_title, product.title)
        binding.yesBtn.setOnClickListener {
            Firebase.firestore.collection(FirestoreOperation.CART_PRODUCTS).document(product.id.toString()).delete()
            dialog?.dismiss()
        }
        binding.noBtn.setOnClickListener {
            dialog?.cancel()
        }
    }
}