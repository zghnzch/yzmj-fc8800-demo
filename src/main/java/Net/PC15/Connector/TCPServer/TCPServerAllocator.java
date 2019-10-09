//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector.TCPServer;
import Net.PC15.Connector.INConnector;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Connector.NettyAllocator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
public class TCPServerAllocator {
	public static AtomicInteger ClientAllocator = new AtomicInteger(1);
	protected ConcurrentHashMap<String, Channel> _ServerMap = new ConcurrentHashMap(20);
	protected ConcurrentHashMap<String, TCPServer_ClientConnector> _ClientMap = new ConcurrentHashMap(20000);
	private ServerBootstrap TCPServerBootstrap = new ServerBootstrap();
	public TCPServerAllocator(INConnectorEvent event) {
		this.TCPServerBootstrap.group(NettyAllocator.GetEventLoopGroup(), NettyAllocator.GetClientEventLoopGroup()).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new TCPServer_ClientChannelInitializer(event, this));
	}
	public static int GetNewClientID() {
		return ClientAllocator.getAndIncrement();
	}
	public void AddClient(TCPServer_ClientConnector client) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("TCPServer_Client:");
		keybuf.append(client.ClientID());
		String key = keybuf.toString();
		if (!this._ClientMap.containsKey(key)) {
			this._ClientMap.put(key, client);
		}
	}
	public void DeleteClient(int clientID) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("TCPServer_Client:");
		keybuf.append(clientID);
		String key = keybuf.toString();
		this._ClientMap.remove(key);
	}
	public void StopClient(int clientID) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("TCPServer_Client:");
		keybuf.append(clientID);
		String key = keybuf.toString();
		if (this._ClientMap.containsKey(key)) {
			TCPServer_ClientConnector client = this._ClientMap.get(key);
			client.Close();
			this._ClientMap.remove(key);
		}
	}
	public INConnector SearchClient(TCPServerClientDetail client) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("TCPServer_Client:");
		keybuf.append(client.ClientID);
		String key = keybuf.toString();
		return this._ClientMap.containsKey(key) ? this._ClientMap.get(key) : null;
	}
	public boolean Listen(String IP, int Port) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("TCPServer:");
		keybuf.append(IP);
		keybuf.append(":");
		keybuf.append(Port);
		String key = keybuf.toString();
		if (this._ServerMap.containsKey(key)) {
			return true;
		}
		else {
			ChannelFuture future;
			if (IP.equals("0.0.0.0")) {
				future = this.TCPServerBootstrap.bind(Port);
			}
			else {
				future = this.TCPServerBootstrap.bind(IP, Port);
			}
			try {
				future.sync();
				this._ServerMap.put(keybuf.toString(), future.channel());
				return true;
			}
			catch (Exception var7) {
				return false;
			}
		}
	}
	public boolean Listen(int Port) {
		return this.Listen("0.0.0.0", Port);
	}
	public void StopListen(String IP, int Port) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("TCPServer:");
		keybuf.append(IP);
		keybuf.append(":");
		keybuf.append(Port);
		String key = keybuf.toString();
		if (this._ServerMap.containsKey(key)) {
			Channel server = this._ServerMap.get(key);
			try {
				server.close().sync();
				this._ServerMap.remove(key);
			}
			catch (Exception var7) {
				System.out.println("Net.PC15.Connector.TCPServer.TCPServerAllocator.StopListen()" + var7.getMessage());
			}
		}
	}
	public void StopListen(int Port) {
		this.StopListen("0.0.0.0", Port);
	}
	public ArrayList<IPEndPoint> getServerList() {
		ArrayList<IPEndPoint> lst = new ArrayList(20);
		if (this._ServerMap == null) {
			return lst;
		}
		else if (this._ServerMap.size() == 0) {
			return lst;
		}
		else {
			this._ServerMap.entrySet().forEach((value) -> {
				Channel ch = value.getValue();
				InetSocketAddress localAddress = (InetSocketAddress) ch.localAddress();
				String hostString = localAddress.getHostString();
				if (hostString.equals("0:0:0:0:0:0:0:0")) {
					hostString = "0.0.0.0";
				}
				IPEndPoint ip = new IPEndPoint(hostString, localAddress.getPort());
				lst.add(ip);
			});
			return lst;
		}
	}
	public void shutdownGracefully() {
		try {
			this.TCPServerBootstrap = null;
		}
		catch (Exception var2) {
		}
	}
	public void CheckClientStatus(ExecutorService workService) {
		if (this._ClientMap.size() > 0) {
			this._ClientMap.entrySet().forEach((value) -> {
				INConnector connector = value.getValue();
				workService.submit(() -> {
					connector.run();
				});
			});
		}
	}
}
