package fr.isen.hadhri.androiderestaurant


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.hadhri.androiderestaurant.databinding.CardViewDesignBinding
import com.squareup.picasso.Picasso
import fr.isen.hadhri.androiderestaurant.model.DishModel

class DishAdapter(val dishes: List<DishModel>, val onDishClicked: (DishModel) -> Unit) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    class DishViewHolder(val binding: CardViewDesignBinding): RecyclerView.ViewHolder(binding.root){
        val dishPicture = binding.dishImage
        val dishName = binding.dishName
        val dishPrice = binding.dishPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = CardViewDesignBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.dishName.text = dish.name_fr

        Picasso.get()
            .load(dishes[position].getFirstPicture())
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.dishPicture)

        holder.dishPrice.text = dishes[position].getFormatedPrice()
        val data = dishes[position]
        holder.itemView.setOnClickListener {
            onDishClicked(data)
        }
    }

    override fun getItemCount(): Int = dishes.size
}