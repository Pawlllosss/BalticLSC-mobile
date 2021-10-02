package pl.oczadly.baltic.lsc

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}