# Virgil E3Kit Android Demo

## Introduction

<a href="https://developer.virgilsecurity.com/docs"><img width="230px" src="https://cdn.virgilsecurity.com/assets/images/github/logos/virgil-logo-red.png" align="left" hspace="10" vspace="6"></a> This is a sample Android project for [Virgil Security](https://virgilsecurity.com)'s [E3Kit SDK](https://github.com/VirgilSecurity/virgil-e3kit-x) which simplifies work with Virgil services and presents an easy-to-use API for adding a security layer to any application. E3Kit interacts with Virgil Cards Service, Keyknox Service and Pythia Service.
Virgil E3Kit allows you to setup user encryption with multidevice support in just a few simple steps.

> The demo is using E3Kit v2.0.4.

> Note: It is a sample project and cannot be used in production.

## Prerequisites

- Android Studio
- [Virgil Developer Account](https://dashboard.virgilsecurity.com/) and a Virgil Application
- Local backend set up to generate Virgil JWT (you can find our Node.js sample [here](https://github.com/VirgilSecurity/sample-backend-nodejs))

## Set up and run demo

1. Make sure the local backend is set up and running ((you can find our Node.js sample [here](https://github.com/VirgilSecurity/sample-backend-nodejs).
2. In `Device.kt` file (path: `./demo-e3kit-android/app/src/main/java/com/virgiltest/cardoso/e3kitandroiddemo/Device.kt`), find the `val baseUrl = "http://10.0.2.2:3000/virgil-jwt"` value and change it to your backend URL.
3. Build and run the Android Studio project in an Android emulator.

## Explore demo

This demo will automatically go through the following steps:
1. **Initialize EThree**. The demo obtains JWT tokens from backend and initializes E3Kit.
2. **Register users**. The app tries to register 2 users - Alice and Bob. If they were registered before, the app revokes their Virgil Cards, generates new key pairs for them and registers new Virgil Cards with the same identities but different Card IDs.
3. **Find users**. Alice looks for Bob's Card, and Bob looks for Alice's Card to get each other's public keys.
4. **Encrypt and sign message**. Alice encrypts a message to Bob using Bob's public key, signs it using her private key, and sends it.
5. **Decrypt and verify**. Bob receives the message, decrypts it using Bob's private key, and verifies it using Alice's public key.

## License

This library is released under the [3-clause BSD License](LICENSE.md).

## Support
Our developer support team is here to help you. Find out more information on our [Help Center](https://help.virgilsecurity.com/).

You can find us on [Twitter](https://twitter.com/VirgilSecurity) or send us email support@VirgilSecurity.com.

Also, get extra help from our support team on [Slack](https://virgilsecurity.com/join-community).

