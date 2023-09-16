package com.bkav.aiotcloud.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.aiobject.ZoneAIObject;

import java.util.ArrayList;
import java.util.List;

public class PaintView extends View {

	float ratioRadius;

	public PaintView(Context context) {
		super(context);
		zoneObjects = new ArrayList<>();
		setNewZone();
	}

	public PaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		zoneObjects = new ArrayList<>();
		setNewZone();
	}

	public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		zoneObjects = new ArrayList<>();
		setNewZone();
	}



//	public void refreshZone() {
//		List<Point> points = new ArrayList<>();
//		this.points = points;
//	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawPoly(canvas, Color.RED, zoneObjects);
	}
	
	public void setShapeRadiusRatio(float ratio){
		ratioRadius = ratio;
	}
	private ArrayList<ZoneAIObject> zoneObjects;
	private boolean isNewZone = false;
	private ZoneAIObject zoneAIObject;

	public void setNewZone(){
		List<Point> points = new ArrayList<>();
		this.zoneAIObject = new ZoneAIObject(points, "");
	}

	public void drawZones( ArrayList<ZoneAIObject> zoneObjects){
		this.zoneObjects = zoneObjects;
		invalidate();
	}

	public void clearZone(ArrayList<ZoneAIObject> zoneObjects){
		this.zoneObjects.clear();
		this.zoneAIObject.getCoordinate().clear();
		this.zoneObjects.addAll(zoneObjects);
		invalidate();
	}

	public void addPoint(int x, int y, ArrayList<ZoneAIObject> zoneObjects){
		Point point = new Point(x, y);
		this.zoneAIObject.getCoordinate().add(point);
		this.zoneObjects.clear();
		this.zoneObjects.addAll(zoneObjects);
		this.zoneObjects.add(zoneAIObject);
	}

	private void drawPoly(Canvas canvas, int color, ArrayList<ZoneAIObject> zoneObjects) {
		// line at minimum...
		Log.e("paint", " repaint " + zoneObjects.size());
		for (ZoneAIObject zoneAIObject : zoneObjects){
			Log.e("paint", " polygo " + zoneAIObject.getCoordinate().size());
			if (zoneAIObject.getCoordinate().size() < 1) {
				return;
			}
			// paint
			Paint polyPaint = new Paint();
			polyPaint.setColor(color);
			polyPaint.setStyle(Paint.Style.FILL);

			polyPaint.setAntiAlias(true);
			polyPaint.setColor(getResources().getColor(R.color.transparentColor));
			// path
			Path polyPath = new Path();
			polyPath.moveTo(zoneAIObject.getCoordinate().get(0).x, zoneAIObject.getCoordinate().get(0).y);

			for (Point point : zoneAIObject.getCoordinate()) {
				canvas.drawCircle(point.x, point.y, 10, polyPaint);
				polyPath.lineTo(point.x, point.y);
			}
			polyPath.lineTo(zoneAIObject.getCoordinate().get(0).x, zoneAIObject.getCoordinate().get(0).y);
			canvas.drawPath(polyPath, polyPaint);
		}
	}

//	private void drawPoly(Canvas canvas, int color, List<Point> points) {
//		// line at minimum...
//		Log.e("paint", " repaint");
//		if (points.size() < 1) {
//			return;
//		}
//		// paint
//		Paint polyPaint = new Paint();
//		polyPaint.setColor(color);
//		polyPaint.setStyle(Paint.Style.FILL);
//
//		polyPaint.setAntiAlias(true);
//		polyPaint.setColor(getResources().getColor(R.color.transparentColor));
//		// path
//		Path polyPath = new Path();
//		polyPath.moveTo(points.get(0).x, points.get(0).y);
//		for (Point point : points) {
//			canvas.drawCircle(point.x, point.y, 10, polyPaint);
//			polyPath.lineTo(point.x, point.y);
//		}
//		polyPath.lineTo(points.get(0).x, points.get(0).y);
//		canvas.drawPath(polyPath, polyPaint);
//	}

}
