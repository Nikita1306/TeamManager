package com.example.finalmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalmanager.playerdatabase.Player
import kotlinx.android.synthetic.main.player_info.view.*

class PlayerAdapter(private val allPlayers: List<Player>, var itemClickListener: onItemClickListener) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_info, parent,false)
        return ViewHolder(view)
    }
    @Override
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemCount() = allPlayers.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var player = allPlayers[position]
        holder.itemView.name.text=allPlayers[position].firstName
        holder.itemView.surname.text = allPlayers[position].lastName
        holder.itemView.position.text = allPlayers[position].position
        holder.itemView.goals_count.text = allPlayers[position].goals.toString()
        holder.itemView.assists_count.text = allPlayers[position].assists.toString()
        holder.bind(player, itemClickListener)
    }
    interface onItemClickListener {
        fun onItemClicked(player: Player)
    }
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind (player: Player, clickListener: onItemClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClicked(player)
            }
        }
    }
}
