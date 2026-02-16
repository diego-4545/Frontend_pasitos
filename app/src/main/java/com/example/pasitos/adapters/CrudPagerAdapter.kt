
package com.example.pasitos

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pasitos.Fragments.MaestroFragment
import com.example.pasitos.Fragments.NinoFragment
import com.example.pasitos.Fragments.PadreFragment


class CrudPagerAdapter(activity: AppCompatActivity)
    : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> NinoFragment()
            0 -> PadreFragment()
            2 -> MaestroFragment()
            else -> NinoFragment()
        }
    }
}
