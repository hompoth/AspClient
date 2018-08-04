package client;

public abstract class Packet {
	private long __Id;
	public long getId() {
		return __Id;
	}
	public void setId(long value) {
		__Id = value;
	}
	
    private String __Message;

    public String getMessage() {
        return __Message;
    }

    public void setMessage(String value) {
    	__Message = value;
    }

    public abstract void ready(World world, Communication comm) throws Exception;

}
