package com.example

import com.jessecorbett.diskord.bot.bot
import com.jessecorbett.diskord.bot.classicCommands
import com.jessecorbett.diskord.bot.events

private val BOT_TOKEN = try {
    ClassLoader.getSystemResource("bot-token.txt").readText().trim()
} catch (error: Exception) {
    throw RuntimeException("Failed to load bot token. Make sure to create a file named bot-token.txt in" +
            " src/main/resources and paste the bot token into that file.", error)
}

suspend fun main(args: Array<String>) {
    bot(BOT_TOKEN) {
        events {
            onGuildMemberAdd {
//                channel(WELCOME_CHANNEL_ID).sendMessage("Welcome to the server, ${it.user?.username}!")
            }
        }

        classicCommands {
            command("ping") {
                it.respond("pong")
            }
        }
    }
}

