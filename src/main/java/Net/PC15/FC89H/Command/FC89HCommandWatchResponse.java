package Net.PC15.FC89H.Command;
import Net.PC15.Command.AbstractWatchResponse;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Packet.FC8800Decompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.FC89H.Command.Data.CardTransaction;
import Net.PC15.Packet.INPacketModel;

import java.util.Calendar;
public class FC89HCommandWatchResponse extends AbstractWatchResponse {
	public FC89HCommandWatchResponse() {
		super(new FC8800Decompile());
	}
	protected void fireWatchEvent(ConnectorDetail connectorDetail, INConnectorEvent oEvent, INPacketModel model) {
		FC8800PacketModel packet = (FC8800PacketModel) model;
		if (packet.GetCode() == 4294967295L && packet.GetCmdType() == 25) {
			FC8800WatchTransaction watchevent = new FC8800WatchTransaction();
			watchevent.CmdType = packet.GetCmdType();
			watchevent.CmdIndex = packet.GetCmdIndex();
			watchevent.CmdPar = packet.GetCmdPar();
			watchevent.SN = packet.GetSN();
			switch (watchevent.CmdIndex) {
				case 1:
					CardTransaction card = new CardTransaction();
					card.SetBytes(packet.GetDatabuff());
					watchevent.EventData = card;
					break;
				case 2:
					ButtonTransaction ButtonTrn = new ButtonTransaction();
					ButtonTrn.SetBytes(packet.GetDatabuff());
					watchevent.EventData = ButtonTrn;
					break;
				case 3:
					DoorSensorTransaction DoorSensorTrn = new DoorSensorTransaction();
					DoorSensorTrn.SetBytes(packet.GetDatabuff());
					watchevent.EventData = DoorSensorTrn;
					break;
				case 4:
					SoftwareTransaction SoftwareTrn = new SoftwareTransaction();
					SoftwareTrn.SetBytes(packet.GetDatabuff());
					watchevent.EventData = SoftwareTrn;
					break;
				case 5:
					AlarmTransaction AlarmTrn = new AlarmTransaction();
					AlarmTrn.SetBytes(packet.GetDatabuff());
					watchevent.EventData = AlarmTrn;
					break;
				case 6:
					SystemTransaction SystemTrn = new SystemTransaction();
					SystemTrn.SetBytes(packet.GetDatabuff());
					watchevent.EventData = SystemTrn;
					break;
				default:
					DefinedTransaction dt = new DefinedTransaction(watchevent.CmdIndex, 0, Calendar.getInstance());
					if (packet.GetDataLen() > 0L) {
						dt.SetWatchData(packet.GetDatabuff());
					}
					watchevent.EventData = dt;
			}
			oEvent.WatchEvent(connectorDetail, watchevent);
		}
	}
}
