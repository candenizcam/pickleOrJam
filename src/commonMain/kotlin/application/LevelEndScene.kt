package application

import application.puntainers.PurchaseButtonsPuntainer
import application.puntainers.SheetNumberDisplayer
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import pungine.PunScene
import pungine.PunStage
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

@OptIn(DelicateCoroutinesApi::class)
class LevelEndScene(stage: PunStage, val gameState: GameState) : PunScene("levelEnd", stage, GlobalAccess.virtualSize.width.toDouble(), GlobalAccess.virtualSize.height.toDouble()) {

    var gameLost = false
        set(value) {
            gameLostList.forEach { it.visible =value }
            gameNotLostList.forEach { it.visible = value.not() }
            field = value
        }
    var gameLostList = mutableListOf<Puntainer>()
    var gameNotLostList = mutableListOf<Puntainer>()


    override suspend fun sceneInit() {
        super.sceneInit()



        punImage("bg", oneRectangle(), resourcesVfs["UI/between.png"].readBitmap())

        Button("nextLevel", GlobalAccess.rectFromXD(Vector(488.0,328.0),304,80),
            GlobalAccess.commonAssets.mediumButtons,
            Rectangle(Vector(308.0, 248.0),304.0,80.0  , Rectangle.Corners.TOP_LEFT ),
            Rectangle(Vector(308.0, 164.0),304.0,80.0 , Rectangle.Corners.TOP_LEFT  ),
            Rectangle(Vector(308.0, 80.0),304.0,80.0 , Rectangle.Corners.TOP_LEFT  )
        ).also {
            addPuntainer(it)
            it.clickFunction = {
                sfxPlayer.play("cash-register.mp3")
                onPlayNextPressed()
            }
            gameNotLostList.add(it)
        }


        val largeButtons = GlobalAccess.commonAssets.largeButtons
        Button("toMenu", GlobalAccess.rectFromXD(Vector(488.0,328.0),480,80),
            largeButtons,
            Rectangle(Vector(484.0, 248.0),480.0,80.0  , Rectangle.Corners.TOP_LEFT ),
            Rectangle(Vector(484.0, 164.0),480.0,80.0 , Rectangle.Corners.TOP_LEFT  ),
            Rectangle(Vector(484.0, 80.0),480.0,80.0 , Rectangle.Corners.TOP_LEFT  ),
        ).also {
            addPuntainer(it)
            it.clickFunction = {

                sfxPlayer.play("cash-register.mp3")
                onPlayNextPressed()
            }
            gameLostList.add(it)
            it.visible = false
        }

        Button("toAd", GlobalAccess.rectFromXD(Vector(8.0,328.0),480,80),
            largeButtons,
            Rectangle(Vector(0.0, 248.0),480.0,80.0 , Rectangle.Corners.TOP_LEFT  ),
            Rectangle(Vector(0.0, 164.0),480.0,80.0  , Rectangle.Corners.TOP_LEFT ),
            Rectangle(Vector(0.0, 80.0),480.0,80.0  , Rectangle.Corners.TOP_LEFT ),
        ).also {
            addPuntainer(it)
            it.clickFunction = {
                //TODO press ad
                //sfxPlayer.play("cash-register.mp3")
                //onPlayNextPressed()
            }
            gameLostList.add(it)
            it.visible = false
        }


        addPuntainer(
            SheetNumberDisplayer.create( "money",
                GlobalAccess.rectFromXD(Vector(872.0,308.0),192,36),
                Rectangle(0.0, 192.0, 0.0, 36.0),
                5,
                false,
                moneySign = true,
                minusSign = true

            ).also {
                it.setValue(gameState.money)
            }
        )

        addPuntainer(
            SheetNumberDisplayer.create( "levelNo",
                GlobalAccess.rectFromXD(Vector(572.0,44.0),52,36),
                Rectangle(0.0, 52.0, 0.0, 36.0),
                2,
                false
            ).also {
                it.setValue(gameState.level + 1)
            }
        )


        val purchaseButtonsPuntainer = PurchaseButtonsPuntainer.create(
            GlobalAccess.rectFromXD(Vector(940.0,86.0),128,2*72)
        )

        purchaseButtonsPuntainer.buySugClick = {
            if(gameState.buy(sugarToBuy = 1)) {
                sfxPlayer.play("cash-register.mp3")
                updateInfoAfter(money=gameState.money, vinegarCount=gameState.vinegar, sugarCount=gameState.sugar)
                //println("MONEY: ${gameState.money}, VINEGAR: ${gameState.vinegar}, SUGAR: ${gameState.sugar}")
            }
        }

        purchaseButtonsPuntainer.buyVinClick = {
            if(gameState.buy(vinegarToBuy = 1)) {
                sfxPlayer.play("cash-register.mp3")
                updateInfoAfter(money=gameState.money, vinegarCount=gameState.vinegar, sugarCount=gameState.sugar)
                //println("MONEY: ${gameState.money}, VINEGAR: ${gameState.vinegar}, SUGAR: ${gameState.sugar}")
            }
        }

        addPuntainer(
            purchaseButtonsPuntainer
        )


        addPuntainer(
            SheetNumberDisplayer.create( "operationsCost",
                GlobalAccess.rectFromXD(Vector(900.0,240.0),164,36),
                Rectangle(0.0, 164.0, 0.0, 36.0),
                4,
                false,
                moneySign = true,
                minusSign = true
            ).also {
                it.setValue(GlobalAccess.levels[gameState.level].rent)
            }
        )






        addPuntainer(
            punImage("gameState",
            GlobalAccess.rectFromXD(Vector(640.0,44.0),228,36
                ),
                resourcesVfs["UI/game_over.png"].readBitmap()
            ).also {
                it.visible = false //TODO sadece oyun bitmişse false olması
                gameLostList.add(it)
            }
        )

        addPuntainer(
            punImage("sugBar",
                GlobalAccess.rectFromXD(Vector(432.0,108.0),480,24),
                resourcesVfs["UI/SugBar_inside.png"].readBitmap()
            )
        )

        addPuntainer(
            punImage("vinBar",
                GlobalAccess.rectFromXD(Vector(432.0,180.0),480,24),
                resourcesVfs["UI/VinBar_inside.png"].readBitmap()
            )
        )

        gameState.gameOver = {
            gameLost = true
        }

        gameState.payRent()
        updateInfoAfter()
    }

    fun updateInfoAfter(levelNo: Int = gameState.level + 1, opCost: Int= GlobalAccess.levels[gameState.level].rent, money: Int = gameState.money, vinegarCount: Int = gameState.vinegar, sugarCount: Int = gameState.sugar){
        toPuntainer("levelNo", forceReshape = true){  it as SheetNumberDisplayer
            it.setValue(levelNo)
        }

        toPuntainer("operationsCost", forceReshape = true){  it as SheetNumberDisplayer
            it.setValue(-1*opCost)
        }

        toPuntainer("money", forceReshape = true){  it as SheetNumberDisplayer
            it.setValue(money)
        }

        updateSugarCount(sugarCount)
        updateVinCount(vinegarCount)
    }

    fun onPlayNextPressed(){
        if(gameLost) {
            val newGame = TestScene(stage)
            newGame.active=false
            GlobalScope.launchImmediately {
                newGame.initialize()

                stage.scenesToAdd.add(Pair(newGame, false))
                stage.scenesToRemove.add("levelEnd")
            }
        } else {
            gameState.level++
            val newLevel = TestScene(stage, gameState)
            GlobalScope.launchImmediately {
                newLevel.initialize()

                stage.scenesToAdd.add(Pair(newLevel, true))
                stage.scenesToRemove.add("levelEnd")
            }
        }
    }

    fun updateSugarCount(n: Int){
        toPuntainer("sugBar", forceReshape = true){
            it.resizeRect(GlobalAccess.rectFromXD(Vector(432.0,108.0),(480/10.0*n).toInt(),24))
        }
    }

    fun updateVinCount(n: Int) {
        toPuntainer("vinBar", forceReshape = true) {
            it.resizeRect(GlobalAccess.rectFromXD(Vector(432.0, 180.0), (480 / 10.0 * n).toInt(), 24))
        }
    }
}