package com.example.nota.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.nota.R
import com.example.nota.databinding.FragmentMainBinding
import com.example.nota.databinding.NoteItemBinding
import com.example.nota.models.NoteResponse
import com.example.nota.ui.adapters.NoteAdapter
import com.example.nota.utils.Constants.NOTE_KEY
import com.example.nota.utils.Resource
import com.example.nota.viewmodels.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

/*
* This is the fragment where your Notes are displayed
* */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var listNotes: List<NoteResponse> = emptyList()
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    private lateinit var ctx: Context
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        ctx = requireContext()
        activity = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel.getNotes()
        setupUI()
        bindObservers()
    }

    private fun setupUI() {
        setUpRecyclerView()
        binding.apply {
            addNote.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.noteList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = NoteAdapter(::onViewClicked)
        binding.noteList.adapter = adapter
        binding.noteList.setHasFixedSize(true)
    }

    private fun onViewClicked(view: View, note: NoteResponse) {
        val itemBinding = NoteItemBinding.inflate(layoutInflater)
        if (view.id == itemBinding.optionMenu.id) {
            onOptionClicked(view, note)
        }
    }

    private fun onOptionClicked(view: View, note: NoteResponse) {
        val popupMenu = PopupMenu(activity, view)
        popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_note_item -> {
                    val bundle = Bundle().apply {
                        putString(NOTE_KEY, Gson().toJson(note))
                    }
                    findNavController().navigate(
                        R.id.action_mainFragment_to_addNoteFragment,
                        bundle
                    )
                }
                R.id.delete_note_item -> {
                    noteViewModel.deleteNote(note._id)
                }
                else -> popupMenu.dismiss()
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }

    private fun bindObservers() {
        noteViewModel.noteList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    listNotes = it.data!!
                    adapter.submitList(listNotes)
                    hideLoader()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    hideLoader()
                }
            }
        }
        noteViewModel.noteData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    noteViewModel.getNotes()
                    hideLoader()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    hideLoader()
                }
            }
        }
    }

    private fun showLoader() {
        binding.loader.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.loader.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}