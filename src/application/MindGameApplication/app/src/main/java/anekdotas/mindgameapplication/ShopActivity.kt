package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import anekdotas.mindgameapplication.adapters.HostsAdapter
import anekdotas.mindgameapplication.databinding.ActivityShopBinding
import anekdotas.mindgameapplication.objects.ShopHostsList
import anekdotas.mindgameapplication.objects.UserObjectConst

class ShopActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShopBinding

    private var viewPager2:ViewPager2? = null

    private val pager2Callback = object:ViewPager2.OnPageChangeCallback(){
//        override fun onPageScrollStateChanged(state: Int) {
//            Log.d("|+|", "onPageScrollStateChanged: yes")
//        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
//            Log.d("|-|", "*PageScrolled*")
            updateBuyButtonsTitle()
        }
    }

    private fun updateBuyButtonsTitle() {
        Log.d("say hello:", "Hi!")
        if (isItemPurchased()) {
            binding.btnBuy.setText("select")
        } else {
            binding.btnBuy.setText("purchase")
        }
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
            if(userCanPurchaseItem() && !isItemPurchased()){
                UserObjectConst.coins -= ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].price
                UserObjectConst.userPhoto = ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].photo
            }
            else if(isItemPurchased()){
                Toast.makeText(this, "Selected " + ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].hostName, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Not enough coins to purchase " + ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].hostName + "\n Current coins: ${UserObjectConst.coins}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isItemPurchased(): Boolean {
        return ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].id in UserObjectConst.purchasedItemIds
    }

    private fun itemIsNotPurchased(): Boolean {
        return ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].id !in UserObjectConst.purchasedItemIds
    }

    private fun userCanPurchaseItem(): Boolean {
        return UserObjectConst.coins>=ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].price
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