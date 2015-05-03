#---
# Excerpted from "Seven Concurrency Models in Seven Weeks",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
#---
defmodule Cache do
  use GenServer.Behaviour

  #####
  # External API

  def start_link do
    :gen_server.start_link({:local, :cache}, __MODULE__, {HashDict.new, 0}, [])
  end

  def put(url, page) do
    :gen_server.cast(:cache, {:put, url, page})
  end

  def get(url) do
    :gen_server.call(:cache, {:get, url})
  end

  def size do
    :gen_server.call(:cache, {:size})
  end

  #####
  # GenServer implementation

  def handle_cast({:put, url, page}, {pages, size}) do
    new_pages = Dict.put(pages, url, page)
    new_size = size + byte_size(page)
    {:noreply, {new_pages, new_size}}
  end

  def handle_call({:get, url}, _from, {pages, size}) do
    {:reply, pages[url], {pages, size}}
  end

  def handle_call({:size}, _from, {pages, size}) do
    {:reply, size, {pages, size}}
  end
end

defmodule CacheSupervisor do
  use Supervisor.Behaviour

  def start_link do
    :supervisor.start_link(__MODULE__, []) 
  end

  def init(_args) do
    workers = [worker(Cache, [])]
    supervise(workers, strategy: :one_for_one)
  end
end
