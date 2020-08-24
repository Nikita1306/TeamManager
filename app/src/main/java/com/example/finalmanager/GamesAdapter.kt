package com.example.finalmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalmanager.gamedatabase.Game

import kotlinx.android.synthetic.main.game_info.view.*
import java.util.*

class GamesAdapter (private val allGames: List<Game>, var itemClickListener: onItemClickListener) : RecyclerView.Adapter<GamesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_info, parent,false)
        return ViewHolder(view)
    }
    @Override
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemCount() = allGames.size
    override fun onBindViewHolder(holder: GamesAdapter.ViewHolder, position: Int) {
        var game = allGames[position]
        holder.itemView.score_first_team.text = allGames[position].scoreFirst.toString()
        holder.itemView.score_second_team.text = allGames[position].scoreSecond.toString()
        holder.itemView.first_team.text = allGames[position].firstTeam
        holder.itemView.second_team.text = allGames[position].secondTeam
        holder.itemView.match_date.text = allGames[position].date
        holder.bind(game, itemClickListener)
    }
    interface onItemClickListener {
        fun onItemClicked(game: Game)
    }
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind (game: Game, clickListener: onItemClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClicked(game)
            }
        }
    }
}