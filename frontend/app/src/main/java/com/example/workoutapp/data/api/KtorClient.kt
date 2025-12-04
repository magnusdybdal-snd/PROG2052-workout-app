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

/**
 * Factory for creating configured Ktor HTTP client instances.
 *
 * Provides a pre-configured HttpClient with:
 * - **TLS/SSL Security**: Custom certificate authority for self-signed certificates
 * - **JSON Serialization**: Kotlinx.serialization with content negotiation
 * - **Logging**: Full request/response logging for debugging
 *
 * The client is configured to trust a custom CA certificate stored in app resources,
 * enabling secure communication with backend servers using self-signed certificates.
 */
object KtorClient {

    /**
     * Creates a configured HttpClient with TLS support for self-signed certificates.
     *
     * Sets up the client with:
     * 1. Custom X.509 certificate loaded from resources (R.raw.ca)
     * 2. KeyStore with trusted CA for SSL verification
     * 3. TrustManager configured to trust only the specified CA
     * 4. JSON content negotiation with kotlinx.serialization
     * 5. HTTP request/response logging
     *
     * **Security Note**: This configuration trusts a specific self-signed certificate,
     * which is appropriate for development/testing with a known backend. For production,
     * consider using certificates from trusted CAs.
     *
     * @param context Application context for accessing certificate resource
     * @return Configured HttpClient ready for API requests
     */
    fun create(context: Context): HttpClient {
        // Load X.509 certificate (public key) from app resources
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = context.resources.openRawResource(
            com.example.workoutapp.R.raw.ca
        )

        val ca = caInput.use { cf.generateCertificate(it) }

        // Create KeyStore with trusted CA
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null)
            setCertificateEntry("local_ca", ca)
        }

        // Create TrustManager based on KeyStore
        val tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        tmf.init(keyStore)

        // Extract the X.509 TrustManager
        val trustManager = tmf.trustManagers[0] as X509TrustManager

        // Create SSLContext using the TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)

        return HttpClient(OkHttp) {
            engine {
                config {
                    sslSocketFactory(sslContext.socketFactory, trustManager)
                    // Note: hostnameVerifier { _, _ -> true } can be used if IP SAN verification is needed
                }
            }

            // Install JSON content negotiation for automatic serialization/deserialization
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true  // Ignore unknown fields from backend
                    prettyPrint = false       // Compact JSON for network efficiency
                    encodeDefaults = true     // Include default values in serialization
                })
            }

            // Install logging for debugging network requests
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }
}