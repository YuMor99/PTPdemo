package test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.applet.*;
import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import jp.co.gitaku.ptp.DeviceInfo;
import jp.co.gitaku.ptpip.PTPIPInitiator;

public class TestApplet extends Applet {
	Label lb1;
	TextField tf1;
	Label lb2, lb3;
	PTPIPInitiator piInit = null;

	public void paint(Graphics g) {
		lb1 = new Label();
		lb2 = new Label();
		lb3 = new Label();

		tf1 = new TextField(5);
		JmDNS jmdns = null;
		try {
			jmdns = JmDNS.create();
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		jmdns.addServiceListener("_ptp._tcp.local.", new ServiceListener() {

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
				// System.out.println(info);

				int[] guid = { 0xe0, 0xcf, 0x20, 0x3a, 0xc3, 0x2a, 0x1b, 0x41, 0x98, 0x10, 0x0a, 0x93, 0x88, 0xec,
						0x53, 0x6c };
				String ip = "192.168.1.131";
				// /192.168.1.131
				ip = info.getInetAddresses()[0].toString().split("/")[1];
				System.out.println(ip);
				// 203e4f68-2423-4b46-9868-157593b63ef3
				// int[] guid = {0x20, 0x3e, 0x4f, 0x68, 0x24, 0x23, 0x4b, 0x46,
				// 0x98, 0x68, 0x15, 0x75, 0x93, 0xb6, 0x3e, 0xf3};
				// String ip = "192.168.1.88";
				try {
					piInit = new PTPIPInitiator(guid, "aaa", ip);
					// piInit.addListeners(new testListener());
					piInit.openSession();
					DeviceInfo devInfo = piInit.getDeviceInfo();
					devInfo.print();
					lb1.setText(devInfo.getDeviceVersion());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		tf1.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						String code = tf1.getText();
						if ("4100".equals(code)) {
							int[] storIds = piInit.getStorageIDs();
							lb2.setText(String.valueOf(storIds[0]));
						}
						lb3.setText("OK");
					} catch (Exception ex) {
						lb3.setText("エラー");
						ex.printStackTrace();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		}

		);
		setLayout(new FlowLayout());
		  
		  add(lb1);
		  add(tf1);
		  
		  add(lb2);
		  add(lb3);
		  
		  setSize(300,100);
	}
}