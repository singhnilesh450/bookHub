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
import com.example.bookhub.modals.Book

class DashboardRecyclerAdapter(
    val context:Context,
    val itemList: ArrayList<Book>
): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {


    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtBookName : TextView = view.findViewById(R.id.txtBookName)
        val txtBookAuthor : TextView = view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice : TextView = view.findViewById(R.id.txtBookPrice)
        val txtBookRating : TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage : ImageView = view.findViewById(R.id.imgBookImage)
        val llcontent : LinearLayout = view.findViewById(R.id.llcontent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.viewholder,parent,false)
        return DashboardViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
       return  itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
      //  holder.imgBookImage.setImageResource(book.bookImage)
        val options: RequestOptions = RequestOptions() //this is for using Glide
            .placeholder(R.drawable.nil) //set default image ig glide is unable to load the image

        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(book.bookImage)
            .into(holder.itemView.findViewById<View>(R.id.imgBookImage) as ImageView)
        holder.llcontent.setOnClickListener {
            val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }
}