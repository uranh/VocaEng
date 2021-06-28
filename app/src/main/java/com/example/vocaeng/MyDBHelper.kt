package com.example.vocaeng

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(val context: Context? ) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="vocabulary.db"
        val DB_VERSION=1
        val TABLE_NAMEs= arrayListOf<String>("TOEIC","TOEFL","SOONEUNG","MYWORD","REVIEW")
        val ID="id"
        val WORD="word"
        val MEANIMG="meaning"
        val CATEGORY="category"
        val ISCHECKED="isChecked"
    }

    fun getAllRecord(tablename:String):ArrayList<Voca>{
        val strsql="select * from $tablename order by $WORD;"
        val db=readableDatabase
        val data:ArrayList<Voca>
        val cursor=db.rawQuery(strsql,null)
        data=returnRecord(cursor,tablename)
        cursor.close()
        db.close()
        return data
    }
    fun getAllRecord2(category:String):ArrayList<Voca>{ //2는 MYWORD 테이블에 관한 함수
        var strsql:String
        if(category=="total"){
            strsql="select * from MYWORD order by $WORD;"
        }
        else{
            strsql="select * from MYWORD where $CATEGORY='$category' order by $WORD;"
        }
        val db=readableDatabase
        val data:ArrayList<Voca>
        val cursor=db.rawQuery(strsql,null)
        data=returnRecord(cursor,"MYWORD")
        cursor.close()
        db.close()
        return data
    }

    fun getAllTestRecord(tablename: String):ArrayList<TestVoca>{
        var strsql=""
        val limit=10
        if(tablename=="OTHERS"){
            strsql="select * from MYWORD where $CATEGORY='OTHERS' order by random() limit $limit;"
        }
        else{
            strsql="select * from $tablename order by random() limit $limit;"
        }
        val db=readableDatabase
        val data=ArrayList<TestVoca>()
        val cursor=db.rawQuery(strsql,null)
        cursor.moveToFirst()
        val attrcount=cursor.columnCount
        if(attrcount==0||cursor.count==0){ //데이터가 없다면
            return data
        }
        do{
            if(tablename=="MYWORD" || tablename=="REVIEW") {
                data.add(TestVoca(cursor.getString(1),cursor.getString(2), cursor.getString(3)))
            }
            else{
                data.add(TestVoca(cursor.getString(1),cursor.getString(2),null))
            }
        }while(cursor.moveToNext())
        cursor.close()
        db.close()
        return data
    }

    private fun returnRecord(cursor: Cursor,tablename: String):ArrayList<Voca> {
        val data=ArrayList<Voca>()

        cursor.moveToFirst()
        val attrcount=cursor.columnCount

        if(attrcount==0||cursor.count==0){ //데이터가 없다면
            return data
        }
        do{
            if(tablename=="MYWORD" || tablename=="REVIEW"){ //카테고리가 있는 경우
                data.add(Voca(cursor.getString(1),cursor.getString(2),cursor.getString(3),-1))
            }
            else{//카테고리가 없는 경우
                data.add(Voca(cursor.getString(1),cursor.getString(2),null,cursor.getInt(3)))
            }

        }while(cursor.moveToNext())

        return data
    }

    override fun onCreate(db: SQLiteDatabase?) {
        for(i in 0 until TABLE_NAMEs.size){
            var create_table=""
            if(TABLE_NAMEs[i]=="MYWORD"|| TABLE_NAMEs[i]=="REVIEW"){
                create_table="create table if not exists ${TABLE_NAMEs[i]}("+
                        "$ID integer primary key autoincrement,"+
                        "$WORD text, " +
                        "$MEANIMG text," +
                        "$CATEGORY text default '기타');"
            }
            else{
                create_table="create table if not exists ${TABLE_NAMEs[i]}("+
                        "$ID integer  primary key autoincrement,"+
                        "$WORD text, " +
                        "$MEANIMG text," +
                        "$ISCHECKED integer not null default 0);"
            }

            db!!.execSQL(create_table)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        for(i in 0 until TABLE_NAMEs.size){
            val drop_table="drop table if exists ${TABLE_NAMEs[i]};"
            db!!.execSQL(drop_table)
        }
        onCreate(db)
    }

    fun insertVoca(voca:Voca, tablename: String):Long{
        if(tablename=="MYWORD"){
            if(findVoca2(voca.word,voca.category!!)){
                return 0
            }
        }
        else{
            if(findVoca(voca.word,tablename)){
                return 0
            }
        }

        //새로운 단어 추가
        val values= ContentValues()
        values.put(WORD,voca.word)
        values.put(MEANIMG,voca.meaning)
        if(tablename=="MYWORD" || tablename=="REVIEW"){
            values.put(CATEGORY,voca.category)
        }
        else{
            values.put(ISCHECKED,voca.isChecked)
        }

        val db=writableDatabase
        val flag=db.insert(tablename,null,values) //>0

        db.close()
        return flag
    }

    fun findVoca(word: String,tablename: String): Boolean {
        val strsql="select * from $tablename where $WORD='$word';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        cursor.close()
        db.close()
        return flag
    }
    fun findVoca2(word: String,category: String): Boolean {
        val strsql="select * from MYWORD where $WORD='$word' and $CATEGORY='$category';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        cursor.close()
        db.close()
        return flag
    }
    //select * from product where word='word';
    fun deleteVoca(word: String,tablename: String): Boolean {
        val strsql="select * from $tablename where $WORD='$word';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            db.delete(tablename,"$WORD=?", arrayOf(word))
        }
        cursor.close()
        db.close()
        return flag
    }
    fun deleteVoca2(word: String,tablename: String,category: String): Boolean {
        val strsql="select * from $tablename where $WORD='$word' and $CATEGORY='$category';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            db.delete("$tablename","$WORD=?", arrayOf(word))
        }
        cursor.close()
        db.close()
        return flag
    }
    fun updateVoca(voca: Voca,tablename: String): Boolean {
        val word=voca.word
        val strsql="select * from $tablename where $WORD='$word';"
        val db=writableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            val values= ContentValues()
            values.put(WORD,voca.word)
            values.put(MEANIMG,voca.meaning)
            if(tablename=="MYWORD" || tablename=="REVIEW"){
                values.put(CATEGORY,voca.category)
            }
            else{
                values.put(ISCHECKED,voca.isChecked)
            }
            db.update(tablename,values,"$WORD=?", arrayOf(word))
        }
        cursor.close()
        db.close()
        return flag
    }
}