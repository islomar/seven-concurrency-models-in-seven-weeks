/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher

import akka.actor._
import akka.cluster._
import scopt.mutable.OptionParser

object WordCount extends App {

  val opts = parseCommandLine

  class Options {
    var localHost = "127.0.0.1"
    var localPort = "2552"
    var clusterHost = "127.0.0.1"
    var clusterPort = 2552
    var counters = 0
    var filename = ""
    var batchSize = 10
    var limit = 100000
  }

  def parseCommandLine = new Options {
    val optionParser = new OptionParser {
      opt("h", "local-host", "hostname", { localHost = _ })
      opt("p", "local-port", "port", { localPort = _ })
      opt("H", "cluster-host", "hostname", { clusterHost = _ })
      intOpt("P", "cluster-port", "port", { clusterPort = _ })
      intOpt("c", "counters", "number of counters", { counters = _ })
      opt("f", "filename", { filename = _ })
      intOpt("b", "batch-size", { batchSize = _ })
      intOpt("l", "limit", { limit = _ })
    }
    if (!optionParser.parse(args))
      sys.exit(0)
  }

  System.setProperty("akka.remote.netty.hostname", opts.localHost)
  System.setProperty("akka.remote.netty.port", opts.localPort)

  val system = ActorSystem("WordCount")

  if (opts.counters > 0)
    system.actorOf(Props(new Counters(opts.counters)), "counters")
  else
    system.actorOf(Props(new Parser(opts.filename, opts.batchSize, opts.limit)), "parser")

  Cluster(system).join(
    Address("akka", "WordCount", opts.clusterHost, opts.clusterPort))
}
