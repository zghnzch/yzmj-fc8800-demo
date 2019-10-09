//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector.TCPClient;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
public class TCPClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	public TCPClientChannelInitializer() {
	}
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new IdleStateHandler(20, 20, 0));
	}
}
