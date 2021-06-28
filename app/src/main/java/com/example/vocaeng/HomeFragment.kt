package com.example.vocaeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vocaeng.databinding.HomeFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup

class HomeFragment : Fragment() {
    val adviceurl="https://api.adviceslip.com/advice"

    val scope= CoroutineScope(Dispatchers.IO)
    var binding: HomeFragmentBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= HomeFragmentBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAdvice()
    }


    fun getAdvice(){
        scope.launch{
            val doc= Jsoup.connect(adviceurl).ignoreContentType(true).get()
            val json= JSONObject(doc.text())
            val advice=json.getJSONObject("slip")
            val advicestr=advice.getString("advice")

            withContext(Dispatchers.Main){
                binding!!.advice.text=advicestr
            }


        }
    }

}