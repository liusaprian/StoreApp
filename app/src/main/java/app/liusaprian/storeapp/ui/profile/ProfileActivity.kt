package app.liusaprian.storeapp.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import app.liusaprian.storeapp.R
import app.liusaprian.storeapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.editProfileBtn.setOnClickListener {
            binding.profilePicture.setImageResource(R.drawable.ic_picture_empty)
            binding.editProfileBtn.visibility = View.GONE
            binding.saveProfileBtn.visibility = View.VISIBLE
        }
        binding.saveProfileBtn.setOnClickListener {
            binding.profilePicture.setImageResource(R.color.white)
            binding.editProfileBtn.visibility = View.VISIBLE
            binding.saveProfileBtn.visibility = View.GONE
        }
    }
}