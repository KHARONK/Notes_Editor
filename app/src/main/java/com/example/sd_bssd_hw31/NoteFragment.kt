package com.example.sd_bssd_hw31

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import edu.nmhu.sd_fileio.NotesData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [NoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteFragment : Fragment()
{
    private var resultKey:String = ""
    private var indexVal:Int = -1

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var nameView: TextView
    private lateinit var dateView: TextView
    private lateinit var descView: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        setFragmentResultListener(resultKey) { requestKey, bundle ->
            val result = bundle.getString("nameKey")
            nameView.text = NotesData.getNoteList()[indexVal].name
            dateView.text = NotesData.getNoteList()[indexVal].date
            descView.text = NotesData.getNoteList()[indexVal].desc

        }
    }

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
   {
        nameView = TextView(context).apply{
           setHint("Name")
            text = NotesData.getNoteList()[indexVal].name
        }
        //Beginning text for left side
        dateView = TextView(context).apply{
            setHint("Date")
            text = NotesData.getNoteList()[indexVal].date
        }
        descView = TextView(context).apply{
            setHint("Description")
            text = NotesData.getNoteList()[indexVal].desc
        }

        val textHolderLL = LinearLayoutCompat(requireContext()).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(nameView)
            addView(dateView)
            addView(descView)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_LEFT)
        }
        //End of text for left Side



        //Edit button for right side
        val editButton = Button(requireContext()).apply {
           id = View.generateViewId()
            text = "Edit"
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_RIGHT)
            setOnClickListener {
               val currentDate = bundleOf(
                   "name" to nameView.text,
                   "date" to dateView.text,
                   "desc" to descView.text)
                NoteEditorDialog.newInstance(resultKey,currentDate, indexVal).show(
                    parentFragmentManager, NoteEditorDialog.TAG)
            }
        }
        //End of edit button for right side

        //Delete button for right side

        val deleteButton = Button(requireContext()).apply {
            text = "Delete"
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.LEFT_OF,editButton.id)
            setOnClickListener{
                AlertDialog.Builder(requireContext()).apply{
                    setTitle("Delete")
                    setPositiveButton("Yes", DialogInterface.OnClickListener{
                            dialogInterface, i ->
                        NotesData.deleteNote(indexVal)
                    })
                    setNegativeButton("no", null)
                    create()
                    show()
                }
            }
        }

        //End of delete Button for right side

        val relativeLayout = RelativeLayout(requireContext()).apply {
            setBackgroundColor(Color.WHITE)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
            addView(textHolderLL)
            addView(editButton)
            addView(deleteButton)
        }
          return relativeLayout

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    companion object {
        var bg:Int = Color.WHITE

        @JvmStatic
        fun newInstance(unique:Int, col:Int) =
            NoteFragment().apply {
                indexVal = unique
                resultKey= "NoteDataChange$unique"
                }
            }
    }
