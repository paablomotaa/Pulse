package com.pmgdev.pulse.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pmgdev.pulse.network.Session
import com.pmgdev.pulse.repository.firebaserepository.ChatRepository
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAuthFirebase():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun providedbFirebase():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Singleton
    @Provides
    fun provideRepository(firestore: FirebaseFirestore,auth: FirebaseAuth,firestorage: FirebaseStorage): UserRepository{
        return UserRepository(
            firestore, auth,firestorage
        )
    }
    @Singleton
    @Provides
    fun provideRepositoryChat(firestore: FirebaseFirestore): ChatRepository
    {
        return ChatRepository(
            firestore
        )
    }
    @Singleton
    @Provides
    fun provideStorage(): FirebaseStorage{
        return FirebaseStorage.getInstance()
    }
    @Singleton
    @Provides
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            produceFile = { context.preferencesDataStoreFile(Session.DATA) }
        )
    }
    @Provides
    @Singleton
    fun provideSession(dataStore:DataStore<Preferences>):Session{
        return Session(dataStore)
    }
}