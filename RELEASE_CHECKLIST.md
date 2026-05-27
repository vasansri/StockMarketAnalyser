# StockVision Release Checklist

## 1. Final Code Preparations
- [ ] Verify `versionCode` and `versionName` in `app/build.gradle.kts`.
- [ ] Run `gradlew clean` and `gradlew assembleRelease` to test the build.
- [ ] Ensure `isMinifyEnabled = true` is set in the release build type.
- [ ] Test the Release APK/AAB on a physical device.

## 2. Security & Keys
- [ ] Move `storePassword` and `keyPassword` from `build.gradle.kts` to `local.properties` or CI/CD secrets.
- [ ] Verify that all API keys (Finnhub, Alpha Vantage) are obfuscated or fetched from a secure remote config.
- [ ] Ensure ProGuard rules effectively hide business logic.

## 3. Play Store Assets
- [ ] **App Icon**: 512x512 PNG (transparent background).
- [ ] **Feature Graphic**: 1024x500 PNG.
- [ ] **Screenshots**: At least 2 for Phone, 7-inch tablet, and 10-inch tablet.
- [ ] **Privacy Policy URL**: Host the `PRIVACY_POLICY.md` content on a live URL.

## 4. Testing
- [ ] Test Firebase Auth (Google and Email) in the signed release build.
- [ ] Verify background alerts trigger correctly via WorkManager.
- [ ] Test the Paywall flow using the Google Play Billing test environment.

# Security Recommendations
1. **Certificate Pinning**: For financial apps, consider implementing SSL certificate pinning in `NetworkModule`.
2. **Biometric Auth**: Add an option for biometric lock to protect the portfolio view.
3. **Encrypted SharedPrefs**: Use `EncryptedSharedPreferences` for storing any sensitive user session data.
