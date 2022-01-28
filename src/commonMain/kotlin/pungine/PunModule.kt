package pungine

import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector

/** This is put here as a reference, for the actual application you will need to copy it like "application.TestModule" and put that to the main as
 * "suspend fun main() = Korge(Korge.Config(module =application.TestModule))"
 *
 */
object PunModule: Module() {
    override val mainScene = PunStage::class
    override val size = InternalGlobalAccess.virtualSize // Virtual Size
    override val windowSize = InternalGlobalAccess.windowSize// Window Size

    override suspend fun AsyncInjector.configure() {
        mapPrototype { PunStage(size.width.toDouble(), size.height.toDouble()) }
    }
}