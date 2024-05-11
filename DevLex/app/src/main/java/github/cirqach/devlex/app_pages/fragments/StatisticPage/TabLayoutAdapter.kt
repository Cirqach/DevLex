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
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> return FragmentFindTranslationTabLayout()
            1 -> return FragmentFindWordTabLayout()
            2 -> return FragmentTrueFalseTabLayout()
            else -> {
                return FragmentFindTranslationTabLayout()
            }
        }
    }
}