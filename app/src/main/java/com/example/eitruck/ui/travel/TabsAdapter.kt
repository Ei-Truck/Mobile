import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eitruck.ui.travel.analyzed_travels.AnalyzedTravels
import com.example.eitruck.ui.travel.pending_travels.PendingTravels

class TabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnalyzedTravels()
            1 -> PendingTravels()
            else -> AnalyzedTravels()
        }
    }
}
