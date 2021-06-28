package com.example.vocaeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.vocaeng.databinding.FragmentChooseTestBinding


class ChooseTestFragment : Fragment() {
    var binding:FragmentChooseTestBinding?=null
    var color2=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentChooseTestBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color2=(activity as MainActivity).getColor2()

        binding!!.apply{
            btn1.backgroundTintList = ContextCompat.getColorStateList(requireContext(),color2)
            btn1.setOnClickListener {
                (activity as MainActivity).replaceFragment(TestFragment("TOEFL"),"test")
            }
            btn2.backgroundTintList = ContextCompat.getColorStateList(requireContext(),color2)
            btn2.setOnClickListener {
                (activity as MainActivity).replaceFragment(TestFragment("TOEIC"),"test")
            }
            btn3.backgroundTintList = ContextCompat.getColorStateList(requireContext(),color2)
            btn3.setOnClickListener {
                (activity as MainActivity).replaceFragment(TestFragment("SOONEUNG"),"test")
            }
            btn4.backgroundTintList = ContextCompat.getColorStateList(requireContext(),color2)
            btn4.setOnClickListener {
                (activity as MainActivity).replaceFragment(TestFragment("OTHERS"),"test")
            }
            btn5.backgroundTintList = ContextCompat.getColorStateList(requireContext(),color2)
            btn5.setOnClickListener {
                (activity as MainActivity).replaceFragment(TestFragment("REVIEW"),"test")
            }
        }
    }

}