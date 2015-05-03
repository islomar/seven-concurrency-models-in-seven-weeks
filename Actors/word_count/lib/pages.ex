#---
# Excerpted from "Seven Concurrency Models in Seven Weeks",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
#---
defmodule Pages do
  def start_link(filename) do
    spawn_link(__MODULE__, :loop, [filename])
  end

  def next(xml_parser) do
    send(xml_parser, {:get_next, self})
    receive do
      {:next_page, page} -> page
    end
  end

  def loop(filename) do
    :xmerl_sax_parser.file(filename,
      event_fun: &event_fun/3,
      event_state: :top)
    loop_done
  end

  defp loop_done do
    receive do
      {:get_next, from} -> send(from, {:next_page, nil})
    end
    loop_done
  end

  defp event_fun({:startElement, _, 'page', _, _}, _, :top) do
    :page
  end

  defp event_fun({:startElement, _, 'text', _, _}, _, :page) do
    :text
  end

  defp event_fun({:characters, chars}, _, :text) do
    {:ok, s} = String.from_char_list(chars)
    receive do
      {:get_next, from} -> send(from, {:next_page, s})
    end
    :text
  end

  defp event_fun({:endElement, _, 'text', _}, _, :text) do
    :page
  end

  defp event_fun({:endElement, _, 'page', _}, _, :page) do
    :top
  end

  defp event_fun({:endDocument}, _, state) do
    receive do
      {:get_next, from} -> send(from, {:done})
    end
    state
  end

  defp event_fun(_, _, state) do
    state
  end
end
