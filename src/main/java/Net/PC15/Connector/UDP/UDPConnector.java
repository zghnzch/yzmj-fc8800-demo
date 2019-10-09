package Net.PC15.Connector.UDP;
import Net.PC15.Command.INCommand;
import Net.PC15.Connector.AbstractConnector;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ConnectorStatus;
import Net.PC15.Connector.E_ConnectorType;
import Net.PC15.Packet.INPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author yz
 */
public class UDPConnector extends AbstractConnector {
	private UDPAllocator _UDPAllocator;
	private UDPDetail _RemoteDetail;
	private Channel _UDPChannel;
	private ChannelFuture _ConnectFuture;
	private UDPNettyHandler _Handler;
	private ChannelFuture _WriteFuture;
	public UDPConnector(UDPAllocator allocator, UDPDetail detail) throws CloneNotSupportedException {
		this._RemoteDetail = detail.clone();
		this._UDPAllocator = allocator;
	}
	@Override
	protected ConnectorDetail GetConnectorDetail() {
		try {
			return this._RemoteDetail.clone();
		}
		catch (CloneNotSupportedException var2) {
			Logger.getLogger(UDPConnector.class.getName()).log(Level.SEVERE, null, var2);
			return null;
		}
	}
	@Override
	public E_ConnectorType GetConnectorType() {
		return E_ConnectorType.OnTCPClient;
	}
	@Override
	protected synchronized void CheckStatus() {
		switch (this._Status) {
			case OnConnectTimeout:
			case OnClosed:
				if (this._CommandList.peek() == null && !this._IsForcibly) {
					this.CheckChanelIsInvalid();
				}
				else {
					this.UpdateActivityTime();
					this.UDPBind();
				}
				break;
			case OnConnected:
				this._ActivityCommand = (INCommand) this._CommandList.peek();
				if (this._ActivityCommand != null) {
					this.UpdateActivityTime();
					switch (this._ActivityCommand.GetStatus()) {
						case OnReady:
							UDPDetail uDetail = (UDPDetail) this._ActivityCommand.getCommandParameter().getCommandDetail().Connector;
							INPacket send = this._ActivityCommand.GetPacket();
							ByteBuf packetBuf = send.GetPacketData();
							packetBuf.markReaderIndex();
							ByteBuf sendBuf = this._UDPChannel.alloc().buffer(packetBuf.readableBytes());
							sendBuf.writeBytes(packetBuf);
							packetBuf.resetReaderIndex();
							try {
								InetSocketAddress remote = new InetSocketAddress(uDetail.IP, uDetail.Port);
								this._UDPChannel.config().setOption(ChannelOption.SO_BROADCAST, uDetail.Broadcast);
								DatagramPacket dp = new DatagramPacket(sendBuf, remote);
								this._WriteFuture = this._UDPChannel.writeAndFlush(dp);
								this._ActivityCommand.SendCommand(this._Event);
								this._WriteFuture.addListener(new WriteCallback(this));
							}
							catch (Exception var7) {
							}
							break;
						case OnWaitResponse:
							if (this._ActivityCommand.CheckTimeout(this._Event)) {
								this._CommandList.poll();
								this._ActivityCommand = null;
							}
						default:
					}
				}
				else {
					if (this._IsForcibly) {
						this.UpdateActivityTime();
						return;
					}
					this._Status = E_ConnectorStatus.OnClosing;
					this._UDPChannel.close();
				}
			default:
		}
	}
	private void UDPBind() {
		if (this._UDPAllocator != null) {
			if (this._UDPChannel != null) {
				this._UDPChannel.close();
				this._UDPChannel = null;
			}
			if (this._ActivityCommand == null) {
				this._ActivityCommand = (INCommand) this._CommandList.peek();
				if (this._ActivityCommand != null) {
					this._ActivityCommand.RaiseCommandProcessEvent(this._Event);
				}
			}
			this._Status = E_ConnectorStatus.OnConnecting;
			try {
				if (this._RemoteDetail.LocalIP == null) {
					this._ConnectFuture = this._UDPAllocator.Bind(this._RemoteDetail.LocalPort);
				}
				else {
					this._ConnectFuture = this._UDPAllocator.Bind(this._RemoteDetail.LocalIP, this._RemoteDetail.LocalPort);
				}
				this._UDPChannel = this._ConnectFuture.channel();
				if (this._ConnectFuture.isDone()) {
					try {
						(new connectCallback(this)).operationComplete(this._ConnectFuture);
					}
					catch (Exception var2) {
					}
				}
				else {
					this._ConnectFuture.addListener(new connectCallback(this));
				}
			}
			catch (Exception var3) {
				System.out.println("Net.PC15.Connector.UDP.UDPConnector.UDPBind()" + var3.getLocalizedMessage());
				this._Status = E_ConnectorStatus.OnError;
			}
		}
	}
	private void ConnectFail() {
		if (this._UDPChannel != null) {
			this._UDPChannel.close();
			this._UDPChannel = null;
			this.fireConnectError();
			this._Status = E_ConnectorStatus.OnError;
		}
	}
	private void ConnectSuccess() {
		this._Status = E_ConnectorStatus.OnConnected;
	}
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (!this._isRelease) {
		}
	}
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (!this._isRelease) {
			if (this._Status != E_ConnectorStatus.OnClosing) {
				this.fireConnectError();
			}
			this._Status = E_ConnectorStatus.OnClosed;
			this._UDPChannel = null;
		}
	}
	public void channelRead0(ChannelHandlerContext ctx, DatagramPacket UDPmsg) throws Exception {
		if (!this._isRelease) {
			synchronized (this) {
				ByteBuf msg = UDPmsg.content();
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
				catch (Exception var11) {
				}
				try {
					this.CheckWatchResponse(msg);
				}
				catch (Exception var9) {
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
		this._Status = E_ConnectorStatus.OnError;
	}
	@Override
	protected void Release0() {
		try {
			if (this._Handler != null) {
				this._Handler.Release();
			}
			this._Handler = null;
			if (this._ConnectFuture != null) {
				this._ConnectFuture.cancel(true);
				this._ConnectFuture.sync();
			}
			this._ConnectFuture = null;
		}
		catch (Exception var3) {
		}
		try {
			if (this._UDPChannel != null) {
				if (this._UDPChannel.isActive()) {
					this._UDPChannel.close().sync();
				}
				this._UDPChannel = null;
			}
		}
		catch (Exception var2) {
		}
		this._UDPAllocator = null;
		this._RemoteDetail = null;
	}
	private class connectCallback implements ChannelFutureListener {
		private UDPConnector _UDP;
		public connectCallback(UDPConnector udp) {
			this._UDP = udp;
		}
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if (this._UDP != null) {
				if (!UDPConnector.this._isRelease) {
					if (future.isDone()) {
						if (future.isCancelled()) {
							this._UDP.ConnectFail();
							this._UDP._Status = E_ConnectorStatus.OnClosed;
						}
						else if (future.isSuccess()) {
							this._UDP._Handler = new UDPNettyHandler(this._UDP);
							future.channel().pipeline().addLast(this._UDP._Handler);
							UDPConnector.this.ConnectSuccess();
						}
						else {
							this._UDP.ConnectFail();
						}
						this._UDP._ConnectFuture = null;
						this._UDP = null;
					}
				}
			}
		}
	}
	private class WriteCallback implements ChannelFutureListener {
		UDPConnector _Client;
		public WriteCallback(UDPConnector client) {
			this._Client = client;
		}
		@Override
		public void operationComplete(ChannelFuture f) throws Exception {
			if (!UDPConnector.this._isRelease) {
				if (f.isDone()) {
					if (UDPConnector.this._ActivityCommand != null) {
						if (f.isSuccess()) {
							synchronized (this._Client) {
								try {
									if (!UDPConnector.this._ActivityCommand.getIsWaitResponse()) {
										UDPConnector.this._CommandList.poll();
										UDPConnector.this._ActivityCommand.RaiseCommandCompleteEvent(UDPConnector.this._Event);
										UDPConnector.this._ActivityCommand = null;
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
							System.out.println("UDP发送数据报失败，可能ip地址格式错误或参数不正确");
						}
						this._Client = null;
					}
				}
			}
		}
	}
}
