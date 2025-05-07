package com.example.terravive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.terravive.databinding.FragmentAdminDashboardBinding

class AdminDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAdminDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
}
