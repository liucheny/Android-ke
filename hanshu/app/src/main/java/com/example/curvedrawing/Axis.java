package com.example.curvedrawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Axis {
    private int nMinX;
    private int nMaxX;
    private int nMinY;
    private int nMaxY;
    //物理范围
    private Rect mRect;
    public Axis(Rect rect) {
        nMinX = -10;
        nMaxX = 10;
        nMinY = -10;
        nMaxY = 10;
        mRect = rect;
    }



    //将逻辑坐标转换为物理坐标
    public int convertXLP2DP(double x) {
        return mRect.left + (int) (mRect.width() / (double) (nMaxX - nMinX) * (x - nMinX));
    }

    //将逻辑坐标转换为物理坐标
    public int convertYLP2DP(double y) {
        return mRect.bottom
                - (int) (mRect.height() / (double) (nMaxY - nMinY) * (y - nMinX));
    }


    //绘制坐标轴
    public void draw(Canvas canvas) {
        //画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        double nX=(double)(nMaxX-nMinX)/20;
        double nY=(double)(nMaxY-nMinY)/20;

        //绘制坐标轴
        canvas.drawLine(convertXLP2DP(nMinX), convertYLP2DP(0), convertXLP2DP(nMaxX), convertYLP2DP(0), paint);//x轴
        canvas.drawLine(convertXLP2DP(0), convertYLP2DP(nMaxY), convertXLP2DP(0), convertYLP2DP(nMinY), paint);//y轴


        //绘制坐标轴上的坐标（数字）
        paint.setStrokeWidth(1);
        paint.setTextSize(9);
        canvas.drawText("0", convertXLP2DP(nX), convertYLP2DP(-nY), paint);//原点
        canvas.drawText(nMinX + "", convertXLP2DP(nMinX+nX), convertYLP2DP(-nY), paint);//x最小
        canvas.drawText(nMinX+"",convertXLP2DP(nMaxX-nX), convertYLP2DP(-nY),paint);//x最大
        canvas.drawText(nMinY+"",convertXLP2DP(-nX), convertYLP2DP(nMinY+nY),paint);//y最小
        canvas.drawText(nMinY+"",convertXLP2DP(-nX), convertYLP2DP(nMaxY-nY),paint);//y最大
    }
}
