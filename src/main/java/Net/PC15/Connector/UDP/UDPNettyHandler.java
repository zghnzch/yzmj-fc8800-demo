//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector.UDP;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
public class UDPNettyHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private UDPConnector _Client;
	public UDPNettyHandler(UDPConnector client) {
		this._Client = client;
	}
	public void Release() {
		this._Client = null;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (this._Client != null) {
			this._Client.exceptionCaught(ctx, cause);
		}
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		if (this._Client != null) {
			this._Client.channelRead0(ctx, msg);
		}
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (this._Client != null) {
			this._Client.channelActive(ctx);
		}
		super.channelActive(ctx);
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (this._Client != null) {
			this._Client.channelInactive(ctx);
		}
		super.channelInactive(ctx);
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (this._Client != null) {
			this._Client.userEventTriggered(ctx, evt);
		}
		super.userEventTriggered(ctx, evt);
	}
}
