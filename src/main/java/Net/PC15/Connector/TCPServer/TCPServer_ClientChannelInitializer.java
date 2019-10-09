//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector.TCPServer;
import Net.PC15.Connector.INConnectorEvent;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
public class TCPServer_ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	protected INConnectorEvent _Event;
	protected TCPServerAllocator _ServerAllocator;
	public TCPServer_ClientChannelInitializer(INConnectorEvent event, TCPServerAllocator alloc) {
		this._Event = event;
		this._ServerAllocator = alloc;
	}
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new IdleStateHandler(10, 10, 0));
		TCPServer_ClientNettyHandler handler = new TCPServer_ClientNettyHandler();
		ch.pipeline().addLast(handler);
		TCPServer_ClientConnector client = new TCPServer_ClientConnector(this._ServerAllocator, this._Event, ch, handler);
		handler.SetClient(client);
		this._ServerAllocator.AddClient(client);
	}
}
