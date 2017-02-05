package gridImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GridImage {
	public static int GAP = 10;
	public static int SINGLE_SIZE = 60;
    public static String DEFAULT_TYPE = "jpg";
    public static int WHITE = -1;
    public static int GRAY = -1381654;
    
    
    public static String mergeGrid(String[] pics,  String dst_pic){
    	return mergeGrid(pics, DEFAULT_TYPE, dst_pic,SINGLE_SIZE,GAP);
    }
    
    public static String mergeGrid(String[] pics, String type, String dst_pic){
    	return mergeGrid(pics, type, dst_pic,SINGLE_SIZE,GAP);
    }
    
    /**
     * 拼成九宫格
     * @param pics
     * @param type
     * @param dst_pic
     * @param size
     * @param gap
     * @return
     */
    public static String mergeGrid(String[] pics, String type, String dst_pic, int size, int gap) {  
    	int size_dest = getDestSize(pics.length,size,gap);
    	
    	int col;
    	if(pics.length > 4){
    		col = 3;
    	}else{
    		col = 2;
    	}
    	
    	int plength =  size_dest*size_dest;
    	int[] white_canvas=new int[plength];

		for (int i = 0; i < plength; i++) {
			white_canvas[i]=GRAY;
//				white_canvas[i]=WHITE;
		}
    	
    	int[][] coords = new int[col*col][2];
    	for(int i=0;i<coords.length;i++){
    		coords[i][0] = gap*(i%col + 1)+size*(i%col);
    		coords[i][1] = gap*(i/col+1)+size*(i/col);
    	}
        int len = pics.length;  
        if (len < 1) {  
            System.out.println("pics len < 1");  
            return null;  
        }  
        File[] src = new File[len];  
        BufferedImage[] images = new BufferedImage[len];  
        int[][] ImageArrays = new int[len][size*size];  
        for (int i = 0; i < len; i++) {  
            try {  
                src[i] = new File(pics[i]);  
                images[i] = ImageIO.read(src[i]);  
            } catch (Exception e) {  
                e.printStackTrace();  
                return null;  
            }  
            ImageArrays[i] = images[i].getRGB(0, 0, size, size,  
                    ImageArrays[i], 0, size);  
        }  
  
  
        // 生成新图片  
        try {  
            // dst_width = images[0].getWidth();  
            BufferedImage ImageNew = new BufferedImage(size_dest, size_dest,  
                    BufferedImage.TYPE_INT_RGB);  
            
            ImageNew.setRGB(0, 0, size_dest, size_dest, white_canvas, 0, size_dest);
            for (int i = 0; i < ImageArrays.length; i++) { 
            	int[] c = coords[i];
                ImageNew.setRGB(c[0], c[1], size, size,  
                        ImageArrays[i], 0, size);  
            }  
  
            File outFile = new File(dst_pic);  
            if(!new File(outFile.getParent()).exists()){
            	new File(outFile.getParent()).mkdirs();
            }
            System.out.println(outFile.getAbsolutePath());
            ImageIO.write(ImageNew, type, outFile);// 写图片  
            return outFile.getAbsolutePath();
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
    
    public static int getDestSize(int num, int size, int gap){
    	if(num > 4){
    		return 3*size+4*gap;
    	}else{
    		return 2*size+3*gap;
    	}
    }
    
    public static void main(String[] args) throws IOException{
    /*	String[] files = new String[8];
    	files[0] = "C:\\Users\\wss\\workspace\\tiy\\image\\a.jpg";
    	files[1] = "C:\\Users\\wss\\workspace\\tiy\\image\\b.jpg";
    	files[2] = "C:\\Users\\wss\\workspace\\tiy\\image\\c.jpg";
    	files[3] = "C:\\Users\\wss\\workspace\\tiy\\image\\d.jpg";
    	files[4] = "C:\\Users\\wss\\workspace\\tiy\\image\\e.jpg";
    	files[5] = "C:\\Users\\wss\\workspace\\tiy\\image\\f.jpg";
    	files[6] = "C:\\Users\\wss\\workspace\\tiy\\image\\g.jpg";
    	files[7] = "C:\\Users\\wss\\workspace\\tiy\\image\\h.jpg";
//    	files[8] = "C:\\Users\\wss\\workspace\\tiy\\image\\i.jpg";
    	String dest = "C:\\Users\\wss\\workspace\\tiy\\image\\8.jpg";
    	mergeGrid(files,"jpg",dest,200,10);*/
    	
    	BufferedImage b  = ImageIO.read(new File("C:\\Users\\wss\\Pictures\\Camera Roll\\新建文件夹\\graytest.png"));
    	System.out.println(b.getRGB(0, 0));
    }
}
