package com.yc.jetpacklib.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yc.jetpacklib.data.entity.YcDataSourceEntity
import com.yc.jetpacklib.extension.ycIsNotEmpty

/**
 *
 */
private const val STARTING_PAGE_INDEX = 1

abstract class YcBaseDataSource<T : Any> : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val currentPage = params.key ?: STARTING_PAGE_INDEX
        val dataSourceEntity = load(currentPage, params.loadSize)
        val nextKey = if (currentPage < dataSourceEntity.pageSum) {
            currentPage + 1
        } else {
            //没有更多数据
            null
        }
        if (dataSourceEntity.data.ycIsNotEmpty()) {
            LoadResult.Page(
                data = dataSourceEntity.data!!,
                prevKey = getPreKey(params.key),
                nextKey = nextKey
            )
        } else {
            LoadResult.Page(data = listOf(), prevKey = getPreKey(params.key), nextKey = nextKey)
        }
    } catch (e: Exception) {
        LoadResult.Error(throwable = e)
    }

    private fun getPreKey(key: Int?): Int? = if (key == null) {
        null
    } else {
        key - 1
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    abstract suspend fun load(pageIndex: Int, pageSize: Int): YcDataSourceEntity<T>
}