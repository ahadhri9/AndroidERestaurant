package fr.isen.hadhri.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.hadhri.androiderestaurant.databinding.FragmentRegisterBinding
import fr.isen.hadhri.androiderestaurant.model.LoginModel
import fr.isen.hadhri.androiderestaurant.model.RegisterModel
import org.json.JSONObject
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.android.material.snackbar.Snackbar


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var buttonValidate = binding.buttonValidate
        buttonValidate.setOnClickListener() {
            register()
            startActivity(Intent(requireContext(), BasketActivity::class.java))
        }
    }

    private fun register() {
        val params = HashMap<String, String>()
        params["id_shop"] = "1"
        params["firstname"] = binding.prenomInput.text.toString()
        params["lastname"] = binding.nomInput.text.toString()
        params["address"] = binding.addressInput.text.toString()
        params["email"] = binding.emailInput.text.toString()
        params["password"] = binding.passwordInput.text.toString()
        var errorBool: Boolean
        errorBool = true
        if (TextUtils.isEmpty(binding.nomInput.text)) {
            binding.nom.error = getString(R.string.error)
            errorBool = false
        } else binding.nom.error = null
        if (TextUtils.isEmpty(binding.prenomInput.text)) {
            binding.prenom.error = getString(R.string.error)
            errorBool = false
        } else binding.prenom.error = null
        if (TextUtils.isEmpty(binding.emailInput.text)) {
            binding.email.error = getString(R.string.error)
            errorBool = false
        } else binding.email.error = null
        if (TextUtils.isEmpty(binding.addressInput.text)) {
            binding.address.error = getString(R.string.error)
            errorBool = false
        } else binding.address.error = null
        if (TextUtils.isEmpty(binding.passwordInput.text)) {
            binding.password.error = getString(R.string.error)
            errorBool = false
        } else binding.password.error = null
        if (errorBool) {
            val queue = Volley.newRequestQueue(requireContext())
            val url = "http://test.api.catering.bluecodegames.com/user/register"
            val jsonObject = JSONObject(params as HashMap<*, *>)
            val request = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    Log.e("response", response.toString())
                    val register =
                        GsonBuilder().create()
                            .fromJson(response.toString(), RegisterModel::class.java)
                    val editor =
                        requireContext().getSharedPreferences(
                            DetailActivity.APP_PREFS,
                            Context.MODE_PRIVATE
                        ).edit()
                    editor.putString(USER_ID, register.data.userId)
                    editor.apply()
                }, {
                    Log.e("API", it.toString())
                })
            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,

                0,
                1f
            )
            queue.add(request)
        }
        else Snackbar.make(binding.root, "Informations mal renseignées", Snackbar.LENGTH_LONG).show()
    }
    companion object {
        const val USER_ID = "USER_ID"
    }

}