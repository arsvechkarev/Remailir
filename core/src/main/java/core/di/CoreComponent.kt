package core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import core.CoroutinesDispatcherProvider
import core.RxJavaSchedulersProvider
import core.di.modules.CoreFirebaseModule
import core.di.modules.CoreModule
import dagger.Component
import javax.inject.Singleton

@Component(
  modules = [
    CoreModule::class,
    CoreFirebaseModule::class
  ]
)
@Singleton
interface CoreComponent {
  
  fun schedulersProvider(): RxJavaSchedulersProvider
  fun dispatchersProvider(): CoroutinesDispatcherProvider
  
  fun firebasePhoneProvider(): PhoneAuthProvider
  fun firebaseAuth(): FirebaseAuth
  fun firebaseFirestore(): FirebaseFirestore
  fun firebaseStorage(): FirebaseStorage
}