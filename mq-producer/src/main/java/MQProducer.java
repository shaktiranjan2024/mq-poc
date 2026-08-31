import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import java.util.Hashtable;

public class MQProducer {

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
            System.out.println("Connected: " + qMgr.getName());

            queue = qMgr.accessQueue(QUEUE_NAME, CMQC.MQOO_OUTPUT);

            String text = args.length > 0 ? args[0] : "Hello from MQProducer!";
            MQMessage message = new MQMessage();
            message.writeString(text);

            queue.put(message, new MQPutMessageOptions());
            System.out.println("Message sent: " + text);

        } finally {
            if (queue != null) queue.close();
            if (qMgr != null) qMgr.disconnect();
            System.out.println("Disconnected.");
        }
    }
}
