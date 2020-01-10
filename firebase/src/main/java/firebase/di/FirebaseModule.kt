package firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {
  
  @Provides
  fun providePhoneProvider(): PhoneAuthProvider = PhoneAuthProvider.getInstance()
  
  @Provides
  fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()
  
  @Provides
  fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
  
  @Provides
  fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}