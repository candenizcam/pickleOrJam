package application

import pungine.Updatable

class CountdownTimer(val timeLimit: Int) : Updatable() {
    var left = timeLimit
    var sec = 0.0
    var onComplete = {}
    var onUpdate = {}
    var completed = false

    override fun update(sec: Double) {
        if (!completed) {
            this.sec += sec
            if (this.sec >= 1) {
                this.sec -= 1
                left--
            }

            onUpdate()

            if (left <= 0) {
                completed = true
                onComplete()
            }
        }
    }

    fun reset() {
        left = timeLimit
        sec = 0.0
        completed = false
    }
}