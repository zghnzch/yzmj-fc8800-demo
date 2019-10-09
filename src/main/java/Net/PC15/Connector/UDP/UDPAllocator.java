package Net.PC15.Connector.UDP;
import Net.PC15.Connector.NettyAllocator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;
public class UDPAllocator {
	private Bootstrap UDPBootstrap = new Bootstrap();
	public UDPAllocator() {
		this.UDPBootstrap.group(NettyAllocator.GetClientEventLoopGroup()).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, false).handler(new UDPChannelInitializer());
	}
	public ChannelFuture Bind(String IP, int Port) {
		return this.UDPBootstrap.bind(IP, Port);
	}
	public ChannelFuture Bind(int Port) {
		return this.UDPBootstrap.bind(Port);
	}
	public void shutdownGracefully() {
		try {
			this.UDPBootstrap = null;
		}
		catch (Exception var2) {
		}
	}
}
