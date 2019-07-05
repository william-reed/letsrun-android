package dev.williamreed.letsrun.viewmodel

import dev.williamreed.letsrun.service.ForumService
import javax.inject.Inject

class ThreadViewModel @Inject constructor(val forumService: ForumService) : BaseViewModel() {
    
}
