package com;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.FloatList;

public class Layer
{
	private ArrayList<Perceptron> layer;

	public Layer(int numberOfPerceptrons, int numberOfInputs)
	{
		this.insertXrandomPerceptrons(numberOfPerceptrons, numberOfInputs);
	}

	public void insertXrandomPerceptrons(int numberOfPerceptrons, int numberOfInputs)
	{
		layer = new ArrayList<Perceptron>();

		for (int i = 0; i < numberOfPerceptrons; i++)
		{
			Perceptron Pp = new Perceptron(numberOfInputs);
			layer.add(Pp);
		}
	}

	public FloatList calculateLayer(FloatList inputs)
	{
		FloatList temp = new FloatList();
		temp.clear();
		for (int index = 0; index < layer.size(); index++)
		{
			temp.append(layer.get(index).calculatePerceptron(inputs));
		}
		return temp;
	}

	public void displayLayer()
	{
		System.out.println("Layer - size: " + layer.size());
		for (int i = 0; i < layer.size(); i++)
		{
			layer.get(i).displayPerceptron();
		}
	}

	public void saveLayer(String layerName)
	{
		for (int i = 0; i < layer.size(); i++)
		{
			layer.get(i).savePerceptron(layerName + "/perceptron" + i);
		}
	}

	public void loadLayer(PApplet pro, String layerName)
	{
		for (int i = 0; i < layer.size(); i++)
		{
			layer.get(i).loadPerceptron(pro, layerName + "/perceptron" + i);
		}
	}

	public void copyLayer(Layer from)
	{
		for (int i = 0; i < from.getLayer().size(); i++)
		{
			layer.get(i).copyPerceptron(from.getLayer().get(i));
		}
	}

	public float[] mutateLayerByError(float[] error)
	{
		float[] change = new float[layer.get(0).weights.size()];

		// for each perceptron
		for (int i = 0; i < layer.size(); i++)
		{
			float[] temp = layer.get(i).mutateByError(error[i], 0.0001f);
			for (int j = 0; j < temp.length; j++)
			{
				change[j] += temp[j];
			}
		}

		return change;
	}

	public ArrayList<Perceptron> getLayer()
	{
		return layer;
	}

	public void setLayer(ArrayList<Perceptron> layer)
	{
		this.layer = layer;
	}
}
