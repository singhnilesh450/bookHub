package com.example.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bookhub.R
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import com.example.bookhub.modals.ConnectionManager
import org.json.JSONException
import org.json.JSONObject


lateinit var txtBookName : TextView
lateinit var txtBookAuthor : TextView
lateinit var txtBookPrice : TextView
lateinit var txtBookRatings : TextView
lateinit var txtBookDesc : TextView
lateinit var imgBookImage : ImageView
lateinit var btnAddToFavorites : Button
lateinit var progressLayout : RelativeLayout
lateinit var progressBar : ProgressBar
lateinit var toolBar : Toolbar

var bookId:String?="100"
class DescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRatings = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFavorites = findViewById(R.id.btnAddToFavorites)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Book Details"
        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some Error Occured", Toast.LENGTH_SHORT)
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some Error Occured", Toast.LENGTH_SHORT)
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    /*handling the response*/
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE

                            val bookImageURL = bookJsonObject.getString("image")

                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookRatings.text = bookJsonObject.getString("rating")
                            txtBookDesc.text = bookJsonObject.getString("description")
                            // Picasso.get().load(bookJsonObject.getString("image"))
                            //.error(R.drawable.default_book_cover).into(imgBookImage)
                            val options: RequestOptions = RequestOptions() //this is for using Glide
                                .placeholder(R.drawable.default_book_cover)
                            Glide.with(this@DescriptionActivity)
                                .setDefaultRequestOptions(options)
                                .load(bookJsonObject.getString("image"))
                                .into(findViewById<View>(R.id.imgBookImage) as ImageView)

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRatings.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImageURL
                            )

                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                btnAddToFavorites.text = getString(R.string.remove_from_favorites)
                                btnAddToFavorites.setBackgroundColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.colorFavorite
                                    )
                                )
                            } else {
                                btnAddToFavorites.text = getString(R.string.add_to_favorites)
                                btnAddToFavorites.setBackgroundColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.colorPrimaryDark
                                    )
                                )
                            }

                            btnAddToFavorites.setOnClickListener {
                                if (!isFav) {
                                    val result =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                            .get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "book added to favorites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddToFavorites.text =
                                            getString(R.string.remove_from_favorites)
                                        btnAddToFavorites.setBackgroundColor(
                                            ContextCompat.getColor(
                                                applicationContext,
                                                R.color.colorFavorite
                                            )
                                        )
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error has occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                } else {
                                    val result =
                                        DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                            .get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "book removed from favorites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddToFavorites.text =
                                            getString(R.string.add_to_favorites)
                                        btnAddToFavorites.setBackgroundColor(
                                            ContextCompat.getColor(
                                                applicationContext,
                                                R.color.colorPrimaryDark
                                            )
                                        )
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error has occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some error occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        // }
                        /* else
                        {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Error Occured",
                                Toast.LENGTH_SHORT
                            )
                        }*/
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occured",
                            Toast.LENGTH_SHORT
                        )
                    }
                },
                    Response.ErrorListener {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Volley error $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "090874971084d4"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Is Not Found")
            dialog.setPositiveButton("Open settings") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit")
            { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
        class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
            AsyncTask<Void, Void, Boolean>() {
            /*mode 1 -> check DB if book is favorite or not
            * mode 2 -> save the book into DB as favorite
            * mode 3 -> remove the favorite book
            * */
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

            override fun doInBackground(vararg p0: Void?): Boolean {
                when (mode) {
                    1 -> {
                        //check DB if book is favorite or not
                        val book: BookEntity? =
                            db.bookDao().getBookById(bookEntity.book_id.toString())
                        db.close()
                        return (book != null)
                    }

                    2 -> {
                        //save the book into DB as favorite
                        db.bookDao().insertBook(bookEntity)
                        db.close()
                        return true
                    }

                    3 -> {
                        //remove the favorite book
                        db.bookDao().deleteBook(bookEntity)
                        db.close()
                        return true
                    }
                }
                return false
            }

        }

}