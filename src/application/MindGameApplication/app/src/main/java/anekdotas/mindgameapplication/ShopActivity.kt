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
import anekdotas.mindgameapplication.objects.UserObjectConst

class ShopActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShopBinding

    private var viewPager2:ViewPager2? = null

    private val pager2Callback = object:ViewPager2.OnPageChangeCallback(){
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //ActionBar setup
        binding.actionBar.title.text = getString(R.string.shop_activity_shop)
        binding.actionBar.actionBarBackArrow.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupViewPager(binding)

        binding.btnBuy.setOnClickListener {
            Toast.makeText(this, "Selected host: " + ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].hostName, Toast.LENGTH_SHORT).show()
            UserObjectConst.userPhoto = ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].photo
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