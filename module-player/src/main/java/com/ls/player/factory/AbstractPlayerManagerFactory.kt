package com.ls.player.factory

import com.ls.player.manager.IPlayerManager

abstract class AbstractPlayerManagerFactory {

    abstract fun createPlayerManager(clazz: Class<out IPlayerManager>): IPlayerManager
}