package com;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.FloatList;

public class YearBookNetwork extends PApplet
{
	// https://stats.stackexchange.com/questions/447451/autoencoder-learning-average-of-training-images

	final int xLen = 45;
	final int yLen = 60;

	final float dev = 3.2f; // 3.2f
	final int la1 = 8100;
	final int la2 = (int) (la1 / dev);
	final int la3 = (int) (la2 / dev);
	final int la4 = (int) (la3 / dev);
	final int laoutput = (int) (la4 / dev);// 31

	// number of images to learn from - max: 624
	final int number = 20;// 624
	// 45 width * 60 height * 3 color chanels = 8100

	ArrayList<FloatList> imagesValues;
	ArrayList<PImage> imgs;
	Network4 encoder;
	Network4 decoder;

	public void settings()
	{
		size(1200, 1200);
	}

	public void setup()
	{
		imgs = new ArrayList<PImage>();
		imagesValues = new ArrayList<FloatList>();

		System.out.println("Layers sizes");
		System.out.println(la1);
		System.out.println(la2);
		System.out.println(la3);
		System.out.println(la4);
		System.out.println(laoutput);

		encoder = new Network4(la1, la1, la2, la3, la4, laoutput, 0);
		decoder = new Network4(laoutput, laoutput, la4, la3, la2, la1, 0);

		frameRate(200);
		colorMode(RGB, 255);

		println("loading images");
		for (int i = 0; i < number; i++)
		{
			PImage img = loadImage("src/data/smaler images 0.3 45x60/Image (" + i + ").png"); // smaller_img
			if (i < 10)
			{
				imgs.add(img);
			}
			FloatList inputs = new FloatList();
			for (int y = 0; y < yLen; y++)
			{
				for (int x = 0; x < xLen; x++)
				{
					int cTemp = img.get(x, y);
					inputs.push(red(cTemp) / 255.0f);
					inputs.push(green(cTemp) / 255.0f);
					inputs.push(blue(cTemp) / 255.0f);
				}
			}
			imagesValues.add(inputs);
		}

		println("loading network");

		// encoder.loadNetwork("data/encoder");
		// decoder.loadNetwork("data/decoder");

		println("starting training");
	}

	public void draw()
	{
		// int startTime;

		// startTime = millis();
		for (int re = 0; re < 1; re++)
		{
			float score = 0;
			float pixelError[] = new float[la1];

			// for every image
			for (int i = 0; i < number; i++)
			{
				FloatList inputs = imagesValues.get(i);
				FloatList f = encoder.calculateNetwork(inputs);
				FloatList outputs = decoder.calculateNetwork(f);

				// for every pixel
				for (int j = 0; j < la1; j++)
				{
					float change = (inputs.get(j) - outputs.get(j)) / number;
					// change = sqrt(abs(change));
					pixelError[j] += change;
					score += (abs(change));
				}
			}
			encoder.mutateByError(decoder.mutateByError(pixelError));
			println("Score:\t" + score);
			/*
			 * if (score < 990) { println("saving"); encoder.saveNetwork("data/encoder");
			 * decoder.saveNetwork("data/decoder"); println("saved"); noLoop(); }
			 */
		}

		// println("------ " + (millis() - startTime));

		for (int i = 0; i < imgs.size(); i++)// number
		{
			// PImage img = loadImage("data/smaler images 0.3 45x60/Image (" + i + ").png");
			// //smaller_img
			PImage img = imgs.get(i);
			FloatList inputs = imagesValues.get(i);
			FloatList f = encoder.calculateNetwork(inputs);
			FloatList outputs = decoder.calculateNetwork(f);

			PImage imgNew = createImage(xLen, yLen, RGB);
			for (int y = 0; y < yLen; y++)
			{
				for (int x = 0; x < xLen; x++)
				{
					int j = 3 * (xLen * y + x);
					int cTemp = this.color(outputs.get(j) * 255, outputs.get(j + 1) * 255, outputs.get(j + 2) * 255);
					imgNew.set(x, y, cTemp);
				}
			}
			final int imgPerRaw = width / xLen;
			image(img, (i % imgPerRaw) * xLen, 2 * yLen * ((int) i / imgPerRaw));// original image
			image(imgNew, (i % imgPerRaw) * xLen, yLen + 2 * yLen * ((int) i / imgPerRaw));// constracted image
		}

		System.out.println("diffrenceBetweenImages:\t" + diffrenceBetweenImages());

		// println("saving");
		// encoder.saveNetwork("data/encoder");
		// decoder.saveNetwork("data/decoder");
		// println("saved");
		// noLoop();
	}

	public void keyReleased()
	{
		if (key == 'p')
		{
			println("saving");
			encoder.saveNetwork("data/encoder");
			decoder.saveNetwork("data/decoder");
			println("saved");
			noLoop();
		}
		else if (key == 'c')
		{
			loop();
		}
	}

	public static void main(String[] args)
	{
		PApplet.main(new String[] { YearBookNetwork.class.getName() });
	}

	public float diffrenceBetweenImages()
	{
		FloatList inputs1 = imagesValues.get(0);
		FloatList f1 = encoder.calculateNetwork(inputs1);
		FloatList outputs1 = decoder.calculateNetwork(f1);

		FloatList inputs2 = imagesValues.get(1);
		FloatList f2 = encoder.calculateNetwork(inputs2);
		FloatList outputs2 = decoder.calculateNetwork(f2);

		float diff = 0;
		for (int i = 0; i < outputs1.size(); i++)
		{
			diff += outputs1.get(i) - outputs2.get(i);
		}
		return diff;
	}
}
