package com.example.application2.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application2.R
import com.example.application2.UI.Fragments.GameFragment
import com.example.application2.UI.Fragments.MenuFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container,MenuFragment(),null)
            .commit()
    }
}