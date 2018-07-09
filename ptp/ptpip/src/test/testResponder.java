//package test;
//
//import jp.co.gitaku.ptpip.EventPacket;
//import jp.co.gitaku.ptpip.PTPIPResponder;
//import org.bytedeco.javacpp.*;
//import org.bytedeco.javacv.*;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferByte;
//import java.awt.image.WritableRaster;
//import java.io.File;
//import java.io.IOException;
//
//public class testResponder {
//	private static PTPIPResponder res = null;
//
//	private boolean shoot =false;
//	public static void main(String args[]) {
//
//
//		try {
//			int[] guid = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 };
//			res = new PTPIPResponder(guid, "dddd");
//			Thread t1 = new Thread(new sendevtThead(res));
//			t1.start();
//
//			Thread t2 = new Thread(new cameraThead());
//			t2.start();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		res.service();
//	}
//
//	/**
//	 *
//	 * ����˵��:��javacv��IplImageͼ��תΪjava 2d�����BufferedImage
//	 * javacvͼ��
//	 * @return BufferedImage
//	 * java 2dͼ��
//	 * @time:2016��8��3������12:24:50
//	 * @author:linghushaoxia
//	 * @exception:
//	 *
//	 */
//	public static BufferedImage iplToBufImgData(opencv_core.IplImage mat) {
//		if (mat.height() > 0 && mat.width() > 0) {
//			BufferedImage image = new BufferedImage(mat.width(), mat.height(),
//					BufferedImage.TYPE_3BYTE_BGR);
//			WritableRaster raster = image.getRaster();
//			DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
//			byte[] data = dataBuffer.getData();
//			BytePointer bytePointer =new BytePointer(data);
//			mat.imageData(bytePointer);
//			return image;
//		}
//		return null;
//	}
//
//}
//
//
//class sendevtThead implements Runnable {
//	private static PTPIPResponder res;
//	public sendevtThead(PTPIPResponder res) {
//		this.res = res;
//	}
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		while (true) {
//			EventPacket event = new EventPacket(16386, 11, 0, 0, 0);
//			try {
//				Thread.sleep(9000);
//				res.sendEvent(event);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//	}
//}
//
//class cameraThead implements Runnable {
//	public cameraThead() {
//	}
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		System.setProperty("java.awt.headless","false");
//		try{
//			transferCameraVideo("rtmp://localhost:1936/live/test",25);
////			testCamera();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private void testCamera()throws FrameGrabber.Exception, InterruptedException{
//		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
//		grabber.start();   //��ʼ��ȡ����ͷ����
//		CanvasFrame canvas = new CanvasFrame("camera");//�½�һ������
//		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		canvas.setAlwaysOnTop(true);
//		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//
//		while(true)
//		{
//			if(!canvas.isDisplayable())
//			{//�����Ƿ�ر�
//				grabber.stop();//ֹͣץȡ
//				System.exit(2);//�˳�
//			}
//			canvas.showImage(grabber.grab());//��ȡ����ͷͼ�񲢷ŵ���������ʾ�� �����Frame frame=grabber.grab(); frame��һ֡��Ƶͼ��
//			opencv_core.Mat mat = converter.convertToMat(grabber.grabFrame());
//			opencv_imgcodecs.imwrite("eguid.jpg", mat);
//
//			Thread.sleep(50);//50����ˢ��һ��ͼ��
//		}
//	}
//
//	/**
//	 * ��֡¼�Ʊ�������ͷ��Ƶ����Ԥ����¼�ƣ�ֹͣԤ����ֹͣ¼�ƣ�
//	 *
//	 * @author eguid
//	 * @param outputFile -¼�Ƶ��ļ�·����Ҳ������rtsp����rtmp����ý�������������ַ
//	 * @param frameRate - ��Ƶ֡��
//	 * @throws FrameGrabber.Exception
//	 * @throws InterruptedException
//	 * @throws org.bytedeco.javacv.FrameRecorder.Exception
//	 * eg. recordCamera("rtmp://localhost:1935/live/test",25);
//	 */
//	private void transferCameraVideo(String outputFile, double frameRate)
//			throws FrameGrabber.Exception, InterruptedException, org.bytedeco.javacv.FrameRecorder.Exception {
//		Loader.load(opencv_objdetect.class);
//		FrameGrabber grabber = FrameGrabber.createDefault(0);//��������ͷĬ��0������ʹ��javacv��ץȡ��������ʹ�õ���ffmpeg����opencv�������в鿴Դ��
//		grabber.start();//����ץȡ��
//
//		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//ת����
//		opencv_core.IplImage grabbedImage = converter.convert(grabber.grab());//ץȡһ֡��Ƶ������ת��Ϊͼ�����������ͼ��������ʲô����ˮӡ������ʶ��ȵ��������
//		int width = grabbedImage.width();
//		int height = grabbedImage.height();
//
//		FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height);
//		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264������
//		recorder.setFormat("flv");//��װ��ʽ����������͵�rtmp�ͱ�����flv��װ��ʽ
//		recorder.setFrameRate(frameRate);
//
//		recorder.start();//����¼����
//		long startTime=0;
//		long videoTS=0;
//		CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setAlwaysOnTop(true);
//		Frame rotatedFrame=converter.convert(grabbedImage);//��֪��Ϊʲô���ﲻ��ת���Ͳ����Ƶ�rtmp
//		while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
//			opencv_core.Mat mat = converter.convertToMat(grabber.grabFrame());
//			opencv_imgcodecs.imwrite("demo.jpg", mat);
//
//			rotatedFrame = converter.convert(grabbedImage);
//			frame.showImage(rotatedFrame);
//			if (startTime == 0) {
//				startTime = System.currentTimeMillis();
//			}
//			videoTS = 1000 * (System.currentTimeMillis() - startTime);
//			recorder.setTimestamp(videoTS);
//			recorder.record(rotatedFrame);
//			Thread.sleep(40);
//		}
//		frame.dispose();
//		recorder.stop();
//		recorder.release();
//		grabber.stop();
//	}
//
//
//}
