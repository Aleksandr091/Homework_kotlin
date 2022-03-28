package ru.chistov.homework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

var note = Note(title = "Заметка", description = "Описание")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        testCycle()
    }

    private fun init() {

        val tViewF: TextView = findViewById(R.id.txtViewF)
        val tView: TextView = findViewById(R.id.txtView)
        val tViewOB: TextView = findViewById(R.id.txtViewOb)
        val btn: Button = findViewById(R.id.btn)


        btn.setOnClickListener(View.OnClickListener {
            tView.setText(note.toString())
            tViewOB.setText(Data.data.toString())

        })

    }

    object Data {
        val data = note.copy(description = "object")
    }
    fun testCycle(){
        val list = listOf(1,2,3,4,5,6,7,8,9)
        list.forEach{
            Log.d("mylogs", "$it testCycle")
        }
        repeat(6){
            Log.d("mylogs", "$it testCycle")
        }
        for (one in list){
            Log.d("mylogs", "$one testCycle")
        }
    }
}




