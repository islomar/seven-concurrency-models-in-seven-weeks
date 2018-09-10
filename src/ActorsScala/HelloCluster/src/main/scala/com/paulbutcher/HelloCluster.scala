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
import akka.cluster.ClusterEvent._
import scopt.mutable.OptionParser

object HelloCluster extends App {

  val opts = parseCommandline

  class Options {
    var localHost = "127.0.0.1"
    var localPort = "2552"
    var clusterHost = "127.0.0.1"
    var clusterPort = 2552
  }

  def parseCommandline = new Options {
    val optionParser = new OptionParser {
      opt("h", "local-host", "hostname", { localHost = _ })
      opt("p", "local-port", "port", { localPort = _ })
      opt("H", "cluster-host", "hostname", { clusterHost = _ })
      intOpt("P", "cluster-port", "port", { clusterPort = _ })
    }
    if (!optionParser.parse(args))
      sys.exit(0)
  }

  System.setProperty("akka.remote.netty.hostname", opts.localHost)
  System.setProperty("akka.remote.netty.port", opts.localPort)

  val system = ActorSystem("ClusterTest")

  val testActor = system.actorOf(Props[TestActor], "test-actor")
  Cluster(system).subscribe(testActor, classOf[MemberEvent])

  Cluster(system).join(
    Address("akka", "ClusterTest", opts.clusterHost, opts.clusterPort))
}
