//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector.UDP;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.IdleStateHandler;
public class UDPChannelInitializer extends ChannelInitializer<NioDatagramChannel> {
	public UDPChannelInitializer() {
	}
	protected void initChannel(NioDatagramChannel ch) throws Exception {
		ch.pipeline().addLast(new IdleStateHandler(20, 20, 0));
	}
}
