//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector.TCPClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
public class TCPClientNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
	private TCPClientConnector _Client;
	public TCPClientNettyHandler(TCPClientConnector client) {
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
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
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
