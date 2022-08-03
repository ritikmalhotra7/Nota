package com.example.nota.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.nota.R
import com.example.nota.databinding.FragmentMainBinding
import com.example.nota.models.NoteResponse
import com.example.nota.ui.adapters.NoteAdapter
import com.example.nota.utils.Constants.NOTE_KEY
import com.example.nota.utils.Resource
import com.example.nota.utils.TokenManager
import com.example.nota.viewmodels.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
* This is the fragment where your Notes are displayed
* */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        adapter = NoteAdapter(::onNoteClick)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel.getNotes()
        setupUI()
        bindObservers()
        setUpRecyclerView()
    }

    private fun setupUI() {
        binding.apply {
            addNote.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
    }

    private fun bindObservers() {
        noteViewModel.noteList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    adapter.submitList(it.data)
                    hideLoader()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_LONG).show()
                    hideLoader()
                }
            }
        }
    }

    private fun onNoteClick(noteResponse:NoteResponse){
        val bundle = Bundle().apply {
            putString(NOTE_KEY,Gson().toJson(noteResponse))
        }
        findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment,bundle)
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