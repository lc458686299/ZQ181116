package com.szboanda.reptile.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;


/**
 * 图片转pdf
 * @author Licong
 *
 */
public class ImgPdfUtil {
	/**
	 * 把图片转化为pdf
	 * @param imageFolderPath
	 * @param pdfPath
	 */
	public static void toPdf(String imageFolderPath, String pdfPath) {
        try {
          
            String imagePath = null;
           
            FileOutputStream fos = new FileOutputStream(pdfPath);
            // 创建文档
            Document doc = new Document(null, 0, 0, 0, 0);
          
            PdfWriter.getInstance(doc, fos);
             
            Image image = null;
          
            File file = new File(imageFolderPath);
            File[] files = file.listFiles();
            
            for (File file1 : files) {
                if (file1.getName().endsWith(".png")
                        || file1.getName().endsWith(".jpg")
                        || file1.getName().endsWith(".gif")
                        || file1.getName().endsWith(".jpeg")
                        || file1.getName().endsWith(".tif")) {
                    // System.out.println(file1.getName());
                    imagePath = imageFolderPath +"/"+ file1.getName();
                    System.out.println(imagePath);
                    // 读取图片流
                 /*   img = ImageIO.read(new File(imagePath));
                    System.out.println(img.getWidth()+" "+img.getHeight()+"****");*/
                    
                    doc.setPageSize(new Rectangle(1488, 2104));
                    // 实例化图片
                    try {    //如果图片下载失败，会出现io异常,这里跳过此图片
                    	 image = Image.getInstance(imagePath);
                         image.setAlignment(Image.MIDDLE);
                         image.scaleAbsolute(1488.0f, 2104.0f);
                         // 添加图片到文档
                         doc.open();
                         doc.add(image);
					} catch (Exception e) {
						e.printStackTrace();
					}
                   
                }
            }
            // 关闭文档
            if(doc.isOpen()){
            	doc.close();
            }
            fos.close();
            //删除文件夹
            deleteDir(file);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
	
	/*private static int getPercent2(float h,float w)
	{
		int p=0;
		float p2=0.0f;
		p2=530/w*100;
		System.out.println("--"+p2);
		p=Math.round(p2);
		return p;
	
	}*/
	
    public static void main(String[] args) {
        long time1 = System.currentTimeMillis();
        toPdf("D:/xkzfb/", "D:/cs.pdf");
        long time2 = System.currentTimeMillis();
        int time = (int) ((time2 - time1)/1000);
        System.out.println("执行了："+time+"秒！");
    }
    
    /**
     * 删除目录和文件
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
  

}
