package app.liusaprian.storeapp.ui.checkout.ordersummary

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.FragmentOrderSummaryBinding
import app.liusaprian.storeapp.response.ProductResponseItem
import app.liusaprian.storeapp.ui.checkout.CheckoutActivity
import app.liusaprian.storeapp.ui.checkout.payment.PaymentFragment

class OrderSummaryFragment : Fragment() {

    private val binding: FragmentOrderSummaryBinding by viewBinding()
    private var checkoutProductList = ArrayList<ProductResponseItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkoutProductList = arguments?.getParcelableArrayList(CheckoutActivity.CHECKOUT_PRODUCTS)!!

        binding.checkoutBackBtn.setOnClickListener { requireActivity().finish() }

        val sp = requireActivity().getSharedPreferences("StoreApp", Context.MODE_PRIVATE)
        binding.checkoutAddress.text = sp.getString("location", "Address has not been saved yet")
        binding.editAddressBtn.setOnClickListener {
            editAddress(true)
            binding.editAddressTextbox.setText(sp.getString("location", ""), TextView.BufferType.EDITABLE)
            binding.editAddressTextbox.requestFocus()
        }
        binding.saveAddressBtn.setOnClickListener {
            sp.edit().putString("location", binding.editAddressTextbox.text.toString()).apply()
            editAddress(false)
            binding.checkoutAddress.text = sp.getString("location", "Address has not been saved yet")
        }
        binding.editOrderBtn.setOnClickListener { requireActivity().finish() }
        binding.goToPaymentBtn.setOnClickListener {
            val paymentFragment = PaymentFragment()
            paymentFragment.arguments = Bundle().apply {
                putDouble(CheckoutActivity.SUBTOTAL, requireArguments().getDouble(CheckoutActivity.SUBTOTAL))
            }

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.checkout_frame_container,
                    paymentFragment
                )
                .addToBackStack(null)
                .commit()
        }

        initRecyclerView(checkoutProductList)
    }

    private fun initRecyclerView(checkoutProductList: java.util.ArrayList<ProductResponseItem>) {
        with(binding) {
            orderSummaryRv.adapter = OrderSummaryAdapter(checkoutProductList)
            orderSummaryRv.layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun editAddress(edit: Boolean) {
        binding.checkoutAddress.visibility = if(edit) View.INVISIBLE else View.VISIBLE
        binding.editAddressTextbox.visibility = if(!edit) View.INVISIBLE else View.VISIBLE
        binding.editAddressBtn.visibility = if(edit) View.INVISIBLE else View.VISIBLE
        binding.saveAddressBtn.visibility = if(!edit) View.INVISIBLE else View.VISIBLE
    }
}