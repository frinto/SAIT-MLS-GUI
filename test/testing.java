import saitMLS.persistence.clientale.ClientBroker;

/**
 * This class is responsible for testing the backend making sure the library works
 * Created by Huy Le on 2/23/2017.
 */

public class testing
{
    /**
     *the main method which is the main execution point for the testing class
     * @param args is a String[] array for commandline if needed
     */
    public static void main(String[] args)
    {

        ClientBroker clientBroker = ClientBroker.getBroker();
        System.out.print(clientBroker.search("Flintstone", "name"));

    }
}
