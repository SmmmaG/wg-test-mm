package ru.iia.test.wg.service

import org.junit.Ignore
import org.junit.jupiter.api.Test
import ru.iia.test.wg.dto.Player
import kotlin.random.Random
@Ignore
class StorageServiceTest {
    @Test
    fun test100players() {
        for (number in 1..100) {
            PlayerStorage.storage.addPlayer(player = Player(latency = Random.nextDouble(300.0), skill = Random.nextDouble(80.0), name = "player$number"))
        }
        PlayerStorage.storage.print()
        for(number in 1..35) {
            PlayerStorage.storage.createGroups(0.3, 4)
        }
    }

    @Test
    fun test10players() {
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = Random.nextDouble(300.0), skill = Random.nextDouble(80.0), name = "player$number"))
        }
        PlayerStorage.storage.print()
        for(number in 1..4) {
            PlayerStorage.storage.createGroups(0.3, 4)
        }

    }

    @Test
    fun test10OrderPlayers() {
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = number * 1.2, skill = number * 3.5, name = "player$number"))
        }
        PlayerStorage.storage.print()

        for(number in 1..4) {
            PlayerStorage.storage.createGroups(0.3, 4)
        }
    }

    @Test
    fun test10OrderAscDescPlayers() {
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = number * 1.2, skill = 35 - number * 3.5, name = "player$number"))
        }
        PlayerStorage.storage.print()
        for(number in 1..4) {
            PlayerStorage.storage.createGroups(0.3, 4)
        }

    }

    @Test
    fun test10EqualPlayers() {
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = 120.0, skill = 35.0, name = "player$number"))
        }
        PlayerStorage.storage.print()
        for(number in 1..4) {
            PlayerStorage.storage.createGroups(0.3, 4)
        }

    }

    @Test
    fun test20EqualPlayers() {
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = 120.0, skill = 35.0, name = "player$number"))
        }
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = 20.0, skill = 65.0, name = "player1$number"))
        }
        PlayerStorage.storage.print()
        for(number in 1..8) {
            PlayerStorage.storage.createGroups(0.3, 4)
        }

    }

    @Test
    fun test20EqualPlayers2() {
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = 120.0, skill = 65.0, name = "player$number"))
        }
        for (number in 1..10) {
            PlayerStorage.storage.addPlayer(player = Player(latency = 20.0, skill = 35.0, name = "player$number"))
        }
        PlayerStorage.storage.print()
        for(number in 1..8) {
            PlayerStorage.storage.createGroups(0.3, 4)
        }

    }

}