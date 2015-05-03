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
import akka.cluster.ClusterEvent._

case class HelloFrom(actor: ActorRef)

class TestActor extends Actor {

  def receive = {
    case MemberUp(member) =>
      println(s"Member is up: $member")
      val remotePath = RootActorPath(member.address) / "user" / "test-actor"
      val remote = context.actorFor(remotePath)
      remote ! HelloFrom(self)
      context.watch(remote)

    case HelloFrom(actor) => println(s"Hello from: $actor")
    case Terminated(actor) => println(s"Terminated: $actor")
    case event => println(s"Event: $event")
  }
}
