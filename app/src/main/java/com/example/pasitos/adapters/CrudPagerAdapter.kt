
package com.example.pasitos

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CrudPagerAdapter(activity: AppCompatActivity)
    : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NinoFragment()
            1 -> PapaFragment()
            2 -> MaestroFragment()
            else -> NinoFragment()
        }
    }
}
