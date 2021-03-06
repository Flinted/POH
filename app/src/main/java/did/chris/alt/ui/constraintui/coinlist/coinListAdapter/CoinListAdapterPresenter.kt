package did.chris.alt.ui.constraintui.coinlist.coinListAdapter

import did.chris.alt.base.BaseContractView
import did.chris.alt.configuration.ALTSharedPreferences
import did.chris.alt.data.coinListItem.CoinListItem
import did.chris.alt.data.dataController.DataController
import did.chris.alt.data.favouriteCoins.FavouriteCoin
import rx.Subscription
import javax.inject.Inject

class CoinListAdapterPresenter @Inject constructor(private var dataController: DataController) :
        CoinListAdapterContractPresenter {

    // Properties
    private var cacheSubscription: Subscription? = null
    private var adapter: CoinListAdapterContractView? = null

    // Lifecycle
    override fun initialise() {
        subscribeToCache()
    }

    override fun attachView(view: BaseContractView) {
        this.adapter = view as CoinListAdapterContractView
    }

    override fun detachView() {
        adapter = null
        cacheSubscription = null
    }

    override fun onDestroy() {
        detachView()
    }

    // Overrides
    override fun onFavouriteStateChanged(isFavourite: Boolean, coin: CoinListItem, position: Int) {
        val symbol = coin.symbol
        if (!isFavourite) {
            addCoinAsFavourite(symbol)
            coin.isFavourite = true
        } else {
            removeCoinAsFavourite(symbol)
            coin.isFavourite = false
        }
        adapter?.itemChangedAt(position)
    }

    // Private Functions
    private fun addCoinAsFavourite(symbol: String) {
        val favouriteCoin = FavouriteCoin(symbol)
        dataController.storeFavouriteCoin(favouriteCoin)
    }

    private fun removeCoinAsFavourite(symbol: String) {
        val favouriteCoin = dataController.getFavouriteCoin(symbol)
        favouriteCoin?.let {
            dataController.deleteFavouriteCoin(it)
        }
    }

    private fun subscribeToCache() {
        val subscription = dataController.coinRefreshSubscriber()
        cacheSubscription = subscription.first.subscribe {
            onGetCoinListSuccess(it)
            checkForListRedraw()
        }
        onGetCoinListSuccess(subscription.second)
    }


    private fun checkForListRedraw() {
        if( !ALTSharedPreferences.getCoinListRedrawRequired()) {
            return
        }
        adapter?.updateIconPack()
        ALTSharedPreferences.setCoinListRedrawRequired(false)
    }

    private fun onGetCoinListSuccess(coinListItems: List<CoinListItem>) {
        adapter?.coinList = coinListItems.toMutableList()
        adapter?.emitSortTypeChanged(ALTSharedPreferences.getSort())
    }
}
