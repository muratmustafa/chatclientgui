package sample;

import java.io.IOException;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Hashtable;

public class User implements Serializable {

    private String userName;
    int port;
    private PrivateKey privateKey;
    public PublicKey publicKey;

    public Hashtable<String, PublicKey> publicKeys = new Hashtable<>();
    public BlockChain blockChain = null;


    public static UDPModel udpModel;
    static String hostname = "192.168.200.50";


    public User(String userName, int port) throws NoSuchAlgorithmException {
        this.userName = userName;
        this.port = port;
        blockChain = new BlockChain(3);

        udpModel = new UDPModel(new UDPEventHandler());

        new Thread(udpModel).start();

        KeyPair keyPair = RSA_ALgos.buildKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public void broadcastPublicKey() throws IOException {
        String pubKey = SerializeObject.serializeObject(publicKey);
        broadCastMessage("NEWUSER: " + userName + "/" + pubKey);
    }

    void createMessage(String plainText, String receiverName) throws Exception {
        Date createTimestamp = new Date();
        String plainMsg = "Sender    : " + userName
                      + "\nBody      : " + plainText
                      + "\nTimestamp : " + createTimestamp;

        PublicKey receiverKey = getUserPublicKey(receiverName);
        if(receiverKey == null) {
            System.out.println("RECEIVER " + receiverName + " DOES NOT EXIST");
            return;
        }
        byte[] cipherText = MessageCodec.encrypt(receiverKey, plainMsg);
        System.out.println(cipherText);
        Message m = new Message(cipherText, receiverName);
        broadCastMessage("MESSAGE," + SerializeObject.serializeObject(m));
    }

    private void broadCastMessage(String m) throws IOException {
        udpModel.sendDataViaUDP(hostname, m);
    }

    String decryptMessage(byte[] cipherText) throws Exception {
        return MessageCodec.decrypt(privateKey, cipherText);
    }

    String printMyMessages() throws Exception {
        String myMessages = "";
        System.out.println("-------------- MY MESSAGES --------------");
        for(Block b : blockChain.blockChain) {
            for(Message m : b.blockMessages) {
                if(m.receiver.equals(userName)){
                    System.out.println(decryptMessage(m.cipherText) + "\n");
                    myMessages += decryptMessage(m.cipherText) + "\n--------------------\n";
                }
            }
        }
        System.out.println("-----------------------------------------");
        return myMessages;
    }

    public PublicKey getUserPublicKey(String receiverName) {
        if(!publicKeys.containsKey(receiverName))
            return null;
        return publicKeys.get(receiverName);
    }

    public void receive(String sentence) {

    }
}
