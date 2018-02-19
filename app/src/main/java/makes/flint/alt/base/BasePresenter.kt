package makes.flint.alt.base

import org.jetbrains.anko.AnkoLogger

/**
 * BasePresenter
 * Copyright © 2018 Flint Makes.. All rights reserved.
 */
abstract class BasePresenter<T : BaseContractView> : BaseContractPresenter<T>, AnkoLogger {

    protected var view: T? = null

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }
}