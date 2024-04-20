package com.ifs21005.lostandfound

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ifs21005.lostandfound.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadFragment(HomeFragment())
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        setSupportActionBar(binding.barAtas)

        binding.barAtas.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.update -> {
                    val fragment = supportFragmentManager.findFragmentById(R.id.tempat_fragment) as? HomeFragment

                    Toast.makeText(this@MainActivity, "Reloading!", Toast.LENGTH_SHORT).show()
                    fragment?.loadData()

                    true
                }

                R.id.profil -> {
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    true
                }

                R.id.keluar -> {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                    true
                }
                R.id.complete -> {
                    binding.barAtas.title= "Completed"
                    loadFragment(CompletedFragment())
                    true
                }
                R.id.not_complete -> {
                    binding.barAtas.title= "Not Completed"
                    loadFragment(NotCompleteFragment())
                    true
                }
                R.id.is_me -> {
                    binding.barAtas.title= "Is Me"
                    loadFragment(IsMeFragment())
                    true
                }
                R.id.lost -> {
                    binding.barAtas.title = "Lost"
                    loadFragment(LostFragment())
                    true
                }
                R.id.found -> {
                    binding.barAtas.title = "Found"
                    loadFragment(FoundFragment())
                    true
                }
                else -> false
            }
        }
        binding.barBawah.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.beranda -> {
                    binding.barAtas.title = "Beranda"
                    loadFragment(HomeFragment())
                    true
                }
                R.id.tambah -> {
                    binding.barAtas.title = "Tambah"
                    loadFragment(AddFragment())
                    true
                }
                R.id.tomboltandai -> {
                    binding.barAtas.title="Lost Found Ditandai"
                    loadFragment(DitandaiFragment())
                    true
                }

                else -> false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu);
        return true
    }

    private fun loadFragment (fragment : Fragment) {
        val fragmentManager = supportFragmentManager

        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.tempat_fragment, fragment)
        fragmentTransaction.commit()
    }

}