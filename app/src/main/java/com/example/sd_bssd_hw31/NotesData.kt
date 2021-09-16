package edu.nmhu.sd_fileio
import android.content.Context
import android.util.Log
import com.example.sd_bssd_hw31.Note
import org.json.JSONArray

class NotesData (context: Context)
{
    interface NotesDataUpdateListener {

    }

    private var mContext: Context? = null
    private var notesData: ArrayList<Note> = ArrayList<Note>()

    init
    {
        //set the private value to the passed in value
        mContext = context
    }

    object NotesData {

        private  val notesData:ArrayList<Note> = ArrayList<Note>()
        interface NotesDataUpdateListener
        {
            public fun updateNotesDependents()
        }
        private var mListener: NotesDataUpdateListener? = null

        fun registerListener(Listener:NotesDataUpdateListener){
            mListener = Listener
        }

        fun deleteNote(index:Int)
        {
            notesData.removeAt(index)
            mListener?.updateNotesDependents()
        }
    }

    fun addNote(note: Note)
    {
        notesData.add(note)
    }

    fun getNoteList(): ArrayList<Note>
    {
        return notesData
    }

    fun toJSON():JSONArray
    {
        val jsonArray = JSONArray()
        for (note in notesData)
        {
            jsonArray.put(note.toJSON())
        }
        return jsonArray
    }

    fun loadNotes(data: JSONArray)
    {
            Log.d("notesData", data.length().toString())
            for (i in 0 until data.length())
            {
                val obj = data.getJSONObject(i)
                addNote(Note(
                        obj.getString("name"),
                        obj.getString("desc"),
                        obj.getString("date")))
            }
    }

    override fun toString(): String
    {
            var allNotes = ""
            for (note in notesData) {
                allNotes += note.toString()
            }
            return allNotes
        }

    }



