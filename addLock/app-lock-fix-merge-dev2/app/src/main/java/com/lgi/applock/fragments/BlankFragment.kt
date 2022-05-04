package com.lgi.applock.fragments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lgi.applock.databinding.FragmentBlankBinding

class BlankFragment : BaseFragment<FragmentBlankBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBlankBinding {
        return FragmentBlankBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.loutPinInputKeyboard.changeKeyboardColor(Color.RED)
        binding.loutPinInputKeyboard.changeKeyboardCharacterColor(Color.GREEN)
    }
}