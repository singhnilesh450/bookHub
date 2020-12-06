package com.example.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.recycler_favorites_single_row.*

class FavoriteRecyclerAdapter(val context : Context, val bookList : List<BookEntity>) : RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {
    class FavoriteViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val txtBookName : TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor : TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice : TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating : TextView = view.findViewById(R.id.txtFavBookRating)
        val imgBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)
        val llContent : LinearLayout = view.findViewById(R.id.llFavContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorites_single_row, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val book = bookList[position]

        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
       Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)
       /* val options: RequestOptions = RequestOptions() //this is for using Glide
            .placeholder(R.drawable.default_book_cover) //set default image ig glide is unable to load the image*/

        /*Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(book.bookImage)
            .into(holder.itemView.findViewById(R.id.imgBookImage))
*/
        }
    }
