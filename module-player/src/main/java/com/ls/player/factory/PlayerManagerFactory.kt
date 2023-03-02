package com.ls.player.factory

import com.ls.player.manager.IPlayerManager

object PlayerManagerFactory : AbstractPlayerManagerFactory() {

    override fun createPlayerManager(clazz: Class<out IPlayerManager>): IPlayerManager {
        return clazz.newInstance()
    }
}