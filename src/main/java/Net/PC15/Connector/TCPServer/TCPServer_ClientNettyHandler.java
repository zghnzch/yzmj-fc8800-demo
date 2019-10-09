//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector.TCPServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
public class TCPServer_ClientNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
	private TCPServer_ClientConnector _Client;
	public TCPServer_ClientNettyHandler() {
	}
	public void SetClient(TCPServer_ClientConnector client) {
		this._Client = client;
	}
	public void Release() {
		this._Client = null;
	}
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (this._Client != null) {
			this._Client.exceptionCaught(ctx, cause);
		}
	}
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		if (this._Client != null) {
			this._Client.channelRead0(ctx, msg);
		}
	}
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (this._Client != null) {
			this._Client.channelActive(ctx);
		}
		super.channelActive(ctx);
	}
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (this._Client != null) {
			this._Client.channelInactive(ctx);
		}
		super.channelInactive(ctx);
	}
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (this._Client != null) {
			this._Client.userEventTriggered(ctx, evt);
		}
		super.userEventTriggered(ctx, evt);
	}
}
