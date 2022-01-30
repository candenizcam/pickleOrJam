package application

import com.soywiz.korio.async.launchImmediately
import kotlinx.coroutines.GlobalScope


class GameState(var level: Int = 0, var money: Int, var vinegar: Int = 10, var sugar: Int = 10) {
    var jams = 0
    var pickles = 0
    var fruitName = "Cat"
    var sugarPrice = 20
    var vinegarPrice = 20
    var gameOver = { }
    var levelOver = { }

    fun getFruit(input: String): Fruit? {
        return GlobalAccess.levels[level].fruitList.find { it.type == input }
    }

    fun checkEnd() {
        if(vinegar == 0 && sugar == 0) {
            levelOver()
        }
    }

    fun pickleIt(input: String) {
        if(vinegar > 0) {
            val fruit = getFruit(input)
            if (fruit != null) {
                fruitName = fruit.type
            }
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
            if (fruit != null) {
                fruitName = fruit.type
            }
            println(fruitName)
            money += fruit?.jam ?: 0
            sugar--
            jams++
            println("MONEY: $money, VINEGAR: $vinegar, SUGAR: $sugar")
            checkEnd()
        }
    }

    fun buy(sugarToBuy: Int = 0, vinegarToBuy: Int = 0) : Boolean{
        if(money >= (sugarToBuy * sugarPrice + vinegarToBuy * vinegarPrice)) {
            money -= (sugarToBuy * sugarPrice + vinegarToBuy * vinegarPrice)
            sugar += sugarToBuy
            vinegar += vinegarToBuy
            return true
        }
        return false
    }

    fun payRent() {
        money -= GlobalAccess.levels[level].rent
        if(money<0) {
            gameOver()
        }
    }
}