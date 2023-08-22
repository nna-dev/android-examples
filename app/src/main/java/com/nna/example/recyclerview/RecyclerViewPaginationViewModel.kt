package com.nna.example.recyclerview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class RecyclerViewPaginationViewModel : ViewModel() {
    private val items = (1..60).map {
        RecyclerItem(
            id = UUID.randomUUID().toString(),
            title = "Title $it",
            isBookmarked = false
        )
    }.toSet().toMutableList()

    private val pageSize = 20
    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    init {
        fetchData()
    }

    private fun getItemsAtPage(page: Int): List<RecyclerItem> {
        if (page < 1 || (page * pageSize > items.size)) return emptyList()
        return items.subList((page - 1) * pageSize, page * pageSize)
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            val page = _viewState.value.currentPage
            if (page == 0 && _viewState.value.items.isEmpty()) {
                _viewState.update { state ->
                    state.copy(isLoading = true)
                }
            } else {
                _viewState.update { state ->
                    state.copy(isLoading = false, isPaginating = true)
                }
            }
            delay(2000)
            val itemsAtPage = getItemsAtPage(page + 1)
            if (itemsAtPage.isEmpty()) {
                if (page == 0) {
                    _viewState.update { state ->
                        state.copy(isLoading = false, isPaginating = false)
                    }
                } else {
                    _viewState.update { state ->
                        state.copy(isPaginating = false, isPaginationError = true)
                    }
                }
            } else {
                _viewState.update { state ->
                    state.copy(
                        currentPage = state.currentPage + 1,
                        isLoading = false,
                        isPaginating = false,
                        items = itemsAtPage
                    )
                }
            }
        }
    }
}

data class ViewState(
    val errorMessage: String? = null,
    val currentPage: Int = 0,
    val isPaginating: Boolean = false,
    val isPaginationError: Boolean = false,
    val isLoading: Boolean = true,
    val items: List<RecyclerItem> = emptyList()
)
