package com.example.finalmanager.ui.gallery

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
import com.example.finalmanager.GamesAdapter
import com.example.finalmanager.R
import com.example.finalmanager.gamedatabase.Game
import com.example.finalmanager.gamedatabase.GameDatabase
import kotlinx.android.synthetic.main.fragment_gallery.*

class CalendarFragment : Fragment(), GamesAdapter.onItemClickListener {

    private lateinit var galleryViewModel: CalendarViewModel
    var database: GameDatabase? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        database = Room.databaseBuilder(requireContext(), GameDatabase::class.java, "game_database")
            .allowMainThreadQueries()
            .build()
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.add_game).setOnClickListener {
            galleryViewModel.date.value = ""
            galleryViewModel.changeGame.value = false
            galleryViewModel.opponent.value = ""
            galleryViewModel.scoreFirst.value = 0
            galleryViewModel.scoreSecond.value = 0
            galleryViewModel.addGame.value = false
            galleryViewModel.message.value = "Добавить"
            galleryViewModel.allGamesDate.value = database!!.gameDao().getGameByDate()
            findNavController().navigate(R.id.action_nav_gallery_to_gameAddFragment)
        }
        updateDatabase()
        galleryViewModel = ViewModelProviders.of(requireActivity()).get(CalendarViewModel::class.java)
        galleryViewModel.addGame.observe(viewLifecycleOwner, Observer { isAdd ->
            if (isAdd) {
                database!!.gameDao().insertGame(Game(firstTeam = resources.getString(R.string.team_name),
                    secondTeam = galleryViewModel.opponent.value.toString(),
                    scoreFirst = galleryViewModel.scoreFirst.value!!,
                    scoreSecond = galleryViewModel.scoreSecond.value!!,
                    date = galleryViewModel.date.value.toString(),
                    isPlayed = galleryViewModel.isPlayed.value!!))
                matches_count.setText(database!!.gameDao().getGamesPlayed().toString())
                updateDatabase()
                galleryViewModel.addGame.value = false
            }
        })
        galleryViewModel.changed.observe(viewLifecycleOwner, Observer { isChange ->
            if (isChange) {
                database!!.gameDao().update(
                    Game(id = galleryViewModel.changedId.value!!,firstTeam = resources.getString(R.string.team_name),
                        secondTeam = galleryViewModel.opponent.value.toString(),
                        scoreFirst = galleryViewModel.scoreFirst.value!!,
                        scoreSecond = galleryViewModel.scoreSecond.value!!,
                        date = galleryViewModel.date.value.toString(),
                        isPlayed = galleryViewModel.isPlayed.value!!
                    )
                )
                matches_count.setText(database!!.gameDao().getGamesPlayed().toString())
                updateDatabase()
                galleryViewModel.changed.value = false
            }
        })
        galleryViewModel.deleteGame.observe(viewLifecycleOwner, Observer { isDeleted ->
            if (isDeleted) {
                database!!.gameDao().delete(galleryViewModel.changedId.value!!)
                database!!.gameDao().updateId(galleryViewModel.changedId.value!!)
                updateDatabase()
                galleryViewModel.deleteGame.value = false
            }
        })
    }
    override fun onItemClicked(game: Game) {
        findNavController().navigate(R.id.action_nav_gallery_to_gameAddFragment)
        galleryViewModel.changeGame.value = true
        galleryViewModel.scoreFirst.value = game.scoreFirst
        galleryViewModel.scoreSecond.value = game.scoreSecond
        galleryViewModel.teamName.value = game.firstTeam
        galleryViewModel.opponent.value = game.secondTeam
        galleryViewModel.changedId.value = game.id
        galleryViewModel.date.value = game.date
        galleryViewModel.isPlayed.value = game.isPlayed
        galleryViewModel.message.value = "Изменить"
    }
    fun updateDatabase() {
        val allGames = database!!.gameDao().getAllGames()
        games_list!!.layoutManager = LinearLayoutManager(requireActivity())
        games_list.adapter = GamesAdapter(allGames, this)
    }
}
