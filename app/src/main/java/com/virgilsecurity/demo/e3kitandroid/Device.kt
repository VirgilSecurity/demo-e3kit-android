package com.virgilsecurity.demo.e3kitandroid

import android.content.Context
import com.virgilsecurity.android.common.callback.OnGetTokenCallback
import com.virgilsecurity.android.common.exception.EThreeException
import com.virgilsecurity.android.common.model.FindUsersResult
import com.virgilsecurity.android.ethree.interaction.EThree
import com.virgilsecurity.common.callback.OnCompleteListener
import com.virgilsecurity.common.callback.OnResultListener
import com.virgilsecurity.sdk.cards.Card

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.measureTimeMillis

class Device(val identity: String, val context: Context) {

    var eThree: EThree? = null

    private val benchmarking = false

    fun _log(e: String) {
        log("[$identity] $e")
    }

    fun initialize(callback: () -> Unit) {

        //# start of snippet: e3kit_authenticate
        fun authenticate(): String {
            val baseUrl = "http://10.0.2.2:3000/authenticate"
            val fullUrl = URL(baseUrl)

            val urlConnection = fullUrl.openConnection() as HttpURLConnection
            urlConnection.doOutput = true
            urlConnection.doInput = true
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            urlConnection.setRequestProperty("Accept", "application/json")
            urlConnection.requestMethod = "POST"

            val cred = JSONObject()

            cred.put("identity", identity)

            val wr = urlConnection.outputStream
            wr.write(cred.toString().toByteArray(charset("UTF-8")))
            wr.close()

            val httpResult = urlConnection.responseCode
            if (httpResult == HttpURLConnection.HTTP_OK) {
                val response = InputStreamReader(urlConnection.inputStream, "UTF-8").buffered().use {
                    it.readText()
                }

                val jsonObject = JSONObject(response)

                return jsonObject.getString("authToken")
            } else {
                throw Throwable("$httpResult")
            }
        }
        //# end of snippet: e3kit_authenticate

        //# start of snippet: e3kit_jwt_callback
        fun getVirgilJwt(authToken: String): String {
            try {
                val baseUrl = "http://10.0.2.2:3000/virgil-jwt"
                val fullUrl = URL(baseUrl)

                val urlConnection = fullUrl.openConnection() as HttpURLConnection
                urlConnection.setRequestProperty("Accept", "application/json")
                urlConnection.setRequestProperty("Authorization", "Bearer $authToken")
                urlConnection.requestMethod = "GET"

                val httpResult = urlConnection.responseCode
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    val response = InputStreamReader(urlConnection.inputStream, "UTF-8").buffered().use {
                        it.readText()
                    }
                    val jsonObject = JSONObject(response)

                    return jsonObject.getString("virgilToken")
                } else {
                    throw RuntimeException("$httpResult $authToken")
                }
            } catch (e: IOException) {
                throw RuntimeException("$e")
            } catch (e: JSONException) {
                throw RuntimeException("$e")
            }
        }
        //# end of snippet: e3kit_jwt_callback

        val authToken = authenticate()

        //# start of snippet: e3kit_initialize
        eThree = EThree(identity, object : OnGetTokenCallback {
            override fun onGetToken(): String {
                return getVirgilJwt(authToken)
            }
        }, context)
        //# end of snippet: e3kit_initialize

        _log("Initialized")

        callback()
    }

    fun getEThreeInstance(): EThree {
        val eThree = eThree

        if (eThree == null) {
            val errorMessage = "eThree not initialized for $identity"
            throw Throwable(errorMessage)
        }

        return eThree
    }

    fun register(callback: () -> Unit) {
        val eThree = getEThreeInstance()

        //# start of snippet: e3kit_register
        eThree.register().addCallback(object : OnCompleteListener {
            override fun onSuccess() {
                _log("Registered")
                callback()
            }

            override fun onError(throwable: Throwable) {
                _log("Failed registering: $throwable")

                if (throwable is EThreeException) {
                    if (eThree.hasLocalPrivateKey()) { eThree.cleanup() }
                    eThree.rotatePrivateKey().addCallback(object : OnCompleteListener {
                        override fun onSuccess() {
                            _log("Rotated private key instead")
                            callback()
                        }

                        override fun onError(throwable: Throwable) {

                        }
                    })
                }
            }
        })
        //# end of snippet: e3kit_register
    }

    fun findUsers(identities: List<String>, callback: (FindUsersResult) -> Unit) {
        val eThree = getEThreeInstance()

        //# start of snippet: e3kit_find_users
        eThree.findUsers(identities).addCallback(object: OnResultListener<FindUsersResult> {
            override fun onSuccess(result: FindUsersResult) {
                _log("Looked up $identities's public key")
                callback(result)
            }

            override fun onError(throwable: Throwable) {
                _log("Failed looking up $identities's public key: $throwable")
            }
        })
        //# end of snippet: e3kit_find_users
    }

    fun encrypt(text: String, lookupResult: FindUsersResult): String {
        val eThree = getEThreeInstance()
        var encryptedText = ""
        var time: Long = 0

        try {
            val repetitions = if (benchmarking) 100 else 1
            for (i in 1..repetitions) {
                time += measureTimeMillis {
                    //# start of snippet: e3kit_encrypt
                    encryptedText = eThree.encrypt(text, lookupResult)
                    //# end of snippet: e3kit_encrypt
                }
            }

            _log("Encrypted and signed: '$encryptedText'. Took: ${time/repetitions}ms")
        } catch(e: Throwable) {
            _log("Failed encrypting and signing: $e")
        }

        return encryptedText
    }

    fun decrypt(text: String, senderCard: Card): String {
        val eThree = getEThreeInstance()
        var decryptedText = ""
        var time: Long = 0

        try {
            val repetitions = if (benchmarking) 100 else 1
            for (i in 1..repetitions) {
                time += measureTimeMillis {
                    //# start of snippet: e3kit_decrypt
                    decryptedText = eThree.decrypt(text, senderCard)
                    //# end of snippet: e3kit_decrypt
                }

            }
            _log("Decrypted and verified: $decryptedText. Took: ${time/repetitions}ms")
        } catch(e: Throwable) {
            _log("Failed decrypting and verifying: $e")
        }

        return decryptedText
    }
}