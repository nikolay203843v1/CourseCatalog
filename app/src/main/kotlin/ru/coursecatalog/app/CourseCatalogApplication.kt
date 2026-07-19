package ru.coursecatalog.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.coursecatalog.data.courses.di.courseDataModule
import ru.coursecatalog.feature.auth.di.authModule
import ru.coursecatalog.feature.main.di.mainFeatureModule

class CourseCatalogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CourseCatalogApplication)
            modules(
                courseDataModule,
                authModule,
                mainFeatureModule,
            )
        }
    }
}
