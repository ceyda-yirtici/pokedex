package com.example.movieproject.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.utils.BundleKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecPagingSource(
    private val api: MovieService,
    private val id:Int
) : PagingSource<Int, MovieDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetail> {
        try {

            val page = params.key ?: 1 // Start from the first page or the given key
            val response = api.getRecommendations(id, BundleKeys.API_KEY, page)
            val data: ArrayList<MovieDetail> = response.results

            val prevPage = if (page == 1) null else page - 1
            val nextPage = if (page < response.total_pages) page + 1 else null

            return LoadResult.Page(
                data = data,
                prevKey = prevPage,
                nextKey = nextPage
            )


        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, MovieDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

class PopularMoviesPagingSource(
    private val api: MovieService,
    val favList: MutableList<Int>?,
) : PagingSource<Int, MovieDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetail> {
        try {

            val page = params.key ?: 1 // Start from the first page or the given key
            val response = api.getMovieList(BundleKeys.API_KEY, page)

            val data: ArrayList<MovieDetail> = response.results


            for(movie in data){
                if(favList?.contains(movie.id) == true)
                    movie.heart_tag = "filled"
            }


            val prevPage = if (page == 1) null else page - 1
            val nextPage = if (page < response.total_pages) page + 1 else null

            return LoadResult.Page(
                data = data,
                prevKey = prevPage,
                nextKey = nextPage
            )


        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, MovieDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
class DiscoveredPagingSource(
    private val api: MovieService,
    private val favList: MutableList<Int>?,
    val with_genres: String,
    val minVote: Float
) : PagingSource<Int, MovieDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetail> {
        try {

            val page = params.key ?: 1 // Start from the first page or the given key
            val response = api.discover(BundleKeys.API_KEY, page, with_genres, minVote)

            val data: ArrayList<MovieDetail> = response.results


            for(movie in data){
                if(favList?.contains(movie.id) == true)
                    movie.heart_tag = "filled"
            }


            val prevPage = if (page == 1) null else page - 1
            val nextPage = if (page < response.total_pages) page + 1 else null

            return LoadResult.Page(
                data = data,
                prevKey = prevPage,
                nextKey = nextPage
            )


        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, MovieDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}


class SearchPagingSource(
    private val api: MovieService,
    private val favList: MutableList<Int>?,
    val query: String
) : PagingSource<Int, MovieDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetail> {
        try {

            val page = params.key ?: 1 // Start from the first page or the given key
            val response = api.getSearchList(BundleKeys.API_KEY, page, query)

            val data: ArrayList<MovieDetail> = response.results


            for(movie in data){
                if(favList?.contains(movie.id) == true)
                    movie.heart_tag = "filled"
            }


            val prevPage = if (page == 1) null else page - 1
            val nextPage = if (page < response.total_pages) page + 1 else null

            return LoadResult.Page(
                data = data,
                prevKey = prevPage,
                nextKey = nextPage
            )


        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, MovieDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}





