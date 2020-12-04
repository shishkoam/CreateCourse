package ua.kblogika.interactive.utils.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Base64
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {
    //Определяем эмулятор
    // ro.hardware=goldfish
    //ro.kernel.qemu=1
    //ro.product.model=sdk
    fun checkEmulator(): Boolean {
        try {
            val goldfish = getSystemProperty("ro.hardware").contains("goldfish")
            val emu = getSystemProperty("ro.kernel.qemu").length > 0
            val sdk = getSystemProperty("ro.product.model").contains("sdk")
            if (emu || goldfish || sdk) {
                return true
            }
        } catch (e: Exception) {
        }
        return false
    }

    @Throws(Exception::class)
    private fun getSystemProperty(name: String): String {
        val sysProp = Class.forName("android.os.SystemProperties")
        return sysProp.getMethod("get", *arrayOf<Class<*>>(String::class.java))
            .invoke(sysProp, *arrayOf<Any>(name)) as String
    }

    //Проверяем источник установки из Play Store
    fun checkInstaller(context: Context): Boolean {
        val installer = context.packageManager.getInstallerPackageName(context.packageName)
        return installer != null && installer.startsWith("com.android.vending")
    }

    //Чтобы защититься от отладки, можно использовать следующий код:
    fun checkDebuggable(context: Context): Boolean {
        return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    //Signature
    fun getSignature(context: Context): String? {
        var apkSignature: String? = null
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in packageInfo.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                apkSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                //                Logger.appendLog("DEBUG SIGNATURE: " + apkSignature);
//                String str = toHex(encryptString(apkSignature, generateKey("mR3wn_W$7FZ3GZpa")));
//                Logger.appendLog("DEBUG SIGNATURE: " + str);
            }
        } catch (e: Exception) {
        }
        return apkSignature
    }

    private const val SIGN = "FISHwYXv0OaQY6QWqgYY93OUjHo="
    private const val SIGN_ENC = "fd97a0f2f0a41fbe1ec4da2ec698ce22535dea0dd6fe5d2f321f64cd32c69ea5"


    //encrypting strings
    @Throws(Exception::class)
    fun encryptString(message: String?, secret: SecretKey?): ByteArray {
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        return cipher.doFinal(message!!.toByteArray(charset("UTF-8")))
    }

    @Throws(Exception::class)
    fun decryptString(cipherText: ByteArray?, secret: SecretKey?): String {
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secret)
        return String(cipher.doFinal(cipherText), UTF_8)
    }

    @Throws(Exception::class)
    fun generateKey(password: String): SecretKey {
        return SecretKeySpec(password.toByteArray(), "AES")
    }

    private const val HEX_DIGITS = "0123456789abcdef"
}