package me.udnek.fnafu.event

import me.udnek.coreu.custom.event.CustomEvent
import me.udnek.fnafu.mechanic.system.System

class SystemRepairedEvent (val system: System) : CustomEvent()
