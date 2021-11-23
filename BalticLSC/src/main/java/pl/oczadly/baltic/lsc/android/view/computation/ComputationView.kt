package pl.oczadly.baltic.lsc.android.view.computation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.computation.dto.ComputationStatus

class ComputationView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.computation_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.computation_recycler_view)
        recyclerView.adapter =
            ComputationAdapter(
                listOf(
                    ComputationTaskGroup(
                        "TestApp",
                        listOf(
                            ComputationTaskEntity(
                                "T01",
                                "v01",
                                LocalDateTime.now().minusDays(1),
                                LocalDateTime.now(),
                                ComputationStatus.COMPLETED,
                                3
                            ),
                            ComputationTaskEntity(
                                "T02",
                                "v02",
                                LocalDateTime.now().minusDays(6),
                                LocalDateTime.now(),
                                ComputationStatus.IN_PROGRESS,
                                2
                            )
                        )
                    ), ComputationTaskGroup("Another App", listOf())
                ), context!!
            )
    }
}