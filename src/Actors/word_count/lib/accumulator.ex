#---
# Excerpted from "Seven Concurrency Models in Seven Weeks",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
#---
defmodule Accumulator do
  use GenServer.Behaviour

  #####
  # External API

  def start_link do
    :gen_server.start_link({:global, :wc_accumulator}, __MODULE__, 
      {HashDict.new, HashSet.new}, [])
  end

  def deliver_counts(ref, counts) do
    :gen_server.cast({:global, :wc_accumulator}, {:deliver_counts, ref, counts}) 
  end

  def get_results do
    :gen_server.call({:global, :wc_accumulator}, {:get_results})
  end

  #####
  # GenServer implementation

  def handle_cast({:deliver_counts, ref, counts}, {totals, processed_pages}) do
    if Set.member?(processed_pages, ref) do
      {:noreply, {totals, processed_pages}}
    else
      new_totals = Dict.merge(totals, counts, fn(_k, v1, v2) -> v1 + v2 end)
      new_processed_pages = Set.put(processed_pages, ref)
      Parser.processed(ref)
      {:noreply, {new_totals, new_processed_pages}}
    end
  end

  def handle_call({:get_results}, _, {totals, processed_pages}) do
    {:reply, {totals, processed_pages}, {totals, processed_pages}}
  end
end

defmodule AccumulatorSupervisor do
  use Supervisor.Behaviour

  def start_link do
    :supervisor.start_link(__MODULE__, []) 
  end

  def init(_args) do
    workers = [worker(Accumulator, [])]
    supervise(workers, strategy: :one_for_one)
  end
end
