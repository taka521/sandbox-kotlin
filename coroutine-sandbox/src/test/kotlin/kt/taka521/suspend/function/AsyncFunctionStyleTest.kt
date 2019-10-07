package kt.taka521.suspend.function

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

/**
 * Asyncスタイル関数
 *
 * 以下のように asyncコルーチンビルダー を用いて、非同期関数を作成する事ができる。
 * メリットとしては、suspend関数ではないためどこでも呼び出し可能である事。
 * （非同期関数である事が分かるように、関数名の末尾に「Async」を付加するとよい）
 *
 * デメリットとしては、Asyncスタイルの関数を呼び出した後、
 * 呼び出し元で何らかのエラーが発生したとしても、Asyncスタイル関数の処理は止まらない。
 */
class AsyncFunctionStyleTest {

    @Test
    fun asyncStyle() {

        val time = measureTimeMillis {

            // Asyncスタイル関数
            //   suspend関数ではないためどこからでも呼び出し可能だが、
            //   呼び出し元で何らかのエラーが発生したとしても、
            //   これらの処理はキャンセルされずに、バックグランドで動き続ける。
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()

            // Deferred#await はsuspend関数であるため、コルーチン内でしか呼び出せない
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }
        }

        println("Completed answer is $time ms")
    }

    ////////////////////////////////
    // async style function
    ////////////////////////////////

    private fun somethingUsefulOneAsync() = GlobalScope.async { doSomethingUsefulOne() }

    private fun somethingUsefulTwoAsync() = GlobalScope.async { doSomethingUsefulTwo() }


    ////////////////////////////////
    // suspend function
    ////////////////////////////////

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L)
        return 13
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L)
        return 29
    }
}