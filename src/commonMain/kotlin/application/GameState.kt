package application

import com.soywiz.korio.async.launchImmediately
import kotlinx.coroutines.GlobalScope

class GameState(var level: Int = 0, var money: Int, var vinegar: Int = 10, var sugar: Int = 10) {
    var jams = 0
    var pickles = 0
    var sugarPrice = 20
    var vinegarPrice = 20
    var rent = 200
    var gameOver = {money: Int -> }

    fun getFruit(input: String): Fruit? {
        return GlobalAccess.levels[level].fruitList.find { it.type == input }
    }

    fun checkEnd() {
        if(vinegar == 0 && sugar == 0) {
            gameOver(money)
        }
    }

    fun pickleIt(input: String) {
        if(vinegar > 0) {
            val fruit = getFruit(input)
            money += fruit?.pickle ?: 0
            vinegar--
            pickles++
            println("MONEY: $money, VINEGAR: $vinegar, SUGAR: $sugar")
            checkEnd()
        }
    }

    fun jamIt(input: String) {
        if(sugar > 0) {
            val fruit = getFruit(input)
            money += fruit?.jam ?: 0
            sugar--
            jams++
            println("MONEY: $money, VINEGAR: $vinegar, SUGAR: $sugar")
            checkEnd()
        }
    }

    fun buy(sugarToBuy: Int, vinegarToBuy: Int) {
        money -= (sugarToBuy * sugarPrice + vinegarToBuy * vinegarPrice)
        sugar += sugarToBuy
        vinegar += vinegarToBuy
    }

    fun payRent() {
        money -= rent
        if(money<0) {
            gameOver(money)
        }
    }
}