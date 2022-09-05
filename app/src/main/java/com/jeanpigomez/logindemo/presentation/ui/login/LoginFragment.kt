package com.jeanpigomez.logindemo.presentation.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.jeanpigomez.logindemo.R
import com.jeanpigomez.logindemo.databinding.FragmentLoginBinding
import com.jeanpigomez.logindemo.utils.Result
import com.jeanpigomez.logindemo.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private var isValidForm = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
    }

    private fun setView() = with(binding) {
        etUsername.doAfterTextChanged {
            isValidForm = it.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()
            btnLogin.isEnabled = isValidForm
        }

        etPassword.doAfterTextChanged {
            isValidForm = it.toString().isNotEmpty() && etUsername.text.toString().isNotEmpty()
            btnLogin.isEnabled = isValidForm
        }

        btnLogin.setOnClickListener {
            val email = etUsername.text.toString()
            val password = etPassword.text.toString()

            viewModel.doLoginUser(email, password)

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    viewModel.loginUserFlow.collect {

                        when (it) {
                            is Result.Success -> {
                                Toast.makeText(requireContext(), it.data.msg, Toast.LENGTH_LONG).show()
                                loading.isGone = true
                                btnLogin.isVisible = true
                            }
                            is Result.Loading -> {
                                loading.isVisible = it.loading
                                btnLogin.isGone = it.loading
                            }
                            is Result.Failure -> {
                                loading.isGone = true
                                btnLogin.isVisible = true
                                Snackbar.make(root, it.t.message ?: "", Snackbar.LENGTH_LONG).apply {
                                    view.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.error_color_material_light))
                                    setTextColor(ContextCompat.getColor(context, R.color.white))
                                }.show()
                            }
                        }
                    }
                }
            }
        }
    }
}
