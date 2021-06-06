package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Network {

	public static List<InetAddress> availableInterfaces() {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			System.out.println(e);
		}
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			try {
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}
			} catch (SocketException e) {
				System.out.println(e);
			}
			networkInterface.getInterfaceAddresses().stream() 
			.map(a -> a.getBroadcast())
			.filter(Objects::nonNull)
			.forEach(broadcastList::add);
		}

		return broadcastList;
	}

	public static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {


		System.out.println("Display name: " + netint.getDisplayName());
		System.out.println("Name: " + netint.getName());
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			System.out.println("InetAddress: %s\n" + inetAddress);
		}
	}

	public static void getips() throws IOException {
		// Create operating system process from arpe.bat file command
		ProcessBuilder pb = new ProcessBuilder("arpe.bat");

		pb.redirectErrorStream();
		// Starts a new process using the attributes of this process builder
		Process p = pb.start();

		BufferedReader br = new BufferedReader (new InputStreamReader(p.getInputStream()));

		// String out is used to store output of this command(process)
		String out="";

		while(true)
		{
			String l=null;
			try
			{
				l = br.readLine();
			}
			catch (IOException ex) {}
			if(l==null)
				break;
			out+="\n"+l;
		}

		// A compiled representation of a regular expression
		Pattern pattern = Pattern.compile(".*\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");

		/* An engine that performs match operations on a character sequence by interpreting a Pattern */
		Matcher match = pattern.matcher(out);

		out="";
		String prev="",pLoc;

		if(!(match.find()))        // In case no IP address Found in out
			out="No IP found!";

		else
		{

			/* Returns the input subsequence matched by the previous match in this case IP of our interface */
			pLoc = match.group();

			out+=pLoc+"\nOther Hosts'(In Same Network) IP addresses:\n";
			while(match.find())
			{
				pLoc = match.group();	// Returns the IP of other hosts
				out+=pLoc+"\n";
			}
			try
			{
				br.close();
			}
			catch (IOException ex) {}
		}

		// Printing IP Addresses of all computers in our network
		System.out.println(out);
	}

	public static void cmd() throws IOException {
		Process process = Runtime.getRuntime().exec("arp -a");

		printResults(process);
	}

	public static void printResults(Process process) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

	public static void getipp(){
		IntStream.rangeClosed(1,254).mapToObj(num -> "192.168.1." + num).parallel()
				.filter((addr) -> {
							try {
								return InetAddress.getByName(addr).isReachable(1000);
							} catch (IOException e) {
								return false;
							}
						}
				).forEach(System.out::println);
	}
}
