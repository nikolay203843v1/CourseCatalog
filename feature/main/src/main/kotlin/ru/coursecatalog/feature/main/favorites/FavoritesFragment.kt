package ru.coursecatalog.feature.main.favorites

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
import ru.coursecatalog.feature.main.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel: FavoritesViewModel by viewModel()
    private val courseAdapter = CourseListAdapter(
        onFavoriteClick = { viewModel.toggleFavorite(it) },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.courseList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
            setHasFixedSize(true)
        }
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    courseAdapter.submitCourses(state.courses)
                    binding.courseList.isVisible = state.courses.isNotEmpty()
                    binding.emptyGroup.isVisible = state.courses.isEmpty()
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.courseList.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
