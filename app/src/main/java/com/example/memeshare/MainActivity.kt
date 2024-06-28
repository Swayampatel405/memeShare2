package com.example.memeshare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val url="https://meme-api.com/gimme"
    private lateinit var imageView: ImageView
    var currentImageUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        imageView=findViewById(R.id.meme_image_view)

        loadImageFromApi();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadImageFromApi() {
        //get dataset using API
        //VOLLEY
        val queue=Volley.newRequestQueue(this);

        val request=JsonObjectRequest(Request.Method.GET,this.url,null,
            {response->
                this.currentImageUrl = response.get("url").toString();
                Log.d("Result",response.toString())
                Picasso.get().load(currentImageUrl).placeholder(R.drawable.loader).into(imageView);
            },
            {
                Log.e("error", it.toString())
                Toast.makeText(applicationContext,"Error in loading the meme from server",Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
        //picasso
    }

    fun nextMeme(view: View) {
        this.loadImageFromApi();
    }

    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type= "image/.png"
        val putExtra = intent.putExtra("Hey ! This is amazing ...", currentImageUrl);
        startActivity(putExtra);
        val chooser=Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
}