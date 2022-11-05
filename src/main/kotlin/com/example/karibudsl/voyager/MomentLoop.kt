package com.example.demo2.voyager

import org.w3c.dom.events.EventTarget
import java.util.*


public class MomentLoop() {

    // config
    var speed = 253;
    var backSpeed = 253;
    var distance = 60;
    var turnDegree = 40
    var turnSpeed = 253

    val maxAllowedSpeed = 253
    val maxAllowedSpeedDistance = 100

    val maxAllowedTurnSpeed = 253
    val maxAllowedTurnDegree = 360



    var gir = 1

    val client = VyAgerClient()
    val scanner = Scanner(System.`in`)


    fun run() {


        while (true) {
            println("Gir : $gir")
            val input = readln()
            val inputChr = if (input.length == 1) input.get(0) else 'æ'
            println(inputChr)
            inputCharTilAction(inputChr)
        }
    }

    fun økForwardSpeed(int: Int){
        speed += int
        if (speed > maxAllowedSpeed) speed = maxAllowedSpeed
        if (speed < maxAllowedSpeed) speed = 1
    }
    fun økForwardDistance(int: Int){
        speed += int
        if (speed > maxAllowedSpeed) speed = maxAllowedSpeed
        if (speed < maxAllowedSpeed) speed = 1
    }
    fun økBackSpeed(int: Int){
        backSpeed += int
        if (backSpeed > maxAllowedSpeed) backSpeed = maxAllowedSpeed
        if (backSpeed < maxAllowedSpeed) backSpeed = 1
    }

    fun økTurnSpeed(int: Int){
        turnSpeed += int
        if (turnSpeed > maxAllowedTurnSpeed) turnSpeed = maxAllowedTurnSpeed
        if (turnSpeed < maxAllowedTurnSpeed) turnSpeed = 1
    }
    fun økTurnDegree(int: Int){
        turnDegree += int
        if (turnDegree > maxAllowedTurnDegree) turnDegree = maxAllowedTurnDegree
        if (turnDegree < maxAllowedTurnDegree) turnDegree = 1
    }

    fun inputCharTilAction(inputChr: Char) {
        when (inputChr) {
            ' ' -> client.sendStopSignal()
            'x' -> client.sendStopSignal()
            'w' -> client.moveForward(speed = speed, distance = distance)
            's' -> client.moveBacward(speed = backSpeed, distance = distance)
            'a' -> client.turnLeft(turnSpeed, degrees = turnDegree) // øk
            'd' -> client.turnRight(turnSpeed, degrees = turnDegree)
            'r' -> client.rfidScann()
            'q' -> client.rightMotorForwards(speed, distance)
            'e' -> client.leftMotorForwards(speed, distance)

            '7' -> gir = 1
            '8' -> gir = 2
            '9' -> gir = 3

            't' -> client.moveForward(speed = speed, distance = distance / 2)
            'g' -> client.moveBacward(speed = backSpeed, distance = distance / 2)
            'f' -> client.turnLeft(turnSpeed, degrees = turnDegree / 2) // øk
            'h' -> client.turnRight(turnSpeed, degrees = turnDegree / 2)

            'i' -> client.moveForward(speed = speed, distance = distance / 3)
            'k' -> client.moveBacward(speed = backSpeed, distance = distance / 3)
            'j' -> client.turnLeft(turnSpeed, degrees = turnDegree / 3) // øk
            'l' -> client.turnRight(turnSpeed, degrees = turnDegree / 3)
        }
    }
}

fun main(){
    val a = MomentLoop()
    a.run()
}