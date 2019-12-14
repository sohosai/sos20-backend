package com.sohosai.sos.infrastructure

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import java.util.concurrent.TimeUnit


object JWTConfig {

    val jwkProvider: JwkProvider by lazy {
        JwkProviderBuilder("tsukuba.auth0.com")
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }
}