package github.cirqach.devlex.app_pages.fragments.StatisticPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import github.cirqach.devlex.R

class StatisticFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayoutAdapter: TabLayoutAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        tabLayout = view.findViewById(R.id.StatisticTestTabLayout)
        viewPager2 = view.findViewById(R.id.viewPagerForTests)

        tabLayoutAdapter = TabLayoutAdapter(parentFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.find_translation_test3)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.find_word_test2)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.true_false_test2)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.hangman_game2)))


        viewPager2.adapter = tabLayoutAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }

}