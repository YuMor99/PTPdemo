package jp.co.gitaku.ptp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.co.gitaku.ptp.Command;
import jp.co.gitaku.ptp.DeviceInfo;
import jp.co.gitaku.ptp.Response;
import jp.co.gitaku.ptp.Session;
import jp.co.gitaku.ptp.Data;
import jp.co.gitaku.ptp.StorageInfo;
import jp.co.gitaku.ptp.ObjectInfo;

import static java.lang.System.out;

//import javax.imageio.stream.FileImageOutputStream;

public abstract class BaselineInitiator {
	private DeviceInfo info;
	private Session session = new Session();

	/** 
	 * Get the device information
	 * @return the device information
	 * @throws Exception
	 */
	public DeviceInfo getDeviceInfo() throws Exception {
		if (this.info == null)
			return getDeviceInfoUncached();
		return this.info;
	}

	/** 
	 * command for taking picture
	 * added by liu
	 * @return void
	 * @throws Exception
	 */
	public void byte2image(byte[] data,String path){
		if(data.length<3||path.equals("")) return;
//		try{
			FileOutputStream out = null;
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
			try {
				out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				System.out.println("___________保存的__sd___下_______________________");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.println("保存已经至"+Environment.getExternalStorageDirectory()+"下");
//			Toast.makeText(HahItemActivity.this,"保存已经至"+Environment.getExternalStorageDirectory()+"下", Toast.LENGTH_SHORT).show();
//		}
//			FileOutputStream imageOutput = new FileOutputStream(new File(path));
//			imageOutput.write(data, 0, data.length);
//			imageOutput.close();
//			out.println("Make Picture success,Please find image in " + path);
//		} catch(Exception ex) {
//			out.println("Exception: " + ex);
//			ex.printStackTrace();
//		}
	}

	public Response takePhoto() throws Exception {
		Command command = new Command(Command.TakePhoto, this.session, 0, 0, 0, 0, 0);
		out.println("i give the command to take photo");
		Response res=sendCommand(command,null);
		out.println("pppppppppppppppppppppppppppppppppppppppppppppppppp");
		byte2image(res.getData(),"ptptest.JPG");//D:\PTPIP\
		return res;
	}

	/**
	 * Get the latest device information
	 * @return the device information
	 * @throws Exception
	 */
	public DeviceInfo getDeviceInfoUncached() throws Exception {
		DeviceInfo dev = new DeviceInfo();
		Response res;
		synchronized (this.session) {
			Command command = new Command(Command.GetDeviceInfo, this.session, 0, 0, 0, 0, 0);
			res = sendCommand(command, dev);
		}

		switch (res.getCode()) {
			case 8193:
				this.info = dev;
				return dev;
		}
		throw new IOException(res.toString());
	}
	/** 
	 * Open the Session
	 * @throws Exception
	 */
	public void openSession() throws Exception {
		synchronized (this.session) {
			int sessionID = this.session.getNextSessionID();
			Command command = new Command(Command.OpenSession, this.session, sessionID, 0, 0, 0, 0);
			Response res = sendCommand(command, null);
			switch (res.getCode()) {
			case Response.OK:
				this.session.open();
				out.println("接続しました。");
				return;
			}
			throw new IOException(res.toString());
		}
	}

	/** 
	 * Close the session
	 * @throws Exception
	 */
	public void closeSession() throws Exception {
		synchronized (this.session) {
			Command command = new Command(Command.CloseSession, this.session, 0, 0, 0, 0, 0);
			Response res = sendCommand(command, null);
			switch (res.getCode()) {
			case Response.OK:
			case Response.SessionNotOpen:
				this.session.close();
				return;
			}
			throw new Exception(res.toString());
		}
	}
	
	/**
	 * Get the StorageIDs
	 * @return the StorageIDs
	 * @throws Exception
	 */
	public int[] getStorageIDs() throws Exception {
		DataArray data = new DataArray(4);
		Command command = new Command(Command.GetStorageIDs, this.session, 0, 0, 0, 0, 0);
		Response res = sendCommand(command, data);

		switch (res.getCode()) {
		case Response.OK:
			return (int[])data.getElementList();
		}
		throw new Exception(res.toString());
	}
	
	/**
	 * Get the Storage information
	 * @param storageID
	 * @return the Storage information
	 * @throws Exception
	 */
	public StorageInfo getStorageInfo(int storageID) throws Exception {
		StorageInfo data = new StorageInfo();
		Command command = new Command(Command.GetStorageInfo, this.session, storageID, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * Get the number of objects
	 * @param storageID
	 * @param objectFormatCode
	 * @param objectHandle
	 * @return the number of objects
	 * @throws Exception
	 */
	public int getNumObjects(int storageID, int objectFormatCode, int objectHandle) throws Exception {
		Command command = new Command(Command.GetNumObjects, this.session, storageID, objectFormatCode, objectHandle, 0, 0);
		Response res = sendCommand(command, null);

		switch (res.getCode()) {
		case Response.OK:
			return res.getParam1();
		}
		throw new Exception(res.toString());
	}

	/**
	 * Get the object handles
	 * @param storageID
	 * @param objectFormatCode
	 * @param objectHandle
	 * @return the object handles
	 * @throws Exception
	 */
	public int[] getObjectHandles(int storageID, int objectFormatCode, int objectHandle) throws Exception {
		DataArray data = new DataArray(4);
		Command command = new Command(Command.GetObjectHandles, this.session, storageID, objectFormatCode, objectHandle, 0, 0);
		Response res = sendCommand(command, data);

		switch (res.getCode()) {
		case Response.OK:
			return (int[])data.getElementList();
		}
		throw new Exception(res.toString());
	}
	
	/**
	 * Get the object information by handle
	 * @param objectHandle
	 * @return the object information
	 * @throws Exception
	 */
	public ObjectInfo getObjectInfo(int objectHandle) throws Exception {
		ObjectInfo data = new ObjectInfo(objectHandle);
		Command command = new Command(Command.GetObjectInfo, this.session, objectHandle, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * Get the object by handle
	 * @param objectHandle
	 * @return the object by handle
	 * @throws Exception
	 */
	public Data getObject(int objectHandle) throws Exception {
		ObjectData data = new ObjectData();
		Command command = new Command(Command.GetObject, this.session, objectHandle, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	
	/**
	 * Get the thumb of object by handle
	 * @param objectHandle
	 * @return the thumb of object
	 * @throws Exception
	 */
	public Data getThumb(int objectHandle) throws Exception {
		ObjectData data = new ObjectData();
		Command command = new Command(Command.GetThumb, this.session, objectHandle, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * Delete the object by handle and format code
	 * @param objectHandle
	 * @param formatCode
	 * @throws Exception
	 */
	public void deleteObject(int objectHandle, int formatCode) throws Exception {
		Command command = new Command(Command.DeleteObject, this.session, objectHandle, formatCode, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Send object information
	 * @param data
	 * @param storageID
	 * @param objectHandle
	 * @return Response
	 * @throws Exception
	 */
	public Response sendObjectInfo(ObjectInfo data, int storageID, int objectHandle) throws Exception {
		Command command = new Command(Command.SendObjectInfo, this.session, storageID, objectHandle, 0, 0, 0);
		Response res = sendCommandWithData(command, data);
		return res;
	}
	
	/**
	 * Send the object
	 * @param data
	 * @throws Exception
	 */
	public void sendObject(Data data) throws Exception {
		Command command = new Command(Command.SendObject, this.session, 0, 0, 0, 0, 0);
		sendData(command, data);
	}
	
	/**
	 * Initiate a capture
	 * @param storageID
	 * @param formatCode
	 * @throws Exception
	 */
	public void initiateCapture(int storageID, int formatCode) throws Exception {
		Command command = new Command(Command.InitiateCapture, this.session, storageID, formatCode, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Format the store
	 * @param storageID
	 * @param filesystemType
	 * @throws Exception
	 */
	public void formatStore(int storageID, int filesystemType) throws Exception {
		Command command = new Command(Command.FormatStore, this.session, storageID, filesystemType, 0, 0, 0);
		exeCommand(command);
	}	
	
	/**
	 * Reset the device
	 * @throws Exception
	 */
	public void resetDevice() throws Exception {
		Command command = new Command(Command.ResetDevice, this.session, 0, 0, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Do a self test
	 * @param type
	 * @throws Exception
	 */
	public void selfTest(int type) throws Exception {
		Command command = new Command(Command.SelfTest, this.session, type, 0, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Set the object protection
	 * @param objectHandle
	 * @param status
	 * @throws Exception
	 */
	public void setObjectProtection(int objectHandle, int status) throws Exception {
		Command command = new Command(Command.SetObjectProtection, this.session, objectHandle, status, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Power down
	 * @throws Exception
	 */
	public void powerDown() throws Exception {
		Command command = new Command(Command.PowerDown, this.session, 0, 0, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Get the appropriate property describing dataset as indicated by the first parameter
	 * @param devicePropCode
	 * @return The property describing dataset
	 * @throws Exception
	 */
	public DevicePropDesc getDevicePropDesc(int devicePropCode) throws Exception {
		DevicePropDesc data = new DevicePropDesc();
		Command command = new Command(Command.GetDevicePropDesc, this.session, devicePropCode, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * Get the current value of a property
	 * @param codeType
	 * @param devicePropCode
	 * @return The current value of a property
	 * @throws Exception
	 */
	public DevicePropValue getDevicePropValue(int codeType, int devicePropCode) throws Exception {
		DevicePropValue data = new DevicePropValue(codeType);
		Command command = new Command(Command.GetDevicePropValue, this.session, devicePropCode, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * Sets the current value of the device property indicated by 
	 * Parameter1 to the value indicated in the data phase of this operation
	 * @param devicePropCode
	 * @param devicePropValue
	 * @throws Exception
	 */
	public void setDevicePropValue(int devicePropCode, DevicePropValue devicePropValue) throws Exception {
		Command command = new Command(Command.SetDevicePropValue, this.session, devicePropCode, 0, 0, 0, 0);
		exeCommand(command, devicePropValue);
	}
	
	/**
	 * Sets the value of the indicated device property to the factory default setting
	 * @param devicePropCode
	 * @throws Exception
	 */
	public void resetDevicePropValue(int devicePropCode) throws Exception {
		Command command = new Command(Command.ResetDevicePropValue, this.session, devicePropCode, 0, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Used after an InitiateOpenCapture operation for situations where the capture 
	 * operation length is open-ended, and determined by the initiator.
	 * @param transactionID
	 * @throws Exception
	 */
	public void terminateOpenCapture(int transactionID) throws Exception {
		Command command = new Command(Command.TerminateOpenCapture, this.session, transactionID, 0, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Causes the object to be moved from its location within the hierarchy 
	 * to a new location indicated by the second and third parameters.
	 * @param objectHandle
	 * @param storageID
	 * @param newObjectHandle
	 * @throws Exception
	 */
	public void moveObject(int objectHandle, int storageID, int newObjectHandle) throws Exception {
		Command command = new Command(Command.MoveObject, this.session, objectHandle, storageID, newObjectHandle, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Causes the object to be replicated within the responder.
	 * @param objectHandle
	 * @param storageID
	 * @param newObjectHandle
	 * @throws Exception
	 */
	public void copyObject(int objectHandle, int storageID, int newObjectHandle) throws Exception {
		Command command = new Command(Command.CopyObject, this.session, objectHandle, storageID, newObjectHandle, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Retrieves a partial object from the device.
	 * @param data
	 * @param objectHandle
	 * @param offset
	 * @param maxNumber
	 * @return a partial object
	 * @throws Exception
	 */
	public int getPartialObject(ObjectData data, int objectHandle, int offset, int maxNumber) throws Exception {
		Command command = new Command(Command.GetPartialObject, this.session, objectHandle, offset, maxNumber, 0, 0);
		Response res = sendCommand(command, data);
		switch (res.getCode()) {
		case Response.OK:
			return res.getParam1();
		}
		throw new Exception(res.toString());
	}
	
	/**
	 * Causes the device to initiate the capture of one or more new data 
	 * objects according to its current device properties, storing the data 
	 * in the store indicated by the StorageID.
	 * @param storageID
	 * @param formatCode
	 * @throws Exception
	 */
	public void initiateOpenCapture(int storageID, int formatCode) throws Exception {
		Command command = new Command(Command.InitiateOpenCapture, this.session, storageID, formatCode, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Initiates an enumeration process in the scope of the active session.
	 * @param storageID
	 * @param formatCode
	 * @param objectHandle
	 * @return
	 * @throws Exception
	 */
	public int startEnumHandles(int storageID, int formatCode, int objectHandle) throws Exception {
		Command command = new Command(Command.StartEnumHandles, this.session, storageID, formatCode, objectHandle, 0, 0);
		Response res = sendCommand(command, null);
		switch (res.getCode()) {
		case Response.OK:
			return res.getParam1();
		}
		throw new Exception(res.toString());
	}
	
	/**
	 * Get an array of ObjectHandles present in the store indicated 
	 * at the initiation of the handle enumeration procedure.
	 * @param enumID
	 * @param maxNum
	 * @return
	 * @throws Exception
	 */
	public DataArray enumHandles(int enumID, int maxNum) throws Exception {
		DataArray data = new DataArray(32);
		Command command = new Command(Command.EnumHandles, this.session, enumID, maxNum, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * Closes an active enumeration process (indicated by the EnumID in 
	 * the first parameter) in the scope of the active session.
	 * @param storageID
	 * @throws Exception
	 */
	public void stopEnumHandles(int storageID) throws Exception {
		Command command = new Command(Command.StopEnumHandles, this.session, storageID, 0, 0, 0, 0);
		exeCommand(command);
	}
	
	/**
	 * Used by a PTP v1.1-aware initiator to retrieve the mapping of 
	 * other vendor specific extensions into the “unused” portion of 
	 * the current default/native vendor space.
	 * @return the mapping of other vendor
	 * @throws Exception
	 */
	public DataArray getVendorExtensionMaps() throws Exception {
		DataArray dataArray = new DataArray(8);
		Command command = new Command(Command.GetVendorExtensionMaps, this.session, 0, 0, 0, 0, 0);
		exeCommand(command, dataArray);
		return dataArray;
	}
	
	/**
	 * Used by a PTP v1.1-aware initiator to retrieve the capabilities 
	 * supported for each vendor.
	 * @param vendorExtID
	 * @return the capabilities supported for each vendor.
	 * @throws Exception
	 */
	public DeviceInfo getVendorDeviceInfo(int vendorExtID) throws Exception {
		DeviceInfo data = new DeviceInfo();
		Command command = new Command(Command.GetVendorDeviceInfo, this.session, vendorExtID, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * An optional operation that retrieves an image object from the device, 
	 * but typically at a non-standard resolution.
	 * @param data
	 * @param objectHandle
	 * @param width
	 * @param height
	 * @return An image object
	 * @throws Exception
	 */
	public Response getResizedImageObject(ObjectData data, int objectHandle, int width, int height) throws Exception {
		Command command = new Command(Command.GetResizedImageObject, this.session, objectHandle, width, height, 0, 0);
		return sendCommand(command, data);
	}
	
	/**
	 * This operation retrieves an array of ObjectFilesystemInfo datasets.
	 * @param storageID
	 * @param formatCode
	 * @param objectHandle
	 * @return An array of ObjectFilesystemInfo datasets
	 * @throws Exception
	 */
	public ObjectFilesystemInfo getFilesystemManifest(int storageID, int formatCode, int objectHandle) throws Exception {
		ObjectFilesystemInfo data = new ObjectFilesystemInfo(); 
		Command command = new Command(Command.GetFilesystemManifest, this.session, storageID, formatCode, objectHandle, 0, 0);
		exeCommand(command, data);
		return data;
	}

	/**
	 * This operation is used to retrieve the information about one of the 
	 * supported streams.
	 * @param streamType
	 * @return the stream information
	 * @throws Exception
	 */
	public StreamInfo getStreamInfo(int streamType) throws Exception {
		StreamInfo data = new StreamInfo(); 
		Command command = new Command(Command.GetStreamInfo, this.session, streamType, 0, 0, 0, 0);
		exeCommand(command, data);
		return data;
	}
	
	/**
	 * This operation will start a continuous stream of packets from the responder
	 * to the initiator, holding the responder in a continuous data phase.
	 * @return a continuous stream
	 * @throws Exception
	 */
	public DataStream getStream() throws Exception {
		Command command = new Command(Command.GetStream, this.session, 0, 0, 0, 0, 0);
		return getStream(command);
	}
	
	/**
	 * execute an operation
	 * @param comm
	 * @throws Exception
	 */
	private void exeCommand(Command comm) throws Exception {
		Response res = sendCommand(comm, null);
		switch (res.getCode()) {
		case Response.OK:
			return;
		}
		throw new Exception(res.toString());
	}
	
	/**
	 * execute an operation to get data
	 * @param comm
	 * @param data
	 * @throws Exception
	 */
	private void exeCommand(Command comm, Data data) throws Exception {
		Response res = sendCommand(comm, data);
		switch (res.getCode()) {
		case Response.OK:
			return;
		}
		throw new Exception(res.toString());
	}
	
	/**
	 * execute an operation to send data
	 * @param comm
	 * @param data
	 * @throws Exception
	 */
	private void sendData(Command comm, Data data) throws Exception {
		Response res = sendCommandWithData(comm, data);
		switch (res.getCode()) {
		case Response.OK:
			return;
		}
		throw new Exception(res.toString());
	}
	
	protected abstract DataStream getStream(Command comm) throws Exception;
	
	protected abstract Response sendCommand(Command comm, Data data) throws Exception;

	protected abstract Response sendCommandWithData(Command comm, Data data) throws Exception;
	// protected abstract Response cancelCommand(Command comm, Data data) throws
	// Exception ;
}
