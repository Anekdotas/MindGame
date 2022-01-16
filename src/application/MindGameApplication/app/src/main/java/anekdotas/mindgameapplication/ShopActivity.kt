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
import anekdotas.mindgameapplication.network.AnalyticModel
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.CoinModel
import anekdotas.mindgameapplication.objects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShopBinding

    private var viewPager2:ViewPager2? = null

    private val pager2Callback = object:ViewPager2.OnPageChangeCallback(){
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            updateBuyButtonsTitle()
        }
    }

    private fun updateBuyButtonsTitle() {
        if (isItemPurchased()) {
            binding.btnBuy.setText(R.string.shop_activity_select)
        } else {
            binding.btnBuy.setText(R.string.shop_activity_purchase)
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

        setUserCoinsBalance()

        setupViewPager(binding)

        binding.btnBuy.setOnClickListener {
            if(userCanPurchaseItem() && !isItemPurchased()){
                callTakeCoins(ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].price)
                UserObjectConst.purchasedItemIds.add(binding.vp2HostPictures.currentItem)
                setUserCoinsBalance()
            }
            else if(isItemPurchased()){
                Toast.makeText(this, getString(R.string.shop_activity_selected) + ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].hostName, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,
                    getString(R.string.shop_activity_not_enough_coins) + ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].hostName
                            + "\n" + getString(R.string.shop_activity_current_coins) + " ${StatObject.analytics.coins}",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserCoinsBalance() {
        binding.tvCoinBalance.text = StatObject.analytics.coins.toString()
    }

    private fun isItemPurchased(): Boolean {
        return ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].id in UserObjectConst.purchasedItemIds
    }

    private fun userCanPurchaseItem(): Boolean {
        return StatObject.analytics.coins>=ShopHostsList.hostPersonalities[binding.vp2HostPictures.currentItem].price
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

    private fun callTakeCoins(price : Int) {
        val clientPOST = ApiClient.apiService.postCoinsDelta("${Const.ipForNetworking}/users/coins",
            "Bearer " + JwtObject.userJwt.token, CoinModel(price*-1)
        )
        clientPOST.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("CoinPost Done! Woohoo!", ""+ response.code())
                }
                else {
                    Log.d("CoinPost failed. Bruh, ${CoinModel(price)}", "" + response.code())
                }
            }
            override fun onFailure(call: Call<Void>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
        callGetAnalytics()
        Thread.sleep(100)
        setUserCoinsBalance()
    }

    private fun callGetAnalytics() {
        val client = ApiClient.apiService.getAnalytics("${Const.ipForNetworking}/users/stats", "Bearer " + JwtObject.userJwt.token)
        client.enqueue(object : Callback<AnalyticModel> {
            override fun onResponse(call: Call<AnalyticModel>, response: Response<AnalyticModel>) {
                if(response.isSuccessful){
                    Log.d("AnalyticsBody ", ""+ response.body())
                    StatObject.analytics= response.body()!!
                }
            }
            override fun onFailure(call: Call<AnalyticModel>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }

}