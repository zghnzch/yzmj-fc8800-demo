//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.Connector;
import Net.PC15.Command.*;
import Net.PC15.Connector.TCPClient.TCPClientAllocator;
import Net.PC15.Connector.TCPClient.TCPClientConnector;
import Net.PC15.Connector.TCPClient.TCPClientDetail;
import Net.PC15.Connector.TCPServer.IPEndPoint;
import Net.PC15.Connector.TCPServer.TCPServerAllocator;
import Net.PC15.Connector.TCPServer.TCPServerClientDetail;
import Net.PC15.Connector.TCPServer.TCPServer_ClientConnector;
import Net.PC15.Connector.UDP.UDPAllocator;
import Net.PC15.Connector.UDP.UDPConnector;
import Net.PC15.Connector.UDP.UDPDetail;
import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBufAllocator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ConnectorAllocator {
	public static final ByteBufAllocator ALLOCATOR;
	private static ConnectorAllocator staticConnectorAllocator;
	static {
		ALLOCATOR = ByteUtil.ALLOCATOR;
	}
	protected TCPClientAllocator _TCPClientAllocator = new TCPClientAllocator();
	protected UDPAllocator _UDPAllocator = new UDPAllocator();
	protected TCPServerAllocator _ServerAllocator;
	protected ConcurrentHashMap<String, INConnector> _ConnectorMap = new ConcurrentHashMap();
	protected ExecutorService mainService = Executors.newFixedThreadPool(2);
	protected ExecutorService workService = Executors.newFixedThreadPool(60);
	protected INConnectorEvent _EventHeandler = new ConnectorAllocatorEventCallback(this);
	protected ArrayList<INConnectorEvent> _EventListener = new ArrayList(10);
	protected ConcurrentLinkedQueue<IOEvent> _EventList = new ConcurrentLinkedQueue();
	protected boolean _IsRelease = false;
	private ConnectorAllocator() {
		this._ServerAllocator = new TCPServerAllocator(this._EventHeandler);
		this.mainService.submit(() -> {
			this.CheckConnectorStatus();
		});
		this.mainService.submit(() -> {
			this.CheckEventCallblack();
		});
	}
	public static synchronized ConnectorAllocator GetAllocator() {
		if (staticConnectorAllocator == null) {
			staticConnectorAllocator = new ConnectorAllocator();
		}
		return staticConnectorAllocator;
	}
	public void AddListener(INConnectorEvent listener) {
		if (!this._IsRelease) {
			synchronized (this._EventListener) {
				if (!this._EventListener.contains(listener)) {
					this._EventListener.add(listener);
				}
			}
		}
	}
	public void DeleteListener(INConnectorEvent listener) {
		if (!this._IsRelease) {
			synchronized (this._EventListener) {
				this._EventListener.remove(listener);
			}
		}
	}
	public void Release() {
		if (!this._IsRelease) {
			this._IsRelease = true;
			this._EventListener.clear();
			this._EventListener = null;
			try {
				this._ConnectorMap.forEach((k, v) -> {
					v.Release();
				});
				this._ConnectorMap.clear();
				this._ConnectorMap = null;
			}
			catch (Exception var2) {
			}
			this._TCPClientAllocator.shutdownGracefully();
			this._TCPClientAllocator = null;
			this._UDPAllocator.shutdownGracefully();
			this._UDPAllocator = null;
			NettyAllocator.shutdownGracefully();
			this.workService.shutdownNow();
			this.mainService.shutdown();
			this.mainService.shutdownNow();
			staticConnectorAllocator = null;
		}
	}
	public boolean IsRelease() {
		return this._IsRelease;
	}
	protected void AddEvent(IOEvent event) {
		synchronized (this._EventList) {
			this._EventList.offer(event);
		}
	}
	public synchronized boolean AddCommand(INCommand cmd) {
		if (this._IsRelease) {
			return false;
		}
		else if (cmd == null) {
			return false;
		}
		else {
			INCommandParameter par = cmd.getCommandParameter();
			if (par == null) {
				return false;
			}
			else {
				CommandDetail detail = par.getCommandDetail();
				if (detail == null) {
					return false;
				}
				else {
					ConnectorDetail connDetail = detail.Connector;
					if (connDetail == null) {
						return false;
					}
					else {
						INConnector connector = this.GetConnector(connDetail, true);
						if (connector == null) {
							return false;
						}
						else {
							connector.AddCommand(cmd);
							return true;
						}
					}
				}
			}
		}
	}
	protected synchronized INConnector GetConnector(ConnectorDetail detail, boolean bNew) {
		switch (detail.GetConnectorType()) {
			case OnComm:
			case OnFile:
			default:
				return null;
			case OnTCPClient:
				if (!(detail instanceof TCPClientDetail)) {
					return null;
				}
				return this.SearchTCPClient((TCPClientDetail) detail, bNew);
			case OnTCPServer_Client:
				if (!(detail instanceof TCPServerClientDetail)) {
					return null;
				}
				return this._ServerAllocator.SearchClient((TCPServerClientDetail) detail);
			case OnUDP:
				return !(detail instanceof UDPDetail) ? null : this.SearchUDPClient((UDPDetail) detail, bNew);
		}
	}
	protected INConnector SearchTCPClient(TCPClientDetail detail, boolean bNew) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("TCPClient:");
		keybuf.append(detail.IP);
		keybuf.append(":");
		keybuf.append(detail.Port);
		String sKey = keybuf.toString();
		if (this._ConnectorMap.containsKey(sKey)) {
			return this._ConnectorMap.get(sKey);
		}
		else if (bNew) {
			try {
				TCPClientConnector Connector = new TCPClientConnector(this._TCPClientAllocator, detail);
				Connector.SetEventHandle(this._EventHeandler);
				this.AddConnector(sKey, Connector);
				return this._ConnectorMap.get(sKey);
			}
			catch (Exception var6) {
				return null;
			}
		}
		else {
			return null;
		}
	}
	protected INConnector SearchUDPClient(UDPDetail detail, boolean bNew) {
		StringBuilder keybuf = new StringBuilder(100);
		keybuf.append("UDP:");
		keybuf.append(detail.LocalIP);
		keybuf.append(":");
		keybuf.append(detail.LocalPort);
		String sKey = keybuf.toString();
		if (this._ConnectorMap.containsKey(sKey)) {
			return this._ConnectorMap.get(sKey);
		}
		else if (bNew) {
			try {
				UDPConnector Connector = new UDPConnector(this._UDPAllocator, detail);
				Connector.SetEventHandle(this._EventHeandler);
				this.AddConnector(sKey, Connector);
				return this._ConnectorMap.get(sKey);
			}
			catch (Exception var6) {
				return null;
			}
		}
		else {
			return null;
		}
	}
	protected void AddConnector(String key, INConnector conn) {
		synchronized (this) {
			this._ConnectorMap.put(key, conn);
		}
	}
	protected void CheckConnectorStatus() {
		ArrayList arrayList = new ArrayList(this._ConnectorMap.size());
		while (!this._IsRelease) {
			synchronized (this) {
				if (this._ConnectorMap.size() > 0) {
					this._ConnectorMap.entrySet().forEach((valuex) -> {
						INConnector connector = valuex.getValue();
						if (!connector.TaskIsBegin()) {
							if (!connector.IsInvalid()) {
								switch (connector.GetStatus()) {
									case OnError:
										if (!connector.IsForciblyConnect()) {
											arrayList.add(valuex.getKey());
										}
									case OnConnectTimeout:
									case OnClosed:
									case OnConnected:
									case OnConnecting:
										connector.SetTaskIsBegin();
										this.workService.submit(() -> {
											connector.run();
										});
								}
							}
							else {
								arrayList.add(valuex.getKey());
							}
						}
					});
					if (arrayList.size() > 0) {
						Iterator var3 = arrayList.iterator();
						while (var3.hasNext()) {
							String value = (String) var3.next();
							this._ConnectorMap.get(value).Release();
							this._ConnectorMap.remove(value);
						}
						arrayList.clear();
					}
				}
				this._ServerAllocator.CheckClientStatus(this.workService);
			}
			try {
				Thread.sleep(5L);
				if (this._IsRelease) {
					return;
				}
			}
			catch (Exception var6) {
			}
		}
	}
	protected void CheckEventCallblack() {
		while (!this._IsRelease) {
			while (true) {
				IOEvent event;
				synchronized (this._EventList) {
					event = this._EventList.poll();
				}
				if (event == null) {
					try {
						Thread.sleep(50L);
						if (this._IsRelease) {
							return;
						}
					}
					catch (Exception var8) {
					}
				}
				else {
					synchronized (this._EventListener) {
						int iLen = this._EventListener.size();
						int i = 0;
						while (true) {
							if (i >= iLen) {
								break;
							}
							INConnectorEvent Listener = this._EventListener.get(i);
							try {
								switch (event.EventType) {
									case eCommandCompleteEvent:
										Listener.CommandCompleteEvent(event.Command, event.Result);
										break;
									case eCommandProcessEvent:
										Listener.CommandProcessEvent(event.Command);
										break;
									case eCommandTimeout:
										Listener.CommandTimeout(event.Command);
										break;
									case eChecksumErrorEvent:
										Listener.ChecksumErrorEvent(event.Command);
										break;
									case ePasswordErrorEvent:
										Listener.PasswordErrorEvent(event.Command);
										break;
									case eConnectorErrorEvent:
										if (event.connectorDetail == null) {
											Listener.ConnectorErrorEvent(event.Command, event.isStop);
										}
										else {
											Listener.ConnectorErrorEvent(event.connectorDetail);
										}
										break;
									case eWatchEvent:
										Listener.WatchEvent(event.connectorDetail, event.EventData);
										break;
									case eClientOnline:
										Listener.ClientOnline((TCPServerClientDetail) event.connectorDetail);
										break;
									case eClientOffline:
										Listener.ClientOffline((TCPServerClientDetail) event.connectorDetail);
								}
							}
							catch (Exception var10) {
								System.out.println("Net.PC15.Connector.ConnectorAllocator.CheckEventCallblack() -- 发送事件时发生错误" + var10.toString());
							}
							if (this._IsRelease) {
								return;
							}
							++i;
						}
					}
					if (this._IsRelease) {
						return;
					}
				}
			}
		}
	}
	public int GetCommandCount(ConnectorDetail detail) {
		if (this._IsRelease) {
			return 0;
		}
		else if (detail == null) {
			return 0;
		}
		else {
			INConnector connector = this.GetConnector(detail, false);
			return connector == null ? 0 : connector.GetCommandCount();
		}
	}
	public boolean IsForciblyConnect(ConnectorDetail detail) {
		if (this._IsRelease) {
			return false;
		}
		else if (detail == null) {
			return false;
		}
		else {
			INConnector connector = this.GetConnector(detail, false);
			return connector != null && connector.IsForciblyConnect();
		}
	}
	public void OpenForciblyConnect(ConnectorDetail detail) {
		if (!this._IsRelease) {
			if (detail != null) {
				INConnector connector = this.GetConnector(detail, true);
				if (connector != null) {
					connector.OpenForciblyConnect();
				}
			}
		}
	}
	public void CloseForciblyConnect(ConnectorDetail detail) {
		if (!this._IsRelease) {
			if (detail != null) {
				INConnector connector = this.GetConnector(detail, false);
				if (connector != null) {
					connector.CloseForciblyConnect();
				}
			}
		}
	}
	public void StopCommand(ConnectorDetail detail, INIdentity dtl) {
		if (!this._IsRelease) {
			if (detail != null) {
				INConnector connector = this.GetConnector(detail, false);
				if (connector != null) {
					connector.StopCommand(dtl);
				}
			}
		}
	}
	public boolean Listen(String IP, int Port) {
		return !this._IsRelease && this._ServerAllocator.Listen(IP, Port);
	}
	public boolean Listen(int Port) {
		return !this._IsRelease && this._ServerAllocator.Listen(Port);
	}
	public void StopListen(String IP, int Port) {
		if (!this._IsRelease) {
			this._ServerAllocator.StopListen(IP, Port);
		}
	}
	public void StopListen(int Port) {
		this.StopListen("0.0.0.0", Port);
	}
	public ArrayList<IPEndPoint> GetTCPServerList() {
		return this._IsRelease ? null : this._ServerAllocator.getServerList();
	}
	public boolean AddWatchDecompile(ConnectorDetail detail, INWatchResponse decompile) {
		INConnector connector = this.GetConnector(detail, false);
		if (connector == null) {
			return false;
		}
		else {
			connector.AddWatchDecompile(decompile);
			return true;
		}
	}
	public boolean CloseConnector(ConnectorDetail detail) {
		INConnector connector = this.GetConnector(detail, false);
		if (connector == null) {
			return false;
		}
		else {
			connector.CloseForciblyConnect();
			connector.StopCommand(null);
			if (detail.GetConnectorType() == E_ConnectorType.OnTCPServer_Client) {
				TCPServer_ClientConnector client = (TCPServer_ClientConnector) connector;
				client.Close();
			}
			return true;
		}
	}
	private static class IOEvent {
		public eEventType EventType;
		public INCommand Command;
		public INCommandResult Result;
		public ConnectorDetail connectorDetail;
		public Boolean isStop;
		public INData EventData;
		private IOEvent() {
		}
		public enum eEventType {
			eCommandCompleteEvent, eCommandProcessEvent, eConnectorErrorEvent, eCommandTimeout, ePasswordErrorEvent, eChecksumErrorEvent, eWatchEvent, eClientOnline, eClientOffline;
			eEventType() {
			}
		}
	}
	private class ConnectorAllocatorEventCallback implements INConnectorEvent {
		ConnectorAllocator _Allocator;
		public ConnectorAllocatorEventCallback(ConnectorAllocator allocator) {
			this._Allocator = allocator;
		}
		@Override
		public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eCommandCompleteEvent;
			event.Command = cmd;
			event.Result = result;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void CommandProcessEvent(INCommand cmd) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eCommandProcessEvent;
			event.Command = cmd;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eConnectorErrorEvent;
			event.Command = cmd;
			event.isStop = isStop;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void ConnectorErrorEvent(ConnectorDetail detail) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eConnectorErrorEvent;
			event.connectorDetail = detail;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void CommandTimeout(INCommand cmd) {
			//ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eCommandTimeout;
			event.Command = cmd;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void PasswordErrorEvent(INCommand cmd) {
			//  ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.ePasswordErrorEvent;
			event.Command = cmd;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void ChecksumErrorEvent(INCommand cmd) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eChecksumErrorEvent;
			event.Command = cmd;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void WatchEvent(ConnectorDetail detail, INData eventData) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eWatchEvent;
			event.connectorDetail = detail;
			event.EventData = eventData;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void ClientOnline(TCPServerClientDetail client) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eClientOnline;
			event.connectorDetail = client;
			this._Allocator.AddEvent(event);
		}
		@Override
		public void ClientOffline(TCPServerClientDetail client) {
			// ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent((SyntheticClass_1)null);
			IOEvent event = new IOEvent();
			event.EventType = IOEvent.eEventType.eClientOffline;
			event.connectorDetail = client;
			this._Allocator.AddEvent(event);
		}
	}
}
