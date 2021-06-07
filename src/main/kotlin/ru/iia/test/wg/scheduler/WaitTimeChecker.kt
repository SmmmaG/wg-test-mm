package ru.iia.test.wg.scheduler

import kotlinx.coroutines.*
import ru.iia.test.wg.service.PlayerStorage
import java.time.ZonedDateTime
import kotlin.coroutines.CoroutineContext

class WaitTimeChecker : CoroutineScope {
    private var job: Job = Job()
    private var delta: Double = 0.1
    private var iteration: Long = 1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun cancel() {
        job.cancel()
    }

    fun schedule() = launch { // launching the coroutine
        var seconds = 1
        while (true) {
            delay(1000)
            if (seconds++ == 5) {
                PlayerStorage.storage.getLastInTime()?.let {
                    if (ZonedDateTime.now().isBefore(it.plusSeconds(30 * iteration))) {
                        iteration++
                        delta = delta.plus(0.03)
                        if (PlayerStorage.storage.createGroups(delta)) {
                            while (PlayerStorage.storage.createGroups(delta)) {
                            }
                        } else {
                            delta = delta.plus(0.03)
                        }
                    } else {
                        iteration = 1
                        delta = 0.1
                    }
                }
            }
        }
    }
}