package com.example.vocaeng

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.vocaeng.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navigation:BottomNavigationView
    lateinit var toolbar:Toolbar
    val colorArray=arrayOf(R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color5,R.color.color6)
    val colorArray2=arrayOf(R.color.lightgreen,R.color.lightblue,R.color.lightpurple,R.color.lightyellow,R.color.lightorange,R.color.lightpink)
    var colorindex=0
    lateinit var sp:SharedPreferences
    lateinit var spEdit:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp=getSharedPreferences("save",0)
        colorindex=sp.getInt("savedcolor",0)
        initDB()
        init()
    }
    fun getColor() : Int {
        return colorArray[colorindex]
    }
    fun getColor2() : Int {
        return colorArray2[colorindex]
    }
    fun setColor(i:Int) {
        colorindex=i
    }

    private fun initDB() {
        val dbfile=getDatabasePath("vocabulary.db")
        if(!dbfile.parentFile.exists()){
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            val file=resources.openRawResource(R.raw.vocabulary)
            val filesize=file.available()
            val buffer=ByteArray(filesize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val output= FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                if(supportFragmentManager.findFragmentByTag("test2")!=null){
                    supportFragmentManager.popBackStack("test2",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                else {
                    supportFragmentManager.popBackStack("test",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }
            R.id.settings ->{
                val intent= Intent(this,SettingActivity::class.java)
                intent.putExtra("color",this.getColor2())
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun init() {
        val intent=getIntent()
        //세팅에서 테마를 변경했을 때
        if(intent!=null){
            val c=intent.getIntExtra("colorindex",colorindex)
            if(c!=-1){
                this.setColor(c)
            }
        }

        toolbar=binding.toolbar
        toolbar.setBackgroundResource(this.getColor2()) //색 설정
        setSupportActionBar(toolbar)
        val actionbar=supportActionBar!!
        actionbar.setDisplayShowTitleEnabled(false)

        replaceFragment(HomeFragment(),"navhome")

        navigation=binding.navigationbar
        navigation.itemIconTintList=ContextCompat.getColorStateList(this,this.getColor()) //색 설정
        navigation.itemTextColor=ContextCompat.getColorStateList(this,this.getColor()) //색 설정

        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navhome ->{
                    replaceFragment(HomeFragment(),"navhome")
                    val tooltitle=toolbar.findViewById<TextView>(R.id.tooltitle)
                    tooltitle.text="Voca Eng..."
                    actionbar.setDisplayHomeAsUpEnabled(false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navword ->{
                    replaceFragment(WordFragment(),"navword")
                    val tooltitle=toolbar.findViewById<TextView>(R.id.tooltitle)
                    tooltitle.text=it.title.toString()
                    actionbar.setDisplayHomeAsUpEnabled(false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navtest ->{
                    replaceFragment(ChooseTestFragment(),"navtest")
                    val tooltitle=toolbar.findViewById<TextView>(R.id.tooltitle)
                    tooltitle.text=it.title.toString()
                    actionbar.setDisplayHomeAsUpEnabled(true)
                    actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navreview ->{
                    replaceFragment(ReviewFragment(),"navreview")
                    val tooltitle=toolbar.findViewById<TextView>(R.id.tooltitle)
                    tooltitle.text=it.title.toString()
                    actionbar.setDisplayHomeAsUpEnabled(false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navmyword ->{
                    replaceFragment(MyWordFragment(),"navmyword")
                    val tooltitle=toolbar.findViewById<TextView>(R.id.tooltitle)
                    tooltitle.text=it.title.toString()
                    actionbar.setDisplayHomeAsUpEnabled(false)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->{
                    replaceFragment(HomeFragment(),"navhome")
                    val tooltitle=toolbar.findViewById<TextView>(R.id.tooltitle)
                    tooltitle.text=it.title.toString()
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }

    }

    fun replaceFragment(fragment: Fragment,tag:String){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment,tag)
        if(tag=="test" || tag=="test2"){
            fragmentTransaction.addToBackStack(tag)
        }
        else{
            supportFragmentManager.popBackStack("test",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.popBackStack("test2",FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        if(supportFragmentManager.findFragmentByTag(tag)!=null){ //똑같은 화면 반복 출력일 경우
            return
        }
        fragmentTransaction.commit()
    }

    var initTime=0L
    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount==0){ //앱 종료 두 번 눌러야 종료
            if(System.currentTimeMillis()-initTime>3000){
                Toast.makeText(this,"종료하려면 한 번 더 누르세요",Toast.LENGTH_SHORT).show()
                initTime=System.currentTimeMillis()
                return
            }
            else{
                finish()
            }
        }

        backToFragment() //백버튼 누르고 바뀔 때마다 네비게이션과 툴바 글자도 바꿔줘야함
        super.onBackPressed()


    }

    private fun backToFragment() {
        if(supportFragmentManager.findFragmentByTag("test2")!=null){
            supportFragmentManager.popBackStack("test2",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            return
        }
        else {
            supportFragmentManager.popBackStack("test",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            return
        }
    }

    override fun onStop() {
        super.onStop()
        sp=getSharedPreferences("save",0)
        spEdit=sp.edit()
        spEdit.putInt("savedcolor",colorindex)
        spEdit.apply()
    }
}


