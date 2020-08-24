package com.example.finalmanager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.finalmanager.R
import com.example.finalmanager.playerdatabase.Player
import com.example.finalmanager.PlayerAdapter
import com.example.manager.PlayerDatabase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), PlayerAdapter.onItemClickListener {

    private lateinit var model: HomeViewModel
    var database: PlayerDatabase? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        model =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        database = Room.databaseBuilder(requireContext(), PlayerDatabase::class.java, "player_database")
            .allowMainThreadQueries()
            .build()
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    view.findViewById<Button>(R.id.add_player).setOnClickListener {
        model.changeSurname.value = ""
        model.changeName.value = ""
        model.changePosition.value = ""
        model.goals.value = 0
        model.assists.value = 0
        model.message.value = "Добавить"
        findNavController().navigate(R.id.action_nav_home_to_addPlayerFragment)
    }
    model= ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)
    model.addPlayer.observe(viewLifecycleOwner, Observer { isAdd ->
        if (isAdd) {
            database!!.playerDao().insertPlayer(
                Player(firstName = model.name.value.toString(),
                    lastName = model.surname.value.toString(),
                    position = model.position.value.toString(),
                    goals = model.goals.value!!,
                    assists = model.assists.value!!
                )
            )
            val allPlayers = database!!.playerDao().getAllPlayers()
            player_list!!.layoutManager = LinearLayoutManager(requireActivity())
            player_list.adapter = PlayerAdapter(allPlayers, this)
            model.addPlayer.value = false
        }
    })
    model.changed.observe(viewLifecycleOwner, Observer { isChange ->
        if (isChange) {
            database!!.playerDao().update(
                Player(id = model.changedId.value!!,firstName = model.name.value.toString(),
                    lastName = model.surname.value.toString(),
                    position = model.position.value.toString(),
                    goals = model.goals.value!!,
                    assists = model.assists.value!!
                )
            )
            val allPlayers = database!!.playerDao().getAllPlayers()
            player_list!!.layoutManager = LinearLayoutManager(requireActivity()!!)
            player_list.adapter = PlayerAdapter(allPlayers, this)
            model.changed.value = false
        }
    })
    model.deletePlayer.observe(viewLifecycleOwner, Observer { isDeleted ->
        if (isDeleted) {
            database!!.playerDao().delete(model.changedId.value!!)
            database!!.playerDao().updateId(model.changedId.value!!)
            model.deletePlayer.value = false
            val allPlayers = database!!.playerDao().getAllPlayers()
            player_list!!.layoutManager = LinearLayoutManager(requireActivity()!!)
            player_list.adapter = PlayerAdapter(allPlayers, this)
        }
    })
    val allPlayers = database!!.playerDao().getAllPlayers()
    player_list!!.layoutManager = LinearLayoutManager(requireActivity()!!)
    player_list.adapter = PlayerAdapter(allPlayers, this)
}
override fun onItemClicked(player: Player) {
    findNavController().navigate(R.id.action_nav_home_to_addPlayerFragment)
    model.changeSurname.value = player.lastName
    model.changeName.value = player.firstName
    model.changePosition.value = player.position
    model.changePlayer.value = true
    model.changedId.value = player.id
    model.assists.value = player.assists
    model.goals.value = player.goals
    model.message.value = "Изменить"
    }
}
