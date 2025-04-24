package com.example.psmusic

import SongAdapter
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity2 : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentlyPlayingPosition = -1
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val userName = sharedPreferences.getString("UserName", "Guest") ?: "Guest"

        val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..19 -> "Good Evening"
            else -> "Good Night"
        }

        val nameDisplay = findViewById<TextView>(R.id.nameDisplay)
        nameDisplay.text = "$greeting, $userName"

        val recyclerView = findViewById<RecyclerView>(R.id.songsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchSongs(recyclerView)
    }

    private fun fetchSongs(recyclerView: RecyclerView) {
        val api = RetrofitClient.instance
        api.searchSongs("Imagine Dragons").enqueue(object : Callback<DeezerResponse> {
            override fun onResponse(call: Call<DeezerResponse>, response: Response<DeezerResponse>) {
                if (response.isSuccessful) {
                    Log.d("API Response", "Raw API Data: ${response.body()}")

                    val songsList = response.body()?.data ?: emptyList()

                    for (song in songsList) {
                        Log.d("API Response", "Song Title: ${song.title}, Preview URL: ${song.previewUrl}")
                    }

                    if (songsList.isEmpty()) {
                        Toast.makeText(this@MainActivity2, "No valid songs found!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    recyclerView.adapter = SongAdapter(songsList)
                } else {
                    Log.e("API Response", "Failed to fetch songs: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity2, "Failed to fetch songs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeezerResponse>, t: Throwable) {
                Log.e("API Response", "Error: ${t.message}")
                Toast.makeText(this@MainActivity2, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private inner class SongAdapter(private val songsList: List<SongItem>) :
        RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
            val view = LayoutInflater.from(this@MainActivity2).inflate(R.layout.song_item, parent, false)
            return SongViewHolder(view)
        }

        override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
            val adapterPosition = holder.adapterPosition // Correct position handling

            if (adapterPosition != RecyclerView.NO_POSITION) { // Ensure valid position
                val song = songsList[adapterPosition]
                holder.songName.text = song.title
                holder.singerName.text = song.artist.name
                Glide.with(holder.itemView.context).load(song.album.cover).into(holder.songImage)

                // Update button state
                holder.playPauseButton.setImageResource(
                    if (adapterPosition == currentlyPlayingPosition && isPlaying) android.R.drawable.ic_media_pause
                    else android.R.drawable.ic_media_play
                )

                // Handle play/pause button click
                holder.playPauseButton.setOnClickListener {
                    if (adapterPosition == currentlyPlayingPosition && isPlaying) {
                        pauseSong()
                        holder.playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                    } else {
                        playSong(song.previewUrl)
                        currentlyPlayingPosition = adapterPosition
                        notifyDataSetChanged()
                    }
                }
            }
        }

        override fun getItemCount(): Int = songsList.size

        inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val songImage: ImageView = itemView.findViewById(R.id.songImage)
            val songName: TextView = itemView.findViewById(R.id.songName)
            val singerName: TextView = itemView.findViewById(R.id.singerName)
            val playPauseButton: ImageButton = itemView.findViewById(R.id.playPauseButton)
        }
    }

    private fun playSong(url: String?) {
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "Song URL is null or empty!", Toast.LENGTH_SHORT).show()
            Log.e("MediaPlayer", "Invalid song URL: $url")
            return // Prevent execution if URL is null or empty
        }

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                setOnPreparedListener {
                    it.start()
                    this@MainActivity2.isPlaying = true
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("MediaPlayer", "Playback Error: $what, Extra: $extra")
                    Toast.makeText(this@MainActivity2, "Playback failed. Try another song.", Toast.LENGTH_SHORT).show()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Unable to play song. Check URL and Internet permission.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pauseSong() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

}
