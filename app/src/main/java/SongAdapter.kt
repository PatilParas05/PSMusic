import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.psmusic.R
import com.example.psmusic.SongItem

class SongAdapter(
    private val context: Context,
    private val songsList: List<SongItem>
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        // Inflate the single song layout part
        val view = LayoutInflater.from(context).inflate(R.layout.activity_main2, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songsList[position]
        holder.songName.text = song.title
        holder.singerName.text = song.artist.name
        Glide.with(context).load(song.album.cover).into(holder.songImage) // Dynamically load the album cover
    }

    override fun getItemCount(): Int = songsList.size

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songImage: ImageView = itemView.findViewById(R.id.songImage)
        val songName: TextView = itemView.findViewById(R.id.songName)
        val singerName: TextView = itemView.findViewById(R.id.singerName)
    }
}