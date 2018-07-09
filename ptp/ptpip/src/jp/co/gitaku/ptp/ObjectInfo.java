package jp.co.gitaku.ptp;

import java.net.URLConnection;

import jp.co.gitaku.util.Common;

public class ObjectInfo extends Data {
	int storageId;
	int objectFormatCode;
	int protectionStatus;
	int objectCompressedSize;
	int thumbFormat;
	int thumbCompressedSize;
	int thumbPixWidth;
	int thumbPixHeight;
	int imagePixWidth;
	int imagePixHeight;
	int imageBitDepth;
	int parentObject;
	int associationType;
	int associationDesc;
	int sequenceNumber;
	String filename;
	String captureDate;
	String modificationDate;
	String keywords;
	int handle;
	public static final int Undefined = 12288;
	public static final int Association = 12289;
	public static final int Script = 12290;
	public static final int Executable = 12291;
	public static final int Text = 12292;
	public static final int HTML = 12293;
	public static final int DPOF = 12294;
	public static final int AIFF = 12295;
	public static final int WAV = 12296;
	public static final int MP3 = 12297;
	public static final int AVI = 12298;
	public static final int MPEG = 12299;
	public static final int ASF = 12300;
	public static final int QuickTime = 12301;
	public static final int XML = 12302;
	public static final int UnknownImage = 14336;
	public static final int EXIF_JPEG = 14337;
	public static final int TIFF_EP = 14338;
	public static final int FlashPix = 14339;
	public static final int BMP = 14340;
	public static final int CIFF = 14341;
	public static final int GIF = 14343;
	public static final int JFIF = 14344;
	public static final int PCD = 14345;
	public static final int PICT = 14346;
	public static final int PNG = 14347;
	public static final int TIFF = 14349;
	public static final int TIFF_IT = 14350;
	public static final int JP2 = 14351;
	public static final int JPX = 14352;
	public static final int DNG = 14353;

	ObjectInfo(int paramInt) {
		this.handle = paramInt;
	}

	public ObjectInfo(URLConnection urlConn, DeviceInfo deviceInfo) {
		super(false, new byte[1024]);

		String str = urlConn.getContentType();

		this.objectCompressedSize = urlConn.getContentLength();

//		this.filename = "11.jpg";
		
		if (str.startsWith("image/")) {
			int i = 0;

			if ("image/jpeg".equals(str)) {
				if (deviceInfo.supportsImageFormat(JFIF)) {
					this.objectFormatCode = JFIF;
				} else if (deviceInfo.supportsImageFormat(EXIF_JPEG))
					this.objectFormatCode = EXIF_JPEG;
				else
					i = 1;
			} else if ("image/tiff".equals(str)) {
				if (deviceInfo.supportsImageFormat(TIFF))
					this.objectFormatCode = TIFF;
				else if (deviceInfo.supportsImageFormat(TIFF_EP))
					this.objectFormatCode = TIFF_EP;
				else if (deviceInfo.supportsImageFormat(TIFF_IT))
					this.objectFormatCode = TIFF_IT;
				else
					i = 1;
			} else if ("image/gif".equals(str))
				this.objectFormatCode = GIF;
			else if ("image/png".equals(str))
				this.objectFormatCode = PNG;
			else if ("image/vnd.fpx".equals(str))
				this.objectFormatCode = FlashPix;
			else if ("image/x-MS-bmp".equals(str))
				this.objectFormatCode = BMP;
			else if ("image/x-photo-cd".equals(str))
				this.objectFormatCode = PCD;
			else {
				this.objectFormatCode = UnknownImage;
			}
			if ((i != 0) || (!deviceInfo.supportsImageFormat(this.objectFormatCode))) {
				throw new IllegalArgumentException("device doesn't support " + str);
			}

		} else if ("text/html".equals(str)) {
			this.objectFormatCode = HTML;
		} else if ("text/plain".equals(str)) {
			this.objectFormatCode = Text;
		} else if ("audio/mp3".equals(str)) {
			this.objectFormatCode = MP3;
		} else if ("audio/x-aiff".equals(str)) {
			this.objectFormatCode = AIFF;
		} else if ("audio/x-wav".equals(str)) {
			this.objectFormatCode = WAV;
		} else if ("video/mpeg".equals(str)) {
			this.objectFormatCode = MPEG;
		} else {
			this.objectFormatCode = Undefined;
		}

		content2Bytes();
	}

	void content2Bytes() {
		put32(this.storageId);
		put16(this.objectFormatCode);
		put16(this.protectionStatus);
		put32(this.objectCompressedSize);

		put16(this.thumbFormat);
		put32(this.thumbCompressedSize);
		put32(this.thumbPixWidth);
		put32(this.thumbPixHeight);

		put32(this.imagePixWidth);
		put32(this.imagePixHeight);
		put32(this.imageBitDepth);
		put32(this.parentObject);

		put16(this.associationType);
		put32(this.associationDesc);
		put32(this.sequenceNumber);
		putString(this.filename);

		putString(this.captureDate);
		putString(this.modificationDate);
		putString(this.keywords);

		this.length = this.offset;
		this.offset = 0;

		byte[] arrayOfByte = new byte[this.length];

		System.arraycopy(this.data, 0, arrayOfByte, 0, this.length);
		this.data = arrayOfByte;
	}

	public int getLength() {
		return this.data.length;
	}

	void bytes2Content() {
		this.storageId = nextS32();
		this.objectFormatCode = nextU16();
		this.protectionStatus = nextU16();
		this.objectCompressedSize = nextS32();

		this.thumbFormat = nextU16();
		this.thumbCompressedSize = nextS32();
		this.thumbPixWidth = nextS32();
		this.thumbPixHeight = nextS32();

		this.imagePixWidth = nextS32();
		this.imagePixHeight = nextS32();
		this.imageBitDepth = nextS32();
		this.parentObject = nextS32();

		this.associationType = nextU16();
		this.associationDesc = nextS32();
		this.sequenceNumber = nextS32();
		this.filename = nextString();

		this.captureDate = nextString();
		this.modificationDate = nextString();
		this.keywords = nextString();
	}

	public boolean isImage() {
		return (this.objectFormatCode & 0xF800) == 14336;
	}

	public String getCodeName(int paramInt) {
		return getFormatString(paramInt);
	}

	public static String getFormatString(int paramInt) {
		switch (paramInt) {
		case Undefined:
			return "UnknownFormat";
		case Association:
			return "Association";
		case Script:
			return "Script";
		case Executable:
			return "Executable";
		case Text:
			return "Text";
		case HTML:
			return "HTML";
		case DPOF:
			return "DPOF";
		case AIFF:
			return "AIFF";
		case WAV:
			return "WAV";
		case MP3:
			return "MP3";
		case AVI:
			return "AVI";
		case MPEG:
			return "MPEG";
		case ASF:
			return "ASF";
		case QuickTime:
			return "QuickTime";
		case XML:
			return "XML";
		case UnknownImage:
			return "UnknownImage";
		case EXIF_JPEG:
			return "EXIF/JPEG";
		case TIFF_EP:
			return "TIFF/EP";
		case FlashPix:
			return "FlashPix";
		case BMP:
			return "BMP";
		case CIFF:
			return "CIFF";
		case GIF:
			return "GIF";
		case JFIF:
			return "JFIF";
		case PCD:
			return "PCD";
		case PICT:
			return "PICT";
		case PNG:
			return "PNG";
		case TIFF:
			return "TIFF";
		case TIFF_IT:
			return "TIFF/IT";
		case JP2:
			return "JP2";
		case JPX:
			return "JPX";
		case DNG:
			return "DNG";
		}
		return Container.getCodeString(paramInt);
	}

	String associationString(int paramInt) {
		switch (paramInt) {
		case 0:
			return null;
		case 1:
			return "GenericFolder";
		case 2:
			return "Album";
		case 3:
			return "TimeSequence";
		case 4:
			return "HorizontalPanorama";
		case 5:
			return "VerticalPanorama";
		case 6:
			return "2DPanorama";
		case 7:
			return "AncillaryData";
		}
		StringBuffer localStringBuffer;
		if ((paramInt & 0x8000) == 0)
			localStringBuffer = new StringBuffer("Reserved-0x");
		else
			localStringBuffer = new StringBuffer("Vendor-0x");
		localStringBuffer.append(Integer.toHexString(paramInt));
		return localStringBuffer.toString();
	}

	public void print() {
		Common.outputln("ObjectInfo:");

		if (this.storageId != 0) {
			Common.output("StorageID: 0x");
			Common.output(Integer.toHexString(this.storageId));
			switch (this.protectionStatus) {
			case 0:
				Common.outputln(", unprotected");
				break;
			case 1:
				Common.outputln(", read-only");
				break;
			default:
				Common.output(", reserved protectionStatus 0x");
				Common.outputln(Integer.toHexString(this.protectionStatus));
			}

		}

		if (this.parentObject != 0)
			Common.outputln("Parent: 0x" + Integer.toHexString(this.parentObject));
		if (this.filename != null)
			Common.outputln("Filename " + this.filename);
		if (this.sequenceNumber != 0) {
			Common.output("Sequence = ");
			Common.output(this.sequenceNumber);
		}

		if (this.thumbFormat != 0) {
			Common.output("Image format: ");
			Common.output(getFormatString(this.objectFormatCode));
			Common.output(", size ");
			Common.output(this.objectCompressedSize);
			Common.output(", width ");
			Common.output(this.imagePixWidth);
			Common.output(", height ");
			Common.output(this.imagePixHeight);
			Common.output(", depth ");
			Common.outputln(this.imageBitDepth);

			Common.output("Thumbnail format: ");
			Common.output(getFormatString(this.thumbFormat));
			Common.output(", size ");
			Common.output(this.thumbCompressedSize);
			Common.output(", width ");
			Common.output(this.thumbPixWidth);
			Common.output(", height ");
			Common.output(this.thumbPixHeight);
			Common.output(", depth ");
			Common.outputln(this.imageBitDepth);
		} else {
			Common.output("Object format: ");
			Common.output(getFormatString(this.objectFormatCode));
			Common.output(", size ");
			Common.outputln(this.objectCompressedSize);

			if (this.objectFormatCode == 12289) {
				String str = associationString(this.associationType);
				if (str != null) {
					Common.output("Association type: ");
					Common.output(str);
					if (this.associationDesc != 0) {
						Common.output(", desc 0x");
						Common.output(Integer.toHexString(this.associationDesc));
					}
					Common.outputln();
				}
			}
		}

		if (this.captureDate != null)
			Common.outputln("capture date: " + this.captureDate);
		if (this.modificationDate != null)
			Common.outputln("modification date: " + this.modificationDate);
		if (this.keywords != null)
			Common.outputln("keywords: " + this.keywords);
	}

	public int getStorageId() {
		return storageId;
	}

	public int getObjectFormatCode() {
		return objectFormatCode;
	}

	public int getProtectionStatus() {
		return protectionStatus;
	}

	public int getObjectCompressedSize() {
		return objectCompressedSize;
	}

	public int getThumbFormat() {
		return thumbFormat;
	}

	public int getThumbCompressedSize() {
		return thumbCompressedSize;
	}

	public int getThumbPixWidth() {
		return thumbPixWidth;
	}

	public int getThumbPixHeight() {
		return thumbPixHeight;
	}

	public int getImagePixWidth() {
		return imagePixWidth;
	}

	public int getImagePixHeight() {
		return imagePixHeight;
	}

	public int getImageBitDepth() {
		return imageBitDepth;
	}

	public int getParentObject() {
		return parentObject;
	}

	public int getAssociationType() {
		return associationType;
	}

	public int getAssociationDesc() {
		return associationDesc;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public String getFilename() {
		return filename;
	}

	public String getCaptureDate() {
		return captureDate;
	}

	public String getModificationDate() {
		return modificationDate;
	}

	public String getKeywords() {
		return keywords;
	}

	public int getHandle() {
		return handle;
	}
}