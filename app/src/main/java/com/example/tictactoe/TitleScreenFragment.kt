package com.example.tictactoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.databinding.TitleScreenFragmentBinding

class TitleScreenFragment : Fragment() {
    private var _binding: TitleScreenFragmentBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: GameSharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TitleScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playWithAiBtn.setOnClickListener {
            sharedViewModel.setGameType(GameType.PVA)
            findNavController().navigate(R.id.action_titleScreenFragment_to_pickSideFragment)
        }

        binding.playWithFriendBtn.setOnClickListener {
            sharedViewModel.setGameType(GameType.PVP)
            findNavController().navigate(R.id.action_titleScreenFragment_to_pickSideFragment)
        }

    }
}

