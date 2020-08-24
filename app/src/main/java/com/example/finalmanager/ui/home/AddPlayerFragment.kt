package com.example.finalmanager.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.finalmanager.R
import kotlinx.android.synthetic.main.fragment_add_player.*
import java.util.regex.Pattern

class AddPlayerFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_player, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model= ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)
        var surnameVisible = false
        var nameVisible = false
        var positionVisible = false
        class NotAllFieldsEnteredException(): Exception() {
            fun showError() {
               if (!nameVisible) {
                   Toast.makeText(
                       context, "Сначала необходимо предоставить Имя игрока",
                       Toast.LENGTH_SHORT
                   ).show()
               }
                if (!surnameVisible) {
                    Toast.makeText(
                        context, "Сначала необходимо предоставить Фамилию игрока",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!positionVisible) {
                    Toast.makeText(
                        context, "Сначала необходимо предоставить Позицию игрока",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        view.findViewById<RadioGroup>(R.id.choose_position)
            .setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                when(checkedId) {
                    R.id.keeper -> {
                        positionVisible = true
                        model.position.value = keeper.text.toString()
                    }
                    R.id.def -> {
                        positionVisible = true
                        model.position.value = def.text.toString()
                    }
                    R.id.half -> {
                        positionVisible = true
                        model.position.value = half.text.toString()
                    }
                    R.id.striker -> {
                        positionVisible = true
                        model.position.value = striker.text.toString()
                    }
                    else -> positionVisible = false
                }
            })
      model.message.observe(viewLifecycleOwner, Observer{newString ->
           done_button.text = newString
      })
        model.changeSurname.observe(viewLifecycleOwner, Observer { surname ->
            player_surname_add.setText(surname)
        })
        model.changeName.observe(viewLifecycleOwner, Observer { name ->
            player_name_add.setText(name)
        })
        model.changePosition.observe(viewLifecycleOwner, Observer { pos ->
            when (pos) {
                "ВРТ" -> choose_position.check(R.id.keeper)
                "ЗАЩ" -> choose_position.check(R.id.def)
                "ПЛЗ" -> choose_position.check(R.id.half)
                "НАП" -> choose_position.check(R.id.striker)
            }
        })
        view.findViewById<Button>(R.id.plus_goals).setOnClickListener {
            model.goals.value = model.goals.value!!.plus(1)
        }
        view.findViewById<Button>(R.id.plus_assists).setOnClickListener {
            model.assists.value = model.assists.value!!.plus(1)
        }
        view.findViewById<Button>(R.id.minus_goals).setOnClickListener {
            if (model.goals.value!! > 0) {
                model.goals.value = model.goals.value!!.minus(1)
            }
            else Toast.makeText(context, "Голы не могут быть отрицательным числом", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<Button>(R.id.minus_assists).setOnClickListener {
            if (model.assists.value!! > 0) {
                model.assists.value = model.assists.value!!.minus(1)
            }
            else Toast.makeText(context, "Передачи не могут быть отрицательным числом", Toast.LENGTH_SHORT).show()
        }
        model.goals.observe(viewLifecycleOwner, Observer { goal ->
            goals_scored.setText(goal.toString())
        })
        model.assists.observe(viewLifecycleOwner, Observer { assist ->
            assists.setText(assist.toString())
        })

        player_surname_add.doAfterTextChanged {
            //done_button.visibility = View.VISIBLE
            surnameVisible = !(Pattern.matches("[а-яА-Я]*[\\s0-9]+[а-яА-Я]*", player_surname_add.text.toString()) ||
                    player_surname_add.text.toString().equals(""))
        }
        player_name_add.doAfterTextChanged {
            nameVisible = !(Pattern.matches("[а-яА-Я]*[\\s0-9]+[а-яА-Я]*", player_name_add.text.toString()) ||
                    player_name_add.text.toString().equals(""))
        }
        view.findViewById<Button>(R.id.done_button).setOnClickListener {
            try {
                if (!nameVisible)
                    throw NotAllFieldsEnteredException()
                if (!surnameVisible)
                    throw NotAllFieldsEnteredException()
                if (!positionVisible)
                    throw NotAllFieldsEnteredException()
                    if (done_button.text.equals("Добавить")) {
                        model.surname.value = player_surname_add.text.toString()
                        model.name.value = player_name_add.text.toString()
                        model.addPlayer.value = true
                    } else {
                        model.changed.value = true
                        model.surname.value = player_surname_add.text.toString()
                        model.name.value = player_name_add.text.toString()
                    }
                    delete_button.visibility = View.GONE
                    findNavController().navigate(R.id.action_addPlayerFragment_to_nav_home)
            } catch (e: NotAllFieldsEnteredException) {
                e.showError()
            }
        }
        view.findViewById<Button>(R.id.delete_button).setOnClickListener {
            model.deletePlayer.value = true
            delete_button.visibility = View.GONE
            findNavController().navigate(R.id.action_addPlayerFragment_to_nav_home)
        }
        model.changePlayer.observe(viewLifecycleOwner, Observer { isChanged ->
            if (isChanged) {
                delete_button.visibility = View.VISIBLE
                done_button.setText(model.message.value)
                model.changePlayer.value = false
            }
        })
    }
}
