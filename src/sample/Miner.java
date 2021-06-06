package sample;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Miner extends User{
	private static final long serialVersionUID = 1L;

	public Miner(String userName, int port) throws NoSuchAlgorithmException {
		super(userName, port);	
	}

	public void broadcastEverything() throws Exception {
		String blockChainData = SerializeObject.serializeObject(blockChain);
		String message = "BLOCKCHAIN," + blockChainData;
		broadCastMessage(message);
	}

	public void broadCastMessage(String m) throws IOException {
		udpModel.sendDataViaUDP(hostname, m);
	}

	public void recieve(String sentence){

	}

	private void broadcastAllPublicKeys() throws IOException {
		String hashtableData = SerializeObject.serializeObject(publicKeys);
		String message = "HASHTABLE," + hashtableData;
		broadCastMessage(message);
		
	}
}
