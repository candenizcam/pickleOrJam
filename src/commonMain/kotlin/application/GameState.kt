package application

class GameState(var money: Int, var vinegar: Int = 10, var sugar: Int = 10) {
    var jams = 0
    var pickles = 0

    fun pickleIt(input: String) {
        val fruit = GlobalAccess.inputList.find{it.type == input}
        money += fruit?.pickle ?: 0
        vinegar --
        println ("MONEY: $money, VINEGAR: $vinegar, SUGAR: $sugar")
    }

    fun jamIt(input: String) {
        val fruit = GlobalAccess.inputList.find{it.type == input}
        money += fruit?.jam ?: 0
        sugar --
        println ("MONEY: $money, VINEGAR: $vinegar, SUGAR: $sugar")
    }
}