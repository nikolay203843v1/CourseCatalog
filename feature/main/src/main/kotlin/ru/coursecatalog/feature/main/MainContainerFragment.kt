package ru.coursecatalog.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import ru.coursecatalog.feature.main.databinding.FragmentMainContainerBinding

class MainContainerFragment : Fragment() {
    private var _binding: FragmentMainContainerBinding? = null
    private val binding get() = checkNotNull(_binding)
    private var navController: NavController? = null
    private var destinationChangedListener: NavController.OnDestinationChangedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHost = childFragmentManager
            .findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHost.navController

        configureTabClicks(navHost.navController)
        observeSelectedTab(navHost.navController)
    }

    private fun configureTabClicks(navController: NavController) = with(binding) {
        homeTabButton.setOnClickListener {
            navController.openTab(R.id.homeFragment)
        }
        favoritesTabButton.setOnClickListener {
            navController.openTab(R.id.favoritesFragment)
        }
        accountTabButton.setOnClickListener {
            navController.openTab(R.id.accountFragment)
        }
    }

    private fun observeSelectedTab(navController: NavController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            binding.homeTabButton.isSelected = destination.id == R.id.homeFragment
            binding.favoritesTabButton.isSelected = destination.id == R.id.favoritesFragment
            binding.accountTabButton.isSelected = destination.id == R.id.accountFragment
        }
        destinationChangedListener = listener
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onDestroyView() {
        destinationChangedListener?.let { listener ->
            navController?.removeOnDestinationChangedListener(listener)
        }
        destinationChangedListener = null
        navController = null
        _binding = null
        super.onDestroyView()
    }

    private fun NavController.openTab(destinationId: Int) {
        if (currentDestination?.id == destinationId) return

        navigate(
            destinationId,
            null,
            navOptions {
                launchSingleTop = true
                restoreState = true
                popUpTo(graph.startDestinationId) {
                    saveState = true
                }
            },
        )
    }
}
