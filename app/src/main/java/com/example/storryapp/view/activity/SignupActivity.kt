package com.example.storryapp.view.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storryapp.R
import com.example.storryapp.data.model.UserPreference
import com.example.storryapp.data.network.response.ResultResponse
import com.example.storryapp.databinding.ActivitySignupBinding
import com.example.storryapp.view.ApiCallbackString
import com.example.storryapp.view.ViewModelFactory
import com.example.storryapp.view.isEmailValid
import com.example.storryapp.view.viewmodel.RegisterViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val registerViewModel : RegisterViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setMyButtonEnable()
        editTextListener()
        buttonListener()
        setupView()
//        setupViewModel()
//        observeLoading()
        playAnimation()
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
        binding.etPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etSignin.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }

//    private fun setupViewModel() {
//        signupViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[SignupViewModel::class.java]
//    }

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

    private fun setMyButtonEnable() {
        binding.btnRegister.isEnabled =
            binding.etEmail.text.toString().isNotEmpty() &&
                    binding.etPass.text.toString().isNotEmpty() &&
                    binding.etPass.text.toString().length >= 6 &&
                    isEmailValid(binding.etEmail.text.toString())
    }

    private fun buttonListener() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()

            registerViewModel.register(name, email, password).observe(this){
                when (it) {
                    is ResultResponse.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showAlertDialog(true, getString(R.string.register_success))
                    }
                    is ResultResponse.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showAlertDialog(false, it.error)
                    }
                }
            }
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.register_success))
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
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
                setMessage(getString(R.string.register_failed)+", $message")
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

//    private fun observeLoading() {
//        registerViewModel.isLoading.observe(this) { isLoading ->
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
        val title = ObjectAnimator.ofFloat(binding.signupText, View.ALPHA, 1f).setDuration(350)
        val nameText = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(350)
        val emailText = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(350)
        val passwordText = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(350)
        val loginButton = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(350)
        val loginText = ObjectAnimator.ofFloat(binding.etSignin, View.ALPHA, 1f).setDuration(350)

        AnimatorSet().apply {
            playSequentially(imageView, title, nameText, emailText, passwordText, loginButton, loginText)
            startDelay = 350
        }.start()
    }
}