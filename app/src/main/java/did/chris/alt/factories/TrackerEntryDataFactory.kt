package did.chris.alt.factories

import did.chris.alt.data.TimeStamp
import did.chris.alt.data.coinListItem.CoinListItem
import did.chris.alt.data.tracker.TRANSACTION_MINED
import did.chris.alt.data.tracker.TRANSACTION_SELL
import did.chris.alt.data.tracker.TrackerDataEntry
import did.chris.alt.data.tracker.TrackerDataTransaction
import javax.inject.Inject

class TrackerEntryDataFactory @Inject constructor() {

    // Internal Functions
    internal fun makeTrackerEntryDataFor(trackerEntryData: TrackerDataEntry?,
                                         coin: CoinListItem?,
                                         exchange: String?,
                                         quantity: String?,
                                         price: String?,
                                         fees: String?,
                                         date: String,
                                         notes: String?,
                                         type: String
    ): TrackerDataEntry? {
        coin ?: return null
        val quantityInput = !quantity.isNullOrBlank()
        val priceInput = !price.isNullOrBlank() || type == TRANSACTION_MINED
        if (!quantityInput || !priceInput) {
            return null
        }
        val trackerEntry = trackerEntryData ?: TrackerDataEntry(coin.symbol, coin.name)
        val transaction = TrackerDataTransaction()
        transaction.exchange = exchange
        transaction.quantity = getQuantityForType(type, quantity)
        transaction.pricePaid = price
        transaction.fees = fees
        transaction.purchaseDate = TimeStamp(date)
        transaction.loggedDate = TimeStamp()
        transaction.notes = notes
        transaction.transactionType = type
        trackerEntry.transactions.add(transaction)
        return trackerEntry
    }

    // Private Functions
    private fun getQuantityForType(type: String, quantity: String?): String {
        quantity ?: return "0"
        if (type == TRANSACTION_SELL) {
            return "-$quantity"
        }
        return quantity
    }
}
