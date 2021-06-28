package com.example.vocaeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocaeng.databinding.AddvocaBinding
import com.example.vocaeng.databinding.EditvocaBinding
import com.example.vocaeng.databinding.FragmentMyWordBinding


class MyWordFragment : Fragment() {
    var binding:FragmentMyWordBinding?=null
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyAdapter
    lateinit var myDBHelper: MyDBHelper
    var data=ArrayList<Voca>()
    var tablename="MYWORD"
    var category="total"
    var color2=0
    var gray=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentMyWordBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initRecyclerView(category)
    }

    private fun init() {
        color2=(activity as MainActivity).getColor2()
        gray=R.color.gray

        myDBHelper= MyDBHelper(requireContext())
        binding!!.total.setOnClickListener {
            category="total"
            initRecyclerView(category)
        }
        binding!!.TOEFL.setOnClickListener {
            category="TOEFL"
            initRecyclerView(category)
        }
        binding!!.TOEIC.setOnClickListener {
            category="TOEIC"
            initRecyclerView(category)
        }
        binding!!.SOONEUNG.setOnClickListener {
            category="SOONEUNG"
            initRecyclerView(category)
        }
        binding!!.OTHERS.setOnClickListener {
            category="OTHERS"
            initRecyclerView(category)
        }

        binding!!.addvoca.setOnClickListener {
            val dlgBinding= AddvocaBinding.inflate(layoutInflater)
            val dlgbuilder= AlertDialog.Builder(requireActivity())
            dlgbuilder.setView(dlgBinding.root)
                .setTitle("단어 추가하기")
                .setPositiveButton("추가"){
                        _,_ ->
                    val word=dlgBinding.editaddword.text.toString()
                    val meaning=dlgBinding.editaddmeaning.text.toString()

                    if(word.length==0||meaning.length==0){
                        Toast.makeText(activity,"단어 추가 실패",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val flag =
                            myDBHelper.insertVoca(Voca(word, meaning, "OTHERS", 0), tablename)
                        if (flag == 0L) {
                            Toast.makeText(activity, "$word 단어가 이미 존재합니다", Toast.LENGTH_SHORT)
                                .show()
                        } else if (flag > 0L) {
                            Toast.makeText(activity, "$word 단어가 추가되었음", Toast.LENGTH_SHORT).show()
                        } else if (flag == -1L) {
                            Toast.makeText(activity, "$word 단어가 추가 실패", Toast.LENGTH_SHORT).show()
                        }
                        if (category == "total" || category == "OTHERS") {
                            initRecyclerView(category)
                        }
                    }
                }
                .setNegativeButton("취소"){
                        _,_ ->
                }
            val dlg=dlgbuilder.create()
            dlg.show()
        }
    }

    private fun initRecyclerView(category:String) {
        recyclerView=binding!!.recyclerview
        recyclerView.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        data=myDBHelper.getAllRecord2(category)
        adapter= MyAdapter(data,color2)
        adapter.itemClickListener=object:MyAdapter.OnItemClickListener {
            override fun OnItemClick(
                holder: MyAdapter.ViewHolder,
                view: View,
                data: Voca,
                position: Int
            ) {
                if(holder.meaning.visibility==View.VISIBLE){
                    holder.meaning.visibility=View.GONE
                }
                else {
                    holder.meaning.visibility= View.VISIBLE
                }
            }

            override fun OnStarClick(
                holder: MyAdapter.ViewHolder,
                view: View,
                data: Voca,
                position: Int
            ) {

            }

            override fun OnItemLongClick(
                holder: MyAdapter.ViewHolder,
                view: View,
                data: Voca,
                position: Int
            ): Boolean {
                editAlertDlg(holder,data)
                return true
            }
        }
        recyclerView.adapter=adapter
        val simpleCallback=object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteAlertDlg(viewHolder)
            }
        }
        val itemTouchHelper= ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    fun deleteAlertDlg(viewHolder: RecyclerView.ViewHolder){
        val word=data.get(viewHolder.adapterPosition).word
        val meaning=data.get(viewHolder.adapterPosition).meaning
        val category=data.get(viewHolder.adapterPosition).category
        val builder=AlertDialog.Builder(requireContext())
        builder.setMessage("해당 단어를 삭제하시겠습니까?")
            .setPositiveButton("네"){
                    _,_ ->
                adapter.removeItem(viewHolder.adapterPosition)
                myDBHelper.deleteVoca(word,tablename) //myword 데이터 삭제
                if(category!="OTHERS"){ //the other인 경우는 myword에만 저장되므로
                    myDBHelper.updateVoca(Voca(word,meaning,null,0),category!!) //category에 해당하는 테이블의 ischecked값 0으로 변경
                }
            }
            .setNegativeButton("아니오"){
                    _,_ ->
            }
        val dlg=builder.create()
        dlg.show()
    }
    fun editAlertDlg(viewHolder: RecyclerView.ViewHolder,data:Voca){
        val dlgBinding= EditvocaBinding.inflate(layoutInflater)
        dlgBinding.addwordview.text=data.word
        dlgBinding.editaddmeaning.setText(data.meaning)
        val builder=AlertDialog.Builder(requireContext())
        builder.setView(dlgBinding.root)
            .setTitle("단어 수정하기")
            .setPositiveButton("네"){
                    _,_ ->
                val word=dlgBinding.addwordview.text.toString()
                val meaning=dlgBinding.editaddmeaning.text.toString()
                adapter.editItem(viewHolder.adapterPosition,meaning) //adapter 갱신
                myDBHelper.updateVoca(Voca(word,meaning,data.category,-1),tablename) //myword 데이터 수정
                if(data.category!="theother"){ //the other인 경우는 myword에만 저장되므로
                    myDBHelper.updateVoca(Voca(word,meaning,null,1),data.category!!) //category에 해당하는 테이블도 수정
                }

            }
            .setNegativeButton("아니오"){
                    _,_ ->
            }
        val dlg=builder.create()
        dlg.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}