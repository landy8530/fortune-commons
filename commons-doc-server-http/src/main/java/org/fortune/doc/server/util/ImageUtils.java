package org.fortune.doc.server.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author: landy
 * @date: 2019/6/16 11:24
 * @description:
 */
public class ImageUtils {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    private String srcPath;
    private String subPath;
    private int x;
    private int y;
    private int width;
    private int height;

    public String getSrcPath() {
        return this.srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getSubPath() {
        return this.subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ImageUtils() {
    }

    public void cut() throws IOException {
        FileInputStream is = null;
        ImageInputStream iis = null;

        try {
            is = new FileInputStream(this.srcPath);
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            logger.info("x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height);
            Rectangle rect = new Rectangle(this.x, this.y, this.width, this.height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "jpg", new File(this.subPath));
        } finally {
            if (is != null) {
                is.close();
            }

            if (iis != null) {
                iis.close();
            }

        }

    }

    private static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }

        return filename;
    }

    public static boolean ratioZoom(File srcFile, String dest, String newFileName, String suffix, int newWidth, int newHeight) throws IOException {
        double Ratio = 0.0D;
        if (!srcFile.isFile()) {
            throw new IOException(srcFile + " is not image file error in CreateThumbnail!");
        } else {
            File ThF = new File(dest, newFileName + "." + suffix);
            BufferedImage Bi = ImageIO.read(srcFile);
            Bi.getScaledInstance(newWidth, newHeight, 8);
            if (Bi.getHeight() > newWidth || Bi.getWidth() > newHeight) {
                if (Bi.getHeight() > Bi.getWidth()) {
                    Ratio = (double)newWidth / (double)Bi.getHeight();
                } else {
                    Ratio = (double)newHeight / (double)Bi.getWidth();
                }
            }

            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(Ratio, Ratio), (RenderingHints)null);
            Image Itemp = op.filter(Bi, (BufferedImage)null);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(ThF);
                ImageIO.write((BufferedImage)Itemp, suffix, os);
            } catch (Exception var22) {
                logger.error("图片缩放存储异常，e = " + var22);
                var22.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException var21) {
                        var21.printStackTrace();
                    }
                }

            }

            return true;
        }
    }

    public static void reduceImg(String imgSrc, String imgDist, int widthDist, int heightDist) {
        try {
            File srcFile = new File(imgSrc);
            if (!srcFile.exists()) {
                return;
            }

            Image src = ImageIO.read(srcFile);
            BufferedImage tag = new BufferedImage(widthDist, heightDist, 1);
            tag.getGraphics().drawImage(src.getScaledInstance(widthDist, heightDist, 4), 0, 0, (ImageObserver)null);
            FileOutputStream out = new FileOutputStream(imgDist);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }

    public static void ratioZoom2(File src, File dist, double ratio0) {
        try {
            if (!src.exists()) {
                throw new NullPointerException("文件不存在");
            }

            BufferedImage image = ImageIO.read(src);
            double ratio = 0.0D;
            if (image.getHeight() > 100 || image.getWidth() > 100) {
                if (image.getHeight() > image.getWidth()) {
                    ratio = ratio0 / (double)image.getHeight();
                } else {
                    ratio = ratio0 / (double)image.getWidth();
                }
            }

            int newWidth = (int)((double)image.getWidth() * ratio);
            int newHeight = (int)((double)image.getHeight() * ratio);
            BufferedImage bfImage = new BufferedImage(newWidth, newHeight, 1);
            bfImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, 4), 0, 0, (ImageObserver)null);
            FileOutputStream os = new FileOutputStream(dist);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            encoder.encode(bfImage);
            os.close();
            System.out.println("创建缩略图成功");
        } catch (Exception var12) {
            System.out.println("创建缩略图发生异常" + var12.getMessage());
        }

    }

    public static void main(String[] args) {
        try {
            new ImageUtils();
            if (ratioZoom(new File("e:/r1-blue-3.jpg"), "e:/", "aaaa", "jpg", 300, 300)) {
                System.out.println("Success");
            } else {
                System.out.println("Error");
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            System.out.print(var2.toString());
        }

        ratioZoom2(new File("e:/r1-blue-3.jpg"), new File("e:/99999.jpg"), 300.0D);
    }

}
