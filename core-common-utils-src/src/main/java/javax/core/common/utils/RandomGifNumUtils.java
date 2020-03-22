package javax.core.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.core.common.utils.giftools.AnimatedGifEncoder;


/**
 * 用于生成动画验证图片
 * 继承自 RandomNumUtil 类
 * 
 * @author Tanyongde
 *
 * @date Jan 10, 2011, 5:23:26 PM
 *
 */
public class RandomGifNumUtils extends RandomNumUtils {
	
	private static String author = ""; //作者、来源、版权
	
	private static RandomGifNumUtils randomGifNumUtil;
	
	private RandomGifNumUtils(){}
	
	/**
     * 默认产生 4 位字母数字组合的验证码
     * @return
     */
    public static RandomGifNumUtils instance(){
    	return instance(4);
    }
    
    /**   
     * 取得RandomNumUtil实例 
     * @param length 设置产生随机码的长度
     * @return
     */
    public static RandomGifNumUtils instance(int length){
    	return instance(EN_CODE,length);
    }
    /**
     * (重载)取得RandomNumUtil实例 
     * @param base_code 设置产生随机码的取值字符范围,传入三个常量， EN_CODE, CN_CODE, UNION_CODE
     * @param length 设置产生随机码的长度
     * @return
     */
   public static RandomGifNumUtils instance(int base_code_key,int length){
	   if(null == randomGifNumUtil){
		   randomGifNumUtil = new RandomGifNumUtils();
   	   }
	   if(null == base_code)
	   {
		   base_code = randomGifNumUtil.getBaseCode(base_code_key);
	   }
	   randomGifNumUtil.code_length = (0 == length) ? 4 : length; 
	   randomGifNumUtil.init();
	   return randomGifNumUtil;
   }

	protected void init() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();   

		// 生成字符
		AnimatedGifEncoder agf = new AnimatedGifEncoder();

		agf.start(output);
		agf.setQuality(10);

		agf.setDelay(100);
		agf.setRepeat(0);
		BufferedImage frame = null;
		Color bgcolor = getRandColor(160, 200);
		Color linecolor = getRandColor(200, 250);
		
		StringBuffer sRand = new StringBuffer();
		
		
		String rands[] = new String[code_length];
		for (int i = 0; i < code_length; i++) {
		 	int start = random.nextInt(base_code.length()); 
        	String rand = base_code.substring(start,start + 1);
            rands[i] = rand;
			sRand.append(rands[i]);
		}
		this.str = sRand.toString();
		
		
		//  创建 6 帧动画,创建的动画帧越多，图片闪动的速度越快
		 
		for (int i = 0; i < 6; i++) {
			frame = this.getImage(bgcolor, linecolor, rands, i);
			agf.addFrame(frame);//添加动画帧
			frame.flush();
		}
		agf.finish();
		
		 //输出
        ByteArrayInputStream input = null;  

        try{   
            input = new ByteArrayInputStream(output.toByteArray());   
        }catch(Exception e){   
            System.out.println("验证码图片产生出现错误：" + e.toString());   
        }   
        this.image=input;/* 赋值图像 */ 
	}

	/** 
     * 取得验证码图片 
     */ 
    public ByteArrayInputStream getImage(){   
        return this.image;   
    }   
    
    /** 
     * 取得图片的验证码 
     */ 
    public String getString(){   
        return this.str;   
    }   
	
	private BufferedImage getImage(Color bgcolor, Color linecolor, String str[], int flag) {
		int width = (18 * code_length) + 6, height = 26;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 或得图形上下文
		Graphics2D g2d = image.createGraphics();
		// 利用指定颜色填充背景
		g2d.setColor(bgcolor);
		g2d.fillRect(0, 0, width, height);
		// 画背景线 4*4
		g2d.setColor(linecolor);
		for (int i = 0; i < height / 4; i++) {

			g2d.drawLine(0, i * 4, width, i * 4);
		}
		for (int i = 0; i < width / 4; i++) {

			g2d.drawLine(i * 4, 0, i * 4, height);
		}
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.2f);
		g2d.setComposite(ac);
		g2d.setFont(new Font("隶书", Font.ITALIC + Font.BOLD, 25));
		g2d.setColor(Color.red);
		g2d.drawString(null == author ? "" : author, 3, 23);

		// 以下生成验证码 //透明度从0 循环到1 步长为0.2 。一共6个字母
		AlphaComposite ac3 = null;
		for (int i = 0; i < str.length; i++) {
			g2d.setFont(this.getFont());
			ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(random.nextInt(6), random.nextInt(6)));
			g2d.setComposite(ac3);
			g2d.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));

			g2d.drawString(str[i], 18 * i + 6, 20);
		}
		g2d.dispose();
		return image;
	}

	/**
	 * 
	 * @参数：
	 * @返回值:
	 * @描述:获得循环透明度，从0到1 步长为0.2
	 */
	private float getAlpha(int i, int j) {
		if ((i + j) > 5) {

			return ((i + j) * 0.2f - 1.2f);
		} else {
			return (i + j) * 0.2f;
		}
	}


	/**
	 * 返回[from,to)之间的一个随机整数
	 * 
	 * @param from
	 *            起始值
	 * @param to
	 *            结束值
	 * @return [from,to)之间的一个随机整数
	 */
	private int randomInt(int from, int to) {
		return from + random.nextInt(to - from);
	}

	public static void setAuthor(String author) {
		RandomGifNumUtils.author = author;
	}


}
