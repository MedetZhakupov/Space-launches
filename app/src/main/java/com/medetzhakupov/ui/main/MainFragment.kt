package com.medetzhakupov.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.medetzhakupov.BuildConfig
import com.medetzhakupov.R
import com.medetzhakupov.SimpleIdlingResource
import com.medetzhakupov.data.model.SpaceLaunch
import com.medetzhakupov.data.repo.SpaceLaunchesRepo
import com.medetzhakupov.ui.viewModel
import kotlinx.android.synthetic.main.main_fragment.progress_bar
import kotlinx.android.synthetic.main.main_fragment.recycler_view
import kotlinx.android.synthetic.main.main_fragment.search_edit_text
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainFragment(
    repo: SpaceLaunchesRepo,
    private val onSpaceLaunchClicked: (SpaceLaunch) -> Unit
) : Fragment() {

    private val viewModel by viewModel { MainViewModel(repo) }

    private val spaceLaunchesAdapter = SpaceLaunchesAdapter { onSpaceLaunchClicked.invoke(it) }
        .apply { stateRestorationPolicy = PREVENT_WHEN_EMPTY }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = spaceLaunchesAdapter

        val onSearch = debounce<String> { viewModel.search(it) }

        search_edit_text.doAfterTextChanged { onSearch.invoke(it?.toString() ?: "") }

        viewModel.spaceLaunchesState.observe(viewLifecycleOwner, Observer(::render))
    }

    private fun render(newState: SpaceLaunchesState) {
        progress_bar.isVisible = newState is SpaceLaunchesState.Loading

        if (newState is SpaceLaunchesState.Loaded) {
            spaceLaunchesAdapter.submitList(newState.spaceLaunches.results) {
                if (BuildConfig.DEBUG) {
                    idlingResource?.setIdleState(true)
                }
            }
        }

        if (newState is SpaceLaunchesState.Failed) {
            spaceLaunchesAdapter.submitList(emptyList())
        }
    }

    private fun <T> debounce(
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = lifecycleScope.launch {
                delay(300L)
                destinationFunction(param)
            }
        }
    }

    private var idlingResource: SimpleIdlingResource? = null

    /**
     * Only called from test, creates and returns a new [SimpleIdlingResource].
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun getIdlingResource(): SimpleIdlingResource? {
        if (idlingResource == null) {
            idlingResource = SimpleIdlingResource()
        }
        return idlingResource
    }
}