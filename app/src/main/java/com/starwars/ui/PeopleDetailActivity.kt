package com.starwars.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.starwars.R
import com.starwars.retrofit.Status
import com.starwars.retrofit.model.HomeWorld
import com.starwars.retrofit.model.People
import com.starwars.viewmodel.PeopleDetailViewModel
import kotlinx.android.synthetic.main.activity_people_detail.*
import kotlinx.android.synthetic.main.layout_loading.*
import kotlinx.android.synthetic.main.layout_no_internet.*
import kotlinx.android.synthetic.main.toolbar.*

class PeopleDetailActivity : AppCompatActivity() {
    lateinit var viewModel: PeopleDetailViewModel

    companion object {
        const val DATA_EXTRA = "data_extra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people_detail)

        val people: People = intent.getSerializableExtra(DATA_EXTRA) as People

        setSupportActionBar(toolbar)

        viewModel =
            ViewModelProvider(this@PeopleDetailActivity).get(PeopleDetailViewModel::class.java)

        configureObserver()
        fillDataCard(people)

        if (savedInstanceState == null) {
            viewModel.getHomeWorld(people.homeworld)
        }
    }

    private fun fillDataCard(people: People) {
        with(people) {
            supportActionBar?.title = name

            txtHeight.text = getString(R.string.label_height, height)
            txtMass.text = getString(R.string.label_mass, mass)
            txtHairColor.text = getString(R.string.label_hair_color, hair_color)
            txtSkinColor.text = getString(R.string.label_skin_color, skin_color)
            txtEyeColor.text = getString(R.string.label_eye_color, eye_color)
            txtBirthYear.text = getString(R.string.label_birth_year, birth_year)
            txtGender.text = getString(R.string.label_gender, gender)
        }
    }

    private fun fillDataHomeWorld(homeWorld: HomeWorld) {
        with(homeWorld) {
            txtNameHomeWorld.text = name
            txtRotationPeriod.text = getString(R.string.label_rotation_period, rotation_period)
            txtOrbitalPeriod.text = getString(R.string.label_orbital_period, orbital_period)
            txtDiameter.text = getString(R.string.label_diameter, diameter)
            txtClimate.text = getString(R.string.label_climate, climate)
            txtGravity.text = getString(R.string.label_gravity, gravity)
            txtTerrain.text = getString(R.string.label_terrain, terrain)
            txtPopulation.text = getString(R.string.label_population, population)
        }
    }

    private fun configureObserver() {
        viewModel.getHomeWorldLiveData().observe(this, Observer { homeWorld ->
            fillDataHomeWorld(homeWorld)
        })

        viewModel.getNetworkStateLiveData().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    no_internet.visibility = View.GONE
                }
                Status.FAILED -> {
                    progress_bar.visibility = View.GONE
                    no_internet.visibility = View.VISIBLE
                }
                Status.RUNNING -> {
                    no_internet.visibility = View.GONE
                    progress_bar.visibility = View.VISIBLE
                }
            }
        })
    }
}
