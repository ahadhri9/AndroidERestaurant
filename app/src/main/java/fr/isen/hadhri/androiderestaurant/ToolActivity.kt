package fr.isen.hadhri.androiderestaurant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.TextView
import androidx.core.view.isVisible


open class ToolActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuView = menu.findItem(R.id.basket).actionView
        val count = getSharedPreferences(
            DetailActivity.APP_PREFS, Context.MODE_PRIVATE
        ).getInt(DetailActivity.basketCount, 0)
        val basketCountText = (menuView.findViewById(R.id.basketCount) as? TextView)
        if (basketCountText != null) {
            basketCountText.text = count.toString()
        }
        if (basketCountText != null) {
            basketCountText.isVisible = count > 0
        }
        menuView.setOnClickListener {
            startActivity(Intent(this, BasketActivity::class.java))
        }
        return true
    }
}