package com.example.vocaeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vocaeng.databinding.FragmentEngToKorBinding
import java.util.*

class KorToEngFragment(category: String) : Fragment() {
    var category:String=category
    var binding: FragmentEngToKorBinding?=null
    lateinit var myDBHelper: MyDBHelper
    lateinit var data: ArrayList<TestVoca>
    val random= Random()
    var index=0
    var answer=0
    var corcount=0
    var wrongcount=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentEngToKorBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        myDBHelper= MyDBHelper(requireContext())
        data=myDBHelper.getAllTestRecord(category)
        if(data.size<3){ //데이터가 부족하다면
            Toast.makeText(requireContext(),"테스트할 단어 수가 부족합니다", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).replaceFragment(ChooseTestFragment(),"navtest")
            return
        }
        initData(index)
    }

    private fun initData(i:Int) {
        if(i==data.size){
            Toast.makeText(requireContext(),"맞은 개수 : $corcount  틀린 개수 : $wrongcount",Toast.LENGTH_LONG).show()
            (activity as MainActivity).replaceFragment(ChooseTestFragment(),"navtest")
            return
        }
        binding!!.apply {
            question.text=data[i].kor
            when(random.nextInt(3)){
                0 ->{
                    radioButton.text=data[i].eng
                    val str=getInCorrect(data[i].kor)
                    radioButton2.text=str[0]
                    radioButton3.text=str[1]
                    answer=0
                }
                1 ->{
                    radioButton2.text=data[i].eng
                    val str=getInCorrect(data[i].kor)
                    radioButton.text=str[0]
                    radioButton3.text=str[1]
                    answer=1
                }
                2 ->{
                    radioButton3.text=data[i].eng
                    val str=getInCorrect(data[i].kor)
                    radioButton.text=str[0]
                    radioButton2.text=str[1]
                    answer=2
                }
            }

            radioButton.setOnClickListener {
                if(answer==0){
                    Toast.makeText(requireContext(),"정답입니다", Toast.LENGTH_SHORT).show()
                    corcount++
                    if(category=="REVIEW"){
                        myDBHelper.deleteVoca2(data[i].eng,"REVIEW",data[i].category!!)
                    }
                }
                else{
                    Toast.makeText(requireContext(),"틀렸습니다", Toast.LENGTH_SHORT).show()
                    wrongcount++
                    if(category!="REVIEW"){
                        myDBHelper.insertVoca(Voca(data[i].eng,data[i].kor,category,-1),"REVIEW")
                    }
                }
                clearBtn()
                initData(++index)
            }
            radioButton2.setOnClickListener {
                if(answer==1){
                    Toast.makeText(requireContext(),"정답입니다", Toast.LENGTH_SHORT).show()
                    corcount++
                    if(category=="REVIEW"){
                        myDBHelper.deleteVoca2(data[i].eng,"REVIEW",data[i].category!!)
                    }
                }
                else{
                    Toast.makeText(requireContext(),"틀렸습니다", Toast.LENGTH_SHORT).show()
                    wrongcount++
                    if(category!="REVIEW"){
                        myDBHelper.insertVoca(Voca(data[i].eng,data[i].kor,category,-1),"REVIEW")
                    }
                }
                clearBtn()
                initData(++index)
            }
            radioButton3.setOnClickListener {
                if(answer==2){
                    Toast.makeText(requireContext(),"정답입니다", Toast.LENGTH_SHORT).show()
                    corcount++
                    if(category=="REVIEW") {
                        myDBHelper.deleteVoca2(data[i].eng, "REVIEW", data[i].category!!)
                    }
                }
                else{
                    Toast.makeText(requireContext(),"틀렸습니다", Toast.LENGTH_SHORT).show()
                    wrongcount++
                    if(category!="REVIEW"){
                        myDBHelper.insertVoca(Voca(data[i].eng,data[i].kor,category,-1),"REVIEW")
                    }
                }
                clearBtn()
                initData(++index)
            }
        }
    }
    fun getInCorrect(kor:String): ArrayList<String> {
        val str= ArrayList<String>()
        while(str.size!=2){
            val tempdata=data.get(random.nextInt(data.size))
            if(tempdata.kor.equals(kor)||tempdata.eng in str){
                continue
            }
            else{
                str.add(tempdata.eng)
            }
        }
        return str
    }
    fun clearBtn(){
        binding!!.radioButton.isChecked=false
        binding!!.radioButton2.isChecked=false
        binding!!.radioButton3.isChecked=false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}