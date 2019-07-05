package dev.williamreed.letsrun.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import dev.williamreed.letsrun.LetsRunApplication
import dev.williamreed.letsrun.di.modules.ActivityModule
import dev.williamreed.letsrun.di.modules.FragmentModule
import dev.williamreed.letsrun.di.modules.ServiceModule
import dev.williamreed.letsrun.di.modules.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        FragmentModule::class,
        ServiceModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: LetsRunApplication)
}
