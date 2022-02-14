package fr.isen.hadhri.androiderestaurant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.isen.hadhri.androiderestaurant.RegisterFragment.Companion.USER_ID
import fr.isen.hadhri.androiderestaurant.databinding.ActivityBasketBinding
import fr.isen.hadhri.androiderestaurant.model.BasketData
import fr.isen.hadhri.androiderestaurant.model.DishBasket
import java.io.File


class BasketActivity : ToolActivity() {
    private lateinit var binding: ActivityBasketBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPreferences = getSharedPreferences(DetailActivity.APP_PREFS, Context.MODE_PRIVATE)
        binding.basketTitle.text = "Votre Panier"
        verifIfConnect()

        val filename = "/panier.json"
        if (File(cacheDir.absolutePath + filename).exists()) {
            val recup = File(cacheDir.absolutePath + filename).readText()
            val resultat = Gson().fromJson(recup, DishBasket::class.java)
            Log.d("panier", recup)
            val data = ArrayList<BasketData>()
            for (j in resultat.dishName.indices) {
                data.add(BasketData(resultat.dishName[j].dishName, resultat.dishName[j].quantity))
            }

            displayDishes(DishBasket(data, resultat.quantity))
        }
        var buttonConnection = binding.buttonConnection
        buttonConnection.setOnClickListener {
            if (binding.buttonConnection.text == "Commander") {
                startActivity(Intent(this, OrderActivity::class.java))
            } else {
                startActivity(Intent(this, ConnectionActivity::class.java))
            }
        }
    }


    private fun displayDishes(dishresult: DishBasket) {
        binding.basketItem.layoutManager = LinearLayoutManager(this)
        binding.basketItem.adapter = BasketAdapter(dishresult.dishName) {
            dishresult.dishName.remove(it)
            updateBasket(dishresult)
            invalidateOptionsMenu()
        }


    }

    private fun updateBasket(basket: DishBasket) {
        val filename = "/panier.json"
        dishCountInPref(basket)
        File(cacheDir.absolutePath + filename).writeText(
            GsonBuilder().create().toJson(basket)
        )
    }

    private fun dishCountInPref(basket: DishBasket) {
        val count = basket.dishName.sumOf { it.quantity }
        basket.quantity = count
        val editor = getSharedPreferences(DetailActivity.APP_PREFS, Context.MODE_PRIVATE).edit()
        editor.putInt(DetailActivity.basketCount, count)
        editor.apply()

    }

    private fun verifIfConnect() {
        val userIdSave =
            getSharedPreferences(DetailActivity.APP_PREFS, MODE_PRIVATE).contains(USER_ID)
        if (userIdSave) {
            binding.buttonConnection.text = "Commander"
        }
    }
}