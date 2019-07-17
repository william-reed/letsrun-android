package dev.williamreed.letsrun

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import dev.williamreed.letsrun.data.ObjectBox
import dev.williamreed.letsrun.data.ThreadReply
import dev.williamreed.letsrun.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class LetsRunApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            handleExceptions()
        }

        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this)

        AndroidThreeTen.init(this)

        ObjectBox.init(this)
        // clear cache on every app start
        ObjectBox.boxStore.boxFor(ThreadReply::class.java).removeAll()
    }

    override fun supportFragmentInjector() = fragmentInjector
    override fun activityInjector() = activityInjector

    /**
     * Handle exceptions in a more informative way. Maybe this should be wrapped in a flag
     */
    private fun handleExceptions() {
        // https://stackoverflow.com/questions/4427515/using-global-exception-handling-on-android
        // TODO: this might need to be adjusted
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            Timber.e(paramThrowable, "Uncaught exception occurred on thread ${paramThread.name}")
        }
    }
}
