package boot;

import server_side.*;

public class Main
{
    public static void main(String[] args)
    {
    	String portstr = "5555";
        int port =Integer.parseInt(portstr);
        Server server=new MySerialServer();
        ClientHandler clientHandler=new MyClientHandler(new FileCacheManager<>());
        server.open(port,clientHandler);
//        server.stop();
    }
}
