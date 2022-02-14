package fr.isen.hadhri.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.hadhri.androiderestaurant.databinding.ActivityDishesBinding
import fr.isen.hadhri.androiderestaurant.model.DishResult
import org.json.JSONObject
import fr.isen.hadhri.androiderestaurant.model.DishModel

class DishesActivity : ToolActivity(){
    private lateinit var binding: ActivityDishesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var categoryType = intent.getStringExtra("category_type")
        binding.dishesTitle.text = categoryType
        if (categoryType != null) {
            loadDishesFromCategory(categoryType)
        }

    }
    private fun loadDishesFromCategory(category: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val jsonObject = JSONObject()
        jsonObject.put("id_shop", "1")
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->

                val gson = Gson()
                val dishresult = gson.fromJson(response.toString(), DishResult::class.java)

                displayDishes(dishresult.data.firstOrNull(){it.name_fr==category}?.items ?: listOf())
            }, {
                Log.e("DishActivity", "Erreur lors de la récupération de la liste des plats")
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,

            0,
            1f
        )
        queue.add(request)
    }

    private fun displayDishes(dishresult : List<DishModel>){

        //}
        // getting the recyclerview by its id
        val recyclerview = binding.dishesItem

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        binding.dishesItem.layoutManager = LinearLayoutManager(this)
        binding.dishesItem.adapter = DishAdapter(dishresult, ) {

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("dish", it)
            startActivity(intent)
        }
    }

}