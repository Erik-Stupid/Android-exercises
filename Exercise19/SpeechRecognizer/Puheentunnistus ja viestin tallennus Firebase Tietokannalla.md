Puheentunnistus ja viestin tallennus Firebase Tietokannalla
=
# Demo video
https://youtu.be/Ot_nGQeACE0

# Firebase
**Firebase** on sovellusten kehitykseen tarkoitettu alusta, joka tarjoaa paljon erilaisia palveluja, kuten *datan tallennusta, koneoppimista, analytiikkaa, jne.*

Yrityksiä, jotka hyödyntävät Firebasea:

![](https://gitlab.dclabra.fi/wiki/uploads/upload_c3d37a9ddc3f81cfe5b4a39f5a6574c7.png)

## Setuppi
Tutoriaali: https://firebase.google.com/docs/android/setup

Android Studiossa: Tools > Firebase:

![](https://gitlab.dclabra.fi/wiki/uploads/upload_b6505fa476385fcc5ee6554fba35ff45.png)


### Lisätättävät riippuvuudet:

Android Manifestiin:
```kotlin
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET" />
```
Grandle (Projekti):
```kotlin
dependencies {
    classpath 'com.google.gms:google-services:4.3.3'
}
```
Gradle (Moduuli):
```kotlin
apply plugin: 'com.google.gms.google-services'
```
Tämä piti lisätä, kun ProGuard ei antanut lukea Firebasesta (kuulemma ei aina tarpeellinen)
```kotlin
buildTypes {
    release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```   

## Reaaliaikainen tietokanta
Perustietoa: https://firebase.google.com/docs/database

Kun jotain Firebasen ominaisuutta käyttää, siitä saa automaattista analytiikkaa, esim:

![](https://gitlab.dclabra.fi/wiki/uploads/upload_cc98493e70d21d1ccf37a098bee7d134.png)
(Itselläni näyttää aika tyhjältä)


### Setup

Mene osoitteeseen:
https://firebase.google.com
Ja paina "Get Started".

Kun olet kirjautunut Google tilillä, paina "Add project":

![](https://gitlab.dclabra.fi/wiki/uploads/upload_ad2b208c5ce5a2bb43295566326be6ec.png)

Kun olet seurannut ohjeita, vasemmalla olevasta välilehdestä valitse "Database":

![](https://gitlab.dclabra.fi/wiki/uploads/upload_2a26bae665083e86100f24e674a91e1f.png)

Käytetään "Real-time databasea":

![](https://gitlab.dclabra.fi/wiki/uploads/upload_21f9592a03a7b1db7a2c8525bcef0212.png)

Pitäsi avautua tälläinen ikkuna:

![](https://gitlab.dclabra.fi/wiki/uploads/upload_a6c87ad3466c643b45d8173cba954242.png)

Kannattaa tarkistaa **"Rules"** tabista onko *"read"* ja *"write"* true.

![](https://gitlab.dclabra.fi/wiki/uploads/upload_7ddcb8d26ff4398117b01ea9a50eeacc.png)


Setup Androidille: https://firebase.google.com/docs/database/android/start

## Tietokantaan kirjoittaminen

Alustaa Firebasedatabasen otsikolla: Message
```kotlin
val database = FirebaseDatabase.getInstance().getReference("Messages")
```

Alustetaan muuttuja ja formatteri, joka ottaa nykyisen ajan tässä muodossa: "M d, yyyy 00:00:00 AM/PM"
```kotlin
val current = Calendar.getInstance().time
val formatter = SimpleDateFormat.getDateTimeInstance()
val formatted = formatter.format(current)
```

Työntö komento "Messages" otsikon alle.
```kotlin
val ref = database.push()
```

Jokainen uusi viesti saa uuden random "avaimen", jonka sisään tulee "message" ja "time" kohdat
```kotlin
ref.child("time").setValue(formatted)
ref.child("message").setValue(edittext_input.toString())
```

Tällä tavalla tietokannan rakenne näyttää tältä:
![](https://gitlab.dclabra.fi/wiki/uploads/upload_190ae0eb8d8bf6812beec30a3595ca42.png)

## Tietokannasta lukeminen
Alustetaan tietokanta samalla tavalla kuin kirjoittamisessa

```kotlin
db = FirebaseDatabase.getInstance().getReference("Messages")
```

Lisätään listener tietokantaa
```kotlin
db.addListenerForSingleValueEvent(object: ValueEventListener {

    //Jos ei voi kuunnella
    override fun onCancelled(error: DatabaseError) {
        Log.d("ERROR", "" + error.message)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        // Tyhjennetään recyclerView, muuten uudet viestit vain lisätään uudestaan.
        mTextList.clear()
        // Loopataan jokaisen merkinnän läpi
        if (snapshot.exists()) {
            for (i: DataSnapshot in snapshot.children) {
                // Otetaan kaikki merkinnät, mitä tietty avain sisältää, eli "message" ja "time"
                val data = i.getValue(FirebaseData::class.java)
                // Lisätään adapteriin
                mTextList.add(data!!)
                Log.d(TAG, "Value = $data")
            }
        }
            mRecyclerView.addItemDecoration(dividerItemDecoration)

            // Kiinnitetään adapteri ja refreshataan view
            mRecyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
    }
```

# Puheentunnistus
Olen tehnyt tutkimusta, josta tulin tulokseen, että Google tarjoaa parhaimman puheentunnistuksen. Microsoft Azure tulee perässä. Suurin osa selain-puheentunnistus sovellukset hyödyntävät Googlen puheentunnistusta. On myös olemassa offline sovellus Sphinx, mutta se ei ymmärrä kuin kourallisen kieliä.

Androidin mukana tulee Googlen assistentti, jonka puheentunnistus ominaisuutta voi hyödyntää suoraan.

## Setup
Seurasin tätä tutoriaalia:
https://www.youtube.com/watch?v=Wv2hafcjEVY

### Lisätättävät riippuvuudet:

Android Manifest:
Äänennauhoitus lupa, mutta toimi ilmankin(?)
```kotlin
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

## Puheentunnistus

Intent, joka avaa puheentunnistus-dialogin
```kotlin
val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
```
Alustaa asetuksia
```kotlin
intent.putExtra(
    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
)
intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
```
Tarkistaa tukeeko laita puheentunnistusta. Pääasiassa emulaattoria varten
```kotlin
if (packageManager?.let { intent.resolveActivity(it) } != null) {
    startActivityForResult(intent, 10)
} else {
    Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT)
        .show()
}
```

# Mutkia matkassa...

Ongelmat tärkeysjärjestyksessä:
- Datan lukeminen Firebasesta
- Fragmenttien välinen tappeleminen
- Laiskuus
- Ideoiden puute

N. 2 viikkoa meni pelkästään siihen kun en millään meinannut saada tuota Firebasesta lukemista toimimaan.
Errorina oli aina jotain
> DatabaseException: Can't convert object of type java.lang.String to type

Tai NullPointerException joko recyclerViewissä tai sitten ihan huvikseen.

En edes muista miten sen sitten sain toimimaan, mutta muistan sen, että se tapahtui semi-vahingossa, kun internetistä ja ei edes debugauksestakaan ollut apua. Olisikohan ollut se että kirjoitin väärällä nimellä ne elementit sinne tietokantaan; ei ollut "message" vaan "Message"... Ja tämä kun on String, niin ei se tietenkää mitään tarkkaa erroria heitä...

Alussa myös meni vähän turhaan aikaa, kun meinasin ensin vain pitää koko höskän sovelluksen sisällä, missä se tallentaa Preferencesiä hyödyntämällä, mutta päätin sitten tutustua vähän tuohon Firebaseen, kun näytti olevan suht suosittu.

Ideaahan en millään meinannut keksiä, enkä keksisi uusia ominaisuuksia tähänkään sovellukseen.

# Yhteenveto

Opin paljon syvällisemmin miten fragmentit ja recyclerView toimii, opin miten vaihdetaan teemaa, Firebasen tietokannan käyttöä ja sen, että ilman Stack Overflowta, maailmassa olisi vain n. 7 kehittäjää.

Anna arvosanaksi sovellukselle kuusi Lauri Tähkää kolmestatoista Sauli Niinistöstä.