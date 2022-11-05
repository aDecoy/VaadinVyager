package com.example.demo2.voyager

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URI
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class VyAgerClient {


    private val client = OkHttpClient.Builder().ignoreAllSSLErrors()
        .retryOnConnectionFailure(true).build()

    //    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    val JSON: MediaType = "application/json".toMediaType()
//    val JSON = MediaType.get("application/json; charset=utf-8")


    val payload = """{"teamName": "VyAger", "password": "what_happened_to_NSB"  }"""

    //    val urlBase = "https://9px9bk3/"
    val urlBase = "https://192.168.0.71/"

    init {
    }

    fun sendStopSignal(): String {
        val request: Request = Request.Builder()
            .url(urlBase + "stop")
            .post(payload.toRequestBody(JSON))
            .build()
        client.newCall(request).execute().use { response ->
            println("Response body")
            val responseString = response.body!!.string()
            println(responseString)
            return responseString
        }
    }

    fun turnLeft(speed: Int, degrees: Int): String {
        return sendSpeedDegreeSignal("turn-left", speed, degrees)
    }

    fun turnRight(speed: Int, degrees: Int): String {
        return sendSpeedDegreeSignal("turn-right", speed, degrees)
    }


    fun moveForward(speed: Int, distance: Int): String {
        return sendSpeeDistanceSignal("move-forwards", speed, distance)
    }
    fun moveBacward(speed: Int, distance: Int): String {
        return sendSpeeDistanceSignal("move-backwards", speed, distance)
    }
    fun rightMotorForwards(speed: Int, distance: Int): String {
        return sendSpeeDistanceSignal("right-motor-forwards", speed, distance)
    }
    fun leftMotorForwards(speed: Int, distance: Int): String {
        return sendSpeeDistanceSignal("left-motor-forwards", speed, distance)
    }

    fun rfidScann(): String {
        val request: Request = Request.Builder()
            .url(urlBase + "read-rfid")
            .post(payload.toRequestBody(JSON))
            .build()
        client.newCall(request).execute().use { response ->
            val responseString = response.body!!.string()
            println(responseString)
            return responseString
        }
    }

    private fun sendSpeedDegreeSignal(command: String, speed: Int, degrees: Int): String {
        val request: Request = Request.Builder()
            .url(urlBase + "${command}?speed=$speed&degrees=$degrees")
            .post(payload.toRequestBody(JSON))
            .build()
        client.newCall(request).execute().use { response ->
            val responseString = response.body!!.string()
            println(responseString)
            return responseString
        }
    }


   private fun sendSpeeDistanceSignal(command: String, speed: Int, distance: Int): String {
        val request: Request = Request.Builder()
            .url(urlBase + "${command}?speed=$speed&distance=$distance")
            .post(payload.toRequestBody(JSON))
            .build()
       try {
           client.newCall(request).execute().use { response ->
               val responseString = response.body!!.string()
               println(responseString)
               return responseString
           }
       } catch (e: Exception) {
           println("Noe gikk galt")
           println(e.message)
           return if (e.message != null) e.message!! else "noe gikk galt"
       }
    }

//    fun sendSignal(command String, speed : Int, degrees : Int ): String {
//        val request: Request = Request.Builder().
//            .url(urlBase + "stop?speed=$speed&degrees=$degrees ")
//            .post(payload.toRequestBody(JSON))
//
//            .build()
//        while (true) {
//            println("prøver nytt kall")
//            client.newCall(request).execute().use { response ->
//               println(response)
//                if (response.isSuccessful) {
//                    for ((name, value) in response.headers) {
//                        println("$name: $value")
//                    }
//                    println("message")
//                    println(response.message)
//
//                    println("Response body")
//                    val responseString = response.body!!.string()
//                    println(responseString)
//                    return responseString
//                }
//            }
//        }
//    }


    fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
        val naiveTrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
            val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        hostnameVerifier(HostnameVerifier { _, _ -> true })
        return this
    }


}