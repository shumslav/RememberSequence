package com.example.application2.UI.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.application2.R

class MenuFragment : Fragment() {

    lateinit var play: ImageView
    lateinit var info:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        play = view.findViewById(R.id.play)
        info = view.findViewById(R.id.info)

        info.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.alpha_one,
                    R.anim.alpha_zero,
                    R.anim.alpha_one,
                    R.anim.alpha_zero
                )
                .replace(R.id.fragment_container,SettingsFragment(),null)
                .addToBackStack(null)
                .commit()
        }

        play.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.alpha_one,
                    R.anim.alpha_zero,
                    R.anim.alpha_one,
                    R.anim.alpha_zero
                )
                .replace(R.id.fragment_container,LevelsFragment(),null)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}