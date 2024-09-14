package tech.pacia.okapi.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.runBlocking
import tech.pacia.okapi.client.OpencachingClient

class App : CliktCommand() {
    val count: Int by option().int().default(1).help("Number of greetings")

    override fun run() {
        echo("executing app")
    }
}

class Logs : CliktCommand(name = "logs") {
    val geocacheCode: String by option("--code").required().help("Cache ID")
    val consumerKey: String by option(envvar = "OKAPI_CONSUMER_KEY").required().help("OKAPI customer key")

    override fun run() = runBlocking {
        println("Hello!")
        val client = OpencachingClient(consumerKey = consumerKey)

        println(client.getGeocache(geocacheCode))

        client.getGeocacheLogs(geocacheCode).reversed().forEach {
            println("${it.user.username} • ${it.dateCreated} • ${it.type}\n${it.comment}\n\n")
        }

    }
}

class Geocache : CliktCommand(name = "geocache") {
    override fun run() {
        echo("executing geocache")
    }
}

class GeocacheGet : CliktCommand(name = "get") {
    override fun run() {
        echo("executing geocache get")
    }
}

class GeocacheList : CliktCommand(name = "list") {
    override fun run() {
        echo("executing geocache list")
    }
}

fun execute(args: Array<String>) {
    App().subcommands(
        Logs(),
        Geocache().subcommands(
            GeocacheGet(),
            GeocacheList(),
        )
    ).main(args)
}
