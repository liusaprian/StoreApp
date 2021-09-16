package app.liusaprian.storeapp.ui.checkout.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.FragmentPaymentBinding
import app.liusaprian.storeapp.ui.checkout.CheckoutActivity

class PaymentFragment : Fragment() {

    private val binding: FragmentPaymentBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subtotal = requireArguments().getDouble(CheckoutActivity.SUBTOTAL)
        val taxes = subtotal.div(10)
        val total = subtotal.plus(taxes)

        binding.paymentBackBtn.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.subtotal.text = getString(R.string.price, String.format("%.2f", subtotal))
        binding.taxes.text = getString(R.string.price, String.format("%.2f", taxes))
        binding.total.text = getString(R.string.price, String.format("%.2f", total))
        binding.saveDataSwitch.setOnCheckedChangeListener { _, checked ->
            if(checked)
                Toast.makeText(requireActivity(), "Saved", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(requireActivity(), "Not Saved", Toast.LENGTH_SHORT).show()
        }
    }
}