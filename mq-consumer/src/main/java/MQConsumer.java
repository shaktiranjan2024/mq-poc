import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import java.util.Hashtable;

public class MQConsumer {

    static final String HOST = "localhost";
    static final int PORT = 1414;
    static final String CHANNEL = "DEV.APP.SVRCONN";
    static final String QUEUE_MANAGER = "QM1";
    static final String QUEUE_NAME = "DEV.QUEUE.1";

    public static void main(String[] args) throws Exception {

        Hashtable<String, Object> props = new Hashtable<>();
        props.put(CMQC.HOST_NAME_PROPERTY, HOST);
        props.put(CMQC.PORT_PROPERTY, PORT);
        props.put(CMQC.CHANNEL_PROPERTY, CHANNEL);
        props.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_CLIENT);

        MQQueueManager qMgr = null;
        MQQueue queue = null;

        try {
            System.out.println("Connecting to Queue Manager...");
            qMgr = new MQQueueManager(QUEUE_MANAGER, props);
            System.out.println("✅ Connected: " + qMgr.getName());

            queue = qMgr.accessQueue(QUEUE_NAME, CMQC.MQOO_INPUT_SHARED);

            System.out.println("Waiting for messages... (polls every 3 sec)");

            while (true) {
                try {
                    MQMessage message = new MQMessage();
                    MQGetMessageOptions gmo = new MQGetMessageOptions();
                    gmo.waitInterval = 3000; // wait 3 seconds for a message
                    gmo.options = CMQC.MQGMO_WAIT;

                    queue.get(message, gmo);
                    String text = message.readString(message.getMessageLength());
                    System.out.println("✅ Received: " + text);

                } catch (MQException e) {
                    if (e.reasonCode == CMQC.MQRC_NO_MSG_AVAILABLE) {
                        System.out.println("No message yet, waiting...");
                    } else {
                        throw e;
                    }
                }
            }

        } finally {
            if (queue != null) queue.close();
            if (qMgr != null) qMgr.disconnect();
        }
    }
}
