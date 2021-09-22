package com.example.application2.UI.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.application2.R

class SettingsFragment : Fragment() {

    lateinit var close:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        close = view.findViewById(R.id.close)

        close.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }
}