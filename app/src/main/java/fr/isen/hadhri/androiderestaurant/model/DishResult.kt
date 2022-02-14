package fr.isen.hadhri.androiderestaurant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DishResult(val data: List<Category>) : Serializable

data class Category(val name_fr: String, val items: List<DishModel>) : Serializable

data class DishModel(
    val name_fr: String,
    @SerializedName("images") val pictures: List<String>,
    val prices: List<Price>,
    val ingredients: List<Ingredient>
) : Serializable {
    fun getFirstPicture() =
        if (pictures[0].isNotEmpty()) pictures[0]
        else null


    fun getFormatedPrice() = prices[0].price + "€"
    fun getFormatedIngredients() = ingredients

    data class Price(val price: String) : Serializable
    data class Ingredient(val name_fr: String) : Serializable
}
