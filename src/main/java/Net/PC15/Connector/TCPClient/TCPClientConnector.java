package Net.PC15.Connector.TCPClient;
import Net.PC15.Command.INCommand;
import Net.PC15.Connector.AbstractConnector;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ConnectorStatus;
import Net.PC15.Connector.E_ConnectorType;
import Net.PC15.Packet.INPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author zch
 */
public class TCPClientConnector extends AbstractConnector {
	private final static org.apache.log4j.Logger myLog = org.apache.log4j.Logger.getRootLogger();
	private TCPClientAllocator _ClientAllocator;
	private TCPClientDetail _RemoteDetail;
	private Channel _ClientChannel;
	private Calendar _ConnectDate;
	private boolean _IsCancelConnect;
	private ChannelFuture _ConnectFuture;
	private int _ConnectTimeoutMSEL;
	private int _ReconnectMax;
	private int _ConnectFailCount;
	private TCPClientNettyHandler _Handler;
	private ChannelFuture _WriteFuture;
	public TCPClientConnector(TCPClientAllocator allocator, TCPClientDetail detail) throws CloneNotSupportedException {
		this._RemoteDetail = detail.clone();
		this._ConnectTimeoutMSEL = detail.Timeout;
		this.SetConnectOption(detail);
		this._ConnectFailCount = 0;
		this._ClientAllocator = allocator;
	}
	private void SetConnectOption(TCPClientDetail detail) {
		this._ConnectTimeoutMSEL = detail.Timeout;
		this._ReconnectMax = detail.RestartCount;
		if (this._ConnectTimeoutMSEL > 30000) {
			this._ConnectTimeoutMSEL = 30000;
		}
		else if (this._ConnectTimeoutMSEL < 1000) {
			this._ConnectTimeoutMSEL = 1000;
		}
		if (this._ReconnectMax > 5) {
			this._ReconnectMax = 5;
		}
		else if (this._ReconnectMax < 0) {
			this._ReconnectMax = 0;
		}
	}
	@Override
	protected ConnectorDetail GetConnectorDetail() {
		try {
			return this._RemoteDetail.clone();
		}
		catch (CloneNotSupportedException var2) {
			Logger.getLogger(TCPClientConnector.class.getName()).log(Level.SEVERE, null, var2);
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
					this.ConnectServer();
				}
				break;
			case OnConnecting:
				this.UpdateActivityTime();
				if (!this._IsCancelConnect && this.IsConnectTimeout()) {
					if (this._ConnectFuture != null) {
						this._ConnectFuture.cancel(true);
						this._IsCancelConnect = true;
					}
					else if (this._ClientChannel != null) {
						try {
							this._Status = E_ConnectorStatus.OnConnectTimeout;
							this._ClientChannel.close().sync();
						}
						catch (Exception var4) {
							myLog.error(var4.getMessage());
						}
					}
				}
				break;
			case OnConnected:
				this._ActivityCommand = (INCommand) this._CommandList.peek();
				if (this._ActivityCommand != null) {
					this.UpdateActivityTime();
					switch (this._ActivityCommand.GetStatus()) {
						case OnReady:
							// TODO
							INPacket send = this._ActivityCommand.GetPacket();
							ByteBuf packetBuf = send.GetPacketData();
							/* ============================== */
							String receiveStr1 = ByteBufUtil.hexDump(packetBuf).toUpperCase();
							myLog.info("发1:" + receiveStr1);
							/* ============================== */
							StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
							// myLog.info("发送内容堆栈:" + Arrays.toString(stackTraceElements));
							packetBuf.markReaderIndex();
							ByteBuf sendBuf = this._ClientChannel.alloc().buffer(packetBuf.readableBytes());
							sendBuf.writeBytes(packetBuf);
							packetBuf.resetReaderIndex();
							this._WriteFuture = this._ClientChannel.writeAndFlush(sendBuf);
							/* ============================== */
							//							byte[] receiveBytes = LogUtil.getBytesFromByteBuf2(sendBuf);
							//							String receiveStr = LogUtil.bytes2HexString(receiveBytes);
							//							myLog.info("发:" + receiveBytes + " " + receiveStr + "  ArraysToString " + Arrays.toString(receiveBytes));
							/* ============================== */
							// String receiveStr2 = ByteBufUtil.hexDump(sendBuf).toUpperCase();
							// myLog.info("发2:" + receiveStr2);
							/* ============================== */
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
				else {
					if (this._IsForcibly) {
						this.UpdateActivityTime();
						return;
					}
					this._Status = E_ConnectorStatus.OnClosing;
					this._ClientChannel.close();
				}
			default:
		}
	}
	private boolean IsConnectTimeout() {
		long lConnectMillis = this._ConnectDate.getTimeInMillis();
		long lNowMillis = Calendar.getInstance().getTimeInMillis();
		long lElapse = lNowMillis - lConnectMillis;
		return lElapse > (long) this._ConnectTimeoutMSEL;
	}
	private void ConnectServer() {
		if (this._ClientAllocator != null) {
			if (this._ClientChannel != null) {
				this._ClientChannel.close();
				this._ClientChannel = null;
			}
			if (this._ActivityCommand == null) {
				this._ActivityCommand = (INCommand) this._CommandList.peek();
				if (this._ActivityCommand != null) {
					this._ConnectFailCount = 0;
					this._ActivityCommand.RaiseCommandProcessEvent(this._Event);
				}
			}
			this._Status = E_ConnectorStatus.OnConnecting;
			this._ConnectFuture = this._ClientAllocator.connect(this._RemoteDetail.IP, this._RemoteDetail.Port);
			this._ClientChannel = this._ConnectFuture.channel();
			this._ConnectDate = Calendar.getInstance();
			this._IsCancelConnect = false;
			if (this._ConnectFuture.isDone()) {
				try {
					(new connectCallback(this)).operationComplete(this._ConnectFuture);
				}
				catch (Exception var2) {
					myLog.error(var2.getMessage());
				}
			}
			else {
				this._ConnectFuture.addListener(new connectCallback(this));
			}
		}
	}
	private void ConnectFail() {
		if (this._ClientChannel != null) {
			this._Status = E_ConnectorStatus.OnConnectTimeout;
			this._ClientChannel.close();
			this._ClientChannel = null;
			++this._ConnectFailCount;
			if (this._ConnectFailCount >= this._ReconnectMax) {
				try {
					this.fireConnectError();
				}
				catch (Exception var2) {
					myLog.error(var2.getMessage());
				}
				this._Status = E_ConnectorStatus.OnError;
			}
		}
	}
	private void ConnectSuccess() {
		this._Status = E_ConnectorStatus.OnConnected;
		this._ConnectFailCount = 0;
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
			this._ClientChannel = null;
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
					myLog.error(var10.getMessage());
				}
				try {
					this.CheckWatchResponse(msg);
				}
				catch (Exception var8) {
					myLog.error(var8.getMessage());
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
				default:
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
			myLog.error(var3.getMessage());
		}
		try {
			if (this._ClientChannel != null) {
				if (this._ClientChannel.isActive()) {
					this._ClientChannel.close().sync();
				}
				this._ClientChannel = null;
			}
		}
		catch (Exception var2) {
			myLog.error(var2.getMessage());
		}
		this._ClientAllocator = null;
		this._RemoteDetail = null;
	}
	private class connectCallback implements ChannelFutureListener {
		private TCPClientConnector _tcpClient;
		public connectCallback(TCPClientConnector tcp) {
			this._tcpClient = tcp;
		}
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if (this._tcpClient != null) {
				if (!TCPClientConnector.this._isRelease) {
					if (future.isDone()) {
						if (future.isCancelled()) {
							this._tcpClient.ConnectFail();
							this._tcpClient._Status = E_ConnectorStatus.OnClosed;
						}
						else if (future.isSuccess()) {
							this._tcpClient._Handler = new TCPClientNettyHandler(this._tcpClient);
							future.channel().pipeline().addLast(this._tcpClient._Handler);
							TCPClientConnector.this.ConnectSuccess();
						}
						else {
							this._tcpClient.ConnectFail();
						}
						this._tcpClient._ConnectFuture = null;
						this._tcpClient = null;
					}
				}
			}
		}
	}
	private class WriteCallback implements ChannelFutureListener {
		TCPClientConnector _Client;
		public WriteCallback(TCPClientConnector client) {
			this._Client = client;
		}
		@Override
		public void operationComplete(ChannelFuture f) throws Exception {
			if (!TCPClientConnector.this._isRelease) {
				if (f.isDone()) {
					if (TCPClientConnector.this._ActivityCommand != null) {
						if (f.isSuccess()) {
							synchronized (this._Client) {
								try {
									if (!TCPClientConnector.this._ActivityCommand.getIsWaitResponse()) {
										TCPClientConnector.this._CommandList.poll();
										TCPClientConnector.this._ActivityCommand.RaiseCommandCompleteEvent(TCPClientConnector.this._Event);
										TCPClientConnector.this._ActivityCommand = null;
									}
								}
								catch (Exception var5) {
									myLog.error(var5.getMessage());
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
