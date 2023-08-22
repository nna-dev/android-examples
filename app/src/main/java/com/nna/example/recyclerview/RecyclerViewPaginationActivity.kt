package com.nna.example.recyclerview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.nna.example.R
import com.nna.example.databinding.ActivityRecyclerViewPaginationBinding
import com.nna.example.extensions.addVerticalItemSpacing
import kotlinx.coroutines.launch

class RecyclerViewPaginationActivity : AppCompatActivity() {
    companion object {
        fun createIntent(context: Context) =
            Intent(context, RecyclerViewPaginationActivity::class.java)
    }

    private val viewModel: RecyclerViewPaginationViewModel by viewModels()
    private lateinit var binding: ActivityRecyclerViewPaginationBinding
    private val pagingAdapter: PaginationAdapter by lazy {
        PaginationAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewPaginationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            adapter = pagingAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addVerticalItemSpacing(
                resources.getDimensionPixelSize(R.dimen.margin),
                resources.getDimensionPixelSize(R.dimen.margin)
            )
            var isScrolling = false
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    isScrolling = newState != SCROLL_STATE_IDLE
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager = layoutManager as LinearLayoutManager
                    val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
                    if (isScrolling &&
                        lastVisiblePosition >= linearLayoutManager.itemCount - 5
                        && pagingAdapter.canPaginating &&
                        !pagingAdapter.isPaginating
                    ) {
                        viewModel.fetchData()
                    }
                }
            })
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    if (viewState.isLoading) {
                        pagingAdapter.setLoadingView(false)
                    } else if (viewState.isPaginating) {
                        pagingAdapter.setLoadingView(true)
                    } else if (viewState.errorMessage != null) {
                        pagingAdapter.setError(message = viewState.errorMessage, false)
                    } else if (viewState.isPaginationError) {
                        pagingAdapter.setError(message = "", true)
                    } else if (viewState.items.isNotEmpty()) {
                        pagingAdapter.setItems(viewState.items, viewState.currentPage > 1)
                    }
                }
            }
        }
    }
}