package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import anekdotas.mindgameapplication.adapters.HostsAdapter
import anekdotas.mindgameapplication.databinding.ActivityShopBinding
import anekdotas.mindgameapplication.objects.ShopHostsList

class ShopActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShopBinding

    private var viewPager2:ViewPager2? = null

    private val pager2Callback = object:ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

        /* If we will have a button, this will be useful
            if (position == ShopHostsList.hostPersonalities.size - 1) {
                binding.*ButtonName*.text = "Finish"
            } else {
                binding.*ButtonName*.text = "Next"

                binding.*ButtonName*.setOnClickListener{
                    viewPager2.currentItem = position + 1
                }
            }
         */
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //ActionBar setup
        binding.actionBar.title.setText("Shop")
        binding.actionBar.actionBarBackArrow.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupViewPager(binding!!)

        binding.btnBuy.setOnClickListener {
            Toast.makeText(this, "Selected host: " + ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].hostName, Toast.LENGTH_SHORT).show()
//            Log.d("Purchase item:", "\tPosition: " + binding.vp2HostPictures.currentItem)
//            Log.d("Purchase item:", "\tName: " + ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].hostName)
        }
    }

    //ViewPager2 setup
    private fun setupViewPager(binding: ActivityShopBinding) {
        val adapter = HostsAdapter(ShopHostsList.hostPersonalities)
        viewPager2 = binding.vp2HostPictures
        viewPager2?.adapter = adapter
        viewPager2?.registerOnPageChangeCallback(pager2Callback)
        binding.diDotShopElement.setViewPager2(viewPager2!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager2?.unregisterOnPageChangeCallback(pager2Callback)
    }
}