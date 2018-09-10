#---
# Excerpted from "Seven Concurrency Models in Seven Weeks",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
#---
defmodule Counter do
  def loop(count) do
    receive do
      {:next, sender, ref} ->
        send(sender, {:ok, ref, count})
        loop(count + 1)
    end
  end

  def start(count) do
    pid = spawn(__MODULE__, :loop, [count])
    Process.register(pid, :counter)
    pid
  end

  def next do
    ref = make_ref()
    send(:counter, {:next, self(), ref})
    receive do
      {:ok, ^ref, count} -> count
    end
  end
end
