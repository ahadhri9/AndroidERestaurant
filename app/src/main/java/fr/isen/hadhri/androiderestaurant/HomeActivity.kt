package fr.isen.hadhri.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.hadhri.androiderestaurant.databinding.ActivityHomeBinding

class HomeActivity : ToolActivity(){
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val buttonEntries = binding.buttonEntries
        val buttonDishes = binding.buttonDishes
        val buttonDeserts = binding.buttonDeserts


        buttonEntries.setOnClickListener {
            changeActivity(getString(R.string.home_entries))
        }
        buttonDishes.setOnClickListener {
            changeActivity(getString(R.string.home_dishes))
        }
        buttonDeserts.setOnClickListener {
            changeActivity(getString(R.string.home_deserts))
        }

    }

    private fun changeActivity(category:String) {
        val changePage: Intent = Intent(this, DishesActivity::class.java)
        changePage.putExtra("category_type",category)
        Log.i("INFO","End of HomeActivity")
        startActivity(changePage)
    }
}