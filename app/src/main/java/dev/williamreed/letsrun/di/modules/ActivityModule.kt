package dev.williamreed.letsrun.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.williamreed.letsrun.ui.MainActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}
