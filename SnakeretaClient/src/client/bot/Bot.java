package client.bot;

import java.io.IOException;
import java.util.Stack;

public interface Bot {

	public boolean getEnabled();
	
	public void setEnabled(boolean enabled);

	void update() throws IOException;

	void afterMapChange() throws IOException;

	public Stack<Point> getPath();

	public void afterCharacterMiss(int loginId);
	
	public void afterCharacterLeave(int loginId);

}
