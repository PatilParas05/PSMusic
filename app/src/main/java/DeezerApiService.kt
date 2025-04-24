import com.example.psmusic.DeezerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApiService {
    @GET("search") // Ensure it's the correct endpoint
    fun searchSongs(@Query("q") query: String): Call<DeezerResponse>
}