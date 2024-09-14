package tech.pacia.okapi.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import io.ktor.http.ContentType.Application.Json
import kotlinx.coroutines.runBlocking
import tech.pacia.okapi.client.OpencachingClient

class App : CliktCommand() {
    override fun run() {}
}

class Logs : CliktCommand(name = "logs", help = "Perform operations on logs") {
    private val geocacheCode: String by option("--code").required().help("Cache ID")
    private val consumerKey: String by option(envvar = "OKAPI_CONSUMER_KEY").required().help("OKAPI customer key")

    private val offset: Int by option("--offset").int().default(0).help("Offset")
    private val limit: Int by option("--limit").int().default(10).help("Number of logs to fetch")

    override fun run() = runBlocking {
        val client = OpencachingClient(consumerKey = consumerKey)

        client
            .getGeocacheLogs(geocacheCode, offset = offset, limit = limit)
            .reversed()
            .forEach {
                echo(Json.encodeToString() it)
                // echo("${it.user.username} • ${it.dateCreated} • ${it.type}\n${it.comment}\n\n")
            }
    }
}

class Geocache : CliktCommand(name = "geocache", help = "Perform operations on geocaches") {
    override fun run() {}
}

class GeocacheGet : CliktCommand(name = "get") {
    private val geocacheCode: String by option("--code").required().help("Cache ID")
    private val consumerKey: String by option(envvar = "OKAPI_CONSUMER_KEY").required().help("OKAPI customer key")

    override fun run() = runBlocking {
        val client = OpencachingClient(consumerKey = consumerKey)
        echo(client.getGeocache(geocacheCode))
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
