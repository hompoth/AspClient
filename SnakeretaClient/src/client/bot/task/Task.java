package client.bot.task;

import java.io.IOException;

public interface Task {
	public long getInstant();

	public boolean handle() throws IOException;
	
}