package core.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class CoreFirebaseModule {
  
  @Provides
  fun providePhoneProvider() = PhoneAuthProvider.getInstance()
  
  @Provides
  fun provideAuth() = FirebaseAuth.getInstance()
  
  @Provides
  fun provideFirestore() = FirebaseFirestore.getInstance()
  
  @Provides
  fun provideStorage() = FirebaseStorage.getInstance()
}
