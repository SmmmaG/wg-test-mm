package ru.iia.test.wg.service

import ru.iia.test.wg.dto.Player
import java.time.ZonedDateTime
import kotlin.math.max
import kotlin.math.min

class PlayerStorage {
    private val playersPool = mutableListOf<Player>()

    private var lastInTime: ZonedDateTime? = null
    private var groupNumber: Int = 1
    private var groupSize: Int = 1

    fun setGroupSize(size: Int) {
        groupSize = size
    }

    fun getLastInTime(): ZonedDateTime? {
        return lastInTime
    }

    fun addPlayer(player: Player): Boolean {
        synchronized(playersPool) {
            if (playersPool.size == 0) {
                player.apply {
                    this.skillNumber = 1
                    this.latencyNumber = 1
                }
            } else {
                player.apply {
                    this.skillNumber = playersPool.filter { p -> p.skill < this.skill }.size + 1
                    playersPool.filter { p -> p.skill > this.skill }.forEach { p -> p.skillNumber = p.skillNumber + 1 }
                    this.latencyNumber = playersPool.filter { p -> p.latency < this.latency }.size + 1
                    playersPool.filter { p -> p.latency > this.latency }.forEach { p -> p.latencyNumber = p.latencyNumber + 1 }
                }
            }
            playersPool.add(player)
            calculateMetrics()
            lastInTime = player.inTime
            createGroups(0.1)
        }
        return true
    }

    fun calculateMetrics() {
        var currentLatencyMetric = 0.0
        var currentLatency = 0.0
        var currentSkillMetric = 0.0
        var currentSkill = 0.0
        playersPool.forEach {
            currentLatencyMetric += it.latency * it.latencyNumber
            currentLatency += it.latency
            currentSkillMetric += it.skill * it.skillNumber
            currentSkill += it.skill
        }
        playersPool.forEach { pl ->
            pl.latencyAverage = pl.latency / currentLatency
            pl.skillAverage = pl.skill / currentSkill
            pl.skillLatencyMetric = ((pl.latency * pl.latencyNumber / currentLatencyMetric) + (pl.skill * pl.skillNumber / currentSkillMetric)) / 2
        }
    }

    fun removePlayersGroup(players: List<Player>): Boolean {
        synchronized(playersPool) {
            if (players.none { !playersPool.contains(it) }) {
                players.forEach { playersPool.remove(it) }
                if (players.isEmpty()) {
                    lastInTime = null
                } else {
                    calculateMetrics()
                }
            } else {
                return false
            }
        }
        return true
    }

    fun print() {
        playersPool.sortedBy { player -> player.skillLatencyMetric }.forEach { player -> println("$player") }
    }

    fun createGroups(delta: Double): Boolean {
        val group = createGroup(playersPool, delta, groupSize)
        if (group.isNotEmpty()) {
            removePlayersGroup(group)
            outputGroupInfo(group)
        } else return false
        return true
    }

    private fun outputGroupInfo(group: List<Player>) {
        var minSkill = Double.MAX_VALUE
        var maxSkill = Double.MIN_VALUE
        var avgSkill = 0.0
        var minLatency = Double.MAX_VALUE
        var maxLatency = Double.MIN_VALUE
        var avgLatency = 0.0
        var minTime = Long.MAX_VALUE
        var maxTime = Long.MIN_VALUE
        var avgTime: Long = 0
        val currentTime = ZonedDateTime.now()
        group.forEach { player ->
            minSkill = min(minSkill, player.skill)
            maxSkill = max(maxSkill, player.skill)
            avgSkill += player.skill
            minLatency = min(minLatency, player.latency)
            maxLatency = max(maxLatency, player.latency)
            avgLatency += player.latency
            minTime = min(minTime, currentTime.toEpochSecond() - player.inTime.toEpochSecond())
            maxTime = max(maxTime, currentTime.toEpochSecond() - player.inTime.toEpochSecond())
            avgTime = currentTime.toEpochSecond() - player.inTime.toEpochSecond()
        }
        println(
            """-
    ${groupNumber++}
    $minSkill/$maxSkill/${avgSkill / groupSize} skill in group
    $minLatency/$maxLatency/${avgLatency / groupSize} latency in group
    ${minTime % 1000}/${maxTime % 1000}/${avgTime / groupSize / 1000} sec time spent in queue
    ${group.joinToString(",\n") { player -> player.name }}
            """.trimIndent()
        )
    }

    fun createGroup(playersList: List<Player>, delta: Double, size: Int): List<Player> {
        val result = mutableListOf<Player>()
        if (playersList.size < size) return result
        val currentList = playersList.sortedByDescending { it.skillLatencyMetric }
        for (i in 0..(currentList.size - size - 1)) {
            val checkedList = currentList.subList(i, min(i + 2 * size, currentList.size))
            checkedList.sortedBy { player -> player.skillAverage }
            for (j in 0..checkedList.size - size - 1) {
                if (checkedList.get(j).skillAverage - checkedList.get(j + size).skillAverage < delta) {
                    result.addAll(checkedList.subList(j, j + size))
                    return result
                }
            }
        }
        return result
    }

    companion object {
        val storage = PlayerStorage()
    }
}