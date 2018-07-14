package makes.flint.alt.ui.constraintui.trackerBarChart

import makes.flint.alt.base.BasePresenter
import makes.flint.alt.data.dataController.DataController
import makes.flint.alt.data.trackerListItem.TrackerListItem
import rx.Subscription
import javax.inject.Inject

/**
 * TrackerBarChartPresenter
 * Copyright © 2018 ChrisDidThis. All rights reserved.
 */
class TrackerBarChartPresenter @Inject constructor(private val dataController: DataController) :
        BasePresenter<TrackerBarChartContractView>(), TrackerBarChartContractPresenter {
    // Properties

    private var trackerItemSubscription: Subscription? = null

    // Lifecycle
    override fun initialise() {
        subscribeToCache()
    }

    fun onDestroy() {
        trackerItemSubscription?.unsubscribe()
        trackerItemSubscription = null
        detachView()
    }

    // Private Functions

    private fun subscribeToCache() {
        val subscription = dataController.trackerRefreshSubscriber()
        trackerItemSubscription = subscription.first.subscribe { trackerListItems ->
            onTrackerListItemsRetrieved(trackerListItems)
        }
        onTrackerListItemsRetrieved(subscription.second)
    }

    private fun onTrackerListItemsRetrieved(trackerListItems: List<TrackerListItem>) {
        if (trackerListItems.isEmpty()) {
            view?.showNoTrackerEntriesMessage()
            return
        }
        view?.displayTrackerEntriesChart(trackerListItems)
    }
}