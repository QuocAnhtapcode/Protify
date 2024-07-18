package com.example.hoangquocanh.home.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hoangquocanh.databinding.ItemDetailBinding
import com.example.hoangquocanh.home.HomeViewModel

class FragmentTopArtist : Fragment() {
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
        binding.title.text = "Top Artist"
        val list = binding.list
        val adapter = ItemTopArtist(
            onClick = {}
        )
        list.adapter = adapter
        list.layoutManager = GridLayoutManager(
            context, 2, GridLayoutManager.VERTICAL, false
        )
        homeViewModel.topArtist.observe(viewLifecycleOwner) {
            if (it.artists == null) {
                adapter.submitList(emptyList())
            } else {
                adapter.submitList(it.artists!!.artist)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}