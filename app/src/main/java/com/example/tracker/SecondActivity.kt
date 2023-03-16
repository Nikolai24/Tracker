package com.example.tracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap


class SecondActivity : AppCompatActivity() {
    private var action: String = ""
    private var name: String = "Habit"
    private var description: String = ""
    private var priority: String = "High"
    private var type: String = "Good habit"
    private var quantity: Int = 0
    private var periodicity: Int = 0
    private lateinit var nameHabit: EditText
    private lateinit var descriptionHabit: EditText
    private lateinit var quantityHabit: EditText
    private lateinit var periodicityHabit: EditText
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val image = findViewById<ImageView>(R.id.image1)
        val linear = findViewById<LinearLayout>(R.id.linear_layout)
        val colorText = findViewById<TextView>(R.id.color_text)
        val color = findViewById<TextView>(R.id.color)
        image.setOnClickListener { view ->
            val values = IntArray(2)
            view.getLocationOnScreen(values)
            val bitmap = linear.background.toBitmap(values[0]+70, values[1]+70)
            val pixel = bitmap.getPixel(values[0], values[1])
            colorText.text = pixel.toString()
            color.setBackgroundColor(pixel)
        }

        val spinner: Spinner = findViewById(R.id.priorities_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.priorities_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        nameHabit = findViewById(R.id.name)
        descriptionHabit = findViewById(R.id.description)
        quantityHabit = findViewById(R.id.quantity)
        periodicityHabit = findViewById(R.id.periodicity)
        radioGroup = findViewById(R.id.radios)
        val saveButton: Button = findViewById(R.id.save_habit)
        val cancelButton: Button = findViewById(R.id.cancel)
        action = intent.getStringExtra("action").toString()
        if (action == "create a new habit"){
            val habit: Habit = Habit(name, description, priority, type, quantity, periodicity)
            saveButton.setOnClickListener {
                sendResult(habit, -1, spinner)
            }
        }
        if (action == "edit habit"){
            val item: Habit = intent.getSerializableExtra("item") as Habit
            val position: Int = intent.getIntExtra("position", 0)
            nameHabit.setText(item.name)
            descriptionHabit.setText(item.description)
            quantityHabit.setText(item.quantity!!.toString())
            periodicityHabit.setText(item.periodicity!!.toString())
            if (item.type == "Good habit"){
                radioGroup.check(R.id.good_habit)
            } else {
                radioGroup.check(R.id.bad_habit)
            }
            if (item.priority == "Medium"){
                spinner.setSelection(1)
            }
            if (item.priority == "Low"){
                spinner.setSelection(2)
            }
            saveButton.setOnClickListener {
                sendResult(item, position, spinner)
            }
        }
        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    fun onRadioButtonClicked(view: View) {
        val checked = (view as RadioButton).isChecked
        when (view.getId()) {
            R.id.good_habit -> if (checked) {
                type = "Good habit"
            }
            R.id.bad_habit -> if (checked) {
                type = "Bad habit"
            }
        }
    }

    private fun sendResult(habit: Habit, position: Int, spinner: Spinner) {
        priority = spinner.selectedItem.toString()
        habit.priority = priority
        var text = nameHabit.text.toString()
        if (text.isNotEmpty()){
            habit.name = text
        }
        text = descriptionHabit.text.toString()
        if (text.isNotEmpty()){
            habit.description = text
        }
        text = quantityHabit.text.toString()
        if (text.isNotEmpty()){
            habit.quantity = text.toInt()
        }
        text = periodicityHabit.text.toString()
        if (text.isNotEmpty()){
            habit.periodicity = text.toInt()
        }
        habit.type = type
        val data = Intent()
        data.putExtra("Habit", habit)
        data.putExtra("position", position)
        setResult(RESULT_OK, data)
        finish()
    }
}