package com.zptech.the.great.seljuk.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.zptech.the.great.seljuk.R
import com.zptech.the.great.seljuk.ads.AdsManager
import com.zptech.the.great.seljuk.databinding.ActivityDetailPlayerBinding
import com.zptech.the.great.seljuk.getList
import com.zptech.the.great.seljuk.model.RingTone
import java.lang.RuntimeException

class DetailPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPlayerBinding
    private lateinit var list: MutableList<RingTone>

    var mPlayer: MediaPlayer? = null
    private var sTime: Int = 0
    private var eTime: Int = 0
    private var fTime: Int = 5000
    private var bTime: Int = 5000
    private var hdlr: Handler? = null
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_player)
        try {


            currentPosition = intent.getIntExtra("position", 0)
            hdlr = Handler(mainLooper)
            list = getList()
            mPlayer = MediaPlayer()

            onRingtoneSelected(list.get(currentPosition), currentPosition)
            replaceFragment(list[currentPosition])

            binding.btnPlay.setOnClickListener {
                if (mPlayer!!.isPlaying) {
                    mPlayer!!.pause()
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                } else {
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                    mPlayer!!.start()
                    eTime = mPlayer!!.duration.toLong().toInt()
                    sTime = mPlayer!!.currentPosition.toLong().toInt()
                    binding.seekbar.max = eTime
                    binding.seekbar.progress = sTime
                    hdlr!!.postDelayed(updateSongTime, 1000)
                }
            }

            binding.seekbar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {


                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mPlayer!!.seekTo(progress)

                    }


                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })


            binding.btnNext.setOnClickListener {

                if (currentPosition < list.size - 1) {
                    currentPosition += 1
                } else {
                    currentPosition = 0
                }
                onRingtoneSelected(list[currentPosition], currentPosition)
                replaceFragment(list[currentPosition])

            }

            binding.btnPrevious.setOnClickListener {


                if (currentPosition < list.size) {

                    if (currentPosition == 0) {
                        currentPosition = list.size - 1
                    } else {
                        currentPosition -= 1
                    }
                } else {
                    currentPosition = 0
                }
                onRingtoneSelected(list[currentPosition], currentPosition)
                replaceFragment(list[currentPosition])
            }

            binding.imgBack.setOnClickListener {
                finish()
            }

            binding.imgNotification.setOnClickListener {
                if (mPlayer!!.isPlaying) {
                    mPlayer!!.pause()
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                }
                val bottomSheetFragment = BottomSheetFragment(list.get(currentPosition))
                bottomSheetFragment.show(supportFragmentManager, "dialog")
            }

            val adsManager = AdsManager(this)
            adsManager.loadNativeBannerMax(findViewById(R.id.nativeSmall), binding.relAd)

        } catch (exp: IndexOutOfBoundsException) {

        } catch (ep: RuntimeException) {

        }
    }

    private fun onRingtoneSelected(ringTone: RingTone, position: Int) {
        try {
            currentPosition = position
            clearPlayer()
            mPlayer = MediaPlayer.create(this, ringTone.ringToneFile)
            mPlayer!!.start()
            setListener()
            eTime = mPlayer!!.duration.toLong().toInt()
            sTime = 0
            binding.seekbar.max = eTime
            binding.seekbar.progress = sTime
            binding.title.text = ringTone.ringToneTitle
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
            hdlr!!.postDelayed(updateSongTime, 1000)

        } catch (runtime: RuntimeException) {

        } catch (exp: Exception) {

        }
    }

    private val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            sTime = mPlayer!!.currentPosition
            binding.seekbar.progress = sTime
            hdlr!!.postDelayed(this, 1000)
        }
    }

    private fun clearPlayer() {
        if (mPlayer != null) {
            if (mPlayer!!.isPlaying) {
                mPlayer!!.stop()
                mPlayer!!.release()
            } else {
                mPlayer!!.release()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if (mPlayer != null && mPlayer!!.isPlaying) {
            mPlayer!!.pause()
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
        }
    }

    private fun replaceFragment(ringTone: RingTone) {
        val transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.container, EmbededFragment(ringTone));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setListener() {
        try {
            mPlayer!!.setOnCompletionListener {
                try {
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                    //                    mPlayer!!.start()
                    //                    binding.seekbar.progress = sTime
                    //                    mPlayer!!.isLooping = true

                } catch (e: Exception) {
                }
            }
        }catch (ex:RuntimeException){}
        catch (exp:Exception){}

    }
}