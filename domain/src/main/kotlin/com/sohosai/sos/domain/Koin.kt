package com.sohosai.sos.domain

import org.koin.core.context.GlobalContext

val KOIN by lazy {
    GlobalContext.get().koin
}