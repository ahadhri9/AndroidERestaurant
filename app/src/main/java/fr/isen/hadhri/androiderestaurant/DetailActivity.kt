package fr.isen.hadhri.androiderestaurant


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.hadhri.androiderestaurant.databinding.ActivityDetailBinding
import fr.isen.hadhri.androiderestaurant.model.BasketData
import fr.isen.hadhri.androiderestaurant.model.DishBasket
import fr.isen.hadhri.androiderestaurant.model.DishModel
import java.io.File


class DetailActivity : ToolActivity() {
    private lateinit var binding: ActivityDetailBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        var dish = intent.getSerializableExtra("dish") as DishModel
        initDetail(dish)
        var list = ""
        val listIngredients =
            (intent.getSerializableExtra("dish") as DishModel).getFormatedIngredients()
        for (i in listIngredients.indices) {
            list += listIngredients[i].name_fr + ", "
        }
        binding.detail.text = list
        var counter = 1
        var dishName = (intent.getSerializableExtra("dish") as DishModel)
        var price = (intent.getSerializableExtra("dish") as DishModel).prices[0].price
        var totPrice = price.toFloat() * counter
        val buttonMinus = binding.buttonMinus
        val buttonPlus = binding.buttonPlus
        binding.buttonTot.text = "Total : " + price + "€"

        buttonPlus.setOnClickListener {
            counter++
            totPrice = price.toFloat() * counter
            binding.counter.text = counter.toString()
            binding.buttonTot.text = "Total : " + totPrice.toString() + "€"
        }
        buttonMinus.setOnClickListener {
            if (counter != 1) {
                counter--
                totPrice = price.toFloat() * counter
                binding.counter.text = counter.toString()
                binding.buttonTot.text = "Total : " + totPrice.toString() + "€"
            }
        }
        val buttonTot = binding.buttonTot

        buttonTot.setOnClickListener {
            var data = ArrayList<BasketData>()
            val filename = "/panier.json"
            if (File(cacheDir.absolutePath + filename).exists()) {
                var basketNumberOfElement: Int
                Snackbar.make(it, "Ajouté au panier", Snackbar.LENGTH_LONG).show()
                if (File(cacheDir.absolutePath + filename).readText().isNotEmpty()) {
                    val recup = File(cacheDir.absolutePath + filename).readText();
                    val result = Gson().fromJson(recup, DishBasket::class.java)
                    basketNumberOfElement = result.quantity
                    for (j in result.dishName.indices) {
                        basketAdd(
                            BasketData(
                                result.dishName[j].dishName,
                                result.dishName[j].quantity
                            ), data
                        )
                    }

                    basketAdd(BasketData(dishName, counter), data)
                    basketNumberOfElement += counter
                    val editor = sharedPreferences.edit()
                    editor.putInt(basketCount, basketNumberOfElement)
                    editor.apply()
                    File(cacheDir.absolutePath + filename).writeText(
                        Gson().toJson(
                            DishBasket(
                                data,
                                basketNumberOfElement
                            )
                        )
                    )
                } else {
                    File(cacheDir.absolutePath + filename).writeText(
                        Gson().toJson(
                            DishBasket(
                                mutableListOf(BasketData(dishName, counter)),
                                1
                            )
                        )
                    )
                    val editor = sharedPreferences.edit()
                    editor.putInt(basketCount, 1)
                    editor.apply()
                }
            }
            else{
                File(cacheDir.absolutePath + filename).writeText(
                    Gson().toJson(
                        DishBasket(
                            mutableListOf(BasketData(dishName, counter)),
                            1
                        )
                    )
                )
                val editor = sharedPreferences.edit()
                editor.putInt(basketCount, 1)
                editor.apply()
            }
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }

    private fun basketAdd(
        objectToAdd: BasketData,
        data: ArrayList<BasketData>
    ) {
        var bool = false

        for (i in data.indices)
            if (objectToAdd.dishName == data[i].dishName) {
                data[i].quantity += objectToAdd.quantity
                bool = true
            }
        if (bool == false) data.add(
            BasketData(
                objectToAdd.dishName,
                objectToAdd.quantity
            )
        )

    }

    private fun initDetail(dish: DishModel) {
        binding.detailTitle.text = dish.name_fr
        binding.dishPhotoPager.adapter = DishPictureAdapter(this, dish.pictures)
    }

    companion object {
        const val APP_PREFS = "app_prefs"
        const val basketCount = "basket_count"
    }

}