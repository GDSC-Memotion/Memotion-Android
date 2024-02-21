package konkuk.gdsc.memotion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.databinding.ActivityMainBinding
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryActivity

@AndroidEntryPoint
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

            ivMainPlusDiary.setOnClickListener {
                val intent = Intent(this@MainActivity, WritingDiaryActivity::class.java)
                intent.putExtra(INTENT_VERSION, 0)
                startActivity(intent)
            }
        }

        binding.ivNotification.setOnClickListener {

        }
    }

    companion object {
        const val INTENT_VERSION = "writing diary version"
    }
}