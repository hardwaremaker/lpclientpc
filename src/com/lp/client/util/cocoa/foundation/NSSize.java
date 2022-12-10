package com.lp.client.util.cocoa.foundation;

import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.List;

import com.lp.client.util.cocoa.CGFloat;
import com.sun.jna.Structure;

public class NSSize extends Structure implements Structure.ByValue {
    public CGFloat width;
    public CGFloat height;

    public NSSize() {
        this(0, 0);
    }

    public NSSize(double width, double height) {
        this.width = new CGFloat(width);
        this.height = new CGFloat(height);
    }

    public NSSize(Dimension2D pSize) {
        this(pSize.getWidth(), pSize.getHeight());
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("width", "height");
    }
}
