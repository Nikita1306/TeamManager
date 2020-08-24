package com.example.finalmanager

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.finalmanager.ui.gallery.CalendarViewModel
import kotlinx.android.synthetic.main.fragment_game_add.*
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

class GameAddFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel
    var date = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_game_add, container, false)
        return root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(CalendarViewModel::class.java)
        var teamVisible = false
        var dateAdded = false
        var dateIsSame = false
        class NotAllFieldsException: Exception() {
            fun showError() {
                if (!teamVisible) {
                    Toast.makeText(
                        context, "Сначала необходимо предоставить Название команды соперника",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (!dateAdded) {
                    Toast.makeText(
                        context, "Сначала необходимо предоставить Дату матча",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (dateIsSame) {
                    Toast.makeText(
                        context, "Игра с такой датой уже существует!\nПожалуйста, выберите другую дату",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        match_date_text.setText(viewModel.opponent.value)
        viewModel.opponent.observe(viewLifecycleOwner, Observer { opp ->
            opponent_add.setText(opp)
        })

        viewModel.date.observe(viewLifecycleOwner, Observer { day ->
            match_date_text.text = day
        })
        var yearPick = Calendar.getInstance().get(Calendar.YEAR)
        var monthPick = Calendar.getInstance().get(Calendar.MONTH)
        var dayPick = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        var setChecked = false
        view.findViewById<Button>(R.id.done_game_button).setOnClickListener {
            try {
                if (!teamVisible)
                    throw NotAllFieldsException()
                if (!dateAdded)
                    throw  NotAllFieldsException()
                if (dateIsSame)
                    throw  NotAllFieldsException()
                if (done_game_button.text == "Добавить") {
                    viewModel.isPlayed.value = false
                    delete_game_button.visibility = View.GONE
                    viewModel.date.value = match_date_text.text.toString()
                    viewModel.opponent.value = opponent_add.text.toString()
                    viewModel.addGame.value = true
                    viewModel.scoreFirst.value = your_goals.text.toString().toInt()
                    viewModel.scoreSecond.value = opponent_goals.text.toString().toInt()
                    if (yearPick < Calendar.getInstance().get(Calendar.YEAR)) {
                        viewModel.isPlayed.value = true
                    } else if (monthPick < Calendar.getInstance().get(Calendar.MONTH)) {
                        viewModel.isPlayed.value = true
                    } else if (dayPick < Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                        viewModel.isPlayed.value = true
                    }
                } else {
                    viewModel.changed.value = true
                    viewModel.date.value = match_date_text.text.toString()
                    viewModel.opponent.value = opponent_add.text.toString()
                    viewModel.scoreFirst.value = your_goals.text.toString().toInt()
                    viewModel.scoreSecond.value = opponent_goals.text.toString().toInt()
                    if (yearPick < Calendar.getInstance().get(Calendar.YEAR)) {
                        viewModel.isPlayed.value = true
                    } else if (monthPick < Calendar.getInstance().get(Calendar.MONTH)) {
                        viewModel.isPlayed.value = true
                    } else if (dayPick < Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                        viewModel.isPlayed.value = true
                    } else if (setChecked) {
                        viewModel.isPlayed.value = false
                    }
                }
                delete_game_button.visibility = View.GONE
                findNavController().navigate(R.id.action_gameAddFragment_to_nav_gallery)
            } catch (e: NotAllFieldsException) {
                e.showError()
            }
        }
        view.findViewById<Button>(R.id.delete_game_button).setOnClickListener {
            viewModel.deleteGame.value = true
            delete_game_button.visibility = View.GONE
            findNavController().navigate(R.id.action_gameAddFragment_to_nav_gallery)
        }
        viewModel.changeGame.observe(viewLifecycleOwner, Observer { changed ->
            if (changed) {
                delete_game_button.visibility = View.VISIBLE
                done_game_button.visibility = View.VISIBLE
                dateAdded = true
            }
        })
        viewModel.message.observe(viewLifecycleOwner, Observer { newString ->
            done_game_button.setText(newString)
        })
        viewModel.date.observe(viewLifecycleOwner, Observer { new ->
            match_date_text.setText(new)
        })
        viewModel.scoreFirst.observe(viewLifecycleOwner, Observer { new ->
            your_goals.setText(new.toString())
        })
        viewModel.scoreSecond.observe(viewLifecycleOwner, Observer { new ->
            opponent_goals.setText(new.toString())
        })
            opponent_add.doAfterTextChanged {
            //done_button.visibility = View.VISIBLE
                teamVisible = !(Pattern.matches("[а-яА-Я]*[\\s0-9]+[а-яА-Я]*", opponent_add.text.toString()) ||
                        opponent_add.text.toString().equals(""))
        }
        var d = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            yearPick = year
            monthPick = month
            dayPick = dayOfMonth
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setChecked = true
            dateAdded = true
            dateIsSame = false
            match_date_text.setText(setInitialDate())
            viewModel.allGamesDate.value!!.forEach { dates ->
                if (match_date_text.text.equals(dates) && !dateIsSame) {
                    dateIsSame = true
                }
            }
        }
        view.findViewById<Button>(R.id.date_pick_button).setOnClickListener {
            var picker = DatePickerDialog(
                requireContext(), d, date.get(Calendar.YEAR),
                date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)
            )
            picker.show()
        }
        view.findViewById<Button>(R.id.plus_your).setOnClickListener {
          viewModel.scoreFirst.value = viewModel.scoreFirst.value!!.plus(1)
        }
        view.findViewById<Button>(R.id.plus_opponent).setOnClickListener {
            viewModel.scoreSecond.value = viewModel.scoreSecond.value!!.plus(1)
        }
        view.findViewById<Button>(R.id.minus_your).setOnClickListener {
            if (viewModel.scoreFirst.value!! > 0) {
               viewModel.scoreFirst.value = viewModel.scoreFirst.value!!.minus(1)
            } else
                Toast.makeText(context, "Голы не могут быть отрицательным числом", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<Button>(R.id.minus_opponent).setOnClickListener {
            if (viewModel.scoreSecond.value!! > 0) {
               viewModel.scoreSecond.value = viewModel.scoreSecond.value!!.minus(1)
            } else
                Toast.makeText(context, "Голы не могут быть отрицательным числом", Toast.LENGTH_SHORT).show()
        }
        match_date_text.setText(setInitialDate())
    }
    fun setInitialDate() = DateUtils.formatDateTime(
        context,
        date.timeInMillis,
        DateUtils.FORMAT_SHOW_DATE + DateUtils.FORMAT_SHOW_YEAR
    )
}
