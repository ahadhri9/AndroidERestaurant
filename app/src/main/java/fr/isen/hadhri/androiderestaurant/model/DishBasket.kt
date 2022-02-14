package fr.isen.hadhri.androiderestaurant.model

import java.io.Serializable

data class DishBasket(val dishName: MutableList<BasketData>, var quantity: Int): Serializable

data class BasketData(val dishName: DishModel, var quantity : Int): Serializable

