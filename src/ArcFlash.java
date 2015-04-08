import java.util.ArrayList;

import processing.core.*;

public class ArcFlash extends PApplet {

	private static final long serialVersionUID = 1L;

	// 1, 3, 5, 7, 15, 21, 25, 35, 49, 75, 105, 147, 175, 245, 525, 735, 1225,

	int outputW = 3675;
	int outputH = 3675;
	int segmentW = 245;
	int segmentH = 245;
	int currentGridpoint = 0;
	PImage source;
	PGraphics canvas;
	ArrayList<PVector> gridPoints = new ArrayList<PVector>();

	public void setup() {
		size(floor(outputW / 4), floor(outputH / 4));
		background(255);

		canvas = createGraphics(outputW, outputH);
		canvas.beginDraw();
		canvas.background(0, 0, 0, 0);
		canvas.endDraw();

		source = loadImage("data/type5.png");
		source.resize(outputW, outputH);

		for (int ix = 0; ix < canvas.width; ix += segmentW) {
			for (int iy = 0; iy < canvas.height; iy += segmentH) {
				gridPoints.add(new PVector(ix, iy));
			}
		}

		while (currentGridpoint < gridPoints.size()) {
			System.out.println(currentGridpoint + " of " + (gridPoints.size() - 1));
			PVector point = gridPoints.get(currentGridpoint);
			render((int) point.x, (int) point.y);
			currentGridpoint++;
		}

		long unixTime = System.currentTimeMillis() / 1000L;
		canvas.save("output/" + unixTime + ".png");
		noLoop();
	}

	public void draw() {
		background(255);
		image(canvas, 0, 0, width, height);
	}

	public void render(int _x, int _y) {
		PImage colors = source.get(_x, _y, segmentW, segmentH);
		Tile tile = new Tile(colors.pixels, segmentW, segmentH);
		canvas.beginDraw();
		canvas.pushMatrix();
		canvas.translate(_x, _y);
		canvas.image(tile.tileCanvas, 0, 0);
		canvas.popMatrix();
		canvas.endDraw();
	}

	class Tile {

		int tileW;
		int tileH;
		int[] tileColors;
		PImage[] slices = new PImage[8];
		PGraphics tileCanvas;

		Tile(int[] _colors, int _width, int _height) {

			tileW = _width;
			tileH = _height;
			tileColors = _colors;
			tileCanvas = createGraphics(tileW, tileH);
			tileCanvas.beginDraw();
			tileCanvas.clear();
			tileCanvas.endDraw();

			PVector[] p = new PVector[9];
			p[0] = new PVector(0, 0);
			p[1] = new PVector(tileW / 2, 0);
			p[2] = new PVector(tileW, 0);
			p[3] = new PVector(0, tileH / 2);
			p[4] = new PVector(tileW / 2, tileH / 2);
			p[5] = new PVector(tileW, tileH / 2);
			p[6] = new PVector(0, tileH);
			p[7] = new PVector(tileW / 2, tileH);
			p[8] = new PVector(tileW, tileH);

			slices[0] = renderSlice(p[0].x, p[0].y, p[1].x, p[1].y, p[3].x, p[3].y);
			slices[1] = renderSlice(p[3].x, p[3].y, p[1].x, p[1].y, p[4].x, p[4].y);
			slices[2] = renderSlice(p[1].x, p[1].y, p[2].x, p[2].y, p[5].x, p[5].y);
			slices[3] = renderSlice(p[1].x, p[1].y, p[4].x, p[4].y, p[5].x, p[5].y);
			slices[4] = renderSlice(p[3].x, p[3].y, p[4].x, p[4].y, p[7].x, p[7].y);
			slices[5] = renderSlice(p[3].x, p[3].y, p[6].x, p[6].y, p[7].x, p[7].y);
			slices[6] = renderSlice(p[4].x, p[4].y, p[5].x, p[5].y, p[7].x, p[7].y);
			slices[7] = renderSlice(p[5].x, p[5].y, p[8].x, p[8].y, p[7].x, p[7].y);

			tileCanvas.beginDraw();
			for (int i = 0; i < slices.length; i++) {
				tileCanvas.image(slices[i], 0, 0);
			}
			tileCanvas.endDraw();
		}

		PImage renderSlice(float x1, float y1, float x2, float y2, float x3, float y3) {
			int c = tileColors[floor(random(tileColors.length))];
			int a = alpha(c) > 200 ? 255 : 0;

			PGraphics img = createGraphics(tileW, tileH);
			img.beginDraw();
			img.clear();
			img.noStroke();
			img.fill(red(c), green(c), blue(c), a);
			img.triangle(x1, y1, x2, y2, x3, y3);
			img.endDraw();
			return img;
		}

	}
}
