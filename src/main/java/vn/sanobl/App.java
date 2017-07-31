package vn.sanobl;

import com.rabbitmq.client.BuiltinExchangeType;
import vn.sanobl.rabbitmq.RBConfiguration;
import vn.sanobl.rabbitmq.RBManager;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        RBConfiguration rbConfiguration = new RBConfiguration("localhost:5672;localhost:5673;localhost:5674", "myuser", "mypass", "/");
        RBConfiguration rbConfiguration2 = new RBConfiguration("localhost:5672;localhost:5672", "hotro", "1235", "/");
        int i = 0;
        while (i < 10000) {
            try {
                String abc = "Banwgf Nguyễn + " + i;
                RBManager.getInstance(rbConfiguration).setMessage("graphite2", "graph1","", abc, BuiltinExchangeType.TOPIC.getType());
//                RBManager.getInstance(rbConfiguration).close();

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            String message = null;
            try {
                message = RBManager.getInstance(rbConfiguration).getMessage("graphite2", "graph1","",BuiltinExchangeType.TOPIC.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("mess: " + message);
            i++;
        }
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (i < 10000000) {
            try {
                String abc = "Banwgf Nguyễn + " + i;
                RBManager.getInstance(rbConfiguration).setMessage("graphite2", "graph1","abc", abc, BuiltinExchangeType.TOPIC.getType());
//                RBManager.getInstance(rbConfiguration).close();
                String message = RBManager.getInstance(rbConfiguration).getMessage("graphite2", "graph1","abc",BuiltinExchangeType.TOPIC.getType());
                System.out.println("mess: " + message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
    }


}
