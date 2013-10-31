package com.jaspersoft.studio.property.color.chooser;

import org.eclipse.swt.graphics.RGB;

public class RedBasedSelector implements IWidgetGovernor{
	@Override
	public int getSliderMax() {
		return 255;
	}

	@Override
	public int getSliderMin() {
		return 0;
	}


	@Override
	public int getPadMinX() {
		return 0;
	}

	@Override
	public int getPadMinY() {
		return 0;
	}

	@Override
	public int getPadMaxX() {
		return 255;
	}

	@Override
	public int getPadMaxY() {
		return 255;
	}

	@Override
	public RGB getPadColor(int x, int y, int sliderPosition) {
		return new RGB(Math.abs(255-sliderPosition),Math.abs(255-y),x);
	}
	
	@Override
	public RGB getSliderColor(int x, int y, int sliderPosition) {
		return getPadColor(x, y, sliderPosition);
	}

	public int getX(RGB color) {
		return color.blue;
	}

	public int getY(RGB color) {
		return Math.abs(255-color.green);
	}

	public int getSlider(RGB color) {
		return Math.abs(255-color.red);
	}
	
	@Override
	public int[] getXYSlider(Object color) {
		if (color instanceof RGB){
			RGB rgbColor = (RGB) color;
			return new int[]{getX(rgbColor), getY(rgbColor), getSlider(rgbColor)};
		} else if (color instanceof float[]){
			float[] hsb = (float[])color;
			RGB rgbColor = new RGB(hsb[0], hsb[1], hsb[2]);
			return new int[]{getX(rgbColor), getY(rgbColor), getSlider(rgbColor)};
		}
		return new int[]{0,0,0};
	}
	
}
