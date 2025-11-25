package com.example.workoutapp.data.api

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.*
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object KtorClient {
    // Creating http client with a self signed TLS
    fun create(context: Context): HttpClient {
        // loading in the certificate (public key)
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = context.resources.openRawResource(
            com.example.workoutapp.R.raw.ca
        )

        val ca = caInput.use { cf.generateCertificate(it) }

        // create keystore, with trusted ca
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null)
            setCertificateEntry("local_ca", ca)
        }

        // create a trust manager depending on keystore
        val tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        tmf.init(keyStore)

        // only one ca to trust with
        val trustManager = tmf.trustManagers[0] as X509TrustManager // x509 certificate type

        // sslcontext that uses the trustmanager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers,null)

        return HttpClient(OkHttp) {
            engine {
                config {
                    sslSocketFactory(sslContext.socketFactory, trustManager)
                    //hostnameVerifier { _,_ -> true } if IP san specifying is needed
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                    encodeDefaults = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    /*
    val instance: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                encodeDefaults = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
     */
}