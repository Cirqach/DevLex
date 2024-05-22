package github.cirqach.devlex.app_pages.fragments.StatisticPage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabLayoutAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> FragmentFindTranslationTabLayout()
            1 -> FragmentFindWordTabLayout()
            2 -> FragmentTrueFalseTabLayout()
            3 -> FragmentHangmanGameTabLayout()
            else -> {
                FragmentFindTranslationTabLayout()
            }
        }
    }
}