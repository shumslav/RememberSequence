package com.example.application2.UI.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import com.example.application2.R
import com.firestu.biggiwins.Settings.GameSettings

class LevelsFragment : Fragment() {

    lateinit var levelsBoard: LinearLayout
    lateinit var testLevel: TextView
    lateinit var gameSettings: GameSettings

    private var isDraw = false

    private var allLevels = mutableListOf<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_levels, container, false)

        levelsBoard = view.findViewById(R.id.levels)
        testLevel = view.findViewById(R.id.level_test)
        gameSettings = GameSettings(requireContext())

        testLevel.doOnPreDraw {
            isDraw = true
            fillLevels()
            allLevels.forEach { view ->
                view.setOnClickListener {
                    clickTextView(view)
                }
            }
        }

        return view
    }

    private fun fillLevels() {
        var currentLevel = 1
        for (i in 0..((gameSettings.maxLevel / 3) + 5)) {
            val column = LinearLayout(requireContext())
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = testLevel.height / 5

            column.layoutParams = params
            column.gravity = Gravity.CENTER
            column.orientation = LinearLayout.HORIZONTAL
            for (j in 1..3) {
                val textView = createLevelButton(currentLevel)
                currentLevel += 1
                column.addView(textView)
                allLevels.add(textView)
            }
            levelsBoard.addView(column)
        }
    }

    private fun createLevelButton(level: Int): TextView {
        val textView = TextView(requireContext())
        textView.layoutParams
        val params = LinearLayout.LayoutParams(testLevel.width, testLevel.height)
        if (level % 3 != 0) {
            params.marginEnd = 20
        }
        textView.layoutParams = params
        if (level <= gameSettings.maxLevel) {
            textView.setBackgroundResource(R.drawable.level_btn_background)
            textView.gravity = Gravity.CENTER
            textView.text = level.toString()
            textView.tag = Pair(level, true)
            textView.typeface = testLevel.typeface
            textView.setTextColor(testLevel.textColors)
            textView.setShadowLayer(
                testLevel.shadowRadius,
                testLevel.shadowDx,
                testLevel.shadowDy,
                testLevel.shadowColor
            )
            textView.textSize = testLevel.height / 4F
        } else {
            textView.setBackgroundResource(R.drawable.close_level)
            textView.tag = Pair(level, false)
        }
        textView.visibility = View.VISIBLE
        return textView
    }

    private fun clickTextView(textView: View) {
        var pair = textView.tag as Pair<*, *>
        if (pair.second.toString().toBoolean()) {
            allLevels.forEach { it.isClickable = false }
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.alpha_one,
                    R.anim.alpha_zero,
                    R.anim.alpha_one,
                    R.anim.alpha_zero
                )
                .replace(
                    R.id.fragment_container,
                    GameFragment.newInstance(pair.first.toString().toInt()),
                    null
                )
                .addToBackStack(null)
                .commit()
        } else {
            Toast.makeText(requireContext(), "Level ${pair.first} locked", Toast.LENGTH_SHORT)
                .show()
        }
    }

}