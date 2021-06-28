package com.example.vocaeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class TestFragment(s: String) : Fragment() {
    var category: String = s
    var color2=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color2=(activity as MainActivity).getColor2()

        val engtokor=requireActivity().findViewById<Button>(R.id.engTokor)
        engtokor.backgroundTintList= ContextCompat.getColorStateList(requireContext(),color2)
        engtokor.setOnClickListener {
            (activity as MainActivity).replaceFragment(EngToKorFragment(category),"test2")
        }

        val kortoeng=requireActivity().findViewById<Button>(R.id.korToeng)
        kortoeng.backgroundTintList = ContextCompat.getColorStateList(requireContext(),color2)
        kortoeng.setOnClickListener {
            (activity as MainActivity).replaceFragment(KorToEngFragment(category),"test2")
        }

    }

}