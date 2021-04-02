package core.impl

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import core.ServiceStarter

class ServiceStarterImpl(private val context: Context) : ServiceStarter {
  
  override fun startService(serviceClass: Class<out Service>, params: Bundle) {
    val intent = Intent(context, serviceClass)
    intent.putExtras(params)
    context.startService(intent)
  }
}