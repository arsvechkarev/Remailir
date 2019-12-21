package com.arsvechkarev.firebase.di

import com.arsvechkarev.core.di.FeatureScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {
  
  @Provides
  @FeatureScope
  fun providePhoneProvider(): PhoneAuthProvider = PhoneAuthProvider.getInstance()
  
  @Provides
  @FeatureScope
  fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()
  
  @Provides
  @FeatureScope
  fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}