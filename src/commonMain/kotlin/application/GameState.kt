package application

class GameState(var money: Int, var vinegar: Int = 10, var sugar: Int = 2) {
    var jams = 0
    var pickles = 0

    fun pickleIt(input: String) {
        val fruit = GlobalAccess.inputList.find{it.type == input}
        money += fruit?.pickle ?: 0
        vinegar--
        pickles++
        println ("MONEY: $money, VINEGAR: $vinegar, SUGAR: $sugar")

        // if money < 0 gameover
    }

    fun jamIt(input: String) {
        val fruit = GlobalAccess.inputList.find{it.type == input}
        money += fruit?.jam ?: 0
        sugar--
        jams++
        println ("MONEY: $money, VINEGAR: $vinegar, SUGAR: $sugar")

        // if money < 0 gameover
    }
}