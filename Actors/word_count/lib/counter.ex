#---
# Excerpted from "Seven Concurrency Models in Seven Weeks",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
#---
defmodule Counter do
  use GenServer.Behaviour

  #####
  # External API

  def start_link do
    :gen_server.start_link(__MODULE__, nil, [])
  end

  def deliver_page(pid, ref, page) do
    :gen_server.cast(pid, {:deliver_page, ref, page})
  end

  #####
  # GenServer implementation

  def init(_args) do
    Parser.request_page(self()) 
    {:ok, nil}
  end

  def handle_cast({:deliver_page, ref, page}, state) do 
    Parser.request_page(self()) 

    words = String.split(page) 
    counts = Enum.reduce(words, HashDict.new, fn(word, counts) -> 
        Dict.update(counts, word, 1, &(&1 + 1))
      end) 
    Accumulator.deliver_counts(ref, counts) 

    {:noreply, state}
  end
end

defmodule CounterSupervisor do
  use Supervisor.Behaviour

  def start_link(num_counters) do
    :supervisor.start_link(__MODULE__, num_counters) 
  end

  def init(num_counters) do
    workers = Enum.map(1..num_counters, fn(n) -> 
      worker(Counter, [], id: "counter#{n}")
    end)
    supervise(workers, strategy: :one_for_one)
  end
end
