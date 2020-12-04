package ua.kblogika.interactive.utils.interactors

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

object SoundProvider {
    private var notificationPlayer: MediaPlayer? = null

    fun playSound(context: Context, uri: Uri?) {
        val volume = ((context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
            .getStreamVolume(AudioManager.STREAM_NOTIFICATION).toFloat()
                / (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
            .getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION))
        if (notificationPlayer == null || !notificationPlayer!!.isPlaying) {
            notificationPlayer = MediaPlayer.create(context, uri)
            if (notificationPlayer == null) {
                return
            }
            notificationPlayer!!.setVolume(volume, volume)
            notificationPlayer!!.start()
        }
    }

    fun stopPlayingSound() {
        if (notificationPlayer != null && notificationPlayer!!.isPlaying) {
            notificationPlayer!!.stop()
        }
    }

    fun releasePlayer() {
        stopPlayingSound()
        if (notificationPlayer != null) {
            notificationPlayer!!.release()
            notificationPlayer = null
        }
    }

    fun playSound(context: Context?) {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playNotificationSound(context: Context, defaul: String) {
        val soundUri: Uri =
            if (defaul.isEmpty()) {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            } else {
                Uri.parse(defaul)
            }
        playSound(context, soundUri)
    }


}