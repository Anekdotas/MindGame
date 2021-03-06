package anekdotas.mindgameapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import anekdotas.mindgameapplication.ShopActivity
import anekdotas.mindgameapplication.java.Host
import anekdotas.mindgameapplication.databinding.ShopHostItemsBinding

class HostsAdapter(private val hostList: List<Host>)
    :RecyclerView.Adapter<HostsAdapter.HostViewHolder>() {
        inner class HostViewHolder(private val binding: ShopHostItemsBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bindItem(host: Host) {
                    binding.tvHostDesc.text = host.desc
                    binding.tvHostName.text = host.hostName
                    binding.ivHostImage.setImageResource(host.photo)
                    binding.tvPrice.text = "Price: " + host.price
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostViewHolder {
        return HostViewHolder(
            ShopHostItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HostViewHolder, position: Int) {
        holder.bindItem(hostList[position])
    }

    override fun getItemCount(): Int {
        return hostList.size
    }

}