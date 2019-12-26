package testui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun background(block: suspend () -> Unit) {
  GlobalScope.launch(Dispatchers.IO) {
    block()
  }
}

fun doAndWait(delayMillis: Long, block: suspend () -> Unit) {
  GlobalScope.launch(Dispatchers.IO) {
    block()
  }
  Thread.sleep(delayMillis)
}
