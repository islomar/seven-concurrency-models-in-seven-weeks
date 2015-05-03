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
import ClusterEvent._
import MemberStatus._
import akka.routing.BroadcastRouter

class Counters(count: Int) extends Actor {

  val counters = context.actorOf(Props[Counter].
    withRouter(new BroadcastRouter(count)), "counter")

  override def preStart {
    Cluster(context.system).subscribe(self, classOf[MemberUp])
  }

  def receive = {
    case state: CurrentClusterState =>
      for (member <- state.members if (member.status == Up))
        counters ! ParserAvailable(findParser(member))

    case MemberUp(member) => counters ! ParserAvailable(findParser(member))
  }

  def findParser(member: Member) = 
    context.actorFor(RootActorPath(member.address) / "user" / "parser")
}
