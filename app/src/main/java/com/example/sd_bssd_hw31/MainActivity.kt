package com.example.sd_bssd_hw31

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.commit
import edu.nmhu.sd_fileio.NotesData
import org.json.JSONArray
import java.io.*

class MainActivity : NotesData.NotesDataUpdateListener, AppCompatActivity()
{
    override fun updateNotesDependents()
    {
        createNoteFragments()
    }

    private lateinit var notesData:NotesData
    private var fid = 0

    override fun onPause()
    {
        super.onPause()
        writeDataToFile()
    }

    override fun onResume()
    {
        super.onResume()

        val jsonResult = readDataAsJSON()
        if (jsonResult != null)
        {
            loadJSONNotes(jsonResult)
            createNoteFragments()
        }
    }

    val addButton = Button(this).apply{
        text = "+"
        setBackgroundColor(Color.RED)
        id = View.generateViewId()
        layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)
        (layoutParams as RelativeLayout.LayoutParams).addRule(
            RelativeLayout.RIGHT_OF, addButton.id)
        setOnClickListener {
            if(supportFragmentManager.fragments.size <10){ //limited to 10
                supportFragmentManager.commit {
                    NotesData.addNote(Note("", "", ""))
                    val uniqueID = NotesData.getNoteList().size-1
                    add(fid, NoteFragment.newInstance(View.generateViewId(), Color.RED),null)
                }
            }
        }
    }

    private fun removeExistingNotes(){
        for (noteF in supportFragmentManager.fragments){
            supportFragmentManager.commit {
                remove(noteF)
            }
        }
    }

    private fun createNoteFragments(){
        removeExistingNotes()
        for (i in 0 until NotesData.getNoteList().size){
            supportFragmentManager.commit {
                add(fid, NoteFragment.newInstance(i, Color.WHITE), null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        NotesData.registerListener(this)
        val addButton = Button(this).apply {
            text = "+"
        }
        makeData()
        readDataAsJSON()
    }

    private fun makeData()
    {
        notesData = NotesData(applicationContext)
        for (i in 1..5)
        {
            val todo = Note("name $i",
                "for the birthday party.", null.toString())
            notesData.addNote(todo)
        }
    }

    private fun loadJSONNotes(data: JSONArray)
    {
        val notesData2 = NotesData(applicationContext).apply {
            loadNotes(data)
        }
        Log.d("MainActivity result", notesData2.toString())
    }

    private fun readData()
    {
        var fileInputStream: FileInputStream? = openFileInput("notes.json")
        var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while (run {
                text = bufferedReader.readLine()
                text
            }
            != null) {
            stringBuilder.append(text)
        }
        fileInputStream?.close()
        Log.d("MainActivity file", stringBuilder.toString())
    }

    private fun writeDataToFile(noteData: NotesData)
    {
        val file: String = "notes.json"
        val data: String = noteData.toString()
        try {
            val fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun readDataAsJSON(): JSONArray?
    {
        try {
            val fileOutputStream: FileInputStream? = openFileInput("notes.json")
            val inputStreamReader: InputStreamReader = InputStreamReader(fileOutputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while (run {
                    text = bufferedReader.readLine()
                    text
                }
                != null) {
                stringBuilder.append(text)
            }
            fileOutputStream?.close()
            return JSONArray(stringBuilder.toString())
        } catch (e: FileNotFoundException)
        {
            return null
        }
    }

}


