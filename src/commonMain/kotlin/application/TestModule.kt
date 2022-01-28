package application

import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector
import pungine.InternalGlobalAccess

object TestModule: Module() {
    override val mainScene = TestStage::class
    override val size = InternalGlobalAccess.virtualSize // Virtual Size
    override val windowSize = InternalGlobalAccess.windowSize// Window Size

    override suspend fun AsyncInjector.configure() {

        mapPrototype { TestStage() }
    }
}