package dev.williamreed.letsrun.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.williamreed.letsrun.ui.HomeFragment
import dev.williamreed.letsrun.ui.ThreadFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun bindThreadFragment(): ThreadFragment
}
