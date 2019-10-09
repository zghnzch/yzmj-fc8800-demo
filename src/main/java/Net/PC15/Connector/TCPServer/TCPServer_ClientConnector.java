package Net.PC15.Connector.TCPServer;
import Net.PC15.Command.INCommand;
import Net.PC15.Connector.*;
import Net.PC15.Data.BytesData;
import Net.PC15.Data.INData;
import Net.PC15.Packet.INPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
//import java.util.logging.Level;
//import java.util.logging.Logger;
public class TCPServer_ClientConnector extends AbstractConnector {
	private final int _ClientID = TCPServerAllocator.GetNewClientID();
	protected TCPServerAllocator _ServerAllocator;
	private TCPServerClientDetail _RemoteDetail;
	private SocketChannel _Client;
	private TCPServer_ClientNettyHandler _Handler;
	private ChannelFuture _WriteFuture;
	public TCPServer_ClientConnector(TCPServerAllocator allocator, INConnectorEvent event, SocketChannel ch, TCPServer_ClientNettyHandler handler) throws CloneNotSupportedException {
		InetSocketAddress localAddress = ch.localAddress();
		InetSocketAddress remoteAddress = ch.remoteAddress();
		String loacalip = localAddress.getHostString();
		if (loacalip.equals("0:0:0:0:0:0:0:0")) {
			loacalip = "0.0.0.0";
		}
		this._RemoteDetail = new TCPServerClientDetail(this._ClientID);
		this._RemoteDetail.Local = new IPEndPoint(loacalip, localAddress.getPort());
		this._RemoteDetail.Remote = new IPEndPoint(remoteAddress.getHostString(), remoteAddress.getPort());
		this.SetEventHandle(event);
		this._ServerAllocator = allocator;
		this._Client = ch;
		this._Handler = handler;
		this._Status = E_ConnectorStatus.OnConnected;
	}
	public int ClientID() {
		return this._ClientID;
	}
	@Override
	protected ConnectorDetail GetConnectorDetail() {
		try {
			return this._RemoteDetail.clone();
		}
		catch (CloneNotSupportedException var2) {
			// Logger.getLogger(TCPServer_ClientConnector.class.getName()).log(Level.SEVERE, (String) null, var2);
			Logger.getRootLogger().info("err:" + var2);
			return null;
		}
	}
	@Override
	public E_ConnectorType GetConnectorType() {
		return E_ConnectorType.OnTCPServer_Client;
	}
	@Override
	protected synchronized void CheckStatus() {
		switch (this._Status) {
			case OnConnectTimeout:
			case OnClosed:
				this.fireConnectError();
				break;
			case OnConnected:
				this._ActivityCommand = (INCommand) this._CommandList.peek();
				if (this._ActivityCommand != null) {
					switch (this._ActivityCommand.GetStatus()) {
						case OnReady:
							INPacket send = this._ActivityCommand.GetPacket();
							ByteBuf packetBuf = send.GetPacketData();
							packetBuf.markReaderIndex();
							ByteBuf sendBuf = this._Client.alloc().buffer(packetBuf.readableBytes());
							sendBuf.writeBytes(packetBuf);
							packetBuf.resetReaderIndex();
							this._WriteFuture = this._Client.writeAndFlush(sendBuf);
							this._ActivityCommand.SendCommand(this._Event);
							this._WriteFuture.addListener(new WriteCallback(this));
							break;
						case OnWaitResponse:
							if (this._ActivityCommand.CheckTimeout(this._Event)) {
								this._CommandList.poll();
								this._ActivityCommand = null;
							}
						default:
					}
				}
			default:
		}
	}
	public void Close() {
		if (this._Client != null && this._Client.isActive()) {
			this._Client.close();
		}
	}
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (!this._isRelease) {
			this.ConnectSuccess();
		}
	}
	public void ConnectSuccess() {
		this._Status = E_ConnectorStatus.OnConnected;
		if (this._Event != null) {
			this._Event.ClientOnline(this._RemoteDetail);
		}
	}
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (!this._isRelease) {
			this.Offline();
			this._Client = null;
		}
	}
	public void Offline() {
		if (this._Status != E_ConnectorStatus.OnClosed) {
			this.fireConnectError();
			this._Status = E_ConnectorStatus.OnClosed;
			if (this._Event != null) {
				this._Event.ClientOffline(this._RemoteDetail);
			}
			if (this._ServerAllocator != null) {
				this._ServerAllocator.DeleteClient(this._ClientID);
			}
			this.Release();
		}
	}
	public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		if (!this._isRelease) {
			synchronized (this) {
				try {
					if (this._ActivityCommand != null) {
						boolean ret = this._ActivityCommand.CheckResponse(this._Event, msg);
						if (ret && this._ActivityCommand.getIsCommandOver()) {
							synchronized (this) {
								this._CommandList.poll();
								this._ActivityCommand = null;
							}
						}
					}
				}
				catch (Exception var10) {
				}
				try {
					if (this._DecompileList.size() > 0) {
						this.CheckWatchResponse(msg);
					}
					else if (this._Event != null) {
						INData bd = new BytesData();
						bd.SetBytes(msg);
						this._Event.WatchEvent(this._RemoteDetail, bd);
					}
				}
				catch (Exception var8) {
				}
			}
		}
	}
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent state = (IdleStateEvent) evt;
			switch (state.state()) {
				case READER_IDLE:
				case WRITER_IDLE:
			}
		}
	}
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		this.Offline();
	}
	@Override
	protected void Release0() {
		this.Offline();
		try {
			if (this._Handler != null) {
				this._Handler.Release();
			}
			this._Handler = null;
		}
		catch (Exception var3) {
		}
		try {
			if (this._Client != null) {
				if (this._Client.isActive()) {
					this._Client.close();
				}
				this._Client = null;
			}
		}
		catch (Exception var2) {
		}
	}
	private class WriteCallback implements ChannelFutureListener {
		TCPServer_ClientConnector _Client;
		public WriteCallback(TCPServer_ClientConnector client) {
			this._Client = client;
		}
		@Override
		public void operationComplete(ChannelFuture f) throws Exception {
			if (!this._Client._isRelease) {
				if (f.isDone()) {
					if (this._Client._ActivityCommand != null) {
						if (f.isSuccess()) {
							synchronized (this._Client) {
								try {
									if (!this._Client._ActivityCommand.getIsWaitResponse()) {
										this._Client._CommandList.poll();
										this._Client._ActivityCommand.RaiseCommandCompleteEvent(TCPServer_ClientConnector.this._Event);
										this._Client._ActivityCommand = null;
									}
								}
								catch (Exception var5) {
								}
							}
						}
						else if (f.isCancellable()) {
							System.out.println("WriteCallback -- 取消写！");
						}
						else {
							System.out.println("WriteCallback -- 写失败,表示连接已失效！");
						}
						this._Client = null;
					}
				}
			}
		}
	}
}
