package com.lgi.applock.fragments
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.findNavController
import com.lgi.applock.databinding.FragmentSettingBinding
import android.view.MenuItem
import com.lgi.applock.activities.SettingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        setOnClickItem()
    }

    private fun setOnClickItem(){
        // set binding event for elements
        binding.languageLayout.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            context?.startActivity(intent)
        }
        // bottom share view
        binding.shareUsLayout.setOnClickListener {
            onShowBottomShareContent()
        }
    }

    private fun onSwitchChanged(){
        binding.switchSecure.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                // turn on quick lock
            }
            else{
                // turn off quick lock
            }
        })
    }

    private fun onShowBottomShareContent(){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share the app with your friends")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Learn Android App Development https://play.google.com/store/apps/details?id="
        )
        intent.type = "text/plain"
        startActivity(intent)
    }
}