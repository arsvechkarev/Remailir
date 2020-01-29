package com.arsvechkarev.auth.presentation.sms

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arsvechkarev.auth.presentation.sms.SmsTimerViewModel.TimerState.Finished
import core.strings.FORMAT_SMS_CODE_TIMER
import core.strings.SMS_TIMEOUT_SECONDS
import timerx.format.TimeFormatter
import javax.inject.Inject

class SmsTimerViewModel @Inject constructor() : ViewModel() {
  
  private var isStarted = false
  private val formatter = TimeFormatter.with(FORMAT_SMS_CODE_TIMER)
  
  private val _timerState = MutableLiveData<TimerState>()
  val timerState: LiveData<TimerState>
    get() = _timerState
  
  private val tickingState = TimerState.Ticking(formatter.format(SMS_TIMEOUT_SECONDS * 1000))
  
  fun startIfNeeded() {
    if (!isStarted) {
      Timer().start()
      isStarted = true
    }
  }
  
  inner class Timer : CountDownTimer(SMS_TIMEOUT_SECONDS * 1000, 1000) {
    
    override fun onFinish() {
      _timerState.value = Finished
    }
    
    override fun onTick(millisUntilFinished: Long) {
      tickingState.time = formatter.format(millisUntilFinished)
      _timerState.value = tickingState
    }
    
  }
  
  sealed class TimerState {
    object Finished : TimerState()
    class Ticking(var time: String) : TimerState()
  }
}