package com.virgiltest.cardoso.e3kitandroiddemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode;
import android.widget.TextView
import android.text.method.ScrollingMovementMethod
import com.virgilsecurity.android.common.model.LookupResult


var log: ((String) -> Unit) = {};

class MainActivity : AppCompatActivity() {

    lateinit var alice: Device
    lateinit var bob: Device

    lateinit var bobLookup: LookupResult
    lateinit var aliceLookup: LookupResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        findViewById<TextView>(R.id.textView).movementMethod = ScrollingMovementMethod()

        fun logTextView(log: String) {
            this.runOnUiThread {
                val view = findViewById<TextView>(R.id.textView)
                val text = view.text.toString()
                view.text = "$text\n$log"
            }
        }

        log = ::logTextView

        alice = Device("Alice", this.applicationContext)
        bob = Device("Bob", this.applicationContext)

        main()
    }

    fun main() {
        log("* Testing main methods:")

        log("\n----- EThree.initialize -----")
        initializeUsers {
            log("\n----- EThree.register -----")
            registerUsers {
                log("\n----- EThree.lookupPublicKeys -----")
                lookupPublicKeys {
                    log("\n----- EThree.encrypt & EThree.decrypt -----")
                    encryptAndDecrypt {

                    }
                }
            }
        }
    }

    fun initializeUsers(callback: () -> Unit) {
        alice.initialize {
            bob.initialize(callback)
        }
    }

    fun registerUsers(callback: () -> Unit) {
        alice.register {
            bob.register(callback)
        }
    }

    fun lookupPublicKeys(callback: () -> Unit) {
        alice.lookupPublicKeys(listOf(bob.identity)) { bobResult ->
            bobLookup = bobResult
            bob.lookupPublicKeys(listOf(alice.identity)) { aliceResult ->
                aliceLookup = aliceResult
                callback()
            }
        }
    }

    fun encryptAndDecrypt(callback: () -> Unit) {
        val aliceEncryptedText = alice.encrypt("Hello ${bob.identity}!", bobLookup)
        bob.decrypt(aliceEncryptedText, aliceLookup[alice.identity]!!)

        val bobEncryptedText = bob.encrypt("Hello ${alice.identity}!", aliceLookup)
        alice.decrypt(bobEncryptedText, bobLookup[bob.identity]!!)
    }


}
