package com.waynebloom.rotmgdpscalculator.builds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.waynebloom.rotmgdpscalculator.R
import com.waynebloom.rotmgdpscalculator.databinding.FragmentBuildsBinding

class BuildsFragment : Fragment() {

    private val viewModel: BuildsViewModel by viewModels()
    private lateinit var binding: FragmentBuildsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_builds, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.buildsViewModel = viewModel
    }

    private fun emptyLayout() {
        TODO("For if no builds are present")
    }
}