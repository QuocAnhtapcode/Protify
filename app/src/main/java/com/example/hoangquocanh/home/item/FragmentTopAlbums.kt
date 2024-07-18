package com.example.hoangquocanh.home.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hoangquocanh.databinding.ItemDetailBinding
import com.example.hoangquocanh.home.HomeViewModel

class FragmentTopAlbums : Fragment() {
    private var _binding: ItemDetailBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.title.text = "Top Albums"
        val list = binding.list
        val adapter = ItemTopAlbums(
            onClick = {}
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context)
        homeViewModel.topAlbums.observe(viewLifecycleOwner) {
            if (it.topalbums == null) {
                adapter.submitList(emptyList())
            } else {
                adapter.submitList(it.topalbums!!.album)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}