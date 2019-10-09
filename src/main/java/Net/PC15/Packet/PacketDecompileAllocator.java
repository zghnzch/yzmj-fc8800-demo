package Net.PC15.Packet;
import Net.PC15.Command.INWatchResponse;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.FC8800.Command.FC8800CommandWatchResponse;
import Net.PC15.FC89H.Command.FC89HCommandWatchResponse;

import java.util.concurrent.ConcurrentHashMap;
public class PacketDecompileAllocator {
	private static final ConcurrentHashMap DecompileMap = new ConcurrentHashMap();
	static {
		DecompileMap.put(E_ControllerType.FC8800.getClass().getCanonicalName(), FC8800CommandWatchResponse.class);
		DecompileMap.put(E_ControllerType.FC8900.getClass().getCanonicalName(), FC89HCommandWatchResponse.class);
		DecompileMap.put(E_ControllerType.MC5800.getClass().getCanonicalName(), FC8800CommandWatchResponse.class);
	}
	public static INWatchResponse GetDecompile(E_ControllerType type) {
		if (type == null) {
			return null;
		}
		else {
			String sKey = type.getClass().getCanonicalName();
			if (DecompileMap.containsKey(sKey)) {
				INWatchResponse watch = null;
				try {
					watch = (INWatchResponse) ((Class) DecompileMap.get(sKey)).newInstance();
				}
				catch (InstantiationException | IllegalAccessException var4) {
				}
				return watch;
			}
			else {
				return null;
			}
		}
	}
}
