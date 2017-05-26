package com.example.frost.movies

import android.graphics.Rect
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.*
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import com.example.frost.movies.API.API.Genre.Genres
import com.example.frost.movies.API.API.Movies.Movie
import com.example.frost.movies.API.API.Movies.MovieService
import com.example.frost.movies.API.API.Movies.Result
import com.example.frost.movies.Adapter.MovieAdapter
import com.example.frost.movies.Utils.Constant
import com.example.frost.movies.Utils.OnLoadMoreListener
import com.example.frost.movies.Utils.ConfigSharePreference
import com.example.frost.movies.Utils.GenreSharePreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.ArrayList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    //private var recyclerView: RecyclerView? = null
    private var movieAdapter: MovieAdapter? = null
    private var movieList: MutableList<Result?>? = null
    private var firstPage = 1
    private val service = MovieService()

    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var isLoading: Boolean = false
    private val visibleThreshold = 1
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        GetConfigurations()
        initCollapsingToolbar()

        //recyclerView = findViewById(R.id.recycler_view) as RecyclerView

        movieList = ArrayList<Result?>()
        movieAdapter = MovieAdapter(movieList, this)
        movieAdapter!!.onLoadMoreListener = OnLoadMoreListener {
            movieList!!.add(null)
            movieAdapter!!.notifyItemInserted(movieList!!.size - 1)


            //load more data
            service.getAllMovies(++firstPage).enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                    Toast.makeText(applicationContext, "Load more failed", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Movie>?, response: Response<Movie>?) {
                    if (response!!.isSuccessful) {
                        movieList!!.removeAt(movieList!!.size - 1)
                        movieAdapter!!.notifyItemRemoved(movieList!!.size)
                        movieList!!.addAll(response.body()!!.results)
                        movieAdapter!!.notifyDataSetChanged()
                        isLoading = false
                    }
                }
            })
        }


        //val mLayoutManager = GridLayoutManager(this, 2)
        val mLayoutManager = LinearLayoutManager(this);
        recycler_view.layoutManager = mLayoutManager
        // recycler_view.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = mLayoutManager.itemCount
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    onLoadMoreListener = movieAdapter!!.getOnLoadMoreListener()
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener?.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
        recycler_view.adapter = movieAdapter


        service.getAllMovies(firstPage).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful && response.body()!!.results != null) {
                    status!!.post {
                        status!!.visibility = View.INVISIBLE
                        progressBar!!.visibility = View.INVISIBLE
                    }

                    movieList!!.addAll(response.body()!!.results)
                    movieAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                status!!.post {
                    status!!.text = "Failed: " + t.message
                    progressBar!!.visibility = View.INVISIBLE
                }
            }
        })

    }

    fun GetConfigurations() {
        val configSharePref = ConfigSharePreference(applicationContext)
        configSharePref.checkConfig()

        val configs = configSharePref.config

        val base_url = configs[ConfigSharePreference.KEY_BASE_URL]
        val secure_base_url = configs[ConfigSharePreference.KEY_SECURE_BASE_URL]
        val backdrop_size = configs[ConfigSharePreference.KEY_BACKDROP_SIZE]
        val logo_size = configs[ConfigSharePreference.KEY_LOGO_SIZE]
        val poster_size = configs[ConfigSharePreference.KEY_POSTER_SIZE]
        val profile_size = configs[ConfigSharePreference.KEY_PROFILE_SIZE]
        val still_size = configs[ConfigSharePreference.KEY_STILL_SIZE]

        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        val BackDropSizes = gson.fromJson<List<String>>(backdrop_size, type)
        val LogoSizes = gson.fromJson<List<String>>(logo_size, type)
        val PosterSizes = gson.fromJson<List<String>>(poster_size, type)
        val ProfileSizes = gson.fromJson<List<String>>(profile_size, type)
        val StillSizes = gson.fromJson<List<String>>(still_size, type)

        Constant.setImage_base_url(base_url)
        Constant.setSecure_image_base_url(secure_base_url)
        Constant.setBackdrop_image_size(BackDropSizes)
        Constant.setLogo_image_size(LogoSizes)
        Constant.setPoster_image_size(PosterSizes)
        Constant.setProfile_image_size(ProfileSizes)
        Constant.setStill_image_size(StillSizes)
    }

    fun GetGenres(){
        val genreSharePref = GenreSharePreference(applicationContext)
        genreSharePref.checkGenre()

        val genresJsonString = genreSharePref.genreJsonString

        val gson = Gson()
        val type = object : TypeToken<List<Genres.Genre>>(){}.type
        val genresList = gson.fromJson<List<Genres.Genre>>(genresJsonString[GenreSharePreference.KEY_GENRES], type)

        Constant.getGenres().genres = genresList;
    }

    private fun initCollapsingToolbar() {
        colapsing_bar.title = " "
        //val appBarLayout = findViewById(R.id.app_bar) as AppBarLayout
        app_bar.setExpanded(true)

        // hiding & showing the title when toolbar expanded & collapsed
        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    colapsing_bar.title = getString(R.string.app_name)
                    isShow = true
                } else if (isShow) {
                    colapsing_bar.title = " "
                    isShow = false
                }
            }
        })
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    inner class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}
