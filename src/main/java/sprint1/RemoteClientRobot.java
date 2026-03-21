package sprint1;

import org.springframework.web.client.RestClient;

public class RemoteClientRobot extends Robot
{
	String ip;
	String port;
	RestClient defaultClient;
	
	public RemoteClientRobot(String name, String ip, String port) {
		super(name, 0, 0);
		this.ip = ip;
		this.port = port;
		this.defaultClient = RestClient.create();	
	}
	
	@Override
	public String makeDecision()
	{
		try
        {
            String baseuri = "http://" + ip + ":" + port;
            String prevDecision;
            if (opponentsPrevDecision != null)
            {
            	prevDecision = opponentsPrevDecision;
            }
            else 
            {
            	prevDecision = null;
            }
            //send get request and retrieve decision
            String decision = defaultClient.get()
    				.uri(baseuri + "/decision/" + prevDecision)
    				.retrieve()
    				.body(String.class);
            return decision;
        }
		//if can't contact remote client, default to cooperate
		catch (Exception e)
		{
			System.out.println("Couldn't contact remote client. Defaulted to cooperate. Error: " + e.getMessage());
			return "Cooperate";
		}
        
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	
	 
	
	
	
	

}
