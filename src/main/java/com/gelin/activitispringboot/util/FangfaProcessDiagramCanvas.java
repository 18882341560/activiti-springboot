package com.gelin.activitispringboot.util;

import org.activiti.image.impl.DefaultProcessDiagramCanvas;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/03/01
 * @description: 类描述:
 **/
public class FangfaProcessDiagramCanvas extends DefaultProcessDiagramCanvas {

    protected static Color HIGHLIGHT_COLOR = new Color(131,111,255);

    public FangfaProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }

    public FangfaProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType) {
        super(width, height, minX, minY, imageType);
    }

    @Override
    public void drawHighLight(int x, int y, int width, int height) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        g.setPaint(new Color(255,87,34));
        g.setStroke(THICK_TASK_BORDER_STROKE);

        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        g.draw(rect);

        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }


}
