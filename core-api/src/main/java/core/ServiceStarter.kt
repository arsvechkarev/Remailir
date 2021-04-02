package core

import android.app.Service
import android.os.Bundle

/**
 * Helps starting services
 */
interface ServiceStarter {
  
  /**
   * Starts service with class [serviceClass] and [params]
   */
  fun startService(serviceClass: Class<out Service>, params: Bundle)
}