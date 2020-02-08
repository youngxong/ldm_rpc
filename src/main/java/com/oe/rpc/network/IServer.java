package com.oe.rpc.network;

public interface IServer extends Runnable {

  void start() throws Exception;

  void close();
}
