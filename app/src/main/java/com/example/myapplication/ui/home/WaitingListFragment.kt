package com.example.myapplication.ui.home
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentWaitingListBinding

class WaitingListFragment : Fragment() {

    private var _binding: FragmentWaitingListBinding? = null
    private val binding get() = _binding!!

    private lateinit var waitingListAdapter: WaitingListAdapter
    private lateinit var waitingList: List<WaitingListItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaitingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        waitingList = listOf(
            WaitingListItem("John Doe", "Waiting since 10:00 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("John Doe", "Waiting since 10:00 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("John Doe", "Waiting since 10:00 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
            WaitingListItem("Jane Smith", "Waiting since 10:30 AM"),
        )

        waitingListAdapter = WaitingListAdapter(waitingList)

        binding.recyclerViewWaitingList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = waitingListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
