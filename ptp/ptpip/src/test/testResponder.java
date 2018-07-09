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
//	 * 功能说明:将javacv的IplImage图像转为java 2d自身的BufferedImage
//	 * javacv图像
//	 * @return BufferedImage
//	 * java 2d图像
//	 * @time:2016年8月3日下午12:24:50
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
//		grabber.start();   //开始获取摄像头数据
//		CanvasFrame canvas = new CanvasFrame("camera");//新建一个窗口
//		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		canvas.setAlwaysOnTop(true);
//		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//
//		while(true)
//		{
//			if(!canvas.isDisplayable())
//			{//窗口是否关闭
//				grabber.stop();//停止抓取
//				System.exit(2);//退出
//			}
//			canvas.showImage(grabber.grab());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
//			opencv_core.Mat mat = converter.convertToMat(grabber.grabFrame());
//			opencv_imgcodecs.imwrite("eguid.jpg", mat);
//
//			Thread.sleep(50);//50毫秒刷新一次图像
//		}
//	}
//
//	/**
//	 * 按帧录制本机摄像头视频（边预览边录制，停止预览即停止录制）
//	 *
//	 * @author eguid
//	 * @param outputFile -录制的文件路径，也可以是rtsp或者rtmp等流媒体服务器发布地址
//	 * @param frameRate - 视频帧率
//	 * @throws FrameGrabber.Exception
//	 * @throws InterruptedException
//	 * @throws org.bytedeco.javacv.FrameRecorder.Exception
//	 * eg. recordCamera("rtmp://localhost:1935/live/test",25);
//	 */
//	private void transferCameraVideo(String outputFile, double frameRate)
//			throws FrameGrabber.Exception, InterruptedException, org.bytedeco.javacv.FrameRecorder.Exception {
//		Loader.load(opencv_objdetect.class);
//		FrameGrabber grabber = FrameGrabber.createDefault(0);//本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
//		grabber.start();//开启抓取器
//
//		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//转换器
//		opencv_core.IplImage grabbedImage = converter.convert(grabber.grab());//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
//		int width = grabbedImage.width();
//		int height = grabbedImage.height();
//
//		FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height);
//		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
//		recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
//		recorder.setFrameRate(frameRate);
//
//		recorder.start();//开启录制器
//		long startTime=0;
//		long videoTS=0;
//		CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setAlwaysOnTop(true);
//		Frame rotatedFrame=converter.convert(grabbedImage);//不知道为什么这里不做转换就不能推到rtmp
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
