package sprint3;

import org.springframework.web.client.RestClient;

import sprint1.MoveObserver;

//registers on tournament game as observer
//send move to viewer app
public class RemoteClientViewer implements MoveObserver
{
    String ip;
    String port;
    RestClient defaultViewerClient;

    public RemoteClientViewer(String ip, String port)
    {
        this.ip = ip;
        this.port = port;
        this.defaultViewerClient = RestClient.create();
    }

    @Override
    public void updateMove(String move)
    {
        try
        {
            String baseuri = "http://" + ip + ":" + port + "/receiveMove/" + move;
            defaultViewerClient.get()
                    .uri(baseuri)
                    .retrieve()
                    .body(String.class);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't contact remote viewer client. Error: " + e.getMessage());
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
