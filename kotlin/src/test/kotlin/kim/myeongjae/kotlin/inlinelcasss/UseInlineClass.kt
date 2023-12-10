package kim.myeongjae.kotlin.inlinelcasss

class UseInlineClass {
    fun useInlineClass() {
        val inlineClass = InlineClass("inline class")
        println(inlineClass.getValue())

        val basicClass = BasicClass("basic class")
        println(basicClass.getValue())
    }
}
