package com.example.tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.adapter.DataAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: DataAdapter
    private lateinit var fab: FloatingActionButton
    private var habits: ArrayList<Habit> = arrayListOf()

    private val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.data != null) {
            val item = it.data!!.getSerializableExtra("Habit") as Habit
            val position = it.data!!.getIntExtra("position", 0)
            if (position == -1) {
                habits.add(item)
            } else {
                habits[position] = item
            }
            dataAdapter.setHabits(habits)
        } else {
            val toast = Toast.makeText(this, "The list of habits has not changed", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                habits = getSerializable("habits") as ArrayList<Habit>
            }
        }
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        dataAdapter = DataAdapter(habits, listener)
        recyclerView.adapter = dataAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        fab = findViewById(R.id.floating_action_button)
        fab.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("action", "create a new habit")
            getAction.launch(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putSerializable("habits", habits)
        }
        super.onSaveInstanceState(outState)
    }

    private val listener: DataAdapter.OnItemClickListener = object: DataAdapter.OnItemClickListener{
        override fun onItemClick(item: Habit, position: Int) {
            startSecondActivity(item, position)
        }
    }

    private fun startSecondActivity(item: Habit, position: Int){
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("action", "edit habit")
        intent.putExtra("item", item)
        intent.putExtra("position", position)
        getAction.launch(intent)
    }
}