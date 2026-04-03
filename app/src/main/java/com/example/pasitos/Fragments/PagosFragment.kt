package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.adapters.PagosPadreAdapter

class PagosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pagos, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerPagos)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = PagosPadreAdapter()

        recycler.isNestedScrollingEnabled = false

        //Línea divisioria entre cada padre
        val divider = androidx.recyclerview.widget.DividerItemDecoration(
            requireContext(),
            (recycler.layoutManager as LinearLayoutManager).orientation
        )
        recycler.addItemDecoration(divider)

        return view
    }
}