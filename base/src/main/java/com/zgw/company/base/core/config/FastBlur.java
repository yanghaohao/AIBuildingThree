package com.zgw.company.base.core.config;

import android.graphics.Bitmap;

public class FastBlur {

    public static Bitmap blur(Bitmap sentBitmap,int radius,boolean canReuseInBitmap){
        Bitmap bitmap;
        if (canReuseInBitmap){
            bitmap = sentBitmap;
        }else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1){
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pix = new int[width * height];
        bitmap.getPixels(pix, 0, width, 0, 0, width, height);

        int widthM = width -1;
        int heightM = height -1;
        int widthAndHeight = width * height;
        int div = radius + radius+1;

        int[] r = new int[widthAndHeight];
        int[] g = new int[widthAndHeight];
        int[] b = new int[widthAndHeight];
        int rSum,gSum,bSum,x,y,i,p,yp,yi,yw;
        int[] vMin = new int[Math.max(width, height)];

        int divSum = (div + 1) >> 1;
        divSum *= divSum;
        int[] dv = new int[256 * divSum];
        for (i = 0; i < 256 * divSum; i++) {
            dv[i] = (i/divSum);
        }

        yw = yi = 0 ;

        int[][] stack = new int[div][3];
        int stackPointer;
        int stackStart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int rOutSum,gOutSum,bOutSum;
        int rInSum,gInSum,bInSum;

        for (y = 0;y < height;y++){
            rInSum = gInSum = bInSum = rOutSum = gOutSum = bOutSum = rSum = gSum = bSum = 0;
            for (i = -radius;i <= radius;i++){
                p = pix[yi + Math.min(widthM, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rSum += sir[0]*rbs;
                gSum += sir[1]*rbs;
                bSum += sir[2]*rbs;
                if (i > 0){
                    rInSum += sir[0];
                    gInSum += sir[1];
                    bInSum += sir[2];
                }else {
                    rOutSum += sir[0];
                    gOutSum += sir[1];
                    bOutSum += sir[2];
                }
            }

            stackPointer = radius;

            for (x = 0;x < width;x++){
                r[yi] = dv[rSum];
                g[yi] = dv[gSum];
                b[yi] = dv[bSum];

                rSum -= rOutSum;
                gSum -= gOutSum;
                bSum -= bOutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                rOutSum -= sir[0];
                gOutSum -= sir[1];
                bOutSum -= sir[2];

                if (y == 0){
                    vMin[x] = Math.min(x + radius + 1, widthM);
                }
                p = pix[yw + vMin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rInSum += sir[0];
                gInSum += sir[1];
                bInSum += sir[2];

                rSum += rInSum;
                gSum += gInSum;
                bSum += bInSum;

                stackPointer = (stackPointer+1) % div;
                sir = stack[stackPointer % div];

                rOutSum += sir[0];
                gOutSum += sir[1];
                bOutSum += sir[2];

                rInSum -= sir[0];
                gInSum -= sir[1];
                bInSum -= sir[2];

                yi++;
            }

            yw += width;
        }

        for (x = 0;x < width ; x++){
            rInSum = gInSum = bInSum = rOutSum = gOutSum = bOutSum = rSum = gSum = bSum = 0;
            yp = -radius*width;
            for (i = -radius;i <= radius;i++){
                yi = Math.max(0, yp)+x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rSum += r[yi] * rbs;
                gSum += g[yi] * rbs;
                bSum += b[yi] * rbs;

                if (i > 0) {
                    rInSum += sir[0];
                    gInSum += sir[1];
                    bInSum += sir[2];
                } else {
                    rOutSum += sir[0];
                    gOutSum += sir[1];
                    bOutSum += sir[2];
                }

                if (i < heightM){
                    yp += width;
                }
            }

            yi = x;
            stackPointer = radius;
            for (y = 0;y < height; y++){
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rSum] << 16) | (dv[gSum] << 8) | dv[bSum];

                rSum -= rOutSum;
                gSum -= gOutSum;
                bSum -= bOutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackPointer % div];

                rOutSum -= sir[0];
                gOutSum -= sir[1];
                bOutSum -= sir[2];

                if (x == 0){
                    vMin[y] = Math.min(y + r1, heightM) * width;
                }

                p = x + vMin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rInSum += sir[0];
                gInSum += sir[1];
                bInSum += sir[2];

                rSum += rInSum;
                gSum += gInSum;
                bSum += bInSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[stackPointer];

                rOutSum += sir[0];
                gOutSum += sir[1];
                bOutSum += sir[2];

                rInSum -= sir[0];
                gInSum -= sir[1];
                bInSum -= sir[2];

                yi += width;
            }
        }

        bitmap.setPixels(pix, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
