package ru.coursecatalog.feature.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.coursecatalog.feature.main.course.CourseListAdapter
import ru.coursecatalog.feature.main.databinding.FragmentHomeBinding
import ru.coursecatalog.feature.main.model.CourseListUiState

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel: HomeViewModel by viewModel()
    private val courseAdapter = CourseListAdapter(
        onFavoriteClick = { viewModel.toggleFavorite(it) },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.courseList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
            setHasFixedSize(true)
        }
        binding.sortButton.setOnClickListener {
            viewModel.sortByPublishDate()
            binding.courseList.post {
                binding.courseList.scrollToPosition(0)
            }
        }
        binding.retryButton.setOnClickListener { viewModel.refresh() }
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: CourseListUiState) = with(binding) {
        courseAdapter.submitCourses(state.courses)
        progressIndicator.isVisible = state.isLoading
        errorGroup.isVisible = state.hasLoadError
        courseList.isVisible = state.courses.isNotEmpty()
    }

    override fun onDestroyView() {
        binding.courseList.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
