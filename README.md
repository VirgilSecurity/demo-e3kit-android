# Virgil E3Kit Android Demo

## Introduction

<a href="https://developer.virgilsecurity.com/docs"><img width="230px" src="https://cdn.virgilsecurity.com/assets/images/github/logos/virgil-logo-red.png" align="left" hspace="10" vspace="6"></a> This is a sample Android project for [Virgil Security](https://virgilsecurity.com)'s [E3Kit SDK](https://github.com/VirgilSecurity/virgil-e3kit-x) which simplifies work with Virgil services and presents an easy-to-use API for adding a security layer to any application. E3Kit interacts with Virgil Cards Service, Keyknox Service and Pythia Service.
Virgil E3Kit allows you to setup user encryption with multidevice support in just a few simple steps.

> The demo is using E3Kit v2.0.4.

> Note: It is a sample project and cannot be used in production.

We know your time is valuable, therefore if you want quickly to take a look at how Demo works you also [can watch the video](https://youtu.be/CWU8Rey52sY) we recorded for you on [our YouTube Channel](https://www.youtube.com/channel/UCU8BhA1nVzKKRiU5P4N3D6A/featured).

## Prerequisites

- Android Studio
- [Virgil Developer Account](https://dashboard.virgilsecurity.com/) and a Virgil Application
- Local backend set up to generate Virgil JWT (you can find our Node.js sample [here](https://github.com/VirgilSecurity/sample-backend-nodejs))

## Set up and run demo

1. Make sure the local backend is set up and running (you can find our Node.js sample [here](https://github.com/VirgilSecurity/sample-backend-nodejs)).
2. In `Device.kt` file (path: `./demo-e3kit-android/app/src/main/java/com/virgiltest/cardoso/e3kitandroiddemo/Device.kt`), find the `val baseUrl = "http://10.0.2.2:3000/virgil-jwt"` value (it will be in **two places**) and change it to your backend URL (if you use our [sample backend](https://github.com/VirgilSecurity/sample-backend-nodejs), the URL will be `http://"YOUR-IP-ADRESS-HERE":3000/virgil-jwt`).
<img width="640px" src="img/authenticate.png" alt="Device.kt file">
<img width="640px" src="img/jwt.png" alt="Device.kt file">

3. Build and run the Android Studio project in an Android emulator.
<img src="img/run.png" alt="Build and run">

## Explore demo

This demo will automatically go through the following steps:
1. **Initialize EThree**. The demo obtains JWT tokens from backend and initializes E3Kit.
2. **Register users**. The app tries to register 2 users - Alice and Bob. If they were registered before, the app revokes their Virgil Cards, generates new key pairs for them and registers new Virgil Cards with the same identities but different Card IDs.
3. **Find users**. Alice looks for Bob's Card, and Bob looks for Alice's Card to get each other's public keys.
4. **Encrypt and sign message**. Alice encrypts a message to Bob using Bob's public key, signs it using her private key, and sends it.
5. **Decrypt and verify**. Bob receives the message, decrypts it using Bob's private key, and verifies it using Alice's public key.

<img src="img/demo.png" alt="Virgil E3Kit Android Demo">

## License

This library is released under the [3-clause BSD License](LICENSE).

## Support

Our developer support team is here to help you. Find out more information on our [Help Center](https://help.virgilsecurity.com/).

You can find us on [Twitter](https://twitter.com/VirgilSecurity) or send us email support@VirgilSecurity.com.

Please don't forget to subscribe our [YouTube channel](https://www.youtube.com/channel/UCU8BhA1nVzKKRiU5P4N3D6A/featured)

Also, get extra help from our support team on [Slack](https://virgilsecurity.com/join-community).
