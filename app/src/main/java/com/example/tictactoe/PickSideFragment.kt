package com.example.tictactoe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.databinding.PickSideFragmentBinding
import kotlinx.coroutines.launch

class PickSideFragment : Fragment() {
    private var _binding: PickSideFragmentBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: GameSharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PickSideFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startGameBtn.setOnClickListener {
            findNavController().navigate(R.id.action_pickSideFragment_to_gameFragment)
        }
        lifecycleScope.launch {
            sharedViewModel.playerSide.collect { playSide ->
                if (playSide == PlayerSide.CROSS) {
                    binding.crossIv.setImageResource(R.drawable.x)
                    binding.noughtsIv.setImageResource(R.drawable.o_disable)
                } else if (playSide == PlayerSide.NOUGHT) {
                    binding.noughtsIv.setImageResource(R.drawable.o)
                    binding.crossIv.setImageResource(R.drawable.x_disable)
                } else {
                    throw Exception("Play Side is not supported")
                }
            }
        }
        binding.crossIv.setOnClickListener {
            sharedViewModel.setPlaySide(PlayerSide.CROSS)
        }
        binding.noughtsIv.setOnClickListener {
            sharedViewModel.setPlaySide(PlayerSide.NOUGHT)
        }
    }
}