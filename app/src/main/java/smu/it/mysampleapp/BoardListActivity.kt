package smu.it.mysampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BoardListActivity : AppCompatActivity() {

    // 타입은 정해주고 값은 나중에 해주겠다
    lateinit var LVAdapter : ListViewAdapter

    val list = mutableListOf<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_list)

        val writeBtn = findViewById<Button>(R.id.writeBtn)
        writeBtn.setOnClickListener{

            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        //val list = mutableListOf<Model>()
        //list.add(Model("a"))
        //list.add(Model("b"))
        //list.add(Model("c"))

        LVAdapter = ListViewAdapter(list)
        val lv = findViewById<ListView>(R.id.lv)
        lv.adapter = LVAdapter

        getData()
    }

    fun getData() {

        val database = Firebase.database
        val myRef = database.getReference("board")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.d("BoardListActivity", dataSnapshot.toString())
                // ...
                for (dataModel in dataSnapshot.children) {

                    // Log.d("BoardListActivity", dataModel.toString())
                    val item = dataModel.getValue(Model::class.java)
                    Log.d("BoardListActivity", item.toString())
                    list.add(item!!)
                }
                LVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("BoardListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)

    }
}