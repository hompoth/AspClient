package client.bot.action;

import java.io.IOException;

public interface Action {
	public long getInstant();

	public boolean handle() throws IOException;
	
}
