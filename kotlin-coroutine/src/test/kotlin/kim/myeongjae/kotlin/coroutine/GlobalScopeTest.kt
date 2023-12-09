package kim.myeongjae.kotlin.coroutine

import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

@OptIn(DelicateCoroutinesApi::class)
class GlobalScopeTest : StringSpec({

    "GlobalScope.launch" {
        // test succeeded because GlobalScope is outside the test scope
        // GlobalScope를 사용하는건 권장하지 않으므로 그냥 자바의 `Executors.newSingleThreadExecutor()`로 스레드 풀을 만들어서 쓰자.
        GlobalScope.launch {
            throw RuntimeException()
        }
    }
})
