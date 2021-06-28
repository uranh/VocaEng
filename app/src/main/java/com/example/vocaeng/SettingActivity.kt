package com.example.vocaeng

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.vocaeng.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingBinding
    var appcolor=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            val intent= Intent(this@SettingActivity,MainActivity::class.java)
            intent.putExtra("colorindex",-1)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun init(){
        val intent=getIntent()
        appcolor=intent.getIntExtra("color",0)

        binding.apply{
            toolbar.setBackgroundResource(appcolor) //색 설정
            setSupportActionBar(toolbar)
            val actionbar=supportActionBar!!
            actionbar.setDisplayShowTitleEnabled(false)
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

            appcolor1.setOnClickListener {
                appcolor=0
                val intent=Intent(this@SettingActivity,MainActivity::class.java)
                intent.putExtra("colorindex",appcolor)
                startActivity(intent)
                finish()
            }
            appcolor2.setOnClickListener {
                appcolor=1
                val intent=Intent(this@SettingActivity,MainActivity::class.java)
                intent.putExtra("colorindex",appcolor)
                startActivity(intent)
                finish()
            }
            appcolor3.setOnClickListener {
                appcolor=2
                val intent=Intent(this@SettingActivity,MainActivity::class.java)
                intent.putExtra("colorindex",appcolor)
                startActivity(intent)
                finish()
            }
            appcolor4.setOnClickListener {
                appcolor=3
                val intent=Intent(this@SettingActivity,MainActivity::class.java)
                intent.putExtra("colorindex",appcolor)
                startActivity(intent)
                finish()
            }
            appcolor5.setOnClickListener {
                appcolor=4
                val intent=Intent(this@SettingActivity,MainActivity::class.java)
                intent.putExtra("colorindex",appcolor)
                startActivity(intent)
                finish()
            }
            appcolor6.setOnClickListener {
                appcolor=5
                val intent=Intent(this@SettingActivity,MainActivity::class.java)
                intent.putExtra("colorindex",appcolor)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val intent= Intent(this@SettingActivity,MainActivity::class.java)
        intent.putExtra("colorindex",-1)
        startActivity(intent)
        finish()
    }
}