package com.example.storryapp.view.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.storryapp.R
import com.example.storryapp.data.model.UserModel
import com.example.storryapp.data.model.UserPreference
import com.example.storryapp.data.network.response.ResultResponse
import com.example.storryapp.databinding.ActivityLoginBinding
import com.example.storryapp.view.ApiCallbackString
import com.example.storryapp.view.ViewModelFactory
import com.example.storryapp.view.isEmailValid
import com.example.storryapp.view.viewmodel.LoginViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
//    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        setupViewModel()
        setupView()
        setMyButtonEnable()
        editTextListener()
        buttonListener()
//        observeLoading()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun editTextListener() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etSignIn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }
    }

    private fun setMyButtonEnable() {
        val resultPass = binding.etPassword.text
        val resultEmail = binding.etEmail.text

        binding.btnSignIn.isEnabled = resultPass != null && resultEmail != null &&
                binding.etPassword.text.toString().length >= 6 &&
                isEmailValid(binding.etEmail.text.toString())
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.sign_in_success))
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.sign_in_failed) +", $message")
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()

            }
        }
    }

    private fun buttonListener() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            loginViewModel.login(email, pass).observe(this) {
                when (it) {
                    is ResultResponse.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val user = UserModel(
                            it.data.name,
                            email,
                            pass,
                            it.data.userId,
                            it.data.token,
                            true
                        )
                        showAlertDialog(true, getString(R.string.sign_in_success))

                        val userPref = UserPreference.getInstance(dataStore)
                        lifecycleScope.launchWhenStarted {
                            userPref.saveUser(user)
                        }
                    }
                    is ResultResponse.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showAlertDialog(false, it.error)
                    }
                }
            }
        }
    }

//    private fun observeLoading() {
//        loginViewModel.isLoading.observe(this) { isLoading ->
//            showLoading(isLoading)
//        }
//    }
//
//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

    private fun playAnimation(){
        val imageView = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(350)
        val title = ObjectAnimator.ofFloat(binding.logintext, View.ALPHA, 1f).setDuration(350)
        val emailText = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(350)
        val passwordText = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(350)
        val loginButton = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(350)
        val loginText = ObjectAnimator.ofFloat(binding.etSignIn, View.ALPHA, 1f).setDuration(350)

        AnimatorSet().apply {
            playSequentially(imageView, title, emailText, passwordText, loginButton, loginText)
            startDelay = 350
        }.start()
    }

}