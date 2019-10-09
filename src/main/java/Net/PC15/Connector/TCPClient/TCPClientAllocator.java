package Net.PC15.Connector.TCPClient;
import Net.PC15.Connector.NettyAllocator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
public class TCPClientAllocator {
	public static final int CONNECT_TIMEOUT_MILLIS_MAX = 30000;
	public static final int CONNECT_TIMEOUT_MILLIS_MIN = 1000;
	public static final int CONNECT_RECONNECT_MAX = 5;
	private Bootstrap TCPBootstrap = new Bootstrap();
	private TCPClientChannelInitializer TCPInitializer = new TCPClientChannelInitializer();
	public TCPClientAllocator() {
		this.TCPBootstrap.group(NettyAllocator.GetClientEventLoopGroup()).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000).handler(this.TCPInitializer);
	}
	public ChannelFuture connect(String IP, int Port) {
		return this.TCPInitializer == null ? null : this.TCPBootstrap.connect(IP, Port);
	}
	public void shutdownGracefully() {
		this.TCPInitializer = null;
		try {
			this.TCPBootstrap = null;
		}
		catch (Exception var2) {
		}
	}
}
