package com.example.bookhub.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.modals.Book
import com.example.bookhub.adapter.DashboardRecyclerAdapter
import com.example.bookhub.R
import com.example.bookhub.modals.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class Dashboard : Fragment() {


    lateinit var recyclerDashboard: RecyclerView
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val bookInfoList = arrayListOf<Book>()

    /*  val bookInfoList= arrayListOf<Book>(
            Book("P.S. I love You","Ceclia aheran","4.5","Rs 299",R.drawable.ps_ily),
            Book("The Great Gatsby","Ceclia aherani","4.50","Rs 2989",R.drawable.great_gatsby),
            Book("Anna Karenina","Ceclia aheran","4.5","Rs 299",R.drawable.anna_kare),
            Book("Madame Bovary","Ceclia aheran","4.5","Rs 299",R.drawable.madame),
            Book("War and Peace","Ceclia aheran","4.5","Rs 299",R.drawable.war_and_peace),
            Book("Lolita","Ceclia aheran","4.5","Rs 299",R.drawable.lolita),
            Book("Middlemarch","Ceclia aheran","4.5","Rs 299",R.drawable.middlemarch),
            Book("The Adventures of Huckleberry Finn","Ceclia aheran","4.5","Rs 299",R.drawable.adventures_finn),
            Book("Moby-Dick","Ceclia aheran","4.5","Rs 299",R.drawable.moby_dick),
            Book("The Lord of the Rings","Ceclia aheran","4.5","Rs 299",R.drawable.lord_of_rings)

    )*/
    var ratingComparator = Comparator<Book> { book1, book2 ->
        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            book1.bookName.compareTo(book2.bookName, true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)

       /* recyclerDashboard.addItemDecoration(
            DividerItemDecoration(
                recyclerDashboard.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )*/


        progressLayout=view.findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        progressBar=view.findViewById(R.id.progressBar)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"
        if(ConnectionManager().checkConnectivity(activity as Context)) {


            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        progressLayout.visibility=View.GONE
                        val success = it.getBoolean("success")

                        if (success) {

                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                recyclerDashboard = view.findViewById(R.id.dashboardrec_view)
                                layoutManager = LinearLayoutManager(activity)
                                recyclerAdapter =
                                    DashboardRecyclerAdapter(
                                        (activity as? Context)!!,
                                        bookInfoList
                                    )
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                                recyclerAdapter =
                                DashboardRecyclerAdapter(
                                    activity as Context,
                                    bookInfoList
                                )

                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Error while Parsing",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }catch (e:JSONException){
                        Toast.makeText(activity as Context,"Some unexpected error occured",Toast.LENGTH_SHORT)
                    }

                },
                    Response.ErrorListener {
                        if(activity!=null) {
                            Toast.makeText(
                                activity as Context,
                                "Volley error occured",
                                Toast.LENGTH_SHORT
                            )
                        }
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "82443d9edf0a08"
                        return headers
                    }

                }

            queue.add(jsonObjectRequest)
        }else
        {
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Is Not Found")
            dialog.setPositiveButton("Open settings"){
                text,listener->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit")
            {
                text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}