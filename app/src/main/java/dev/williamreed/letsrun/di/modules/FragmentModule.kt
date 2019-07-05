package dev.williamreed.letsrun.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.williamreed.letsrun.ui.HomeFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment
}
