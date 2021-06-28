package com.example.vocaeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocaeng.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:MyReviewAdapter
    lateinit var myDBHelper: MyDBHelper
    lateinit var data:ArrayList<Voca>
    var binding:FragmentReviewBinding?=null
    var color2=0
    val tablename="REVIEW"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentReviewBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initRecyclerView()
    }

    private fun init() {
        color2=(activity as MainActivity).getColor2()

        myDBHelper= MyDBHelper(requireContext())
        data=myDBHelper.getAllRecord(tablename)

        if(data.size==0){
            binding!!.engword.text="단어가 없어요~"
            binding!!.engmeaning.visibility=View.INVISIBLE
            binding!!.blackbox.visibility=View.INVISIBLE
        }
        else{
            binding!!.engword.text=data[0].word
            binding!!.engmeaning.text=data[0].meaning
            binding!!.blackbox.visibility=View.VISIBLE
            binding!!.blackbox.setBackgroundResource(color2)
            binding!!.blackbox.setOnClickListener {
                binding!!.blackbox.visibility=View.GONE
                binding!!.engmeaning.visibility=View.VISIBLE
            }
        }

    }

    private fun initRecyclerView() {
        recyclerView=requireActivity().findViewById(R.id.reviewrecycler)
        recyclerView.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter= MyReviewAdapter(data,color2)
        adapter.itemClickListener=object:MyReviewAdapter.OnItemClickListener {

            override fun OnItemClick(
                holder: MyReviewAdapter.ViewHolder,
                view: View,
                data: Voca,
                position: Int
            ) {
                binding!!.engword.text=data.word
                binding!!.engmeaning.text=data.meaning
                binding!!.blackbox.visibility=View.VISIBLE
            }
        }
        recyclerView.adapter=adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}