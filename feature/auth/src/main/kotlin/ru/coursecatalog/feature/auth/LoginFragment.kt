package ru.coursecatalog.feature.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.coursecatalog.core.ui.R as CoreUiR
import ru.coursecatalog.feature.auth.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureInputs()
        configureActions()
        observeState()
    }

    private fun configureInputs() = with(binding) {
        emailInput.filters = emailInput.filters + rejectCyrillicFilter
        emailInput.doAfterTextChanged { viewModel.onEmailChanged(it?.toString().orEmpty()) }
        passwordInput.doAfterTextChanged { viewModel.onPasswordChanged(it?.toString().orEmpty()) }
    }

    private fun configureActions() = with(binding) {
        loginButton.setOnClickListener {
            findNavController().navigate(
                CoreUiR.id.destination_main,
                null,
                navOptions = navOptions {
                    popUpTo(CoreUiR.id.destination_login) {
                        inclusive = true
                    }
                },
            )
        }
        vkButton.setOnClickListener { openBrowser(VK_URL) }
        okButton.setOnClickListener { openBrowser(OK_URL) }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.loginButton.isEnabled = state.canLogIn
                }
            }
        }
    }

    private fun openBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private companion object {
        const val VK_URL = "https://vk.com/"
        const val OK_URL = "https://ok.ru/"

        val rejectCyrillicFilter = InputFilter { source, start, end, _, _, _ ->
            val accepted = source.substring(start, end)
                .filterNot { it in '\u0400'..'\u04FF' }
            accepted.takeIf { it.length != end - start }
        }
    }
}
