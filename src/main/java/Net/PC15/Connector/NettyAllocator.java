package Net.PC15.Connector;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
public class NettyAllocator {
	private static EventLoopGroup NettyEventLoopGroup = new NioEventLoopGroup();
	private static EventLoopGroup NettyClientEventLoopGroup = new NioEventLoopGroup();
	public static synchronized EventLoopGroup GetEventLoopGroup() {
		if (NettyEventLoopGroup == null) {
			NettyEventLoopGroup = new NioEventLoopGroup();
		}
		return NettyEventLoopGroup;
	}
	public static synchronized EventLoopGroup GetClientEventLoopGroup() {
		if (NettyClientEventLoopGroup == null) {
			NettyClientEventLoopGroup = new NioEventLoopGroup();
		}
		return NettyClientEventLoopGroup;
	}
	public static void shutdownGracefully() {
		try {
			if (NettyEventLoopGroup != null) {
				NettyEventLoopGroup.shutdownGracefully().sync();
				NettyEventLoopGroup = null;
			}
			if (NettyClientEventLoopGroup != null) {
				NettyClientEventLoopGroup.shutdownGracefully().sync();
				NettyClientEventLoopGroup = null;
			}
		}
		catch (Exception var1) {
		}
	}
}
