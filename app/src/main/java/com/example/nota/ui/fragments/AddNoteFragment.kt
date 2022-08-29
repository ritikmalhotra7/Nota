package com.example.nota.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nota.R
import com.example.nota.databinding.FragmentAddNoteBinding
import com.example.nota.models.NoteRequest
import com.example.nota.models.NoteResponse
import com.example.nota.ui.activity.MainActivity
import com.example.nota.utils.Constants.NOTE_KEY
import com.example.nota.utils.Resource
import com.example.nota.viewmodels.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding: FragmentAddNoteBinding get() = _binding!!

    private val noteViewModel: NoteViewModel by viewModels()
    private var note: NoteResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater)
        (activity as MainActivity).hideToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialUI()
        setupUI()
        bindObservers()
    }

    private fun setInitialUI() {
        val jsonNote = arguments?.getString(NOTE_KEY)
        if (jsonNote != null) {
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note.let {
                binding.apply {
                    txtTitle.setText(note!!.title)
                    txtDescription.setText(note!!.description)
                }
            }
        } else {
            binding.addEditText.text = resources.getString(R.string.add_note)
        }
    }

    private fun setupUI() {
        binding.apply {
            btnSubmit.setOnClickListener{
                val title = binding.txtTitle.text.toString()
                val description = binding.txtDescription.text.toString()
                val noteRequest = NoteRequest(title, description)
                if (note == null) {
                    noteViewModel.createNote(noteRequest)
                }else{
                    note?.let { noteViewModel.updateNote(note!!._id, noteRequest) }
                }
            }
        }
    }

    private fun bindObservers(){
        noteViewModel.noteData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("taget","Loading")
                    (activity as MainActivity).makeLoaderVisible()
                }
                is Resource.Success -> {
                    Log.d("taget","Success")
                    findNavController().popBackStack()
                    (activity as MainActivity).makeLoaderGone()
                }
                is Resource.Error -> {
                    Log.d("taget","Error")
                    Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_LONG).show()
                    (activity as MainActivity).makeLoaderGone()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}