# 📱 Phone Authentication Testing Setup

## 🚨 **CRITICAL: Add Test Numbers to Firebase Console**

**You MUST do this step for phone auth to work without sending real SMS:**

### **Firebase Console Setup:**
1. **Go to**: https://console.firebase.google.com/
2. **Select**: ConnectHub project  
3. **Navigate**: Authentication → Sign-in method
4. **Click**: Phone provider (should already be enabled)
5. **Scroll down**: "Phone numbers for testing" section
6. **Add these test numbers**:

```
Phone Number: +917037644920    Verification Code: 123456
Phone Number: +15551234567     Verification Code: 654321  
Phone Number: +447123456789    Verification Code: 111111
```

7. **Click "Add"** for each number
8. **Click "Save"** 

---

## 📱 **Testing Instructions (Updated)**

### **What's Changed:**
- ✅ **OTP Input Fixed**: Number keyboard, proper focus
- ✅ **Google Sign-In Disabled**: Until google-services.json is fixed
- ✅ **Phone Auth Ready**: Just needs Firebase test numbers

### **Phone Authentication Test:**
1. **Open app**
2. **Tap "Continue with Phone"**  
3. **Country should default to +91 India**
4. **Enter: 7037644920** (without +91)
5. **Tap "Send Code"** 
6. **Should navigate to OTP screen**
7. **Tap anywhere on OTP boxes** 
8. **Number keyboard should appear**
9. **Type: 123456** (test verification code)
10. **Boxes should fill automatically**
11. **Tap "Verify"**
12. **Should navigate to Dashboard** ✅

### **Alternative Numbers to Test:**
- **US**: 5551234567 → Code: 654321
- **UK**: 7123456789 → Code: 111111

---

## 🔍 **Debug with Logcat**

**Filter**: `tag:ConnectHub OR tag:FirebaseAuth`

**Expected logs for successful flow:**
```
📱 Starting phone verification for: +917037644920
🚀 Phone verification request sent to Firebase  
📨 Verification code sent successfully! VerificationId: AMvEBZTL...
🔐 Attempting to verify OTP code: 123456
🚀 Calling Firebase to verify code...
🎉 Phone verification successful! User: firebase_user_id_here
```

---

## ⚠️ **If Phone Auth Still Fails:**

### **Check Firebase Console:**
1. **Authentication enabled?** ✓
2. **Phone provider enabled?** ✓  
3. **Test numbers added?** ← **MOST IMPORTANT**
4. **SHA-1 fingerprint added?** ✓

### **Common Issues:**
- **"No verification ID"** → Test numbers not added to Firebase
- **"Invalid code"** → Using wrong test code (should be 123456)
- **"Phone verification failed"** → Phone auth not enabled in console
- **OTP boxes don't work** → Tap anywhere on the row to focus

---

## 🎯 **Success Criteria:**

✅ **App launches** with Google disabled message  
✅ **Phone auth button** works  
✅ **OTP screen** shows with working input  
✅ **Number keyboard** appears when tapping OTP boxes  
✅ **Verification succeeds** with test code 123456  
✅ **Navigation** to dashboard after verification  

**Test this flow now and let me know if the phone authentication works! 🚀**