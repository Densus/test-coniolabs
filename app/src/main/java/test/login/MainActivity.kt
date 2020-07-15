package test.login

import android.app.Dialog
import android.app.Service
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import test.login.model.Auth
import test.login.model.LoginUser
import test.login.network.ServiceClient
import test.login.network.ServiceRepository
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var progressDialog: Dialog
    private lateinit var service: ServiceRepository
    private var request = LoginUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        progressDialog = Dialog(this)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setContentView(R.layout.loading_activity)
        service = ServiceClient.build().create(ServiceRepository::class.java)

        initListener()
    }

    private fun login() {
        val callAsync: Call<Auth> = service.login(request)
        progressDialog.show()

        callAsync.enqueue(object : Callback<Auth> {
            override fun onResponse(
                call: Call<Auth>,
                response: Response<Auth>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        progressDialog.dismiss()
                        homePage()
                    }

                } else {
                    progressDialog.dismiss()
                    showMessage(response.message())
                }
            }

            override fun onFailure(
                call: Call<Auth>,
                t: Throwable
            ) {
                progressDialog.dismiss()
            }
        })
    }

    private fun homePage() {
        finish()
        startActivity(HomeActivity.newIntent(this))
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initListener() {
        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                request.email = email.text.toString()
                isValidBtn()
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                request.password = password.text.toString()
                isValidBtn()
            }
        })

        email.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) email.setBackgroundResource(R.drawable.form_active)
            else email.setBackgroundResource(R.drawable.form_inactive)
        }

        password.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) password.setBackgroundResource(R.drawable.form_active)
            else password.setBackgroundResource(R.drawable.form_inactive)
        }

        signInBtn.setOnClickListener { login() }
    }

    fun isValid(): Boolean {
        return (request.email.isNotEmpty()
                && request.password.isNotEmpty())
    }

    private fun isValidBtn() {
        if (isValid()) {
            if (isValidEmail(email.text.toString())) {
                signInBtn.isEnabled = true
                signInBtn.setBackgroundResource(R.drawable.btn_active)
            }
            else {
                signInBtn.isEnabled = false
                signInBtn.setBackgroundResource(R.drawable.btn_inactive)
            }
        } else {
            signInBtn.isEnabled = false
            signInBtn.setBackgroundResource(R.drawable.btn_inactive)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}