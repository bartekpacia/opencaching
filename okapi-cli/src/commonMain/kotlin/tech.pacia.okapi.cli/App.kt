package tech.pacia.okapi.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

class App : CliktCommand() {
    val count: Int by option().int().default(1).help("Number of greetings")

    override fun run() {
        echo("executing app")
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
        Geocache().subcommands(
            GeocacheGet(),
            GeocacheList(),
        )
    ).main(args)
}
