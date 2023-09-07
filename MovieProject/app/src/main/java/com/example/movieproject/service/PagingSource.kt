package com.example.movieproject.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.moviedetail.DetailsViewModel
import com.example.movieproject.utils.BundleKeys

class CustomPagingSource(
    private val api: MovieService
) : PagingSource<Int, ArrayList<MovieDetail>>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArrayList<MovieDetail>> {
        try {
            /*
            val page = params.key ?: 1 // Start from the first page or the given key
            val response = api.getRecommendations(id, BundleKeys.API_KEY, page)

            val prevPage = if (page == 1) null else page - 1
            val nextPage = if (page < pageCount) page + 1 else null

            return LoadResult.Page(
                data = response.results,
                prevKey = prevPage,
                nextKey = nextPage
            )*/
            return LoadResult.Page(listOf(),null,null)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ArrayList<MovieDetail>>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}
