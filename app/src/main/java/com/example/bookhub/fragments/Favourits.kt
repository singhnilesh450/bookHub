package com.example.bookhub.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookhub.R
import com.example.bookhub.adapter.DashboardRecyclerAdapter
import com.example.bookhub.adapter.FavoriteRecyclerAdapter
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import kotlinx.android.synthetic.main.fragment_favourits.*

class Favourits : Fragment() {



    lateinit var recyclerFavorite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavoriteRecyclerAdapter
    var  dbBookList= listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourits, container, false)

        recyclerFavorite=view.findViewById(R.id.recyclerFavourite)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        //This time, we'd arrange the items in a Grid Layout
        layoutManager=GridLayoutManager(activity as Context,2)
        dbBookList=RetrieveFavorites(activity as Context).execute().get()
        //A fragment cannot access the ApplicationContext directly

        if(activity!=null)
        {
            progressLayout.visibility=View.GONE
            recyclerAdapter= FavoriteRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavorite.adapter=recyclerAdapter
            recyclerFavorite.layoutManager=layoutManager
            return view;
        }

        return view
    }
    //since BookDao returns the list of BookEntity, return type will be List<BookEntity>
    class RetrieveFavorites(val context: Context): AsyncTask<Void, Void, List<BookEntity>>(){
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
            return db.bookDao().getAllBooks()
        }
    }

}

