package org.axisvnit.axis_14;

import android.graphics.PointF;

public class Marker {
	public String name;
	public String description;

	public PointF point;
	public String buildingName;

	public Marker(String name, String description, float x, float y,
			String buildingName) {
		this.point = new PointF(x, y);
		this.name = name;
		this.buildingName = buildingName;
		this.description = description;
	}
}
