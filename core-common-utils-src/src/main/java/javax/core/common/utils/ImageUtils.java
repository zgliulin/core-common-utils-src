package javax.core.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片操作工具 
 * 主要功能有读写图片，图片裁剪，图片旋转，图片加水印
 * @author Tanyongde
 *
 */
public class ImageUtils {
	public static final String GIF = "gif",PNG = "png",JPG = "jpg",BMP = "bmp";
	
	private ImageUtils(){}
	
	/**
     * 判断一个图片文件的类型。
     * 前提是，已知该文件是图片；本函数仅读取文件头部两个字节进行判断。
     * 虽然可以多读几个字节会更精确，这里没必要，因为已知是图片了。
     * 
     * @param file
     * @return 图片类型后缀
     * @throws IOException 
     */
    
    private static String getImageType(String filePath) throws IOException{
    	
        File f = new File(filePath);
        FileInputStream in = null;
        String type = null;
        byte[] bytes = { 0, 0 }; // 用于存放文件头两个字节

        in = new FileInputStream(f);

        in.read(bytes, 0, 2);

        if (((bytes[0] & 0xFF) == 0x47) && ((bytes[1] & 0xFF) == 0x49)) { // GIF
            type = GIF;
        } else if (((bytes[0] & 0xFF) == 0x89) && ((bytes[1] & 0xFF) == 0x50)) { // PNG
            type = PNG;
        } else if (((bytes[0] & 0xFF) == 0xFF) && ((bytes[1] & 0xFF) == 0xD8)) { // JPG
            type = JPG;
        } else if (((bytes[0] & 0xFF) == 0x42) && ((bytes[1] & 0xFF) == 0x4D)) { // BMP
            type = BMP;
        } else { 
            System.out.println("无法识别!");
        }

        in.close();

        return type;
    }
    
    
    /**
     * 判断一个TYPE_INT_ARGB彩色是靠近白色还是靠近黑色
     * 
     * @param pixel 一个 TYPE_INT_ARGB颜色
     * @return 对应的黑色或白色
     */
    private static int convertToBlackWhite(int pixel) {
        int result = 0;

        int red = (pixel >> 16) & 0xff; //红 R
        int green = (pixel >> 8) & 0xff; //绿 G
        int blue = (pixel) & 0xff; //蓝 B
        
        result = 0xff000000; // 这样，白色就为全F，即 -1
        
        int tmp = red * red + green * green + blue * blue; 
        if(tmp > 3*128*128){ // 大于，则是白色
            result += 0x00ffffff;
        } else { // 是黑色
            
        }
        return result;
    }
    
    
    /**
     * 将图片转化为单色图片,雕刻效果
     * 
     * @param imgFile 输出文件路径
     * @return
     */
    public static void toMonochromeImg(String scrFilePath,String targetFilePath) { 
    	BufferedImage bi = readImageFromFile(scrFilePath);
    
        // 得到宽和高
        int width = bi.getWidth(null);
        int height = bi.getHeight(null);
        
        // 读取像素
        int [] pixels = new int[width * height];
        bi.getRGB(0, 0, width, height, pixels, 0, width);
        
        int newPixels[] = new int[width * height];
        for(int i = 0; i < width * height; i++) {
            newPixels[i] = convertToBlackWhite(pixels[i]);
        }
        
        bi = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, width, height, newPixels, 0, width);
        newPixels = null;
        
        writeImageToFile(targetFilePath, bi);
    }
    
    /**
     * 转换为灰度图片
     * @param srcFilePath
     * @param targetFilePath
     */
    public static void toGrayImg(String srcFilePath,String targetFilePath){
		File srcFile = new File(srcFilePath);
		File targetFile = new File(targetFilePath);
		toGrayImg(srcFile,targetFile);
    }
    
    /**
     * 转换为灰度图片
     * @param scrFilePath
     * @param targetFilePath
     */
    public static void toGrayImg(File srcFile,File targetFile){
		try {
			String fileName = targetFile.getName();
			String formatName = fileName.substring(targetFile.getName().lastIndexOf('.') + 1);
			BufferedImage src  = ImageIO.read(srcFile);
	        ImageIO.write(toGrayImg(src), formatName, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 判断一张图片是否是黑白图片
     * 
     * @param imgFile
     * @return 是，返回true，灰度或彩色图片，返回false
     */
    public static boolean isMonochromeImg(String imgFile) {
        BufferedImage bi = null;
        boolean result = false;
        int w = 0, h = 0;
        int i = 0, j = 0;
        
        bi = readImageFromFile(imgFile);

        w = bi.getWidth();
        h = bi.getHeight();
        int count = 0; //黑白像素个数
        int n = 0; // 非黑白个数
        for(j = 0; j < h; j++)
            for(i = 0; i < w; i++) {
                int rgb = bi.getRGB(i, j);
                rgb &= 0x00FFFFFF;
                if((rgb != 0x00FFFFFF) && (rgb != 0)){ // 既不是白色也不是黑色
                    n++;
                    break;
                }
                else {
                    count ++;
                }
            }
        if((i == w) && (j == h)) {
            result = true;
        } else {
            result = false;
        }
        
        return result;
    }
    
    /**
     * 从磁盘文件读取图片
     * 
     * @param imageFile 文件路径
     * @return BufferedImage对象，失败为null
     * @throws IOException 
     */
    public static BufferedImage readImageFromFile(String imageFile){
        BufferedImage bi = null;
        ImageInputStream iis = null;
        try{
	        // 获取某种图片格式的reader对象
	        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(getImageType(imageFile));
	        ImageReader reader = (ImageReader)readers.next();
	        // 为该reader对象设置输入源
	        iis = ImageIO.createImageInputStream(
	                new File(imageFile));
	        reader.setInput(iis);
	        
	        // 创建图片对象
	        bi = reader.read(0);
	        readers = null;
	        reader = null;
	        
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try {
        		if(null != iis)
        		{
					iis.close();
	        		iis = null;
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return bi;
    }
    
    /**
     * 将图片写入磁盘文件
     * 
     * @param imgFile 文件路径
     * @param bi BufferedImage 对象
     * @return 无
     */
    public static void  writeImageToFile(String imgFile, BufferedImage bi){
    	ImageOutputStream ios = null;
	    try{
	        // 写图片到磁盘上
	        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(
	                imgFile.substring(imgFile.lastIndexOf('.') + 1));
	        ImageWriter writer = (ImageWriter) writers.next();
	        // 设置输出源
	        File f = new File(imgFile);
	
	        ios = ImageIO.createImageOutputStream(f);
	        writer.setOutput(ios);
	        // 写入到磁盘
	        writer.write(bi);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	try {
	    		if(null != ios){
	    			ios.close();
	    			ios = null;
	    		}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
    }

    
    /**
     * 图片缩放
     * @param srcFilePath
     * @param targetFilePath
     * @param zoomSize
     */
    public static void zoomImg(String srcFilePath,String targetFilePath,int targetWidth, int targetHeight){
    	File srcFile = new File(srcFilePath);
		File targetFile = new File(targetFilePath);
		zoomImg(srcFile, targetFile, targetWidth, targetHeight);
    }
    
    /**
     * 图片缩放
     * @param srcFile
     * @param targetFile
     * @param zoomSize
     */
    public static void zoomImg(File srcFile,File targetFile,int targetWidth, int targetHeight){
		try {
			String fileName = targetFile.getName();
			String formatName = fileName.substring(targetFile.getName().lastIndexOf('.') + 1);
			BufferedImage bImage  = ImageIO.read(srcFile);
			bImage = zoomImg(bImage, targetWidth, targetHeight);
			ImageIO.write(bImage, formatName, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 旋转图片
     * @param srcFilePath 原文件路径
     * @param targetFilePath 目标文件路径
     * @param degree 旋转角度
     */
    public static void rotateImg(String srcFilePath,String targetFilePath, int degree){
    	try{
    		Color whiteColor = new Color(255,255,255);
    		File srcFile = new File(srcFilePath);
    		File targetFile = new File(targetFilePath);
    		rotateImg(srcFile, targetFile, degree,whiteColor);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    /**
     * 旋转图片
     * @param srcFile
     * @param targetFile
     * @param degree
     */
    public static void rotateImg(File srcFile,File targetFile, int degree,Color color){
    	try
    	{
	    	String fileName = targetFile.getName();
			String formatName = fileName.substring(targetFile.getName().lastIndexOf('.') + 1);
			Image image = ImageIO.read(srcFile);
			BufferedImage bImage = new BufferedImage(image.getWidth(null),image.getHeight(null),1);
			rotateImg(bImage, degree, color);
	        ImageIO.write(bImage, formatName, targetFile);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    
    
	/**
	 * 裁剪图片
	 * @param srcFilePath 要裁剪的源图片路径
	 * @param targetFilePath 裁剪后的图片存放路径
	 * @param cutX 图片裁剪的区域在原图片上的 x 位置
	 * @param cutY 图片裁剪的区域在原图片上的 y 位置
	 * @param cutWidth 图片裁剪的区域在原图片上的宽度
	 * @param cutHeight 图片裁剪的区域在原图片上的高度
	 * @throws IOException
	 */
    public static void cutImg(
    		String srcFilePath, String targetFilePath,
			int cutX, int cutY,
			int cutWidth, int cutHeight) throws IOException {
		Rectangle rec = new Rectangle(cutX, cutY, cutWidth, cutHeight);
		saveSubImage(new File(srcFilePath), new File(targetFilePath), rec);
	}


	/**
	 * 裁剪图片
	 * @param srcFile 要裁剪的源图片
	 * @param targetFile 裁剪后的图片存放
	 * @param cutX 图片裁剪的区域在原图片上的 x 位置
	 * @param cutY 图片裁剪的区域在原图片上的 y 位置
	 * @param cutWidth 图片裁剪的区域在原图片上的宽度
	 * @param cutHeight 图片裁剪的区域在原图片上的高度
	 * @throws IOException
	 */
	public static void cutImg(
			File srcFile, File targetFile,
			int cutX, int cutY, 
			int cutWidth, int cutHeight) throws IOException {
		Rectangle rec = new Rectangle(cutX, cutY, cutWidth, cutHeight);
		saveSubImage(srcFile, targetFile, rec);
	}

	/**
	 * 
	 * 裁剪图片
	 * @param srcFilePath 要裁剪的源图片路径
	 * @param targetFilePath 裁剪后的图片存放路径
	 * @param rect 在原图片上的裁剪区域
	 * @throws IOException
	 */
	public static void cutImg(String srcFilePath, String targetFilePath,Rectangle rect) throws IOException {
		cutImg(new File(srcFilePath), new File(targetFilePath), rect);
	}
	
	/**
	 * 裁剪图片并缩放
	  * @param srcFilePath 要裁剪的源图片路径
	 * @param targetFilePath 裁剪后的图片存放路径
	 * @param cutX 图片裁剪的区域在原图片上的 x 位置
	 * @param cutY 图片裁剪的区域在原图片上的 y 位置
	 * @param cutWidth 图片裁剪的区域在原图片上的宽度
	 * @param cutHeight 图片裁剪的区域在原图片上的高度
	 * @param targetWidth 缩放后的大小
	 * @param targetHeight 缩放后的大小
	 * @throws IOException
	 */
	public static void cutImg(String srcFilePath, String targetFilePath,
		int cutX,int cutY,int cutWidth,int cutHeight,
		int targetWidth,int targetHeight) throws IOException {
		Rectangle cutRec = new Rectangle(cutX, cutY, cutWidth, cutHeight);
		cutImg(new File(srcFilePath), new File(targetFilePath),cutRec,targetWidth,targetHeight);
	}
	

	/**
	 * 裁剪图片并缩放
	  * @param srcFile 要裁剪的源图片路径
	 * @param targetFile 裁剪后的图片存放路径
	 * @param rect 在原图片上的裁剪区域
	 * @param targetWidth 缩放后的大小
	 * @param targetHeight 缩放后的大小
	 * @throws IOException
	 */
	public static void cutImg(File srcFile, File targetFile,Rectangle rect,int targetWidth,int targetHeight) throws IOException {
		Image image = ImageIO.read(srcFile);
		BufferedImage bImage = makeThumbnail(image);
		bImage = getSubImage(bImage, rect);
		bImage = zoomImg(bImage, targetWidth, targetHeight);
		ImageIO.write(bImage, FileUtils.getFileExtName(targetFile), targetFile);
	}
	

	/**
	 * 裁剪图片
	 * @param srcFile 要裁剪的源图片
	 * @param targetFile 裁剪后的图片存放
	 * @param rect 在原图片上的裁剪区域
	 * @throws IOException
	 */
	public static void cutImg(File srcFile, File targetFile,Rectangle rect) throws IOException {
		Image image = ImageIO.read(srcFile);
		BufferedImage bImage = makeThumbnail(image);
		bImage = getSubImage(bImage, rect);
		ImageIO.write(bImage, FileUtils.getFileExtName(srcFile), targetFile);
	}

	
	/**
	 * 添加水印
	 * 
	 * @param fileBytes 源图片内容
	 * @param watermarkBytes 水印图片路径 
	 * @param words 要添加的文字 
	 * @param alpha 水印透明度
	 * @param savePath 为你添加水印后的图片保存路径文件夹
	 * @throws Exception 抛出异常，可能某些类型的图片格式转换不了 小于minW*minH的图片不添加水印
	 */
	/*public static void createMark(byte [] fileBytes, byte [] watermarkBytes,
			String words, float alpha,int minW,int minH,int watermarkX,int watermarkY) throws Exception {
		//int wid = 0,het = 0;
		ImageIcon imgIcon = new ImageIcon(fileBytes);
		Image theImg = imgIcon.getImage();
		Image waterImg = null;
//		File f = new File(filePath);
//		String picname = f.getName();// 取得图片名
		if (!(null == watermarkBytes || 0 == watermarkBytes.length)) {// 当水印图标不为空时
			ImageIcon waterIcon = new ImageIcon(watermarkBytes);
			waterImg = waterIcon.getImage();
//			wid = markImg.getWidth(null); // 水印图标宽度
//			het = markImg.getHeight(null); // 水印图标高度
		}
		int width = theImg.getWidth(null); // 源图片宽度
		int height = theImg.getHeight(null); // 源图片高度
		if (width < minW || height < minH) {
			return;
		}
//		if (savePath.equals(""))
//			savePath = filePath;// 如果未指定保存路径则保存回原路径
//		else
//			savePath = savePath + "/" + picname;// 指定保存文件夹时,拼接出保存路径
		try {
			BufferedImage bimage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bimage.createGraphics();
			if(null != waterImg){
				g.drawImage(waterImg, watermarkX, watermarkY, null); // 添加图标中间两个数字参数
			}
			// 添加文字
			if(!(null == words || "".equals(words.trim()))){
				Font font = new Font("黑体", Font.PLAIN, 35);
				g.setColor(Color.white); // 设置颜色
				g.setFont(font);
				g.setBackground(Color.white);
				g.drawImage(theImg, 0, 0, null);
				g.drawString(words, watermarkX, watermarkY);
			}
//			FileOutputStream out = new FileOutputStream(savePath);
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
//			param.setQuality(100f, true); // 图片质量
//			encoder.encode(bimage, param);
//			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();// 清理 垃圾对象
		}
	}
	*/
  
    /**  
     * 给图片添加水印、可设置水印图片旋转角度  
     * @param fileBytes 水印图片路径  
     * @param iconBytes 源图片路径  
     * @param words 水印文字
     * @param alpha 目标图片路径  
     * @param size 图片透明度
     * @param marginLeft 左边间距
     * @param marginBottom 底部间距
     */  
    public static byte [] createMark(byte [] fileBytes,byte [] iconBytes,String words, float alpha,int size,int marginLeft,int marginBottom) {   
    	ByteArrayOutputStream os = null;
    	ByteArrayInputStream fis = null;
    	ByteArrayInputStream iis = null;
        try {
        	os = new ByteArrayOutputStream();
        	fis = new ByteArrayInputStream(fileBytes);
        	iis = new ByteArrayInputStream(iconBytes);
        	
        	
            Image srcImg = ImageIO.read(fis);//new ImageIcon(fileBytes).getImage();
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度   
            Image iconImg = ImageIO.read(iis);// ImageIO.read(new File(iconPath));
            if(null == srcImg){
            	return fileBytes;
            }
            int srcW = srcImg.getWidth(null); //原图片的宽度
            int srcH = srcImg.getHeight(null); //原图片的高度
            if (Math.max(srcW, srcH) < size) {
    			return fileBytes;
    		}
            int b = 22;
            int iconRealW = iconImg.getWidth(null); //水印图片的实际宽度
            int iconRealH = iconImg.getHeight(null); //水印图片的实际高度
            
            int iconTheoryH = (srcW - marginBottom) / (b - 1);//水印图片的理论高度
            int ionTheoryW = 0;//水印图片的理论宽度
            
            //计算等比缩放理论值
			if (iconRealW > ionTheoryW || iconRealH > iconTheoryH) {
					float rate1 = (float)ionTheoryW / (float)iconRealW;
					float rate2 = (float)iconTheoryH / (float)iconRealH;
					float rate = rate1 > rate2 ? rate1 : rate2;
					ionTheoryW = (int)(iconRealW * rate);
					iconTheoryH = (int)(iconRealH * rate);
			} else {
				ionTheoryW = iconRealW;
				iconTheoryH = iconRealH;
			}  
            
            if(marginLeft > srcW - ionTheoryW || marginBottom > srcH - iconTheoryH){
            	return fileBytes;
            }
            //等比缩放水印图案
            BufferedImage iconBuffImg = new BufferedImage(ionTheoryW, iconTheoryH, BufferedImage.TYPE_INT_RGB);
            Graphics2D ig = iconBuffImg.createGraphics();
            ig.drawImage(iconImg.getScaledInstance(ionTheoryW, iconTheoryH, Image.SCALE_SMOOTH), 0, 0, null);
            ig.dispose();
            BufferedImage srcBuffImg = new BufferedImage(srcW,srcH, BufferedImage.TYPE_INT_RGB);   
            // 得到画笔对象   
            // Graphics g= buffImg.getGraphics();   
            Graphics2D g = srcBuffImg.createGraphics();   
            // 设置对线段的锯齿状边缘处理   
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);   
            g.drawImage(srcImg.getScaledInstance(srcW, srcH, Image.SCALE_SMOOTH), 0, 0, null);   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));   
            // 表示水印图片的位置 
            g.drawImage(iconBuffImg, marginLeft, (srcH - marginBottom - iconTheoryH), null);   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));   
            g.dispose();   
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(srcBuffImg);
			param.setQuality(100f, true); // 图片质量
			encoder.encode(srcBuffImg, param);
			return os.toByteArray();
        } catch (Exception e) {   
            e.printStackTrace();   
            return fileBytes;
        } finally {   
            try {
            	if(null != fis){
            		fis.close();
            	}
            	if(null != iis){
            		iis.close();
            	}
                if (null != os){
                    os.close();
                }
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
            System.gc();// 清理 垃圾对象
        }   
    }   
    
    
    
    
    
    
	
   
	/**
	 * 添加水印
	 * 
	 * @param filePath 源图片路径 含图片名，
	 * @param watermark 水印图片路径 
	 * @param words 要添加的文字 
	 * @param alpha 水印透明度
	 * @param savePath 为你添加水印后的图片保存路径文件夹
	 * @throws Exception 抛出异常，可能某些类型的图片格式转换不了 小于200*200的图片不添加水印
	 */
	public static void createMark(String filePath, String watermark,
			String words, float alpha, String savePath) throws Exception {
		int wid = 0,het = 0;
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image theImg = imgIcon.getImage();
		ImageIcon waterIcon = new ImageIcon(watermark);
		Image waterImg = waterIcon.getImage();
		File f = new File(filePath);
		String picname = f.getName();// 取得图片名
		if (watermark != null && !watermark.equals("")) {// 当水印图标不为空时
			ImageIcon markIcon = new ImageIcon(watermark); // 要添加的水印图标
			Image markImg = markIcon.getImage();
			wid = markImg.getWidth(null); // 水印图标宽度
			het = markImg.getHeight(null); // 水印图标高度
		}
		int width = theImg.getWidth(null); // 源图片宽度
		int height = theImg.getHeight(null); // 源图片高度
		if (width < 200 || height < 200) {
			return;
		}
		if (savePath.equals(""))
			savePath = filePath;// 如果未指定保存路径则保存回原路径
		else
			savePath = savePath + "/" + picname;// 指定保存文件夹时,拼接出保存路径
		try {
			BufferedImage bimage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bimage.createGraphics();
			Font font = new Font("黑体", Font.PLAIN, 35);
			g.setColor(Color.white); // 设置颜色
			g.setFont(font);
			g.setBackground(Color.white);
			g.drawImage(theImg, 0, 0, null);
			g.drawImage(waterImg, width - wid - 90, height - het - 70, null); // 添加图标中间两个数字参数
			// 添加文字
			g.drawString(words, width - 480, height - 40);
			FileOutputStream out = new FileOutputStream(savePath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
			param.setQuality(100f, true); // 图片质量
			encoder.encode(bimage, param);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();// 清理 垃圾对象
		}
	}
	
	
	
//======================= 私有方法 =================================
	
	//图片去色
	private static BufferedImage toGrayImg(BufferedImage bi) {
		//设置图片亮度
		 RescaleOp op = new RescaleOp(1.2f, 0f, null);
		 bi = op.filter(bi, null);
		
		 //设置颜色模式
		 ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
	     ColorConvertOp cop = new ColorConvertOp(cs, null);
	     return cop.filter(bi, null);
    }

	
	//缩放图片
	private static BufferedImage zoomImg(BufferedImage bImage,int targetWidth,int targetHeight){
		BufferedImage target = null;

		double sx = (double) targetWidth / bImage.getWidth();
		double sy = (double) targetHeight / bImage.getHeight();

		int type = bImage.getType();
		if (type == BufferedImage.TYPE_CUSTOM) {
			ColorModel cm = bImage.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetWidth,targetHeight);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			target = new BufferedImage(targetWidth, targetHeight, type);
		}
		Graphics2D g = target.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(bImage, AffineTransform.getScaleInstance(sx,sy));
		g.dispose();
		return target;
	}
	
	//旋转图片
	private static void rotateImg(BufferedImage image,int degree, Color bgcolor) throws IOException {  
    	//String fileName = subImageFile.getName();
		//String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
        int iw = image.getWidth();//原始图象的宽度   
        int ih = image.getHeight();//原始图象的高度  
        int w = 0;  
        int h = 0;  
        int x = 0;  
        int y = 0;  
        degree = degree % 360;  
        if (degree < 0)  
            degree = 360 + degree;//将角度转换到0-360度之间  
        double ang = Math.toRadians(degree);//将角度转为弧度  
  
        /** 
         *确定旋转后的图象的高度和宽度 
         */  
  
        if (degree == 180 || degree == 0 || degree == 360) {  
            w = iw;  
            h = ih;  
        } else if (degree == 90 || degree == 270) {  
            w = ih;  
            h = iw;  
        } else {  
            int d = iw + ih;  
            w = (int) (d * Math.abs(Math.cos(ang)));  
            h = (int) (d * Math.abs(Math.sin(ang)));  
        }  
  
        x = (w / 2) - (iw / 2);//确定原点坐标  
        y = (h / 2) - (ih / 2);  
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());  
        Graphics2D gs = (Graphics2D)rotatedImage.getGraphics();  
        if(bgcolor==null){  
            rotatedImage  = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);  
        }else{  
            gs.setColor(bgcolor);  
            gs.fillRect(0, 0, w, h);//以给定颜色绘制旋转后图片的背景  
        }  
          
        AffineTransform at = new AffineTransform();  
        at.rotate(ang, w / 2, h / 2);//旋转图象  
        at.translate(x, y);  
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);  
        op.filter(image, rotatedImage);  
        image = rotatedImage;  
          
        //ByteArrayOutputStream  byteOut= new ByteArrayOutputStream();  
        //ImageOutputStream iamgeOut = ImageIO.createImageOutputStream(byteOut);  
          
        //ImageIO.write(image, formatName, iamgeOut);  
        // InputStream  inputStream = new ByteArrayInputStream(byteOut.toByteArray());  
          
        // return inputStream;  
    }
	
	//将裁剪内容保存到目标图片上
	private static void saveSubImage(File srcFile, File targetFile,Rectangle rect) throws IOException {
		cutImg(srcFile, targetFile, rect);
	}

	//绘制新图片
	private static BufferedImage makeThumbnail(Image img) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage tag = new BufferedImage(width, height, 1);
		Graphics g = tag.getGraphics();
		g.drawImage(img.getScaledInstance(width, height, 4), 0, 0, null);
		g.dispose();
		return tag;
	}

	//将裁剪内容保存到目标图片上
	private static BufferedImage getSubImage(BufferedImage image,
			Rectangle subImageBounds) throws IOException {
//		String fileName = subImageFile.getName();
//		String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
		BufferedImage subImage = new BufferedImage(subImageBounds.width,
				subImageBounds.height, 1);
		Graphics g = subImage.getGraphics();
		if (subImageBounds.width > image.getWidth()
				|| subImageBounds.height > image.getHeight()) {
			int left = subImageBounds.x;
			int top = subImageBounds.y;
			if (image.getWidth() < subImageBounds.width)
				left = (subImageBounds.width - image.getWidth()) / 2;
			if (image.getHeight() < subImageBounds.height)
				top = (subImageBounds.height - image.getHeight()) / 2;
			g.setColor(Color.white);
			g.fillRect(0, 0, subImageBounds.width, subImageBounds.height);
			g.drawImage(image, left, top, null);
//			ImageIO.write(image, formatName, subImageFile);
//			System.out.println((new StringBuilder("if is running left:"))
//					.append(left).append(" top: ").append(top).toString());
		} else {
//			System.out.println("else is running");
			g.drawImage(image.getSubimage(subImageBounds.x, subImageBounds.y,
					subImageBounds.width, subImageBounds.height), 0, 0, null);
		}
		g.dispose();
//		ImageIO.write(subImage, formatName, subImageFile);
		return subImage;
	}
	
	
	/**
	 * 配置滤镜
	 * @param image
	 * @param outPath
	 */
	private static void alpha(BufferedImage image, String outPath) {
		// 创建java2D对象
		Graphics2D g2d = image.createGraphics();
		// g2d.setBackground(Color.PINK);
		// 填充背景
		g2d.setColor(Color.yellow);
		g2d.fillRect(0, 0, 600, 400);

		// 创建AlphaComposite对象，并设定透明度
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f);
		g2d.setComposite(ac);
		g2d.setFont(new Font("隶书", Font.PLAIN, 42));
		g2d.setColor(Color.black);
		g2d.drawString("方正粗宋简体透明度为0.5", 20, 40);

		// 画一个背景色为..的长方形

		AlphaComposite ac2 = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.2f);
		g2d.setComposite(ac2);
		g2d.setColor(Color.CYAN);
		g2d.fill3DRect(10, 200, 180, 80, false);

		g2d.dispose();
		try {
			FileOutputStream fs = new FileOutputStream(outPath);
			ImageIO.write(image, "jpg", fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把图片印刷到图片上
	 * 
	 * @param pressImg 水印文件
	 * @param targetImg  目标文件
	 * @param x  x坐标
	 * @param y  y坐标
	 * @param alpha  透明度
	 */
	private static void pressImage(String pressImg, String targetImg, int x,
			int y, float alpha) {
		try {
			// 目标文件
			float Alpha = alpha;
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					Alpha));
			g.drawImage(src_biao, (wideth - wideth_biao) / 2,
					(height - height_biao) / 2, wideth_biao, height_biao, null);
			// 水印文件结束
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印文字水印图片
	 * 
	 * @param pressText  文字
	 * @param targetImg 目标图片
	 * @param fontName   字体名
	 * @param fontStyle  字体样式
	 * @param color  字体颜色
	 * @param fontSize  字体大小
	 * @param x  偏移量
	 * @param y
	 */

	private static void pressText(String pressText, String targetImg,
			String fontName, int fontStyle, int color, int fontSize, int x,
			int y, float alpha) {
		try {
			float Alpha = alpha;
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();

			g.drawImage(src, 0, 0, wideth, height, null);
			g.setColor(Color.RED);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					Alpha));
			g.drawString(pressText, wideth - fontSize - x, height - fontSize
					/ 2 - y);
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	
	
//	public static void main(String[] args) {
//		try
//		{
//			String downloadPath = new StringBuilder(CommonUtil.WEBAPP_PATH).append(SystemConfig.fileUploadPath).toString();
//			String fileName = "C:/Users/panpan/Desktop/20090117105013734";
//			String extName = "jpg";
//			String name100 = UUID.randomUUID().toString();
//			String name80 = UUID.randomUUID().toString();
//			String name60 = UUID.randomUUID().toString();
//			String w100Path = downloadPath + name100;
//			String w80Path = downloadPath + name80;
//			String w60Path = downloadPath + name60;
//			ImageUtil.cutImg(fileName + "." + extName, w100Path + "." + extName, 117, 21, 397, 397);
//			ImageUtil.zoomImg(fileName + "." + extName, w100Path + "." + extName, 100, 100);
//			//toGrayImg(fileName + "." + extName,downloadPath + "a.jpg");
////			ImageUtil.cutImg(fileName + "." + extName, w80Path + "." + extName, 80,80, 202, 66, 239, 239);
////			ImageUtil.cutImg(fileName + "." + extName, w60Path + "." + extName, 60,60, 202, 66, 239, 239);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
}
