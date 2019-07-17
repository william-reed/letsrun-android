package dev.williamreed.letsrun.di.modules

import dagger.Module
import dagger.Provides
import dev.williamreed.letsrun.data.ObjectBox
import dev.williamreed.letsrun.service.ForumService
import javax.inject.Singleton

@Module
class ServiceModule {
    @[Provides Singleton]
    fun providesForumService(): ForumService = ForumService(ObjectBox)
}
