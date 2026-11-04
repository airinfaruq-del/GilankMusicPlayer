# 🎵 GILANK MUSIC PLAYER - Android Native Application

Aplikasi pemutar musik Android native modern, elegan, dan bertema **Hitam & Emas (Black & Gold)** yang dibangun menggunakan **Kotlin**, **Jetpack Compose**, dan **Android Media3 ExoPlayer**.

---

## ✨ Fitur Utama
1. **Memindai & Membaca MP3 dari Penyimpanan HP**:
   - Menggunakan `MediaStore.Audio.Media.EXTERNAL_CONTENT_URI` otomatis membaca seluruh file audio/MP3 yang tersimpan di HP.
2. **Daftar Lagu Otomatis**:
   - Menampilkan judul lagu, artis, durasi, dan cover album secara otomatis saat aplikasi dibuka.
3. **Kontrol Musik Penuh**:
   - Play, Pause, Next, Previous.
   - Seek progress bar realtime dan durasi (menit:detik).
   - Mode Shuffle (Acak) & Repeat (Ulangi Semua / Ulangi Satu Lagu).
4. **Pemutaran Tetap Berjalan Saat Layar Mati (Background Playback)**:
   - Menggunakan `androidx.media3.session.MediaSessionService` dengan `WAKE_MODE_LOCAL` dan `FOREGROUND_SERVICE_MEDIA_PLAYBACK` sehingga musik tidak terhenti ketika HP dikunci atau beralih aplikasi.
5. **Notifikasi Kontrol Musik & Lockscreen**:
   - Terintegrasi dengan notifikasi Android (Play, Pause, Next, Prev, Cover Art) langsung di Notification Bar dan Lock Screen.
6. **100% Offline**:
   - Bekerja tanpa membutuhkan koneksi internet.
7. **Desain Mewah Hitam & Emas**:
   - Nuansa Obsidian Dark (#070707) dipadu aksen Emas Royal (#D4AF37) dengan piringan vinyl animasi.

---

## 🚀 Cara Build Menjadi APK Debug (Instalasi Langsung ke HP)

### Opsi 1: Menggunakan Android Studio (Paling Mudah)
1. Ekstrak file zip hasil unduhan ini ke komputer Anda.
2. Buka **Android Studio** (Hedgehog, Iguana, Koala, atau versi terbaru).
3. Pilih **Open** -> Arahkan ke folder proyek `GilankMusicPlayer`.
4. Tunggu Gradle sync selesai.
5. Untuk membuat file APK:
   - Klik menu **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**.
6. Setelah selesai, klik tautan **locate** di pojok kanan bawah.
7. File APK debug siap diinstal:
   `app/build/outputs/apk/debug/app-debug.apk`
8. Kirim file APK ke HP Anda via WhatsApp/USB/Google Drive, lalu buka file tersebut di HP untuk instalasi langsung.

### Opsi 2: Menggunakan Terminal / Command Line (CLI)
Jika Anda memiliki Android SDK dan Java 17 terinstal:
```bash
# Di terminal root proyek:
./gradlew assembleDebug
```
File APK akan otomatis tergenerate di:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Opsi 3: GitHub Actions (Build Otomatis di Cloud Tanpa Komputer Berat)
Proyek ini sudah dilengkapi dengan file workflow `.github/workflows/build-apk.yml`. Cukup push proyek ini ke repositori GitHub pribadi Anda, dan GitHub akan otomatis mem-build file `app-debug.apk` di tab **Actions** untuk diunduh langsung ke HP Anda!

---
© 2026 GILANK MUSIC PLAYER. All rights reserved.
