#---
# Excerpted from "Seven Concurrency Models in Seven Weeks",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
#---
defmodule WordCount.Mixfile do
  use Mix.Project

  def project do
    [ app: :word_count,
      version: "0.0.1",
      elixir: "~> 0.10.1",
      deps: deps ]
  end

  def application do
    []
  end

  defp deps do
    []
  end
end
