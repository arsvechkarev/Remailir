package core

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Rx java scheduler provider to facilitate tests
 */
interface RxJavaSchedulersProvider {
  
  val mainThread: Scheduler
  val io: Scheduler
  
  object DefaultImpl : RxJavaSchedulersProvider {
    override val mainThread: Scheduler = Schedulers.io()
    override val io: Scheduler = AndroidSchedulers.mainThread()
  }
}