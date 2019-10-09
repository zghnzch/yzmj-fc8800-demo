package Net.PC15.Command;
public interface INCommand extends INCommandRuntime {
	int getProcessMax();
	int getProcessStep();
	int getSendCount();
	boolean getIsTimeout();
	INCommandParameter getCommandParameter();
	void setCommandParameter(INCommandParameter var1);
	INCommandResult getCommandResult();
}
