package javax.jmdns.test;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
public class test1 {

    /**
     * @param args
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
        JmDNS jmdns = null;
        try {
            jmdns = JmDNS.create();
        } catch (IOException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }
        jmdns.addServiceListener("_ptp._tcp.local.", new ServiceListener(){

            @Override
            public void serviceAdded(ServiceEvent event) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void serviceRemoved(ServiceEvent event) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void serviceResolved(ServiceEvent event) {
                ServiceInfo info = event.getInfo();
                System.out.println(info);
                
            }
            
        });
    }

}
