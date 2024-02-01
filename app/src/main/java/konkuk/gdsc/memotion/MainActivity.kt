package konkuk.gdsc.memotion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import konkuk.gdsc.memotion.databinding.ActivityMainBinding
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fcv_main_nav_host) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            bnvMain.setupWithNavController(navController)

            this.ivMainPlusDiary.setOnClickListener {
                val intent = Intent(this@MainActivity, WritingDiaryActivity::class.java)
                startActivity(intent)
            }
        }
    }
}