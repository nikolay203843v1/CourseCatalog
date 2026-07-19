package ru.coursecatalog.data.courses.di

import android.content.Context
import androidx.room.Room
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.coursecatalog.data.courses.local.CourseDatabase
import ru.coursecatalog.data.courses.remote.CourseApi
import ru.coursecatalog.data.courses.repository.DefaultCourseRepository
import ru.coursecatalog.domain.courses.repository.CourseRepository

val courseDataModule = module {
    single { provideDatabase(androidContext()) }
    single { get<CourseDatabase>().courseDao() }
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { get<Retrofit>().create(CourseApi::class.java) }
    single<CourseRepository> {
        DefaultCourseRepository(
            courseApi = get(),
            courseDao = get(),
            ioDispatcher = Dispatchers.IO,
        )
    }
}

private fun provideDatabase(context: Context): CourseDatabase =
    Room.databaseBuilder(
        context,
        CourseDatabase::class.java,
        "course-catalog.db",
    ).build()

private fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://drive.usercontent.google.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
